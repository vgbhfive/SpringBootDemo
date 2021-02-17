package cn.vgbhfive.vertxDemo3.http;

import cn.vgbhfive.vertxDemo3.databse.WikiDatabaseService;
import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @time: 2019/09/23
 * @author: Vgbh
 */
public class HttpServerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

  public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
  public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";

  private String wikiDbQueue = "wikidb.queue";

  private FreeMarkerTemplateEngine templateEngine;

  private WikiDatabaseService dbService;

  private static final String EMPTY_PAGE_MARKDOWN = "# A new page\n" +
      "\n" +
      "Feel-free to write in Markdown!\n";

  @Override
  public void start(Promise<Void> promise) throws Exception {
    wikiDbQueue = config().getString(CONFIG_WIKIDB_QUEUE, "wikidb.queue");

    dbService = WikiDatabaseService.createProxy(vertx, wikiDbQueue);

    HttpServer server  = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.get("/").handler(this::indexHandler);
    router.get("/backup").handler(this::backupHandler);
    router.get("/wiki/:page").handler(this::PageRenderingHandler);
    router.post().handler(BodyHandler.create()); // 所有POST 请求都会过BodyHandler
    router.post("/save").handler(this::PageUpdateHandler);
    router.post("/create").handler(this::PageCreateHandler);
    router.post("/delete").handler(this::PageDeletionHandler);

    Router apiRouter = Router.router(vertx);
    apiRouter.get("/pages").handler(this::apiRoot);
    apiRouter.get("/pages/:id").handler(this::apiGetPage);
    apiRouter.post().handler(BodyHandler.create());
    apiRouter.post("/pages").handler(this::apiCreatePage);
    apiRouter.put().handler(BodyHandler.create());
    apiRouter.put("/pages/:id").handler(this::apiUpdatePage);
    apiRouter.delete("/pages/:id").handler(this::apiDeletePage);
    router.mountSubRouter("/api", apiRouter);

    templateEngine = FreeMarkerTemplateEngine.create(vertx);

    int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 18080);
    server
      .requestHandler(apiRouter)
      .listen(portNumber, ar -> {
        if (ar.succeeded()) {
          LOGGER.info("HTTP server running on port " + portNumber);
          promise.complete();
        } else {
          LOGGER.error("Could not start a HTTP server", ar.cause());
          promise.fail(ar.cause());
        }
      });
  }

  /**
   *
   * @param context
   */
  private void indexHandler(RoutingContext context) {
    LOGGER.info(dbService.toString());
    dbService.fetchAllPages(reply -> {
      if (reply.succeeded()) {
        context.put("title", "Wiki Home");
        context.put("pages", reply.result().getList());
        templateEngine.render(context.data(), "templates/index.ftl", ar -> {
          if (ar.succeeded()) {
            context.response().putHeader("Content-Type", "text/html");
            context.response().end(ar.result());
          } else {
            LOGGER.info("Fetch all pages error!");
            context.fail(ar.cause());
          }
        });
      } else {
        context.fail(reply.cause());
      }
    });
  }

  /**
   * 页面删除处理器
   *
   * @param context
   */
  private void PageDeletionHandler(RoutingContext context) {
    dbService.deletePage(Integer.parseInt(context.request().getParam("id")), reply -> {
      if (reply.succeeded()) {
        context.response().setStatusCode(303);
        context.response().putHeader("Location", "/");
        context.response().end();
      } else {
        context.fail(reply.cause());
      }
    });
  }

  /**
   * 页面创建处理器
   *
   * @param context
   */
  private void PageCreateHandler(RoutingContext context) {
    String pageName = context.request().getParam("name"); // 创建页面名称
    String location = "/wiki/" + pageName; // 定义URl 路径
    if (pageName == null || pageName.isEmpty()) {
      location = "/";
    }
    context.response().setStatusCode(303);
    context.response().putHeader("Location", location);
    context.response().end();
  }

  /**
   * 页面更新处理器
   *
   * @param context
   */
  private void PageUpdateHandler(RoutingContext context) {
    String title = context.request().getParam("title"); // 标题
    String markdown = context.request().getParam("markdown");

    Handler<AsyncResult<Void>> handler = reply -> {
      if (reply.succeeded()) {
        context.response().setStatusCode(303);
        context.response().putHeader("Location", "/wiki/" + title);
        context.response().end();
      } else {
        context.fail(reply.cause());
      }
    };

    if ("yes".equals(context.request().getParam("newPage"))) {
      dbService.createPage(title, markdown, handler);
    } else {
      dbService.savePage(Integer.parseInt(context.request().getParam("id")), markdown, handler);
    }
  }

  /**
   * 获取页面处理器
   *
   * @param context
   */
  private void PageRenderingHandler(RoutingContext context) {
    String page = context.request().getParam("page");

    dbService.fetchPage(page, reply -> {
      if (reply.succeeded()) {
        JsonObject body = reply.result();

        boolean found = body.getBoolean("found");
        String rawContent = body.getString("rawContent", EMPTY_PAGE_MARKDOWN);
        rawContent = rawContent == null ? EMPTY_PAGE_MARKDOWN : rawContent;
        context.put("title", page);
        context.put("id", body.getInteger("id", -1));
        context.put("newPage", found ? "no" : "yes");
        context.put("rawContent", rawContent);
        context.put("content", Processor.process(rawContent));
        context.put("timestamp", new Date().toString());

        templateEngine.render(context.data(), "templates/page.ftl", ar -> {
          if (ar.succeeded()) {
            context.response().putHeader("Content-Type", "text/html");
            context.response().end(ar.result());
          } else {
            context.fail(ar.cause());
          }
        });
      } else {
        context.fail(reply.cause());
      }
    });
  }

  /**
   *
   * @param context
   */
  private void backupHandler(RoutingContext context) {
    dbService.fetchAllPagesData(res -> {
      if (res.succeeded()) {
        JsonArray filesObject = new JsonArray();
        JsonObject payload = new JsonObject()
          .put("files", filesObject)
          .put("language", "plaintext")
          .put("title", "vertx-wiki-page")
          .put("public", true);

        res.result().forEach(page -> {
          JsonObject fileObject = new JsonObject();
          fileObject.put("name", page.getString("NAME"));
          fileObject.put("content", page.getString("CONTENT"));
          filesObject.add(fileObject);
        });

        WebClient webClient = WebClient.create(vertx, new WebClientOptions().setSsl(true).setUserAgent("vertx3.0"));
        webClient.post(443, "snippets.glot.io", "/snippets")
          .putHeader("Content-Type", "application/json")
          .as(BodyCodec.jsonObject())
          .sendJsonObject(payload, ar -> {
            if (ar.succeeded()) {
              HttpResponse<JsonObject> response = ar.result();
              if (response.statusCode() == 200) {
                String url = "https://glot.io/snippets/" + response.body().getString("id");
                context.put("backup_gist_url", url);
                indexHandler(context);
              } else {
                StringBuilder message = new StringBuilder()
                  .append("Could not backup this wiki: ")
                  .append(response.statusMessage());
                JsonObject body = response.body();
                if (body != null) {
                  message.append(System.getProperty("line.separator"))
                    .append(body.encodePrettily());
                }
                LOGGER.error(message.toString());
                context.fail(502);
              }
            } else {
              LOGGER.error("Http Client error ", ar.cause());
              context.fail(ar.cause());
            }
          });
      } else {
        context.fail(res.cause());
      }
    });
  }

  /**
   * 展示全部信息
   * @param context
   */
  private void apiRoot(RoutingContext context) {
    dbService.fetchAllPagesData(reply -> {
      JsonObject response = new JsonObject();
      if (reply.succeeded()) {
        List<JsonObject> pages = reply.result()
          .stream()
          .map(obj -> new JsonObject()
            .put("id", obj.getInteger("ID"))
            .put("name", obj.getString("NAME")))
          .collect(Collectors.toList());

        response
          .put("success", true)
          .put("pages", pages);
        context.response().setStatusCode(200);
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(response.encode());
      } else {
        response
          .put("success", false)
          .put("error", reply.cause().getMessage());
        context.response().setStatusCode(500);
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(response.encode());
      }
    });
  }

  /**
   * 展示单条信息
   * @param context
   */
  private void apiGetPage(RoutingContext context) {
    dbService.fetchPageById(Integer.parseInt(context.request().getParam("id")), reply -> {
      JsonObject response = new JsonObject();
      if (reply.succeeded()) {
        JsonObject dbObject = reply.result();
        if (dbObject.getBoolean("found")) {
          JsonObject payload = new JsonObject()
            .put("name", dbObject.getString("name"))
            .put("id", dbObject.getInteger("id"))
            .put("markdown", dbObject.getString("content"))
            .put("html", Processor.process(dbObject.getString("content")));
          response
            .put("success", true)
            .put("page", payload);
          context.response().setStatusCode(200);
        } else {
          context.response().setStatusCode(404);
          response
            .put("success", false)
            .put("error", "There is no page with ID " + context.request().getParam("id"));
        }
      } else {
        response
          .put("success", false)
          .put("error", reply.cause().getMessage());
        context.response().setStatusCode(500);
      }
      context.response().putHeader("Content-Type", "application/json");
      context.response().end(response.encode());
    });
  }

  /**
   * 创建信息
   * @param context
   */
  private void apiCreatePage(RoutingContext context) {
    JsonObject page = context.getBodyAsJson();
    if (!validateJsonPageDocument(context, page, "name", "markdown")) {
      return;
    }
    dbService.createPage(page.getString("name"), page.getString("markdown"), reply -> {
      if (reply.succeeded()) {
        context.response().setStatusCode(201);
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(new JsonObject().put("success", true).encode());
      } else {
        context.response().setStatusCode(500);
        context.response().putHeader("Content-Type", "application/json");
        context.response().end(new JsonObject()
          .put("success", false)
          .put("error", reply.cause().getMessage()).encode());
      }
    });
  }

  /**
   * 更新信息
   * @param context
   */
  private void apiUpdatePage(RoutingContext context) {
    int id = Integer.valueOf(context.request().getParam("id"));
    JsonObject page = context.getBodyAsJson();
    if (!validateJsonPageDocument(context, page, "markdown")) {
      return;
    }
    dbService.savePage(id, page.getString("markdown"), reply -> {
      handleSimpleDbReply(context, reply);
    });
  }

  /**
   * 删除信息
   * @param context
   */
  private void apiDeletePage(RoutingContext context) {
    int id = Integer.valueOf(context.request().getParam("id"));
    dbService.deletePage(id, reply -> {
      handleSimpleDbReply(context, reply);
    });
  }

  private boolean validateJsonPageDocument(RoutingContext context, JsonObject page, String... expectedKeys) {
    if (!Arrays.stream(expectedKeys).allMatch(page::containsKey)) {
      LOGGER.error("Bad page creation JSON payload: " + page.encodePrettily() + " from " + context.request().remoteAddress());
      context.response().setStatusCode(400);
      context.response().putHeader("Content-Type", "application/json");
      context.response().end(new JsonObject()
        .put("success", false)
        .put("error", "Bad request payload").encode());
      return false;
    }
    return true;
  }

  private void handleSimpleDbReply(RoutingContext context, AsyncResult<Void> reply) {
    if (reply.succeeded()) {
      context.response().setStatusCode(200);
      context.response().putHeader("Content-Type", "application/json");
      context.response().end(new JsonObject().put("success", true).encode());
    } else {
      context.response().setStatusCode(500);
      context.response().putHeader("Content-Type", "application/json");
      context.response().end(new JsonObject()
        .put("success", false)
        .put("error", reply.cause().getMessage()).encode());
    }
  }

}
