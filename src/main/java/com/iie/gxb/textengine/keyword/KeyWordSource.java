package com.iie.gxb.textengine.keyword;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class KeyWordSource {
	public  AcStateOp treeRoot;
	public static  String[] words;
	private static String name = KeyWordSource.class.getName();
	final static Log logger = LogFactory.getLog(name); 
	
	public AcStateOp getAcStateOp() {
		return treeRoot;
	}

	public boolean MakeTree(String[] words) {
		treeRoot = new AcStateOp();
		if (words != null && words.length >= 1) {
			KeyWordSource.words = words;
			// rootAcStateOp = new AcStateOp();
			for (int i = 0; i < words.length; i++) {
				// System.out.println(rootAcStateOp.children.size());
				if ((!words[i].isEmpty())&&(!words[i].equals(" "))) {
					treeRoot = treeRoot.extendString(words[i], treeRoot, words);
				}
				// treeRoot = treeRoot.extendString(words[i], rootAcStateOp,
				// words);
			}
			treeRoot = treeRoot.failDeal(treeRoot);
		} else {
			logger.info("make keyword tree failed!");
			return false;
		}
		logger.info(" make keyword tree success!");
		return true;
	}

	public boolean addmgc(String ss) {
		if (words != null && words.length >= 1) {
			String[] s = new String[words.length + 1];

			for (int i = 0; i < words.length; i++) {
				s[i] = words[i];
			}
			s[words.length] = ss;
			words = s;

			treeRoot = treeRoot.extendString(ss, treeRoot, words);
			treeRoot = treeRoot.failDeal(treeRoot);
			return true;
		} else {
			logger.info("plese make keyword tree");
			return false;
		}
	}

	public boolean delmgc(String ss) {
		if (words != null && words.length >= 1) {
			treeRoot = treeRoot.delmgc(ss, treeRoot);
			String[] tmps = new String[words.length - 1];
			int tmpdex = 0;
			for (int i = 0; i < words.length; i++) {
				if (words[i] != ss) {
					tmps[tmpdex] = words[i];
					tmpdex++;
				}
			}
			words = tmps;
			treeRoot = treeRoot.failDeal(treeRoot);
			return true;
		} else {
			logger.info("plese keyword make tree");
			return false;
		}
	}
	

	public ArrayList<String> match(String text) {
		
		//if(text.isEmpty()){return null;}
		return new KeywordMatch().match(text, treeRoot);
	}

	
/*	  public static void main(String[] args) { String[] wordsdd =
	  {"世界","中国","中国人民","国人","中國","太原","風景秀麗"}; 
	  
	  KeyWordSource s = new KeyWordSource();
	  
	  s.MakeTree(wordsdd); // s.addmgc("多少"); //s.delmgc("中国人民"); //
	  s.addmgc("中国人民"); String text = "中國,太原,風景秀麗您ز  世界中国人民总量是多少呢ssssssssss";
	  
	  List<String> li = s.match(text);
	  
	  System.out.println(li.toString()); }*/
	 

}
