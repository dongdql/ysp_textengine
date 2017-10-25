package com.iie.gxb.textengine.keyword;

import java.util.ArrayList;
//import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 *
 * @author wkp
 */
public class KeywordMatch {

	// private final ReentrantReadWriteLock keywordMatchLock = new
	// ReentrantReadWriteLock();// added

	public ArrayList<String> match(String text, AcStateOp root) {
		ArrayList<String> mgclist = new ArrayList<String>();
		// keywordMatchLock.readLock().lock();
		if (text != null) {
			char[] source = text.toCharArray();
			AcStateOp current = root;
			char currentChar = '\0';
			// String wyu = "غشسژزردخچجتپبئەئائېۋئۈئۆئۇئوھنملڭگكقفيئى";
			// wyu = wyu.trim();
			// mgclist.add("true");

			// boolean ifwyu = false;

			for (int i = 0; i < text.length(); i++) {
				currentChar = source[i];
				// if (wyu.indexOf(String.valueOf(currentChar)) != -1) {
				// ifwyu = true;

				// }

				if (current.getChildren().containsKey(currentChar)) {
					current = current.getChildren().get(currentChar);
					if (!current.getOutPut().isEmpty()) {
						for (int ss = 0; ss < current.getOutPut().size(); ss++) {
							String word = (String) current.getOutPut().get(ss);
							if (!mgclist.contains(word)) {
								mgclist.add(word);
							}
						}
					}
					continue;
				} else {
					if (current == root) {
						continue;
					} else {
						current = current.getFail();
						while (true) {
							if (current.getChildren().containsKey(currentChar)) {
								current = current.getChildren().get(currentChar);
								if (!current.getOutPut().isEmpty()) {
									for (int ss = 0; ss < current.getOutPut().size(); ss++) {
										String word = (String) current.getOutPut().get(ss);
										if (!mgclist.contains(word)) {
											mgclist.add(word);

										}
									}
								}
								break;
							} else if (current == root) {
								break;
							} else {
								current = current.getFail();
							}
						}
					}
				}
			}
		}
		// keywordMatchLock.readLock().unlock();
		return mgclist;
	}
}
