package test.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  System.out.println(System.getProperty("user.dir"));
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}
