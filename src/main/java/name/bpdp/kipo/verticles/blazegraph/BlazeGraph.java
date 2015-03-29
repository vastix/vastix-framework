package name.bpdp.kipo.verticles.blazegraph;

import io.vertx.core.AbstractVerticle;

import name.bpdp.kipo.verticles.blazegraph.SparqlEndpoint;

import java.util.Map;
import java.util.LinkedHashMap;

import com.bigdata.journal.IIndexManager;
import com.bigdata.rdf.sail.BigdataSail;

import org.eclipse.jetty.server.Server;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class BlazeGraph extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		System.out.println("Starting BlazeGraph ... ");
        final int port = 1234; // random port.

        /*
         * Create or re-open a durable database instance using default
         * configuration properties. There are other constructors that allow you
         * to take more control over this process.
         */
        final BigdataSail sail = new BigdataSail();

        sail.initialize();

        try {

            final IIndexManager indexManager = sail.getDatabase()
                    .getIndexManager();

            final Map<String, String> initParams = new LinkedHashMap<String, String>();

            SparqlEndpoint spe = new SparqlEndpoint(port, indexManager, initParams);
			spe.run();

        } finally {

            sail.shutDown();

        }		

	}

	public void stop() {


	}

}
