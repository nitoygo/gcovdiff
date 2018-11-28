package seecov.lib.coverage;

public class LineInformation {
	public int lineNumber;
	public int timesExecuted;
	
	public LineInformation(int lineNumber, int timesExecuted) {
		this.lineNumber = lineNumber;
		this.timesExecuted = timesExecuted;
	}
}
