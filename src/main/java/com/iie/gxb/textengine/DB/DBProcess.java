package com.iie.gxb.textengine.DB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iie.gxb.textengine.common.sampleDB;
import com.iie.gxb.textengine.config.Constants;

import jframe.http.HttpUtils;
import net.sf.json.JSONObject;

public class DBProcess {

	final static Log logger = LogFactory.getLog(DBProcess.class.getName());

	public static void process_db(ArrayList<sampleDB> samplesDB, preProcess keywords_white, preProcess keywords_black) {

		if (samplesDB == null ) {
			logger.info("total samples number is zero");
			return;
		}
		// post
		String postUrl = Constants.getValue("POSTURL");
		ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		// insert
		// ArrayList<String> sqls = new ArrayList<String>();
		String create_time=null;
		int i=0;
		for (sampleDB sample : samplesDB) {
			i++;
			String nameStr = sample.getName();
			String titleStr = sample.getTitle();
			String descriptionStr = sample.getDescription();
			String confirmStr = "false";
			String keywordStr = "";// 匹配结果
			create_time = sample.getCreate_time();
			
			//logger.info("处理本次取出的第"+i+"条数据:"+sample.getBhCode());
			try {
				ArrayList<String> white_result = keywords_match(keywords_white, nameStr, titleStr, descriptionStr);
				if (white_result.size() < 1) {
					ArrayList<String> black_result = keywords_match(keywords_black, nameStr, titleStr, descriptionStr);
					if (black_result.size() > 0) {
						confirmStr = "true";
						keywordStr = black_result.toString();
						logger.info("黑名单" + keywordStr + ":" + sample.getBhCode());
					}
				} else {
					logger.info("白名单" + white_result.toString() + ":" + sample.getBhCode());
					keywordStr = "baimingdan";
				}
			} catch (Exception e) {
				Constants.setDEADLINE(create_time);
				logger.error(e);
			}

			Date date = new Date();
			DateFormat format_f = new SimpleDateFormat("yyyyMMddHHmmss");
			String time_f = format_f.format(date);

			JSONObject textEngineServiceJson = new JSONObject();
			Map<String, String> map = new HashMap<String, String>();

			textEngineServiceJson.put("id", sample.getBhCode());
			textEngineServiceJson.put("uri", sample.getUrl());
			textEngineServiceJson.put("confirm", confirmStr);
			textEngineServiceJson.put("confirmTime", time_f);
			textEngineServiceJson.put("confirmReason", "keyword");
			textEngineServiceJson.put("confirmUri", "");
			textEngineServiceJson.put("src", "KEYWORD");

			JSONObject keywordJson = new JSONObject();
			JSONObject keywordJson_state = new JSONObject();
			keywordJson.put("confirm", confirmStr);
			keywordJson.put("items", keywordStr);

			keywordJson_state.put("ok", 1);
			keywordJson_state.put("failed", "");

			keywordJson.put("state", keywordJson_state.toString());

			textEngineServiceJson.put("keywordResult", keywordJson.toString());
			textEngineServiceJson.put("state", keywordJson_state.toString());

			map.put("confirm", confirmStr);
			map.put("jsonResult", textEngineServiceJson.toString());
			maps.add(map);
			
			try {
				// logger.info("POST该条数据......");
				String postResult = HttpUtils.post(postUrl, map);
				logger.info("POST后返回结果:" +i +postResult);
			} catch (Exception e) {
				logger.debug("POST异常......");
				Constants.setDEADLINE(create_time);
				logger.error(e);
			}
			/*
			 * if (confirmStr == "true") {
			 * 
			 * } else { String sql =
			 * "INSERT INTO choujian.gxb_suspicious_result (gxb_suspicious_info_id,gxb_suspicious_type_id,src,state,confirm,confirm_time,engine_callback_time)VALUES ('"
			 * + sample.getId() + "','9','KEYWORD','1','0','" +
			 * sample.getCreate_time() + "','" + time_f + "')"; sqls.add(sql); }
			 */

		}
		if (create_time != null) {
			Constants.setDEADLINE(create_time);
			logger.info("此次:" + "endTime:" + create_time);
		}
		/*if (maps.size() > 0) {
			String postUrl = Constants.getValue("POSTURL");
			postResult.post_result(postUrl, maps);
		}

		
		 * if (sqls.size() > 0) { Connection connDB = (Connection)
		 * DBUtil.getConnection(textEngine.dbUrl); InsertResult.insertDB(connDB,
		 * sqls); try { connDB.close(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
	}

	public static ArrayList<String> keywords_match(preProcess keywords, String nameStr, String titleStr,
			String descriptionStr) {

		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> _single_description = keywords.source_single.match(descriptionStr);
		ArrayList<String> _single_title = keywords.source_single.match(titleStr);
		ArrayList<String> _single_name = keywords.source_single.match(nameStr);

		if (_single_name.size() > 0 || _single_title.size() > 0 || _single_description.size() > 0) {
			result.addAll(_single_name);
			result.addAll(_single_title);

		} else {
			ArrayList<String> _combine_description = keywords.source_combine.match(descriptionStr);
			ArrayList<String> _combine_title = keywords.source_combine.match(titleStr);
			ArrayList<String> _combine_name = keywords.source_combine.match(nameStr);
			_combine_description.addAll(_combine_title);
			_combine_description.addAll(_combine_name);

			if (_combine_description.size() > 1 && keywords.combine_words != null) {

				if (JudgeCombination.judgeCombination(_combine_description, keywords.combine_words)) {
					result = _combine_description;
					logger.info("命中组合白名单："+_combine_description.toString());
				}
			}
		}
		return result;
	}

}
