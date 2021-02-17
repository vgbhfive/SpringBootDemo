package cn.vgbhfive.vertxDemo2.databse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @time: 2019/09/23
 * @author: Vgbh
 */
public class WikiDatabaseVerticle extends AbstractVerticle {

  public static final String CONFIG_WIKIDB_JDBC_URL = "wikidb.jdbc.url";
  public static final String CONFIG_WIKIDB_JDBC_DRIVER_CLASS = "wikidb.jdbc.driver_class";
  public static final String CONFIG_WIKIDB_JDBC_MAX_POOL_SIZE = "wikidb.jdbc.max_pool_size";
  public static final String CONFIG_WIKIDB_SQL_QUERIES_RESOURCE_FILE = "wikidb.sqlqueries.resource.file";

  public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";

  private static final Logger LOGGER = LoggerFactory.getLogger(WikiDatabaseVerticle.class);

  private JDBCClient jdbcClient;

  @Override
  public void start(Promise<Void> promise) throws Exception {
    sqlQueries = loadSQLQueries();

    jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
      .put("url", config().getString(CONFIG_WIKIDB_JDBC_URL, "jdbc:hsqldb:file:db/wiki"))
      .put("driver_class", config().getString(CONFIG_WIKIDB_JDBC_DRIVER_CLASS, "org.hsqldb.jdbcDriver"))
      .put("max_pool_size", config().getInteger(CONFIG_WIKIDB_JDBC_MAX_POOL_SIZE, 30)));

//    WikiDatabaseService.create(jdbcClient, sqlQueries, res -> {
//      if (res.succeeded()) {
//        ServiceBinder binder = new ServiceBinder(vertx);
//        binder
//          .setAddress(CONFIG_WIKIDB_QUEUE)
//          .register(WikiDatabaseService.class, res.result());
//        promise.complete();
//      } else {
//        promise.fail(res.cause());
//      }
//    });

