package xyz.bpdp.vastix.helper;

/*
 * @author <a href="http://bpdp.xyz">Bambang Purnomosidi D. P.</a>
 */
public class VastixRunner {

    private static final String VASTIX_JAVA_SRC_DIR = "src/main/java/";
    private static final String VASTIX_GROOVY_SRC_DIR = "src/main/groovy/";

    public static void runJavaVerticle(Class clazz, boolean isClustered) {
        if (isClustered) {
            RunnerUtil.runJava(VASTIX_JAVA_SRC_DIR, clazz, true);
        } else {
            RunnerUtil.runJava(VASTIX_JAVA_SRC_DIR, clazz, false);
        }
    }

    public static void runGroovyVerticle(String clazz, boolean isClustered) {
        if (isClustered) {
            RunnerUtil.runGroovy(VASTIX_GROOVY_SRC_DIR, clazz, true);
        } else {
            RunnerUtil.runGroovy(VASTIX_GROOVY_SRC_DIR, clazz, false);
        }
    }
}
