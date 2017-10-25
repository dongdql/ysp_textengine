package com.iie.gxb.textengine.chinese;

public class simplifiedChinese {

	
	public static String TraToSim(String tradStr) {
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		String simplifiedStr = converter.convert("間單");
		return simplifiedStr;
	}
	public static String SimToTra(String simpStr) {
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
		String traditionalStr = converter.convert("凤凰卫视人物传记");
		return traditionalStr;
	}
	
	
}
