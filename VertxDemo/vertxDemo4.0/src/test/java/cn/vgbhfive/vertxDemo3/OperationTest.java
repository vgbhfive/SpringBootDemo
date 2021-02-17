package cn.vgbhfive.vertxDemo3;

import cn.vgbhfive.vertxDemo3.databse.WikiDatabaseService;
import cn.vgbhfive.vertxDemo3.databse.WikiDatabaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @time: 2019/09/26
 * @author: Vgbh
 */
@RunWith(VertxUnitRunner.class)
public class OperationTest {

  private Vertx vertx;
  private WikiDatabaseService wikiDatabaseService;

  @Test
  public void async_behavior(TestContext context) {
    Vertx vertx = Vertx.vertx();
    context.assertEquals("foo", "foo");

    Async a1 = context.async();
    Async a2 = context.async();
    vertx.setTimer(100, n -> a1.complete());
    vertx.setPeriodic(100, n -> a2.countDown());
  }

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();

    JsonObject conf = new JsonObject()
      .put(WikiDatabaseVerticle.CONFIG_WIKIDB_JDBC_URL, "jdbc:hsqldb:mem:testdb;shutdown=true")
      .put(WikiDatabaseVerticle.CONFIG_WIKIDB_JDBC_MAX_POOL_SIZE, 4);

    vertx.deployVerticle(new WikiDatabaseVerticle(), new DeploymentOptions().setConfig(conf), context.asyncAssertSuccess(id -> {
      wikiDatabaseService = WikiDatabaseService.createProxy(vertx, WikiDatabaseVerticle.CONFIG_WIKIDB_QUEUE);
    }));
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testOperation(TestContext context) {
    Async async = context.async();

    wikiDatabaseService.createPage("test1", "qwer", context.asyncAssertSuccess(v1 -> {
      wikiDatabaseService.fetchPage("test1", context.asyncAssertSuccess(json1 -> {
        context.assertTrue(json1.getBoolean("found"));
        context.assertTrue(json1.containsKey("id"));
        context.assertEquals("qwer", json1.getString("rawContent"));

        wikiDatabaseService.savePage(json1.getInteger("id"), "asdf", context.asyncAssertSuccess(v2 -> {
          wikiDatabaseService.fetchAllPages(context.asyncAssertSuccess(array1 -> {
            context.assertEquals(1, array1.size());

            wikiDatabaseService.fetchPage("test1", context.asyncAssertSuccess(json2 -> {
              context.assertEquals("asdf", json2.getString("rawContent"));

              wikiDatabaseService.deletePage(json2.getInteger("id"), v3 -> {

                wikiDatabaseService.fetchAllPages(context.asyncAssertSuccess(array2 -> {
                  context.assertTrue(array2.isEmpty());

                  async.complete();
                }));
              });
            }));
          }));
        }));
      }));
    }));
    async.awaitSuccess(5000);
  }

  @Test
  public void startHttpServer(TestContext context) {
    Async async = context.async();

    vertx.createHttpServer().requestHandler(req -> {
      req.response().putHeader("Content-Type", "text/plain").end("OK");
    }).listen(18080, context.asyncAssertSuccess(server -> {
      WebClient webClient = WebClient.create(vertx);

      webClient.get(18080, "localhost", "/").send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          context.assertTrue(response.headers().contains("Content-Type"));
          context.assertEquals("text/plain", response.getHeader("Content-Type"));
          context.assertEquals("OK", response.body().toString());
          webClient.close();
          async.complete();
        } else {
          async.resolve(Promise.failedPromise(ar.cause()));
        }
      });
    }));
  }

}
