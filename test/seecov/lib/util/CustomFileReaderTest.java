package seecov.lib.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomFileReaderTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws Exception {
		String currentPath = new File(".").getAbsolutePath();
		String filepath = currentPath + "\\test\\seecov\\lib\\testdata\\git-sample.diff";
		
		CustomFileReader reader = new CustomFileReader(filepath);
		
		while (reader.getNextLine() != null) {
			// do nothing
		}
		
		System.out.println("[CustomFileReaderTest] Last line read: " + reader.getLineNumber());
	}

}
