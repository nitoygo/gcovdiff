package seecov.lib.coverage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seecov.lib.util.CustomFileReader;

/** 
 * GCOV named longer for naming's sake
 * @brief Reads a raw gcov file, then parse it to be programmer-friendly
 */
public class GCov extends Coverage {
	
	private final Pattern lineCovPattern = Pattern.compile("^\\s*(\\#+|-|\\d+)\\s*:\\s*(\\d+)\\s*:");
	
	private final String gcovExtension = ".gcov";
	
	public GCov(String sourceFilename) {
		super(sourceFilename);
	}
	
	@Override
	public FileCoverageData getLineCoverage() throws Exception {
		ArrayList<LineInformation> linesInformation = getLineCoverage(sourceFilename + gcovExtension);
		
		LineCoverage lineCoverage = new LineCoverage();
		lineCoverage.setLinesInformation(linesInformation);
		
		FileCoverageData fileCoverageData = new FileCoverageData();
		
		fileCoverageData.sourceName = sourceFilename;
		fileCoverageData.lineCoverage = lineCoverage;
		
		return fileCoverageData;
	}
	
	private ArrayList<LineInformation> getLineCoverage(String gcovFilename) throws Exception {
		CustomFileReader reader = new CustomFileReader(gcovFilename);
		ArrayList<LineInformation> allLineCoverage = new ArrayList<>();
		LineInformation nextLineCoverage;
		
		while((nextLineCoverage = getNextLineCoverage(reader)) != null) {
			allLineCoverage.add(nextLineCoverage);
		}
		
		return allLineCoverage;
	}
	
	private LineInformation getNextLineCoverage(CustomFileReader reader) {
		String buffer;
		int lineNumber;
		int timesExecuted;
		
		while ((buffer = reader.getNextLine()) != null) {
			Matcher m = lineCovPattern.matcher(buffer);
			if (m.find()) {
				lineNumber = Integer.parseInt(m.group(2));
				if (lineNumber > 0) {
					if (m.group(1).startsWith("-"))  {
						timesExecuted = -1;
					} else if (m.group(1).startsWith("#")) {
						timesExecuted = 0;
					} else {
						timesExecuted = Integer.parseInt(m.group(1));
					}
					
					return new LineInformation(lineNumber, timesExecuted);
				}
			} else {
				System.out.println("not match " + buffer);
			}
		}
		
		return null;
	}

}
