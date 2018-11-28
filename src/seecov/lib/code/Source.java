package seecov.lib.code;

import java.util.ArrayList;

import seecov.lib.util.CustomFileReader;

public class Source {
	
	public String filepath;
	ArrayList<String> lines;
	
	public Source() {
		lines = new ArrayList<>();
	}
	
	public Source(String filepath) {
		this.filepath = filepath;
		this.lines = new ArrayList<>();
	}
	
	public ArrayList<String> getLines() throws Exception {
		try(CustomFileReader reader = new CustomFileReader(filepath)) {
			String nextLine;
			
			while ((nextLine = reader.getNextLine()) != null) {
				lines.add(nextLine);
			}
			
			reader.close();
		}
		
		return lines;
	}

}
