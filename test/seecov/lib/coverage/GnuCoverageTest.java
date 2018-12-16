package seecov.lib.coverage;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seecov.lib.coverage.core.FileCoverageData;
import seecov.lib.coverage.gnu.GCov;

class GnuCoverageTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws Exception {
		String currentPath = new File(".").getAbsolutePath();
		GCov gcov = new GCov(currentPath + "\\test\\seecov\\lib\\testdata\\hexdump.c");
		
		FileCoverageData data = gcov.getLineCoverage();
		
		System.out.println("[GnuCoverageTest] " + data.sourceName);
		System.out.println("[GnuCoverageTest] " + data.lineCoverage.getNumberOfExecutableLines());
		System.out.println("[GnuCoverageTest] " + data.lineCoverage.getNumberOfExecutedLines());

	}

}
