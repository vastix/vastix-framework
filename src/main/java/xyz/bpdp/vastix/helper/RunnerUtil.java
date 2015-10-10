package xyz.bpdp.vastix.helper;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RunnerUtil {

    public static void runJava(String prefix, Class clazz, boolean clustered) {
        String srcDir = prefix + clazz.getPackage().getName().replace(".", "/");
        deployJava(srcDir, clazz.getName(), clustered);
    }

    /*
    * use this: runGroovy("src/main/groovy/","name.bpdp.verticles.DomainSpecificLanguage",true)
    */
    public static void runGroovy(String prefix, String clazz, boolean clustered) {
        String src = prefix + clazz.replace(".", "/") + ".groovy";
        deployGroovy(src, clustered);
    }

    public static void deployJava(String srcDir, String verticleID, boolean clustered) {
        System.setProperty("vertx.cwd", srcDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(verticleID);
			} catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (clustered) {

            VertxOptions options = new VertxOptions();

            Vertx.clusteredVertx(options.setClustered(true), res -> {
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

        System.out.println("Deploy verticle " + verticleID);

    }

    public static void deployGroovy(String src, boolean clustered) {
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(src);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };

        if (clustered) {

            VertxOptions options = new VertxOptions();

            Vertx.clusteredVertx(options.setClustered(true), res -> {

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

        System.out.println("Deploy verticle " + src);

    }

}
