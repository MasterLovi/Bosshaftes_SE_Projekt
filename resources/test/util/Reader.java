package test.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 
 * File Reader Utility Class
 *
 */
public class Reader {
	
	/**
	 * 
	 * @param path {String} - Path of the file to read
	 * @param encoding {Charset} - Specifies Encoding for the String containing the files content.
	 * @return {String} - Content of the read file.
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
		
			// Read File into byte Array, convert it to String and return the files content in the specified Character encoding.
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}
