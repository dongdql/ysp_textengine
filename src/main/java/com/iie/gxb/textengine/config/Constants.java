package com.iie.gxb.textengine.config;

public class Constants {

	private static Configuration config = Configuration.getInstance();
	public static String DRIVER = "com.mysql.jdbc.Driver";
	public static String DATAURL = "";
	public static String USERNAME = "";
	public static String CYCLETIME = "";
	public static String CYCLETIME_KEYWORD = "";
	public static String DIRECTORY = "";
	public static String KEYWORDPATH = "";
	public static String KEYWORDURL = "";
	public static String DEADLINE = "";
	public static String getValue(String key) {
		String keyValue = config.getValue(key);
		if (!(keyValue.equalsIgnoreCase("") || keyValue.equalsIgnoreCase(null))) {
			return keyValue;
		}
		return null;
	}
	public static String getUSERNAME() {
		String username = config.getValue("USERNAME");
		if (!(username.equalsIgnoreCase("") || username.equalsIgnoreCase(null))) {
			USERNAME = username;
		}
		return USERNAME;
	}
	public static String getDEADLINE() {
		String deadline = config.getValue("DEADLINE");
		if (!(deadline.equalsIgnoreCase("") || deadline.equalsIgnoreCase(null))) {
			DEADLINE = deadline;
		}
		
		return DEADLINE;
	}

	public static void setDEADLINE(String deadline) {
		config.setDeadlineValue(deadline);
		// DEADLINE =deadline;
	}
	
	public static void setUSERNAME(String uSRENAME) {
		USERNAME = uSRENAME;
	}

	public static String getDIRECTORY() {
		String directory = config.getValue("DIRECTORY");
		if (!(directory.equalsIgnoreCase("") || directory.equalsIgnoreCase(null))) {
			DIRECTORY = directory;
		}
		return DIRECTORY;
	}

	public static void setDIRECTORY(String directory) {
		DIRECTORY = directory;
	}

	public static String getCYCLETIME() {
		String cycletime = config.getValue("CYCLETIME");
		if (!(cycletime.equalsIgnoreCase("") || cycletime.equalsIgnoreCase(null))) {
			CYCLETIME = cycletime;
		}
		return CYCLETIME;
	}

	public static void setCYCLETIME(String cycletime) {
		CYCLETIME = cycletime;
	}

	public static String getCYCLETIME_KEYWORD() {
		String cycletime_keyword = config.getValue("CYCLETIME_KEYWORD");
		if (!(cycletime_keyword.equalsIgnoreCase("") || cycletime_keyword.equalsIgnoreCase(null))) {
			CYCLETIME_KEYWORD = cycletime_keyword;
		}
		return CYCLETIME_KEYWORD;
	}

	public static void setCYCLETIME_KEYWORD(String cycletime_keyword) {
		CYCLETIME_KEYWORD = cycletime_keyword;
	}
	
	public static String getDRIVER() {
		String ip = config.getValue("DRIVER");
		if (!(ip.equalsIgnoreCase("") || ip.equalsIgnoreCase(null))) {
			DRIVER = ip;
		}
		return DRIVER;
	}

	public static void setDRIVER(String driver) {
		DRIVER = driver;
	}

	public static String getKeyWordURL() {
		String keywordport = config.getValue("KEYWORDURL");
		if (!(keywordport.equalsIgnoreCase("") || keywordport.equalsIgnoreCase(null))) {
			KEYWORDURL = keywordport;
		}
		return KEYWORDURL;
	}

	public static void setKeyWordURL(String keywordurl) {
		KEYWORDURL = keywordurl;
	}
	
	public static String getDataURL() {
		String port = config.getValue("DATAURL");
		if (!(port.equalsIgnoreCase("") || port.equalsIgnoreCase(null))) {
			DATAURL = port;
		}
		return DATAURL;
	}

	public static void setDataURL(String dataurl) {
		DATAURL = dataurl;
	}

	public static String getKEYWORDPATH() {
		String keywordpath = config.getValue("KEYWORDPATH");
		if (!(keywordpath.equalsIgnoreCase("") || keywordpath.equalsIgnoreCase(null))) {
			KEYWORDPATH = keywordpath;
		}
		return KEYWORDPATH;
	}

	public static void setKEYWORDPATH(String keywordpath) {
		KEYWORDPATH = keywordpath;
	}

}
