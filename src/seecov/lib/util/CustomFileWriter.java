package seecov.lib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class CustomFileWriter implements AutoCloseable {
	private BufferedWriter writer;
	
	public static void createDirectory(String pathname) {
		File path = new File(pathname);
		path.mkdirs();
	}
	
	public CustomFileWriter(String filepath) throws Exception {
		// in case directory does not exist yet, create it
		createDirectory(Paths.get(filepath).getParent().toString());
		
		// then open buffer
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
