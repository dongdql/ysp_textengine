package com.iie.gxb.textengine.main;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iie.gxb.textengine.DB.DBProcess;
import com.iie.gxb.textengine.DB.DBProcessInsert;
import com.iie.gxb.textengine.DB.DBUtil;
import com.iie.gxb.textengine.DB.preProcess;
import com.iie.gxb.textengine.common.keywordDB;
import com.iie.gxb.textengine.common.sampleDB;
import com.iie.gxb.textengine.config.Configuration;
import com.iie.gxb.textengine.config.Constants;
import com.mysql.jdbc.Connection;

public class textEngine {

	/***
	 * main
	 * 
	 * @param args
	 * @author dqliu
	 */
	public static void main(String[] args) {
		// get_keywords();
		engine_start();
	}

	final static Log logger = LogFactory.getLog(textEngine.class.getName());

	public static String dbUrl = "";
	private static String keyWordUrl = "";
	private static long engineTime = 0;
	private static long keyWordTime = 0;
	private static String begainTime = "";
	private static long totalNumber = 0;
	static {

		Configuration config = new Configuration();
		dbUrl = config.getValue("DATAURL").trim();
		keyWordUrl = config.getValue("KEYWORDURL").trim();
		engineTime = Long.parseLong(config.getValue("CYCLETIME").trim());
		keyWordTime = Long.parseLong(config.getValue("CYCLETIME_KEYWORD").trim());
	}
	public static preProcess keywords_black = null;
	public static preProcess keywords_white = null;
	private static ArrayList<sampleDB> samplesDB = null;

	public static void get_keywords() {
		/***
		 * ��ȡ�ؼ���
		 */
		Connection connKeyword = (Connection) DBUtil.getConnection(keyWordUrl);
		logger.info("***��ȡ������***");
		ArrayList<keywordDB> keywordsDB_black = DBUtil.get_keyworddbs(connKeyword,
				"SELECT FEATURENAME FROM choujian.feature_keywords where STATE_FOR_KEYWORD='1'");
		logger.info("***��ȡ������***");
		ArrayList<keywordDB> keywordsDB_white = DBUtil.get_keyworddbs(connKeyword,
				"SELECT FEATURENAME FROM choujian.feature_keywords_white where STATE_FOR_KEYWORD='1'");
		try {
			connKeyword.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/***
		 * �ؼ���Ԥ����
		 */
		keywords_black = null;
		keywords_black = new preProcess();
		keywords_black.setProcessKeywords(keywordsDB_black);
		keywords_white = null;
		keywords_white = new preProcess();
		keywords_white.setProcessKeywords(keywordsDB_white);
	}

	public static void get_samples() {

		/***
		 * ��ҵ����
		 */
		// �ϴ�ʱ���ֹ��
		Configuration config = new Configuration();
		begainTime = config.getValue("DEADLINE").trim();
		// ��ǰϵͳʱ��
		Date dateDeadline = new Date();
		DateFormat formatdeadline = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String deadline = formatdeadline.format(dateDeadline);

		String sql_samples = "SELECT id,name,bhcode,title,description,url,create_time FROM choujian.gxb_suspicious_info  where create_time >'"
				+ begainTime + "' and create_time <= '" + deadline + "' order by create_time  asc ";
		logger.info("create_time: " + begainTime + " -- " + deadline);
		samplesDB = null;
		samplesDB = new ArrayList<sampleDB>();	
		try {
			Connection connDB = (Connection) DBUtil.getConnection(dbUrl);
			samplesDB = DBUtil.get_sampledbs(connDB, sql_samples);
			connDB.close();
			Constants.setDEADLINE(deadline);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * ���庯��
	 * 
	 */
	public static Date begantimeKeyword = null;

	public static void engine_start() {

		Timer timer = new Timer();
		begantimeKeyword = new Date();
		TimerTask task = new TimerTask() {

			public void run() {
				logger.info("****************************����������������****************************");
				try {
					Date endtime = new Date();
					long interval = (endtime.getTime() - begantimeKeyword.getTime()) / 1000;
					if (interval > keyWordTime || keywords_black == null) {
						begantimeKeyword = null;
						begantimeKeyword = endtime;
						get_keywords();
					}
					get_samples();
					DBProcess.process_db(samplesDB, keywords_white, keywords_black);
					int every_num = samplesDB.size();
					totalNumber = totalNumber + every_num;
					logger.info("���δ�������������" + every_num + ";���湲��������������" + totalNumber);
					logger.info("****************************���������������****************************\n");
				} catch (Exception e) {
					logger.debug("---�������й���---");
					e.printStackTrace();
				}
			}

		};
		timer.scheduleAtFixedRate(task, 0, engineTime * 1000);
	}

}
