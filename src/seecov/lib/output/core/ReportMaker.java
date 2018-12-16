package seecov.lib.output.core;

import java.util.ArrayList;

import seecov.lib.code.Source;
import seecov.lib.coverage.core.FileCoverageData;
import seecov.lib.coverage.gnu.GCov;
import seecov.lib.output.core.FileSummary;
import seecov.lib.output.core.ReportMaker;
import seecov.lib.patch.Patch;
import seecov.lib.patch.PatchFactory;
import seecov.lib.patch.PatchInfo;

public abstract class ReportMaker {
	
	public String outLocation;
	public String srcLocation;
	public String covLocation;
	public String patchFile;
	public int patchFormat;
	
	/*=====================================================================
	 * 
	 * Public APIs to be called by application
	 * 
	 *--------------------------------------------------------------------*/
	
	public void makeReport() {
		prepareOutputDirectory();
		writeReport();
	}
	
	
	/*=====================================================================
	 * 
	 * Abstract methods to be implemented by report maker sub-classes
	 * 
	 *--------------------------------------------------------------------*/
	
	protected abstract void prepareOutputDirectory();
	
	protected abstract void makeReportSummary(ModuleSummary moduleSummary);
	
	protected abstract void makeReportForFile(FileSummary fileSummary);
	
	
	/*=====================================================================
	 * 
	 * Private methods
	 * 
	 *--------------------------------------------------------------------*/
	
	private void writeReport() {
		ModuleSummary moduleSummary = createModuleSummary();
		
		int size = moduleSummary.getFileCount();
		for (int i = 0; i < size; i++) {
			makeReportForFile(moduleSummary.getFileSummaryAt(i));
		}
		
		makeReportSummary(moduleSummary);
	}
	
	private ModuleSummary createModuleSummary() {
		ModuleSummary moduleSummary = new ModuleSummary();
		
		Patch patch = PatchFactory.CreatePatchContext(patchFile, patchFormat);
		ArrayList<PatchInfo> allPatchInfo = patch.getAllPatchInfo();
		
		for (PatchInfo patchInfo : allPatchInfo) {
			
			FileCoverageData coverageData = getCoverageForPatchedFile(patchInfo);
			
			if (coverageData.hasCoveredLines()) {
				FileSummary sourceOut = new FileSummary();
				
				sourceOut.code = new Source(srcLocation + patchInfo.relativeFilename);
				sourceOut.coverageData = coverageData;
				sourceOut.patchInfo = patchInfo;
				
				sourceOut.summarize();
				
				moduleSummary.addFileSummary(sourceOut);
			}
		}
		
		return moduleSummary;
	}
	
	private FileCoverageData getCoverageForPatchedFile(PatchInfo patchInfo) {
		return new GCov(covLocation + patchInfo.relativeFilename).getLineCoverage();
	}

}
