package seecov.lib.coverage.core;

public abstract class Coverage implements LineAnalyzer {
	
	public String sourceFilename;
	
	public Coverage(String sourceFilename) {
		this.sourceFilename = sourceFilename;
	}
	
}
