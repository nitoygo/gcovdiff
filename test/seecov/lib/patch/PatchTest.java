package seecov.lib.patch;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seecov.lib.patch.GitPatch;
import seecov.lib.patch.PatchParser;
import seecov.lib.patch.PatchInfo;

class PatchTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGitPatch() throws Exception {
		String currentPath = new File(".").getAbsolutePath();
		String patchPath = currentPath + "\\test\\seecov\\lib\\testdata\\hexdump.diff";
		
		Patch patch = PatchFactory.CreatePatchContext(patchPath, Patch.PATCH_TYPE_UNIFIED_GIT);
		
		ArrayList<PatchInfo> allSummary = patch.getAllPatchInfo();
		
		assertTrue(allSummary.size() > 0);
		
		for (PatchInfo summary : allSummary) {
			assertTrue(summary.modifiedLines.size() != 0);
		}
	}

	@Test
	void testUnknownPatch() throws Exception {
		String currentPath = new File(".").getAbsolutePath();
		String patchPath = currentPath + "\\test\\seecov\\lib\\testdata\\git-sample.diff";
		
		Patch patch = PatchFactory.CreatePatchContext(patchPath, 0xb00b);
		assertEquals(null, patch);
	}
	
	@Test
	void testPatchFactoryConstructor() throws Exception {		
		PatchFactory factory = new PatchFactory();
		assertNotNull(factory);
	}
}
