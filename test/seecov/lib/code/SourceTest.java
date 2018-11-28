package seecov.lib.code;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seecov.lib.code.Source;

class SourceTest {

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void test() throws Exception {
		String currentPath = new File("").getAbsolutePath();
		Source code = new Source(currentPath + "\\test\\seecov\\lib\\code\\SourceTest.java");
		
		ArrayList<String> lines = code.getLines();
		
		System.out.println("[SourceTest] " + code.filepath + " : " + lines.size());
		assertTrue(lines.size() != 0);
	}

}
