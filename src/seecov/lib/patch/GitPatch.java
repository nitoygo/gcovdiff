package seecov.lib.patch;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seecov.lib.util.CustomFileReader;

/** 
 * @brief A Class for reading a git formatted patch
 */
public final class GitPatch extends Patch {
	
	/** 
	 * @brief Denotes the current line is a start of a new file and its diffs.
	 * File marker always starts with 'd' as in diff --git
	 */
	private final int HEADER_TYPE_FILE = 'd';
	
	/** 
	 * @brief Denotes the current line is a patch chunk header.
	 * A patch chunk header should always starts with '@'
	 */
	private final int HEADER_TYPE_CHUNK = '@';
	
	/** 
	 * @brief Denotes out of lines to parse for headers
	 */
	private final int HEADER_TYPE_EMPTY = -1;
	
	/** 
	 * @brief Diff'd files pattern for extracting source/destination files
	 */
	private final Pattern headerFilePattern = Pattern.compile("^(diff --git) a/(.*) b/(.*)");
	
	/**
	 * @brief Creates a patch object from given filepath
	 */
	public GitPatch(String patchFilename) {
	 	super(patchFilename);
	}
	
	/**
	 * @brief Generate a summary from the patch file
	 */
	public ArrayList<PatchInfo> getAllPatchInfo()  throws Exception {
		ArrayList<PatchInfo> allSummary = new ArrayList<>();
		CustomFileReader reader = new CustomFileReader(patchFilename);
		String sourceFname;
		
		while (!(sourceFname = getNextFileMarker(reader)).isEmpty()) {
			PatchInfo fileSummary = getSummaryForFile(reader, sourceFname);
			allSummary.add(fileSummary);
		}
		
		reader.close();
		
		return allSummary;
	}
	
	/**
	 * @brief Seeks the next file marker and returns the filename
	 */
	private String getNextFileMarker(CustomFileReader reader) {
		String buffer;
		
		while ((buffer = reader.getNextLine()) != null) {
			String filename = extractDestName(buffer);
			if (!filename.isEmpty()) {
				return filename;
			}
		}
		
		return "";
	}
	
	private String extractDestName(String buffer) {
		Matcher m = headerFilePattern.matcher(buffer);
		if (m.find()) {
			return m.group(3);
		}
		
		return "";
	}

	/**
	 * @brief Gets summary or all modified lines of the current filename
	 */
	private PatchInfo getSummaryForFile(
			CustomFileReader reader, String filename) throws Exception {
		
		PatchInfo fileSummary = new PatchInfo();
		ArrayList<Integer> modifiedLines = new ArrayList<>();
		
		while (getNextHeader(reader) == HEADER_TYPE_CHUNK) {
			ArrayList<Integer> parsedLines = getModifiedLinesFromChunk(reader);
			modifiedLines.addAll(parsedLines);
		}
		
		fileSummary.relativeFilename = filename;
		fileSummary.modifiedLines = modifiedLines;
		
		return fileSummary;
	}
	
	/**
	 * @brief Finds and returns the next header
	 */
	private int getNextHeader(CustomFileReader reader) {
		int header = HEADER_TYPE_EMPTY;
		
		while (true) {
			int tempSymbol = reader.peek();
			
			if (tempSymbol == -1) {
				break;
			}
			
			if (isHeader(tempSymbol)) {
				header = tempSymbol;
				break;
			}
			
			reader.getNextLine();
		}
		
		return header;
	}
	
	/**
	 * @brief Check if the next starting symbol is a header
	 */
	private boolean isHeader(int symbol) {
		return (symbol == HEADER_TYPE_CHUNK || symbol == HEADER_TYPE_FILE);
	}
	
	/**
	 * @brief Gets the modified lines from the relative chunk/context
	 */
	private ArrayList<Integer> getModifiedLinesFromChunk(CustomFileReader reader)
	throws Exception {
		
		ArrayList<Integer> modifiedLines = new ArrayList<>();
		String chunkHeader = reader.getNextLine();
		
		String markerNew = chunkHeader.substring(chunkHeader.indexOf('+'));
		
		String[] relativeInfo = markerNew.split(",");
		int startLineNum = Integer.parseInt(relativeInfo[0]);
		
		int line = startLineNum;
		
		while (peekedLineIsCode(reader)) {
			String lineStr = reader.getNextLine();
			
			if (isAdded(lineStr)) {
				modifiedLines.add(line);
			} else if (isDeleted(lineStr)) {
				line--;
			}
			
			line++;
		}
	
		return modifiedLines;
	}

	/**
	 * @brief Checks whether the next line is still a part of code
	 */
	private boolean peekedLineIsCode(CustomFileReader reader) {
		int leadingSymbol = -1;
		
		leadingSymbol = reader.peek();
		if (leadingSymbol != -1) {

			// source lines always starts with a white space, a plus, or a minus 
			if (leadingSymbol == ' ' || leadingSymbol == '+' || leadingSymbol == '-' ) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @brief Checks if buffer is an added code
	 */
	private boolean isAdded(String buffer) {
		return buffer.startsWith("+");
	}

	/**
	 * @brief Checks if buffer is an added code
	 */
	private boolean isDeleted(String buffer) {
		return buffer.startsWith("-");
	}
}
