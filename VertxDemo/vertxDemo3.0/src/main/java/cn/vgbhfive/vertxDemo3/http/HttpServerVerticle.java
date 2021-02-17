package cn.vgbhfive.vertxDemo3.http;

import cn.vgbhfive.vertxDemo3.databse.WikiDatabaseService;
import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

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
    router.get("/wiki/:page").handler(this::PageRenderingHandler);
    router.post().handler(BodyHandler.create()); // 所有POST 请求都会过BodyHandler
    router.post("/save").handler(this::PageUpdateHandler);
    router.post("/create").handler(this::PageCreateHandler);
    router.post("/delete").handler(this::PageDeletionHandler);

    templateEngine = FreeMarkerTemplateEngine.create(vertx);

    int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 18080);
    server
      .requestHandler(router)
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
}
