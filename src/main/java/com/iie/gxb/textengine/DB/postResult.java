package com.iie.gxb.textengine.DB;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jframe.http.HttpUtils;

public class postResult {

	final static Log logger = LogFactory.getLog(postResult.class.getName());

	public static void post_result(String post_url, ArrayList<Map<String, String>> maps) {

		if (post_url == null || maps == null) {
			return;
		}
		int i = 0;
		for (Map<String, String> map : maps) {
			i++;
			try {
				String postResult = HttpUtils.post(post_url, map);
				logger.info("POST后返回结果:" + i + postResult);
			} catch (Exception e) {
				logger.error(e);
			}
		}

	}
}
