package seecov.lib.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
		writer = new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(filepath), 
						StandardCharsets.UTF_8
					)
				);
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
