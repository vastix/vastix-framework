package name.bpdp.kipo.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.handler.StaticHandler;
import io.vertx.ext.apex.RoutingContext;
import io.vertx.ext.apex.handler.BodyHandler;
import java.util.function.Consumer;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import io.vertx.ext.apex.templ.TemplateEngine;
import io.vertx.ext.apex.handler.TemplateHandler;
import io.vertx.ext.apex.templ.ThymeleafTemplateEngine;

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
    	KipoRunner.runVerticle(KipoServer.class, true);
//		KipoRunner.runVerticle(BlazeGraph.class, false);
//		KipoRunner.runVerticle(TuProlog.class, false);


		Consumer<Vertx> runner = vertx -> {
			try {
				vertx.deployVerticle("src/main/groovy/name/bpdp/kipo/verticles/dsl/DomainSpecificLanguage.groovy");
			} catch (Throwable t) {
				t.printStackTrace();
			}
		};
		Vertx.clusteredVertx(new VertxOptions().setClustered(true), res -> {
			if (res.succeeded()) {
				Vertx vertx = res.result();
				runner.accept(vertx);
			} else {
				res.cause().printStackTrace();
			}
		});



		/*
		Vertx vertx = Vertx.vertx();

		VertxOptions options = new VertxOptions();

		vertx.deployVerticle("src/main/groovy/name/bpdp/kipo/verticles/dsl/DomainSpecificLanguage.groovy", res -> {
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

		//router.route().handler(BodyHandler.create());
		//router.route().handler(that::handleHome);

		// using templates
		TemplateEngine tengine = ThymeleafTemplateEngine.create();
		TemplateHandler thandler = TemplateHandler.create(tengine);

		// This will route all GET requests starting with /dynamic/ to the template handler
		// E.g. /dynamic/graph.hbs will look for a template in /templates/dynamic/graph.hbs
		//router.get("/dynamic/").handler(thandler);

		// Route all GET requests for resource ending in .hbs to the template handler
		router.getWithRegex(".+\\.tl").handler(thandler);

		// for static content, it will take webroot/index.html
		router.route().handler(StaticHandler.create());

		// for dialog between machinge
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
