package com.iie.gxb.textengine.DB;

import java.util.List;

public class JudgeCombination {

	public static boolean judgeCombination(List<String> wordList,List<List<String>> combine_words){
		
		for(int i =0 ;i<combine_words.size();i++){
			
			boolean flag=true;
			
			List<String> combinations=combine_words.get(i);
			// System.out.println("组合判断"+combinations);
			if(combinations.size()<1)
			{
				flag=false;
				break;
			}
			for(int j=0;j<combinations.size();j++){
				if(!wordList.contains(combinations.get(j)))
				{
					flag=false;
					break;
				}
			}
			if(flag)
				return true;
		}
		return false;
	}
}
