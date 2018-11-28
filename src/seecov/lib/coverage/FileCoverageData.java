package seecov.lib.coverage;

public class FileCoverageData {
	public String sourceName;
	public LineCoverage lineCoverage;
	
	public FileCoverageData() {
		this.sourceName = "";
		this.lineCoverage = new LineCoverage();
	}
	
	public FileCoverageData(String sourceName, LineCoverage lineCoverage) {
		this.sourceName = sourceName;
		this.lineCoverage = lineCoverage;
	}
}
