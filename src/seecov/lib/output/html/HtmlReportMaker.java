package seecov.lib.output.html;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import seecov.lib.code.Source;
import seecov.lib.coverage.FileCoverageData;
import seecov.lib.output.ReportMaker;
import seecov.lib.output.FileSummary;
import seecov.lib.output.ModuleSummary;
import seecov.lib.patch.PatchInfo;
import seecov.lib.util.CustomFileWriter;


public class HtmlReportMaker extends ReportMaker {

	protected void prepareOutputDirectory() {
		CustomFileWriter.createDirectory(outLocation);
		
		try {
			
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/gcov.css"), outLocation + "gcov.css");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/amber.png"), outLocation + "amber.png");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/emerald.png"), outLocation + "emerald.png");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/glass.png"), outLocation + "glass.png");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/ruby.png"), outLocation + "ruby.png");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/snow.png"), outLocation + "snow.png");
			copyResource(getClass().getResourceAsStream("/seecov/lib/output/html/files/updown.png"), outLocation + "updown.png");
			
		} catch(Exception e) {
			throw new RuntimeException("Could not copy files for html output to destination!");
		}
	}
	
	@Override
	protected void makeReportSummary(ModuleSummary moduleSummary) {
		String outputName = outLocation + "index" + ".html";
		
		try (CustomFileWriter writer = new CustomFileWriter(outputName)) {
			
			writeSummaryHtml(writer, moduleSummary);
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to create report summary");
		}
	}

	@Override
	protected void makeReportForFile(FileSummary fileSummary) {
		String relativeName = fileSummary.patchInfo.relativeFilename;
		String outputName = outLocation + relativeName + ".html";
		
		try (CustomFileWriter writer = new CustomFileWriter(outputName)) {
			
			writeSourceHtml(writer, fileSummary);
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to create report for: " + relativeName);
		}
	}
	
	private static void copyResource(InputStream source , String destination) throws IOException {
		Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
	}

	
	
	
	/*=====================================================================
	 * 
	 * HTML code below
	 * TODO: Needs some serious improvement
	 * 
	 *--------------------------------------------------------------------*/
	
	private void writeSummaryHtml(CustomFileWriter writer, ModuleSummary moduleSummary) 
	throws Exception {
		int lines = moduleSummary.getExecutableLines();
		int hit = moduleSummary.getExecutableLinesHit();
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

	private void writeSummaryBody(CustomFileWriter writer, ModuleSummary moduleSummary)
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
		
		int fileCount = moduleSummary.getFileCount();
		for (int i = 0; i < fileCount; i++) {
			writeSummaryEntry(writer, moduleSummary.getFileSummaryAt(i));
		}
		
		writer.writeLine (
			"</table>" +
			"</center>"
		);
		
	}
	
	private void writeSummaryEntry(CustomFileWriter writer, FileSummary fileSummary)
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

	private void writeSummaryFooter(CustomFileWriter writer, ModuleSummary moduleSummary)
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
	
	private void writeSourceBody(CustomFileWriter writer, FileSummary fileSummary)
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
			hitCount = coverageData.lineCoverage.getLineInfoAt(index).timesExecuted;
			
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

	private void writeSourceFooter(CustomFileWriter writer, FileSummary fileSummary)
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
	
	private void writeSourceHtml(CustomFileWriter writer, FileSummary fileSummary)
	throws Exception {
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
