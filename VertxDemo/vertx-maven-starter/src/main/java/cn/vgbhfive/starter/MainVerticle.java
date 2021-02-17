package cn.vgbhfive.starter;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private static final String SQL_CREATE_PAGES_TABLE = "create table if not exists Pages (Id integer identity primary key, Name varchar(255) unique, Content clob)";
  private static final String SQL_ALL_PAGES = "select Name from Pages";
  private static final String SQL_DELETE_PAGE = "delete from Pages where Id = ?";
  private static final String SQL_GET_PAGE = "select Id, Content from Pages where Name = ?";
  private static final String SQL_CREATE_PAGE = "insert into Pages values (NULL, ?, ?)";
  private static final String SQL_SAVE_PAGE = "update Pages set Content = ? where Id = ?";

  private static final String EMPTY_PAGE_MARKDOWN =
    "# A new page\n" +
      "\n" +
      "Feel-free to write in Markdown!\n";

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  private JDBCClient dbClient;

  private FreeMarkerTemplateEngine templateEngine;

  /**
   * 启动类
   *
   * @param startPromise
   * @throws Exception
   */
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());

    steps.setHandler(ar -> {
      if (ar.succeeded()) {
        logger.info("HTTP server running on port 8080");
        startPromise.complete();
      } else {
        logger.error("Could not start a HTTP server", ar.cause());
        startPromise.fail(ar.cause());
      }
    });
  }

  /**
   * 获取数据库连接
   *
   * @return
   */
  private Future<Void> prepareDatabase() {
    Promise<Void> promise = Promise.promise();

    dbClient = JDBCClient.createShared(vertx, new JsonObject() // vertx创建一个共享链接
      .put("url", "jdbc:hsqldb:file:db/wiki")
      .put("driver_class", "com.hsqldb.jdbcDriver")
      .put("max_pool_size", 30)); // 驱动程序所需的参数

    dbClient.getConnection(ar -> {
      if (ar.failed()) { // 异步结果获取连接失败
        logger.error("open DB Failed", ar.cause());
        promise.fail(ar.cause());
      } else { // 执行SQL 查询
        SQLConnection connection = ar.result();
        connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
          connection.close();
          if (create.failed()) {
            logger.error("Database preparation error", create.cause());
            promise.fail(create.cause());
          } else {
            promise.complete();
          }
        });
      }
    });

    return promise.future();
  }

  /**
   * 路由
   *
   * @return
   */
  private Future<Void> startHttpServer() {
    Promise<Void> promise = Promise.promise();
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.get("/").handler(this::indexHandler);
    router.get("/wiki/:page").handler(this::PageRenderingHandler);
    router.post().handler(BodyHandler.create()); // 所有POST 请求都会过BodyHandler
    router.post("/save").handler(this::PageUpdateHandler);
    router.post("/create").handler(this::PageCreateHandler);
    router.post("/delete").handler(this::PageDeletionHandler);

    templateEngine = FreeMarkerTemplateEngine.create(vertx);

    server.requestHandler(router)
      .listen(8080, ar -> {
        if (ar.succeeded()) {
          logger.error("HTTP server running on port 8080");
          promise.complete();
        } else {
          logger.error("Could not start a HTTP server", ar.cause());
          promise.fail(ar.cause());
        }
      });

    return promise.future();
  }

  /**
   * 页面删除处理器
   *
   * @param context
   */
  private void PageDeletionHandler(RoutingContext context) {
    String id = context.request().getParam("id"); // 获取id
    dbClient.getConnection(car -> { // car 连接数据库返回对象
      if (car.succeeded()) {
        SQLConnection connection = car.result(); // connection 连接数据库结果对象
        connection.updateWithParams(SQL_DELETE_PAGE, new JsonArray().add(id), res -> { // res 执行SQL 语句结果
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303); // 设置返回码
            context.response().putHeader("Location", "/"); // 设置返回主界面URL
            context.response().end(); // 结束返回
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause()); // fail() 会很好的处理异常信息
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
    String id = context.request().getParam("id"); // id
    String title = context.request().getParam("title"); // 标题
    String markdown = context.request().getParam("markdown"); // 内容
    boolean newPage = "yes".equals(context.request().getParam("newPage")); // 是否创建新页面

    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        String sql = newPage ? SQL_CREATE_PAGE : SQL_SAVE_PAGE; // 获取SQL 语句
        JsonArray params = new JsonArray();
        if (newPage) {
          params.add(title).add(markdown);
        } else {
          params.add(markdown).add(id);
        }
        connection.updateWithParams(sql, params, res -> {
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);
            context.response().putHeader("Location", "/wiki/" + title);
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }

  /**
   * 获取页面处理器
   *
   * @param context
   */
  private void PageRenderingHandler(RoutingContext context) {
    String page = context.request().getParam("page");

    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.queryWithParams(SQL_GET_PAGE, new JsonArray().add(page), fetch -> {
          connection.close();
          if (fetch.succeeded()) {
            JsonArray row = fetch.result().getResults()
              .stream()
              .findFirst()
              .orElseGet(() -> new JsonArray().add(-1).add(EMPTY_PAGE_MARKDOWN));
            Integer id = row.getInteger(0);
            String rawContent = row.getString(1);

            context.put("title", page);
            context.put("id", id);
            context.put("newPage", fetch.result().getResults().size() == 0 ? "yes" : "no");
            context.put("rawContent", rawContent);
            context.put("content", Processor.process(rawContent)); // 使用MarkDown 渲染库
            context.put("timestamp", new Date().toString());

            templateEngine.render(context.data(), "templates/page.ftl", ar -> { // ar 写入页面结果
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });
          } else {
            context.fail(fetch.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }

  /**
   * 主界面处理器
   *
   * @param context
   */
  private void indexHandler(RoutingContext context) {
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.query(SQL_ALL_PAGES, res -> {
          connection.close();
          if (res.succeeded()) {
            List<String> pages = res.result()
              .getResults()
              .stream()
              .map(json -> json.getString(0))
              .sorted()
              .collect(Collectors.toList());
            context.put("title", "Wiki home");
            context.put("pages", pages);
            templateEngine.render(context.data(), "templates/index.ftl", ar -> {
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }


}