    jdbcClient.getConnection(ar -> {
      if (ar.failed()) {
        LOGGER.error("Could not open a database connection", ar.cause());
        promise.fail(ar.cause());
      } else {
        SQLConnection connection = ar.result();
        connection.execute(sqlQueries.get(SQLQuery.CREATE_PAGES_TABLE), create -> {
          connection.close();
          if (create.failed()) {
            LOGGER.error("Database preparation error", create.cause());
            promise.fail(create.cause());
          } else {
            vertx.eventBus().consumer(config().getString(CONFIG_WIKIDB_QUEUE, "wikidb.queue"), this::onMessage);
            promise.complete();
            LOGGER.info("Database start running!");
          }
        });
      }
    });
  }

  HashMap<SQLQuery, String> sqlQueries = new HashMap<>();

  /**
   * 加载SQL
   * @throws IOException
   */
  private HashMap<SQLQuery, String> loadSQLQueries () throws IOException {

    String sqlQueriesFile = config().getString(CONFIG_WIKIDB_SQL_QUERIES_RESOURCE_FILE);

    InputStream queriesInputStream;
    if (sqlQueriesFile != null) {
      queriesInputStream = new FileInputStream(sqlQueriesFile);
    } else {
      queriesInputStream = getClass().getResourceAsStream("/db-requeries.properties");
    }

    Properties queriesProps = new Properties();
    queriesProps.load(queriesInputStream);
    LOGGER.info(queriesProps.toString());
    queriesInputStream.close();

    sqlQueries.put(SQLQuery.CREATE_PAGES_TABLE, queriesProps.getProperty("create-pages-table"));
    sqlQueries.put(SQLQuery.ALL_PAGES, queriesProps.getProperty("all-pages"));
    sqlQueries.put(SQLQuery.GET_PAGE, queriesProps.getProperty("get-page"));
    sqlQueries.put(SQLQuery.CREATE_PAGE, queriesProps.getProperty("create-page"));
    sqlQueries.put(SQLQuery.SAVE_PAGE, queriesProps.getProperty("save-page"));
    sqlQueries.put(SQLQuery.DELETE_PAGE, queriesProps.getProperty("delete-page"));
    LOGGER.info(sqlQueries.toString());
    return sqlQueries;
  }

  /**
   * 处理SQL
   * @param message
   */
  private void onMessage(Message<JsonObject> message) {
    if (!message.headers().contains("action")) {
      LOGGER.error("No action header specified for message with headers {} and body {}",
        message.headers(), message.body().encodePrettily());
      message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
      return;
    }

    String action = message.headers().get("action");

    switch (action) {
      case "all-pages":
        fetchAllPages(message);
        break;
      case "get-page":
        fetchPage(message);
        break;
      case "create-page":
        createPage(message);
        break;
      case "save-page":
        savePage(message);
        break;
      case "delete-page":
        deletePage(message);
        break;
      default:
        message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action" + action);
          break;
    }
  }

  private void fetchAllPages(Message<JsonObject> message) {
    jdbcClient.query(sqlQueries.get(SQLQuery.ALL_PAGES), res -> {
      if (res.succeeded()) {
        List<String> pages = res.result()
          .getResults()
          .stream()
          .map(json -> json.getString(0))
          .sorted()
          .collect(Collectors.toList());
        message.reply(new JsonObject().put("pages", new JsonArray(pages)));
        LOGGER.info("Successful Fetching Pages!");
      } else {
        reportQueryError(message, res.cause());
      }
    });
  }

  private void fetchPage(Message<JsonObject> message) {
    String requestedPage = message.body().getString("page");
    JsonArray params = new JsonArray().add(requestedPage);

    jdbcClient.queryWithParams(sqlQueries.get(SQLQuery.GET_PAGE), params, fetch -> {
      if (fetch.succeeded()) {
        JsonObject response = new JsonObject();
        ResultSet resultSet = fetch.result();
        if (resultSet.getNumRows() == 0) {
          response.put("found", false);
        } else {
          response.put("found", true);
          JsonArray row = resultSet.getResults().get(0);
          response.put("id", row.getInteger(0));
          response.put("rawContent", row.getString(1));
        }
        LOGGER.info("Successful Fetching Page!");
        message.reply(response);
      } else {
        reportQueryError(message, fetch.cause());
      }
    });
  }

  private void createPage(Message<JsonObject> message) {
    JsonObject request = message.body();
    JsonArray data = new JsonArray()
      .add(request.getString("title"))
      .add(request.getString("merkdown"));

    jdbcClient.updateWithParams(sqlQueries.get(SQLQuery.CREATE_PAGE), data, res -> {
      if (res.succeeded()) {
        LOGGER.info("Successful Creating Page!");
        message.reply("ok");
      } else {
        reportQueryError(message, res.cause());
      }
    });
  }

  private void savePage(Message<JsonObject> message) {
    JsonObject request = message.body();

    JsonArray data = new JsonArray()
      .add(request.getString("merkdown"))
      .add(request.getString("id"));

    jdbcClient.updateWithParams(sqlQueries.get(SQLQuery.SAVE_PAGE), data, res -> {
      if (res.succeeded()) {
        LOGGER.info("Successful Saving Page!");
        message.reply("ok");
      } else {
        reportQueryError(message, res.cause());
      }
    });
  }

  private void deletePage(Message<JsonObject> message) {
    JsonArray data = new JsonArray().add(message.body().getString("id"));

    jdbcClient.updateWithParams(sqlQueries.get(SQLQuery.DELETE_PAGE), data, res -> {
      if (res.succeeded()) {
        LOGGER.info("Successful Deleting Page!");
        message.reply("ok");
      } else {
        reportQueryError(message, res.cause());
      }
    });
  }

  private void reportQueryError(Message<JsonObject> message, Throwable cause) {
    LOGGER.error("Database query error", cause);
    message.fail(ErrorCodes.DB_ERROR.ordinal(), cause.getMessage());
  }

}
