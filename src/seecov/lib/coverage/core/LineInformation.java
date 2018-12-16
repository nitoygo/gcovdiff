package seecov.lib.coverage.core;

public class LineInformation {
	public int lineNumber;
	public int timesExecuted;
	
	public LineInformation(int lineNumber, int timesExecuted) {
		this.lineNumber = lineNumber;
		this.timesExecuted = timesExecuted;
	}
}
