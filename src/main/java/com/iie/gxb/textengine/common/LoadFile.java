package com.iie.gxb.textengine.common;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LoadFile {
	private static String name = LoadFile.class.getName();
	final static Log logger = LogFactory.getLog(name); 
    public static void downLoadFromUrl(String flieUrl,String dir,String _fileName) {  
    	  
        try {  
            URL httpurl = new URL(flieUrl);  
    		String format =flieUrl.substring(flieUrl.lastIndexOf('.') + 1);
    		
    		if(_fileName.isEmpty()){
    			_fileName=FilenameUtils.getName(flieUrl);
    			//System.out.println("_fileName:"+_fileName);
    		}
    		
    		
    		
            String fileName =  _fileName + "." + format;
            //System.out.println(fileName);  
            File f = new File(dir + fileName);  
            FileUtils.copyURLToFile(httpurl, f);  
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.info( "download falied !");  
        }   
        logger.info("download successful!");  
    }   
	 
}
