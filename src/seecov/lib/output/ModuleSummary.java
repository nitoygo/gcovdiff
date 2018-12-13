package seecov.lib.output;

import java.util.ArrayList;

public class ModuleSummary {

	public ArrayList<FileSummary> fileSummaries;
	
	public ModuleSummary() {
		fileSummaries = new ArrayList<>();
	}
	
	public int getFileCount() {
		return fileSummaries.size();
	}
	
	public FileSummary getFileSummaryAt(int index) {
		return fileSummaries.get(index);
	}
	
	public void addFileSummary(FileSummary fileSummary) {
		fileSummaries.add(fileSummary);
	}
	
	public int getExecutableLines() {
		int count = 0;
		
		for (FileSummary sourceSummary : fileSummaries) {
			count += sourceSummary.getAddedExecutableLines();
		}
		
		return count;
	}
	
	public int getExecutableLinesHit() {
		int count = 0;
		
		for (FileSummary sourceSummary : fileSummaries) {
			count += sourceSummary.getAddedExecutableLinesHit();
		}
		
		return count;
	}
}
