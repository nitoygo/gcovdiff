package seecov.lib.output;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seecov.lib.coverage.Coverage;
import seecov.lib.patch.Patch;

class HtmlReportMakerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testHtmlReportMakerForGcov() throws Exception {
		String currentPath = new File(".").getAbsolutePath();
		String patchFile = currentPath + "\\test\\seecov\\lib\\testdata\\hexdump.diff";
		String coverageRoot = currentPath + "\\test\\seecov\\lib\\testdata\\";
		String sourceRoot = currentPath + "\\test\\seecov\\lib\\testdata\\";
		
		HtmlReportMaker hrm = new HtmlReportMaker();
		hrm.outLocation = sourceRoot;
		hrm.srcLocation = sourceRoot;
		hrm.covLocation = coverageRoot;
		hrm.patchFile = patchFile;
		hrm.patchFormat = Patch.PATCH_TYPE_UNIFIED_GIT;
		
		hrm.makeReport();
	}

}
