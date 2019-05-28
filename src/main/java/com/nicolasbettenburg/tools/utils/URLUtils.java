/*
 * $Id: URLUtils.java,v 1.9 2006/04/30 19:35:04 zimmerth Exp $
 * 
 * LICENSE:
 */
package com.nicolasbettenburg.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Thomas Zimmermann, zimmerth@cs.uni-sb.de
 */
public class URLUtils {


	/**
	 * Copies an internet resource.
	 * 
	 * @param url path to the original resource.
	 * @param file name of the destination file.
	 * @throws IOException if any file system error occurs.
	 */
	public static String copyResourceToFile(
		URL url,
		File file,
		int waitSec,
		final boolean follow,
		final boolean isXML) throws IOException {

			class URLConnectionThread extends Thread {

				private URL myUrl;
				private StringBuffer buf;
				private String contentType = null;
				private boolean finished = false;
				private IOException exception = null;

				public URLConnectionThread(URL myUrl, StringBuffer buf) {
					this.myUrl = myUrl;
					this.buf = buf;
				}

				public void run() {
	
					HttpURLConnection con = null;
					
					try {
						finished = false;

						con = (HttpURLConnection) myUrl.openConnection();
                        con.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/312.1 (KHTML, like Gecko) Safari/312");
                        con.setInstanceFollowRedirects(follow);
						con.connect();
						contentType = con.getContentType();
						BufferedReader data = new BufferedReader
							(new InputStreamReader(con.getInputStream()));

						char[] input = new char[4096];
						int len = 0;
						while ((len = data.read(input)) > 0) {
							buf.append(input, 0, len);
						}
						data.close();
						
						finished = true;
					} catch (FileNotFoundException e) {
						exception = e;
					} catch (IOException e) {
						exception = e;
					}
					
					if (con != null) {
						con.disconnect();
					}
				}
				
				public boolean isFinished() {
					return finished;
				}
				public String getContentType() {
					return contentType;
				}
				public IOException getException() {
					return exception;
				}
			}

			StringBuffer buf = new StringBuffer(4000);
			URLConnectionThread t = new URLConnectionThread(url, buf);

			t.start();
			try {
				t.join(waitSec * 1000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			t.interrupt();

			if (t.isFinished()) {
				if (file.getParentFile() != null) {
					file.getParentFile().mkdirs();
				}
				
				FileWriter w = new FileWriter(file);
				if (isXML) {
				    w.write(XMLUtils.removeInvalidXMLCharacters(buf.toString()));
				} else {
				    w.write(buf.toString());				    
				}
				w.close();
			} else if (t.getException() != null){
			    throw t.getException();
			} else {
			    throw new IOException("Could not download " + url);
			}
			return t.getContentType();
	}


	/**
	 * Copies an internet resource.
	 * 
	 * @param urlName path to the original resource.
	 * @param fileName name of the destination file.
	 * @throws IOException if any file system error occurs.
	 */
	public static String copyResourceToFile(
		String urlName,
		String fileName,
		int waitSec, 
		final boolean follow,
		boolean isXML)
		throws IOException {

		URL url = new URL(urlName);
		File file = new File(fileName);
		return URLUtils.copyResourceToFile(url, file, waitSec, follow, isXML);
	}
	
}
