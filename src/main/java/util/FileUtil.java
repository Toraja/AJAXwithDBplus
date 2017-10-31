package util;

import java.io.File;

// Not in use (as AppProperties is not in use)
public class FileUtil {
	private static final String CLASS_PATH_COMMON = "/target";
	private static final String MAIN_CLASS_PATH = CLASS_PATH_COMMON + "/classes";
	private static final String TEST_CLASS_PATH = CLASS_PATH_COMMON + "/test-classes";

	public static String getProjectRootPath(){
		return new File("").getAbsolutePath();
	}
	
	public static String getMainClassPath(){
		return getProjectRootPath() + MAIN_CLASS_PATH;
	}
	
	public static String getTestClassPath(){
		return getProjectRootPath() + TEST_CLASS_PATH;
	}
}
