package com.iie.gxb.textengine.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InsertResult {

	final static Log logger = LogFactory.getLog(InsertResult.class.getName());
	
	
	
	public static void insertDB(Connection conn, ArrayList<String> sqls){
		if(sqls == null || conn == null){
			return;
		}
		int i=0;
		try {Statement insert = conn.createStatement();
		for(String sql:sqls){
			i++;
			
				
				insert.executeUpdate(sql);
				logger.info(i+"检测结果入库成功");
			
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
