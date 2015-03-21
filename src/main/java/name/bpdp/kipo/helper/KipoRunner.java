package name.bpdp.kipo.helper;

import name.bpdp.kipo.helper.RunnerUtil;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class KipoRunner {

	private static final String KIPO_SRC_DIR = "src/main/java/";

	public static void runKipo(Class clazz) {
		RunnerUtil.runApp(KIPO_SRC_DIR, clazz, false);
	}

}
