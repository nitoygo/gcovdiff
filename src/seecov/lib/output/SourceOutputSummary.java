package seecov.lib.output;

import java.util.ArrayList;

import seecov.lib.code.Source;
import seecov.lib.coverage.FileCoverageData;
import seecov.lib.coverage.LineCoverage;
import seecov.lib.coverage.LineInformation;
import seecov.lib.patch.PatchInfo;

public class SourceOutputSummary {

	public Source code;
	public FileCoverageData coverageData;
	public PatchInfo patchInfo;
	
	private int lineCount;
	private int hitCount;
	
	public SourceOutputSummary() {
		this.code = new Source();
		this.coverageData = new FileCoverageData();
		this.patchInfo = new PatchInfo();
	}
	
	public SourceOutputSummary (Source code, FileCoverageData coverageData, PatchInfo patchInfo) {
		this.code = code;
		this.coverageData = coverageData;
		this.patchInfo = patchInfo;
	}
	
	public void summarize() {
		countAddedExecutableLines();
		countAddedExecutableLinesHit();
	}
	
	public int getAddedExecutableLines() {		
		return lineCount;
	}
	
	public int getAddedExecutableLinesHit() {
		return hitCount;
	}
	

	private void countAddedExecutableLines() {
		LineCoverage coverage = coverageData.lineCoverage;
		ArrayList<LineInformation> linesList = coverage.getLinesInformation();
		LineInformation[] linesArray = linesList.toArray(new LineInformation[linesList.size()]);
		
		lineCount = 0;
		
		for (int lineNumber : patchInfo.modifiedLines) {
			if (linesArray[lineNumber - 1].timesExecuted != -1) {
				lineCount++;
			}
		}
	}
	
	private void countAddedExecutableLinesHit() {
		LineCoverage coverage = coverageData.lineCoverage;
		ArrayList<LineInformation> linesList = coverage.getLinesInformation();
		LineInformation[] linesArray = linesList.toArray(new LineInformation[linesList.size()]);
		
		hitCount = 0;
		
		for (int lineNumber : patchInfo.modifiedLines) {
			if (linesArray[lineNumber - 1].timesExecuted > 0) {
				hitCount++;
			}
		}
	}
	
	
}
