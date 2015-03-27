package name.bpdp.kipo.helper;

import name.bpdp.kipo.helper.RunnerUtil;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class KipoRunner {

	private static final String KIPO_JAVA_SRC_DIR = "src/main/java/";
	private static final String KIPO_GROOVY_SRC_DIR = "src/main/groovy/";

	public static void runJavaVerticle(Class clazz, boolean isClustered) {
		if (isClustered) {
			RunnerUtil.runJava(KIPO_JAVA_SRC_DIR, clazz, true);
		} else {
			RunnerUtil.runJava(KIPO_JAVA_SRC_DIR, clazz, false);
		}
	}

	public static void runGroovyVerticle(String clazz, boolean isClustered) {
		if (isClustered) {
			RunnerUtil.runGroovy(KIPO_GROOVY_SRC_DIR, clazz, true);
		} else {
			RunnerUtil.runGroovy(KIPO_GROOVY_SRC_DIR, clazz, false);
		}
	}

}
