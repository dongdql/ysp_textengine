package com.iie.gxb.textengine.extract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iie.gxb.textengine.chinese.ZHConverter;
import com.iie.gxb.textengine.config.Constants;
import com.iie.gxb.textengine.keyword.KeyWordSource;

public class ExtractKeyWord {
	
	public static KeyWordSource keyWordSource() throws Exception {
		String[] wordsdd = ExtractSqlWord();
		KeyWordSource s = new KeyWordSource();
		s.MakeTree(wordsdd);
		return s;
	}
	private static String name =ExtractKeyWord.class.getName();
	final static Log logger = LogFactory.getLog(name);
	
	public static String[] ExtractSqlWord() throws Exception  {
		

		Connection conn = null;
		String[] keyword = null;
		String keyWordUrl = Constants.getKeyWordURL();
		try {

			String driver = Constants.getDRIVER();
			Class.forName(driver);
			logger.info("成功加载keyword_MySQL驱动程序");

			conn = DriverManager.getConnection(keyWordUrl);

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT FEATUREALIAS FROM dev.SAMPLE_FEATURE_KEYWORD  where  STATE='1'");

			rs.last();
			int dataNum = 2 * rs.getRow();
			rs.beforeFirst();
			if (dataNum > 0) {
				keyword = new String[dataNum];
			}

			ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
			int i = 0;
			while (rs.next()) {
				String simpleStr = rs.getString("FEATUREALIAS");
				if (!simpleStr.isEmpty()) {
					keyword[i] = simpleStr;					
					i++;
					String traditionalStr = converter.convert(simpleStr);
					keyword[i] = traditionalStr;
					i++;
				}
			}

		} catch (SQLException e) {
			System.out.println("keyword_MySQL操作错误");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		return keyword;
	}

}
