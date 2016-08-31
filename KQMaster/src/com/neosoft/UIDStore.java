package com.neosoft;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

public class UIDStore {
	private Map<String, Boolean> uids = new HashMap<String, Boolean>();

	public void load(File file) throws IOException {
		if (file != null && file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String uid = reader.readLine();
				while (uid != null) {
					uids.put(uid, Boolean.FALSE);
					uid = reader.readLine();
				}
			} finally {
				reader.close();
			}
		}
	}
	
	public void store(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		try {
			Iterator<String> iterator = uids.keySet().iterator();
			while (iterator.hasNext()) {
				String uid = iterator.next();
				if (uids.get(uid) == Boolean.TRUE) {
					writer.write(uid);
					if (iterator.hasNext()) {
						writer.write('\n');
					}
				}
			}
		} finally {
			writer.close();
		}
	}

	// if we've seen this UID on the server, store it in the list
	// if it was not already in the list, return true to indicate
	// that the mail is new
	public boolean isNew(String uid) {
		boolean inList = uids.containsKey(uid);
		uids.put(uid, Boolean.TRUE);
		return !inList;
	}
}
