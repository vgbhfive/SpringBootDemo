package cn.vgbhfive.vertxDemo3;

import cn.vgbhfive.vertxDemo3.databse.WikiDatabaseVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Promise<String> dbVerticleDeployment = Promise.promise();

    vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment);

    dbVerticleDeployment.future().compose(id -> {
      Promise<String> httpVerticleDeployment = Promise.promise();
      vertx.deployVerticle("cn.vgbhfive.vertxDemo3.http.HttpServerVerticle", // A class name as a string is also an option to specify a verticle to deploy
        new DeploymentOptions().setInstances(3), // 可创建多个实例
        httpVerticleDeployment);

      return httpVerticleDeployment.future();
    }).setHandler(ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
        LOGGER.info("Start running!");
      } else {
        startPromise.fail(ar.cause());
        LOGGER.error("Error : " + ar.cause());
      }
    });
  }

}
