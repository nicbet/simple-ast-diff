package com.nicolasbettenburg.tools.utils;

import java.util.*;
import java.io.*;

/**
 * Recursive file listing under a specified directory.
 */
public final class FileListing {

	public static File[] listFilesAsArray(File directory, FilenameFilter filter, boolean recurse) {
		Collection<File> files = listFiles(directory, filter, recurse);
		File[] arr = new File[files.size()];
		files.toArray(arr);
		Arrays.sort(arr);
		return arr;
	}

	public static Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) {
		Vector<File> files = new Vector<File>();
		File[] entries = directory.listFiles();
		
		for (File entry : entries) {
			// If there is no filter or the filter accepts the file / directory, add it to the list
			if (filter == null || filter.accept(directory, entry.getName())) {
				files.add(entry);
			}
			
			// If the file is a directory and the recurse flag is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}
		// Return collection of files
		return files;
	}
}
