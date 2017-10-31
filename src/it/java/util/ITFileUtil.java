package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ITFileUtil {

	@Test
	public void testGetProjectRootPath() {
		assertEquals("C:\\Users\\Asus\\coding\\lang\\webapp\\AJAXwithDBplus", FileUtil.getProjectRootPath());
	}

}
