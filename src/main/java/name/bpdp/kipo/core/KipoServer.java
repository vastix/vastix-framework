package name.bpdp.kipo.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.RoutingContext;
import io.vertx.ext.apex.handler.BodyHandler;

import io.vertx.core.Vertx;

import name.bpdp.kipo.helper.KipoRunner;

// Verticles
import name.bpdp.kipo.verticles.blazegraph.BlazeGraph;
import name.bpdp.kipo.verticles.prolog.TuProlog;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class KipoServer extends AbstractVerticle {

	private KipoServer that = this;

	public static void main(String[] args) {
    	KipoRunner.runVerticle(KipoServer.class, false);
		KipoRunner.runVerticle(BlazeGraph.class, false);
		KipoRunner.runVerticle(TuProlog.class, false);

		Vertx vertx = Vertx.vertx();

		vertx.deployVerticle("src/main/groovy/name/bpdp/kipo/verticles/dsl/DomainSpecificLanguage.groovy", res -> {
			if (res.succeeded()) {
				System.out.println("Deployment id is: " + res.result());
			} else {
				System.out.println("Deployment failed!");
			}
		});


	}

	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);

		//router.route().handler(BodyHandler.create());
		//router.route().handler(that::handleHome);
		router.get("/dialog/:messageDlg").handler(that::handleDialog);

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);

	}

	private void handleHome(RoutingContext routingContext) {

		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "text/html");
		response.end("Hello World!");

	}

	private void handleDialog(RoutingContext routingContext) {

		HttpServerResponse response = routingContext.response();

		String messageDlg = routingContext.request().getParam("messageDlg");

		System.out.println("Send " + messageDlg + " to kipo.dialog");

		EventBus evb = vertx.eventBus();

		evb.send("kipo.dialog", messageDlg, ar ->  {
			if (ar.succeeded()) {
				response.putHeader("content-type", "text/html");
				response.end("Received reply: " + ar.result().body());
			} else {
				response.putHeader("content-type", "text/html");
				response.end("Gagal maning son: " + ar.cause());
			}
		});

	}


}
