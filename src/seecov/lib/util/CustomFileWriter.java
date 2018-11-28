package seecov.lib.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomFileWriter implements AutoCloseable {
	private BufferedWriter writer;
	
	public CustomFileWriter(String filepath) throws Exception {
		writer = new BufferedWriter(new FileWriter(filepath));
	}
	
	public void writeLine(String buffer) throws Exception {
		writer.write(buffer);
		writer.append('\n');
	}
	
	public void write(String buffer) throws Exception {
		writer.write(buffer);
	}
	
	public void close() throws IOException {
		writer.close();
	}
}
