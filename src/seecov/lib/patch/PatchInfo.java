package seecov.lib.patch;

import java.util.ArrayList;

public class PatchInfo {
	public String relativeFilename;
	public ArrayList<Integer> modifiedLines;
	
	public PatchInfo() {
		this.relativeFilename = "";
		modifiedLines = new ArrayList<>();
	}
}
