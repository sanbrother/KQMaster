package com.neosoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	private static String workingPath = "";
	
	private final static String[] passwdSuffixArray = {
		"!", "@", "#", "$", "%", "^", "&", "*", "(", ")" 
	};

	public static final void main(String[] args) {
		boolean ret = false;
		String passwdSuffix = null;

		if (args.length != 1) {
			return;
		}

		workingPath = args[0];

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(workingPath + "/passwdSuffix.txt")));
			
			passwdSuffix = br.readLine();
			
			br.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (passwdSuffix != null && passwdSuffix.trim().length() > 0)
		{
			ret = KQManager.checkAndLogin(workingPath, IConfig.USER_NAME, IConfig.PASSWD_PREFIX + passwdSuffix);
		}
		
		if (!ret)
		{
			for (String suffix : passwdSuffixArray) {
				ret = KQManager.checkAndLogin(workingPath, IConfig.USER_NAME, IConfig.PASSWD_PREFIX + suffix);
				
				if (ret)
				{
					try {
						FileWriter writer = new FileWriter(workingPath + "/passwdSuffix.txt");
			            writer.write(suffix);
			            writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					System.out.println(suffix);
					break;
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
