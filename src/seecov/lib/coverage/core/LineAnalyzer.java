package seecov.lib.coverage.core;

public interface LineAnalyzer {
	public abstract FileCoverageData getLineCoverage() throws Exception;
}
