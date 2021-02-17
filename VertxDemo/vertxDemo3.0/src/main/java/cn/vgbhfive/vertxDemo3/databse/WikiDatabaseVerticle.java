package cn.vgbhfive.vertxDemo3.databse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.serviceproxy.ServiceBinder;
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

    WikiDatabaseService.create(jdbcClient, sqlQueries, res -> {
      if (res.succeeded()) {
        ServiceBinder binder = new ServiceBinder(vertx);
        binder
          .setAddress(CONFIG_WIKIDB_QUEUE)
          .register(WikiDatabaseService.class, res.result());
        promise.complete();
      } else {
        promise.fail(res.cause());
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

}
