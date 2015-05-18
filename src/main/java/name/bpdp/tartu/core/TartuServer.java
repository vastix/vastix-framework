package name.bpdp.tartu.core;

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

import name.bpdp.tartu.helper.TartuRunner;
import name.bpdp.vertx.blazegraph.BlazegraphServiceVerticle;
import name.bpdp.vertx.blazegraph.BlazegraphService;
// Verticles
//import name.bpdp.tartu.verticles.blazegraph.BlazeGraph;
//import name.bpdp.tartu.verticles.prolog.TuProlog;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class TartuServer extends AbstractVerticle {

	private TartuServer that = this;

	public static void main(String[] args) {
    	TartuRunner.runJavaVerticle(TartuServer.class, true);
//		KipoRunner.runJavaVerticle(BlazeGraph.class, true);
//		KipoRunner.runJavaVerticle(TuProlog.class, true);

		Vertx vertx = Vertx.vertx();

		JsonObject config = new JsonObject().put("address", "tartu.blazegraph");
		DeploymentOptions depOptions = new DeploymentOptions().setConfig(config);

		vertx.deployVerticle("service:name.bpdp.blazegraph-service", depOptions, res -> {
			if (res.succeeded()) {
				System.out.println("Start service - succeed");
			} else {
				System.out.println("Start service - failed");
			}
		});

		TartuRunner.runGroovyVerticle("name.bpdp.tartu.verticles.dsl.DomainSpecificLanguage", true);

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

		// for dialog between machinge
		// This should be the first route
		router.get("/dialog/:messageDlg").handler(that::handleDialog);

		// SPARQL endpoint - REST
		router.get("/sparql/:sparqlQuery").handler(that::handleSparql);

		// for static content, it will take webroot/index.html
		router.route().handler(StaticHandler.create());

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

		System.out.println("Send " + messageDlg + " to tartu.dialog");

		EventBus evb = vertx.eventBus();

		evb.send("tartu.dialog", messageDlg, ar ->  {
			if (ar.succeeded()) {
				response.putHeader("content-type", "text/html");
				response.end("Received reply: " + ar.result().body());
			} else {
				response.putHeader("content-type", "text/html");
				response.end("Gagal maning son: " + ar.cause());
			}
		});


	}

	private void handleSparql(RoutingContext routingContext) {

		/*
		Vertx vertx = Vertx.vertx();
		*/

		HttpServerResponse response = routingContext.response();

		String sparqlQuery = routingContext.request().getParam("sparqlQuery");

		System.out.println("Send " + sparqlQuery + " to tartu.blazegraph");

		BlazegraphService bgsvcs = BlazegraphService.createProxy(vertx, "tartu.blazegraph");
		bgsvcs.save(sparqlQuery);

		response.putHeader("content-type", "text/html");
		response.end("Send " + sparqlQuery + " to tartu.blazegraph");


		/*
		EventBus evb = vertx.eventBus();

		evb.send("tartu.blazegraph", sparqlQuery, ar ->  {
			if (ar.succeeded()) {
				response.putHeader("content-type", "text/html");
				response.end("Received reply: " + ar.result().body());
			} else {
				response.putHeader("content-type", "text/html");
				response.end("Gagal maning son: " + ar.cause());
			}
		});
		*/

	}
}
