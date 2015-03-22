package name.bpdp.kipo.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.apex.Router;
import io.vertx.core.Vertx;

import name.bpdp.kipo.helper.KipoRunner;

// Verticles
import name.bpdp.kipo.verticles.blazegraph.BlazeGraph;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class KipoServer extends AbstractVerticle {

  public static void main(String[] args) {
    KipoRunner.runVerticle(KipoServer.class, true);
    KipoRunner.runVerticle(BlazeGraph.class, false);

	/* will try to test this whenever @GenIgnore is removed from Vertx.java
	 * the source code still reside in 
	 * src/main/groovy/name/bpdp/kipo/verticles/dsl/DomainSpecificLanguage.groovy
	 *
	 *
	Vertx vertx = Vertx.vertx();

	vertx.deployVerticle("groovy:name.bpdp.kipo.verticles.dsl.DomainSpecificLanguage", res -> {
		if (res.succeeded()) {
			System.out.println("Deployment id is: " + res.result());
		} else {
			System.out.println("Deployment failed!");
		}
	});
	*/

  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(routingContext -> {
      routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
    });

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
