package seecov.lib.util.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomFileReader implements AutoCloseable {

	private BufferedReader reader;
	private int lineNumber;
	
	public CustomFileReader(String filepath) throws Exception {
		reader = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(filepath), 
						"UTF-8"
					)
				);
		
		lineNumber = 0;
	}
	
	public String getNextLine() {
		String lineBuffer = null;
		
		try {
			lineBuffer = reader.readLine();
			
			lineNumber++;
		} catch (Exception e) {}
		
		return lineBuffer;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int peek() {
		int singleChar = -1;
		
		try {
			reader.mark(1);
			singleChar = reader.read();
			reader.reset();
		}
		catch (Exception e) {}
		
		return singleChar;
	}
	
	public void close() throws IOException {
		reader.close();
	}
	
}
