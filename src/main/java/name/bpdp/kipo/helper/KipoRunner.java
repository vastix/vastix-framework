package name.bpdp.kipo.helper;

import name.bpdp.kipo.helper.RunnerUtil;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class KipoRunner {

	private static final String KIPO_SRC_DIR = "src/main/java/";

	public static void runVerticle(Class clazz, boolean isClustered) {
		if (isClustered) {
			RunnerUtil.run(KIPO_SRC_DIR, clazz, true);
		} else {
			RunnerUtil.run(KIPO_SRC_DIR, clazz, false);
		}
	}

}
