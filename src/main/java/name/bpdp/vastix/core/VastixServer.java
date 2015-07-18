package name.bpdp.vastix.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.RoutingContext;

import io.vertx.core.Vertx;

import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import name.bpdp.vastix.helper.VastixRunner;
import name.bpdp.vertx.blazegraph.BlazegraphService;
// Verticles
//import name.bpdp.vastix.verticles.blazegraph.BlazeGraph;
//import name.bpdp.vastix.verticles.prolog.TuProlog;

import io.vertx.core.json.JsonObject;
import io.vertx.core.DeploymentOptions;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class VastixServer extends AbstractVerticle {

    private VastixServer that = this;

    public static void main(String[] args) {
        
    	VastixRunner.runJavaVerticle(VastixServer.class, true);
        //	KipoRunner.runJavaVerticle(BlazeGraph.class, true);
        //	KipoRunner.runJavaVerticle(TuProlog.class, true);

    	Vertx vertx = Vertx.vertx();

	    JsonObject config = new JsonObject().put("address", "vastix.blazegraph");
    	DeploymentOptions depOptions = new DeploymentOptions().setConfig(config);

    	vertx.deployVerticle("service:name.bpdp.blazegraph-service", depOptions, res -> {
            if (res.succeeded()) {
                System.out.println("Start service - succeed");
            } else {
		        System.out.println("Start service - failed");
            }
	    });

	    VastixRunner.runGroovyVerticle("name.bpdp.vastix.verticles.dsl.DomainSpecificLanguage", true);

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

	    System.out.println("Send " + messageDlg + " to vastix.dialog");

	    EventBus evb = vertx.eventBus();

	    evb.send("vastix.dialog", messageDlg, ar ->  {
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

	    System.out.println("Send " + sparqlQuery + " to vastix.blazegraph");

	    BlazegraphService bgsvcs = BlazegraphService.createProxy(vertx, "vastix.blazegraph");
	    bgsvcs.save(sparqlQuery);

	    response.putHeader("content-type", "text/html");
	    response.end("Send " + sparqlQuery + " to vastix.blazegraph");


	    /*
	        EventBus evb = vertx.eventBus();

            evb.send("vastix.blazegraph", sparqlQuery, ar ->  {
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
