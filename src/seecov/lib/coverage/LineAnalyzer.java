package seecov.lib.coverage;

public interface LineAnalyzer {
	public abstract FileCoverageData getLineCoverage() throws Exception;
}
