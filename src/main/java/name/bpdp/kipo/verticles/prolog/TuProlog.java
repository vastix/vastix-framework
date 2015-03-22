package name.bpdp.kipo.verticles.prolog;

import io.vertx.core.AbstractVerticle;
import alice.tuprolog.*;

/*
 * @author <a href="http://bpdp.name">Bambang Purnomosidi</a>
 *
 */
public class TuProlog extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		Prolog engine = new Prolog();
		SolveInfo info = engine.solve("append(X,Y,[1,2]).");
		while (info.isSuccess()) {
			System.out.println("solution: " + info.getSolution() + " - bindings: " + info);
			if (engine.hasOpenAlternatives()) {
				info = engine.solveNext();
			} else {
				break;
			}
		}
	}

}
