package com.iie.gxb.textengine.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iie.gxb.textengine.common.keywordDB;
import com.iie.gxb.textengine.common.sampleDB;

public class DBUtil {

	final static Log logger = LogFactory.getLog(DBUtil.class.getName());

	public static Connection getConnection(String DBUrl) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DBUrl);
			logger.info("---success on db Connection---");
		} catch (Exception e) {
			logger.debug("---fail on db Connection---");
			e.printStackTrace();
		}
		return conn;
	}

	public static ArrayList<sampleDB> get_sampledbs(Connection conn, String sql) {

		ArrayList<sampleDB> samples = new ArrayList<sampleDB>();
		Statement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
		
			int total_num = 0;
			logger.info("***开始取出样本数据***");
			while (rs.next()) {
				total_num++;
				sampleDB sample_db = new sampleDB();
				sample_db.setId(rs.getString("id"));
				sample_db.setBhCode(rs.getString("bhcode"));
				sample_db.setName(rs.getString("name"));
				sample_db.setTitle(rs.getString("title"));
				sample_db.setCreate_time(rs.getString("create_time"));
				sample_db.setUrl(rs.getString("url"));
				sample_db.setDescription(rs.getString("description"));
				samples.add(sample_db);
			}
			logger.info("本次取出样本数据总条数：" + total_num);
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			logger.debug(e);
			e.printStackTrace();
		}
		return samples;
	}

	public static ArrayList<keywordDB> get_keyworddbs(Connection conn, String sql) {

		ArrayList<keywordDB> keywords = new ArrayList<keywordDB>();
		Statement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.createStatement();
			rs = ps.executeQuery(sql);

			int total_num = 0;
			while (rs.next()) {
				total_num++;
				keywordDB keyword_db = new keywordDB();
				String keyword = rs.getString("featurename");
				if (keyword != null) {
					keyword_db.setBhFeatureName(keyword.trim());
				}
				keywords.add(keyword_db);
			}
			logger.info("获取关键词数目：" + total_num);
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			logger.debug(e);
			e.printStackTrace();
		}
		return keywords;
	}

	public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
