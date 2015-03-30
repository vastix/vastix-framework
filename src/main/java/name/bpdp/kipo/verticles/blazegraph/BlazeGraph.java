package name.bpdp.kipo.verticles.blazegraph;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

// cancelled
//import name.bpdp.kipo.verticles.blazegraph.SparqlEndpoint;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import org.openrdf.repository.Repository;

import com.bigdata.rdf.sail.BigdataSailRepository;
import com.bigdata.journal.IIndexManager;
import com.bigdata.rdf.sail.BigdataSail;

// for SPARQL endpoint - cancelled
//import org.eclipse.jetty.server.Server;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class BlazeGraph extends AbstractVerticle {

	/**
	* Load a Properties object from a file.
	* 
	* @param resource
	* @return
	* @throws Exception
	*/

	private Properties loadProperties(String resource) throws Exception {

		System.out.println("1\n");
		Properties p = new Properties();

		System.out.println("2\n");
		InputStream is = getClass().getResourceAsStream(resource);

		System.out.println("3\n");
		p.load(new InputStreamReader(new BufferedInputStream(is)));
		System.out.println("4\n");
		return p;
	}


	@Override
	public void start() throws Exception {

		System.out.println("Starting BlazeGraph ... ");

		// cancelled
        //final int port = 1234; // put 0 if you want random port.

        /*
         * Create or re-open a durable database instance using default
         * configuration properties. There are other constructors that allow you
         * to take more control over this process.
         */

		Properties bgProperties = this.loadProperties("src/resources/RWStore.properties");

        final BigdataSail sail = new BigdataSail(bgProperties);
		Repository repo = new BigdataSailRepository(sail);

        repo.initialize();

		/* SPARQL endpoint is cancelled because of its blocking feature
        try {

            final IIndexManager indexManager = sail.getDatabase().getIndexManager();
            final Map<String, String> initParams = new LinkedHashMap<String, String>();

            SparqlEndpoint spe = new SparqlEndpoint(port, indexManager, initParams);
			spe.run();

        } finally {

            sail.shutDown();

        }
		*/

		EventBus eb = vertx.eventBus();

		MessageConsumer<String> consumer = eb.consumer("kipo.blazegraph");
		consumer.handler(message -> {
			System.out.println("Blazegraph has received a message: " + message.body());
			message.reply("Reply from kipo.blazegraph!");
		});

		consumer.completionHandler(res -> {
		if (res.succeeded()) {
			System.out.println("The handler registration for Blazegraph verticle has reached all nodes");
	  	} else {
			System.out.println("Registration failed!");
		}
		});

	}

	public void stop() {


	}

}
