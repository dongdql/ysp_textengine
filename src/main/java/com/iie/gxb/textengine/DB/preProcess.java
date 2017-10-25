package com.iie.gxb.textengine.DB;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iie.gxb.textengine.chinese.ZHConverter;
import com.iie.gxb.textengine.common.keywordDB;

import com.iie.gxb.textengine.keyword.KeyWordSource;

public class preProcess {

	final static Log logger = LogFactory.getLog(preProcess.class.getName());
	/***
	 * 关键词
	 */
	public List<List<String>> combine_words = new ArrayList<List<String>>();
	public KeyWordSource source_single = new KeyWordSource();
	public KeyWordSource source_combine = new KeyWordSource();

	/***
	 * 预处理
	 * 
	 * @param keywordDBs
	 */
	public void setProcessKeywords(ArrayList<keywordDB> keywordDBs) {

		if (keywordDBs == null) {
			return;
		}

		ArrayList<String> single = new ArrayList<String>();
		ArrayList<String> combine = new ArrayList<String>();
		String[] keywords_single = null;
		String[] keywords_combine = null;

		for (int i = 0; i < keywordDBs.size(); i++) {

			/***
			 * 繁体转换
			 */
			ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
			keywordDB keywordDB = keywordDBs.get(i);
			String keywordStr = keywordDB.getFeatureName().trim();

			if (keywordStr == null) {
				continue;
			}

			/***
			 * 组合
			 * 
			 */
			if (keywordStr.contains(" ")) {

				ArrayList<String> list_combine_cp1 = new ArrayList<String>();
				String[] words = keywordStr.trim().split("\\s+");

				for (String word : words) {
					combine.add(word);
					list_combine_cp1.add(word);
				}

				this.combine_words.add(list_combine_cp1);
				ArrayList<String> list_combine_cp2 = new ArrayList<String>();

				for (String word : words) {
					String traditionalStr = converter.convert(word);
					combine.add(traditionalStr);
					list_combine_cp2.add(traditionalStr);
				}
				this.combine_words.add(list_combine_cp2);

			} else {
				single.add(keywordStr);
				String traditionalStr = converter.convert(keywordStr);
				single.add(traditionalStr);
			}
		}

		/***
		 * 单个
		 */
		keywords_single = new String[single.size()];
		logger.info("单个关键词个数：" + single.size() / 2);
		for (int idx = 0; idx < single.size(); idx++) {
			keywords_single[idx] = (String) single.get(idx);
		}

		/***
		 * 组合——单个
		 */
		keywords_combine = new String[combine.size()];
		logger.info("组合关键词个数：" + combine.size() / 2);
		for (int idx = 0; idx < combine.size(); idx++) {
			// logger.info("组合关键词：" +(String) combine.get(idx));
			keywords_combine[idx] = (String) combine.get(idx);

		}

		source_single.MakeTree(keywords_single);
		source_combine.MakeTree(keywords_combine);		
	}
}
