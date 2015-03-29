import org.eclipse.jetty.server.Server;
import com.bigdata.journal.IIndexManager;
import com.bigdata.rdf.sail.webapp.NanoSparqlServer;

import java.util.Map;

public class SparqlEndpoint {

	private int port;
	private final IIndexManager indexManager;
	private final Map<String, String> initParams;

	/**
	* 
	* @param port
	*            The desired port -or- ZERO (0) to use a random open port.
	*/
	public SparqlEndpoint (final int port, final IIndexManager indexManager, final Map<String, String> initParams) {
        
		if (indexManager == null) throw new IllegalArgumentException();

		if (initParams == null) throw new IllegalArgumentException();

		this.port = port;
		this.indexManager = indexManager;
		this.initParams = initParams;

	}

	@Override
	public void run() throws Exception {

		Server server = null;
		try {

			server = NanoSparqlServer.newInstance(port, indexManager, initParams);

			NanoSparqlServer.awaitServerStart(server);

			// Block and wait. The SPARQL endpoint is running.
			server.join();
            
		} catch (Throwable t) {

			log.error(t, t);

		} finally {

			if (server != null) {

				try {

					server.stop();

				} catch (Exception e) {

					log.error(e, e);

				}

				server = null;

				System.out.println("Halted.");
                
			}

		}

	}

}
