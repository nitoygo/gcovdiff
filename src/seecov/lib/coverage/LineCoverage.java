package seecov.lib.coverage;

import java.util.ArrayList;

public class LineCoverage {
	
	private ArrayList<LineInformation> lines;
	
	private int numberOfExecutableLines;
	private int numberOfExecutedLines;
	
	public LineCoverage() {
		lines = new ArrayList<>();
		
		numberOfExecutableLines = 0;
		numberOfExecutedLines = 0;
	}
	
	public int getNumberOfExecutableLines() {
		return numberOfExecutableLines;
	}
	
	public int getNumberOfExecutedLines() {
		return numberOfExecutedLines;
	}
	
	public ArrayList<LineInformation> getLinesInformation() {
		return lines;
	}
	
	public void setLinesInformation(ArrayList<LineInformation> lines) {
		this.lines = lines;
		
		numberOfExecutableLines = 0;
		numberOfExecutedLines = 0;
		
		for (LineInformation line : lines) {
			if (line.timesExecuted != -1) {
				numberOfExecutableLines += 1;
				numberOfExecutedLines += line.timesExecuted > 0? 1: 0;
			}
		}
	}
	
}
