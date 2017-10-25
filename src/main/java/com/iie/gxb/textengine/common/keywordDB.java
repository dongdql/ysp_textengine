package com.iie.gxb.textengine.common;


public class keywordDB {

	
	/***
	 * 关键词
	 * 
	 */
	private String featurename="";
	private String featuretype="";
	
	private String state_for_keyword="";
	private String state_for_ocr="";
	
	/***
	 * 
	 */
	public String getFeatureName() {
		return featurename;
	}
	public void setBhFeatureName(String featurename) {
		this.featurename = featurename;
	}
	public String getFeatureType() {
		return featuretype;
	}
	public void setBhFeatureType(String featuretype) {
		this.featuretype = featuretype;
	}
	public String getStateforKeyword() {
		return state_for_keyword;
	}
	public void setStateforKeyword(String state_for_keyword) {
		this.state_for_keyword = state_for_keyword;
	}
	public String getStateforOcr() {
		return state_for_ocr;
	}
	public void setStateforOcr(String state_for_ocr) {
		this.state_for_ocr = state_for_ocr;
	}
}
