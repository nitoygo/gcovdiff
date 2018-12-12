package seecov.lib.output.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import seecov.lib.code.Source;
import seecov.lib.coverage.FileCoverageData;
import seecov.lib.coverage.GCov;
import seecov.lib.output.ReportMaker;
import seecov.lib.output.SourceOutputSummary;
import seecov.lib.patch.Patch;
import seecov.lib.patch.PatchFactory;
import seecov.lib.patch.PatchInfo;
import seecov.lib.util.CustomFileWriter;

import seecov.lib.output.html.*;

public class HtmlReportMaker implements ReportMaker {
	public String outLocation;
	public String srcLocation;
	public String covLocation;
	public String patchFile;
	public int patchFormat;
	
	@Override
	public void makeReport() throws Exception {
		prepareOutputDirectory();
		writeReport();
	}
	
	private void prepareOutputDirectory() {
		CustomFileWriter.createDirectory(outLocation);
		
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/gcov.css"), outLocation + "gcov.css");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/amber.png"), outLocation + "amber.png");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/emerald.png"), outLocation + "emerald.png");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/glass.png"), outLocation + "glass.png");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/ruby.png"), outLocation + "ruby.png");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/snow.png"), outLocation + "snow.png");
		copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/updown.png"), outLocation + "updown.png");
	}
	
    public static void copyResource(InputStream source , String destination) {
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        }
    }
	
	private void writeReport() throws Exception {
		ArrayList<SourceOutputSummary> moduleSummary = createModuleSummary();
		
		for (SourceOutputSummary sourceSummary : moduleSummary) {
			makeReportForFile(sourceSummary);
		}
		
		makeReportForModule(moduleSummary);
	}
	
	private ArrayList<SourceOutputSummary> createModuleSummary() throws Exception {
		ArrayList<SourceOutputSummary> moduleSummary = new ArrayList<>();
		
		Patch patch = PatchFactory.CreatePatchContext(patchFile, patchFormat);
		ArrayList<PatchInfo> allPatchInfo = patch.getAllPatchInfo();
		
		for (PatchInfo patchInfo : allPatchInfo) {
			
			FileCoverageData coverageData = getCoverageForPatchedFile(patchInfo);
			
			if (coverageData.hasCoveredLines()) {
				SourceOutputSummary sourceOut = new SourceOutputSummary();
				
				sourceOut.code = new Source(srcLocation + patchInfo.relativeFilename);
				sourceOut.coverageData = coverageData;
				sourceOut.patchInfo = patchInfo;
				
				sourceOut.summarize();
				
				moduleSummary.add(sourceOut);
			}
		}
		
		return moduleSummary;
	}
	
	private FileCoverageData getCoverageForPatchedFile(PatchInfo patchInfo) {
		return new GCov(covLocation + patchInfo.relativeFilename).getLineCoverage();
	}
	
	private void makeReportForModule(ArrayList<SourceOutputSummary> moduleSummary) 
	throws Exception {
		String outputName = outLocation + "index" + ".html";
		
		try (CustomFileWriter writer = new CustomFileWriter(outputName)) {
			writeSummaryHtml(writer, moduleSummary);
		}
	}
	
	private void makeReportForFile(SourceOutputSummary fileSummary) throws Exception {
		String relativeName = fileSummary.patchInfo.relativeFilename;
		String outputName = outLocation + relativeName + ".html";
		
		try (CustomFileWriter writer = new CustomFileWriter(outputName)) {
			writeSourceHtml(writer, fileSummary);
		}
	}
	
	private int getAllExecutableLines(ArrayList<SourceOutputSummary> moduleSummary) {
		int count = 0;
		
		for (SourceOutputSummary sourceSummary : moduleSummary) {
			count += sourceSummary.getAddedExecutableLines();
		}
		
		return count;
	}
	
	private int getAllExecutableLinesHit(ArrayList<SourceOutputSummary> moduleSummary) {
		int count = 0;
		
		for (SourceOutputSummary sourceSummary : moduleSummary) {
			count += sourceSummary.getAddedExecutableLinesHit();
		}
		
		return count;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*******************************************************************************
	 * DON'T SCROLL DOWN ANYMORE. IT WILL HURT YOUR EYES!
	 * ...
	 * ...
	 * ...
	 * 
	 * TODO: FIX THIS SHIT!
	 *******************************************************************************/
	
	
	private void writeSummaryHtml(CustomFileWriter writer, ArrayList<SourceOutputSummary> moduleSummary) 
	throws Exception {
		int lines = getAllExecutableLines(moduleSummary);
		int hit = getAllExecutableLinesHit(moduleSummary);
		int hitPercent = (int) ((lines != 0? ((double) hit / lines) : 0) * 100);
		
		writer.writeLine(
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
				"<html lang=\"en\">" +
				"<head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
				"<title>Coverage Information Index</title>" +
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"gcov.css\">" +
				"</head>" +
				"<table width=\"100%\" border=0 cellspacing=0 cellpadding=0>" +
				"<tr><td class=\"title\">Code coverage report</td></tr>" +
				"<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>" +
				"<tr>" +
				"<td width=\"100%\">" +
				"<table cellpadding=1 border=0 width=\"100%\">" +
				"<tr>" +
				"<td width=\"10%\" class=\"headerItem\">Current View</td>" +
				"<td width=\"35%\" class=\"headerValue\">Index</td>" +
				"<td width=\"5%\"></td>" +
				"<td width=\"15%\"></td>" +
				"<td width=\"10%\" class=\"headerCovTableHead\">Hit</td>" +
				"<td width=\"10%\" class=\"headerCovTableHead\">Total</td>" +
				"<td width=\"15%\" class=\"headerCovTableHead\">Coverage</td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"headerItem\"></td>" +
				"<td class=\"headerValue\"></td>" +
				"<td></td>" +
				"<td class=\"headerItem\">Modified Lines:</td>" +
				"<td class=\"headerCovTableEntry\">" + hit + "</td>" +
				"<td class=\"headerCovTableEntry\">" + lines + "</td>"
		);
		
		if (hitPercent < 60) {
			writer.writeLine("<td class=\"headerCovTableEntryLo\">"+ hitPercent + "%</td>");
		} else if (hitPercent < 90) {
			writer.writeLine("<td class=\"headerCovTableEntryMed\">"+ hitPercent + "%</td>");
		} else {
			writer.writeLine("<td class=\"headerCovTableEntryHi\">"+ hitPercent + "%</td>");
		}
		
		writer.writeLine(
				"</tr>"  +
				"<tr><td><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>" +
				"</table>" +
				"</td>" +
				"</tr>" +
				"<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>" +
				"</table>"
			);
		
		writeSummaryBody(writer, moduleSummary);
		writeSummaryFooter(writer, moduleSummary);
		
		writer.writeLine("</body>");
		writer.writeLine("</html>");
	}

	private void writeSummaryBody(CustomFileWriter writer, ArrayList<SourceOutputSummary> moduleSummary)
	throws Exception {
		writer.writeLine(
				"<center>" +
				"<table width=\"80%\" cellpadding=1 cellspacing=1 border=0>" +
				"<tr>" +
				"<td width=\"60%\"><br></td>" +
				"<td width=\"10%\"></td>" +
				"<td width=\"10%\"></td>" +
				"<td width=\"10%\"></td>" +
				"<td width=\"10%\"></td>" +
				"</tr>" +
				"<tr>" +
				"<td class=\"tableHead\">Filename</td>" +
				"<td class=\"tableHead\" colspan=4>Line Coverage</td>" +
				"</tr>"
			);
		
		for (SourceOutputSummary fileSummary : moduleSummary) {
			writeSummaryEntry(writer, fileSummary);
		}
		
		writer.writeLine (
			"</table>" +
			"</center>"
		);
		
	}
	
	private void writeSummaryEntry(CustomFileWriter writer, SourceOutputSummary fileSummary)
	throws Exception {
		String sourceName = fileSummary.patchInfo.relativeFilename;
		String sourceLink = sourceName + ".html";
		
		int hit = fileSummary.getAddedExecutableLinesHit();
		int lines = fileSummary.getAddedExecutableLines();
		int hitPercent = (int) ((lines != 0? ((double) hit / lines) : 0) * 100);
		
		String color;
		String barClass;
		
		if (hitPercent < 60) {
			color = "ruby";
			barClass = "coverPerLo";
		} else if (hitPercent < 90) {
			color = "amber";
			barClass = "coverPerMed";
		} else {
			color = "emerald";
			barClass = "coverPerHi";
		}
		
		writer.writeLine (
				"<tr>"  +
				"<td class=\"coverFile\"><a href=" + sourceLink + ">" + sourceName + "</a></td>" +
				"<td class=\"coverBar\" colspan=2 align=\"center\">" +
				"<table border=0 cellspacing=0 cellpadding=1>" +
				"<tr>" +
				"<img src=\"" + color + ".png\" width=" + (hitPercent * 2) + " height=10 alt=" + hitPercent + "%\">"
			);
		
		if (hitPercent < 100) {
			writer.writeLine (
				"<img src=\"snow.png\" width=" + ((100-hitPercent)*2) + " height=10 alt=" + hitPercent + "%\">"
			);
		}
		
		writer.writeLine (
				"</tr>" +
				"</table>" +
				"</td>" +
				"<td class=\"" + barClass + "\" colspan=1>"+ hitPercent + "&nbsp;%</td>" +
				"<td class=\"" + barClass + "\" colspan=1>" + hit + "/" + lines + "</td>" +
				"</tr>"
			);
	}

	private void writeSummaryFooter(CustomFileWriter writer, ArrayList<SourceOutputSummary> moduleSummary)
	throws Exception {
		writer.writeLine (
				"<br>" +
				"<table width=\"100%\" border=0 cellspacing=0 cellpadding=0>" + 
				"<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>" +
				"<tr><td class=\"versionInfo\">Generated by: SeeCov v1.0</td></tr>" +
				"</table>" +
				"<br>"
			);
	}
	
	private void writeSourceBody(CustomFileWriter writer, SourceOutputSummary fileSummary)
	throws Exception {
		Source code = fileSummary.code;
		PatchInfo patchInfo = fileSummary.patchInfo;
		FileCoverageData coverageData = fileSummary.coverageData;
		
		writer.writeLine(
				"<table cellpadding=0 cellspacing=0 border=0>" +
				"<tr>" +
				"<td><br></td>" +
				"</tr>" +
				"<tr>" +
				"<td>" +
				"<pre class=\"sourceHeading\">          Line data    Source code</pre>" +
				"<pre class=\"source\">"
			);

		ArrayList<String> codeLines = null;
		codeLines = code.getLines();
		
		for (int index = 0; index < codeLines.size(); index++) {
			int hitCount = -1;			
			hitCount = coverageData.lineCoverage.getLinesInformation().get(index).timesExecuted;
			
			String format = "";
			
			if (patchInfo.modifiedLines.contains(index + 1)) {
				
				if (hitCount > 0) {
					format = String.format("<span class=\"lineNum\">%8d </span><span class=\"lineCov\">%10d  : %s</span>", (index + 1), hitCount, codeLines.get(index));
				} else if (hitCount == 0) {
					format = String.format("<span class=\"lineNum\">%8d </span><span class=\"lineNoCov\">%10d  : %s</span>", (index + 1), hitCount, codeLines.get(index));
				} else {
					format = String.format("<span class=\"lineNum\">%8d </span>            : %s", (index + 1), codeLines.get(index));
				}				
			} else {
				if (hitCount > 0) {
					format = String.format("<span class=\"lineNum\">%8d </span>%10d  : %s", (index + 1), hitCount, codeLines.get(index));
				} else if (hitCount == 0) {
					format = String.format("<span class=\"lineNum\">%8d </span>%10d  : %s", (index + 1), hitCount, codeLines.get(index));
				} else {
					format = String.format("<span class=\"lineNum\">%8d </span>            : %s", (index + 1), codeLines.get(index));
				}
			}
			
			writer.writeLine(format);
		}
		
		writer.writeLine (
				"</td>" +
				"</tr>" +
				"</table>"
			);
	}

	private void writeSourceFooter(CustomFileWriter writer, SourceOutputSummary fileSummary)
	throws Exception {
		writer.writeLine(
				"<br>" +
				"<table width=\"100%\" border=0 cellspacing=0 cellpadding=0>" + 
				"<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>" +
				"<tr><td class=\"versionInfo\">Generated by: SeeCov v1.0</td></tr>" +
				"</table>" +
				"<br>"
			);
	}

	private String getCssLocation(String relativePath) {
		String[] paths = relativePath.split("/");
		
		String cssLocation = "gcov.css";
		
		for (int i = 0; i < paths.length - 1; i++) {
			cssLocation = "../" + cssLocation;
		}
		
		return cssLocation;
	}
	
	private void writeSourceHtml(CustomFileWriter writer, SourceOutputSummary fileSummary)
	throws Exception {
		Source code = fileSummary.code;
		PatchInfo patchInfo = fileSummary.patchInfo;
		
		String css = getCssLocation(patchInfo.relativeFilename);
		
		int hit = fileSummary.getAddedExecutableLinesHit();
		int lines = fileSummary.getAddedExecutableLines();
		int hitPercent = (int) ((lines != 0? ((double) hit / lines) : 0) * 100);
		
		writer.writeLine("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		writer.writeLine("<html lang=\"en\">");
		writer.writeLine("<head>");
		writer.writeLine("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		writer.writeLine("<title>Coverage Information - " + patchInfo.relativeFilename + " </title>");
		writer.writeLine("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css + "\">");
		writer.writeLine("</head>");
		writer.writeLine("<table width=\"100%\" border=0 cellspacing=0 cellpadding=0>");
		writer.writeLine("<tr><td class=\"title\">Code coverage report</td></tr>");
		writer.writeLine("<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>");
		writer.writeLine("<tr>");
		writer.writeLine("<td width=\"100%\">");
		writer.writeLine("<table cellpadding=1 border=0 width=\"100%\">");
		writer.writeLine("<tr>");
		writer.writeLine("<td width=\"10%\" class=\"headerItem\">Current View</td>");
		writer.writeLine("<td width=\"35%\" class=\"headerValue\">" + patchInfo.relativeFilename + "</td>");
		writer.writeLine("<td width=\"5%\"></td>");
		writer.writeLine("<td width=\"15%\"></td>");
		writer.writeLine("<td width=\"10%\" class=\"headerCovTableHead\">Hit</td>");
		writer.writeLine("<td width=\"10%\" class=\"headerCovTableHead\">Total</td>");
		writer.writeLine("<td width=\"15%\" class=\"headerCovTableHead\">Coverage</td>");
		writer.writeLine("</tr>");
		writer.writeLine("<tr>");
		writer.writeLine("<td class=\"headerItem\"></td>");
		writer.writeLine("<td class=\"headerValue\"></td>");
		writer.writeLine("<td></td>");
		writer.writeLine("<td class=\"headerItem\">Modified Lines:</td>");
		writer.writeLine("<td class=\"headerCovTableEntry\">" + hit + "</td>");
		writer.writeLine("<td class=\"headerCovTableEntry\">" + lines + "</td>");
		
		if (hitPercent < 60) {
			writer.writeLine("<td class=\"headerCovTableEntryLo\">"+ hitPercent + "%</td>");
		} else if (hitPercent < 90) {
			writer.writeLine("<td class=\"headerCovTableEntryMed\">"+ hitPercent + "%</td>");
		} else {
			writer.writeLine("<td class=\"headerCovTableEntryHi\">"+ hitPercent + "%</td>");
		}
		
		writer.writeLine("</tr>");
		writer.writeLine("<tr><td><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>");
		writer.writeLine("</table>");
		writer.writeLine("</td>");
		writer.writeLine("</tr>");
		writer.writeLine("<tr><td class=\"ruler\"><img src=\"glass.png\" width=3 height=3 alt=\"\"></td></tr>");
		writer.writeLine("</table>");
		
		writeSourceBody(writer, fileSummary);
		writeSourceFooter(writer, fileSummary);
		
		writer.writeLine("</body>");
		writer.writeLine("</html>");
	}
	
}
