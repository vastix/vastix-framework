package name.bpdp.kipo.helper;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.io.File;
import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RunnerUtil {

	public static void runApp(String prefix, Class clazz, boolean clustered) {
		String srcDir = prefix + clazz.getPackage().getName().replace(".", "/");
		execute(srcDir, clazz.getName(), clustered);
	}

	public static void execute(String srcDir, String verticleID, boolean clustered) {
		System.setProperty("vertx.cwd", srcDir);
		Consumer<Vertx> runner = vertx -> {

			try {
				vertx.deployVerticle(verticleID);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		};
		if (clustered) {
			Vertx.clusteredVertx(new VertxOptions().setClustered(true), res -> {
				if (res.succeeded()) {
					Vertx vertx = res.result();
					runner.accept(vertx);
				} else {
					res.cause().printStackTrace();
				}
			});
		} else {
			Vertx vertx = Vertx.vertx();
			runner.accept(vertx);
		}
	}
}
