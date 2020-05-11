package com.enjoy.traffic.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public final class Common {
	
	public static String getNowDate(){
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sim.format(new Date());
	}
	public static String TEMPLATE_YEAR="yyyy";
	public static String TEMPLATE_MONTH="yyyyMM";
	public static String TEMPLATE_DAY="yyyyMMdd";
	public static String TEMPLATE_HOUR="yyyyMMddHH";
	public static String TEMPLATE_MINUTE="yyyyMMddHHmm";
	public static String getFormatDate(String Template,Calendar cal){
        SimpleDateFormat sim = new SimpleDateFormat(Template);
        return sim.format(cal.getTime());
	}

	/**
	 * MQ通讯Topic
	 */
	public static String HMD="MQ_GCSJ_BKBJ";
	public static String HISENSE_PASS="HIATMP.HISENSE.PASS.PASSINF";
	public static String MQ_DefaultUser="admin";
	public static String MQ_DefaultPassword="admin";
	//配置表字段名
	public static String PARAMKEY="PARAMKEY";
	public static String PARAMVALUE="PARAMVALUE";
	public static String UPLOAD_MQ_USER="UPLOAD_MQ_USER_GC";
	public static String UPLOAD_MQ_PWD="UPLOAD_MQ_PWD_GC";
	public static String ZP_LSSC_MQDestination="ZP_LSSC_MQDestination";
	public static String ZP_WFSC_MQURL="ZP_WFSC_MQURL";
	//视频网配置表字段名
	public static String UPLOAD_VIDEO_MQ_USER="UPLOAD_VIDEO_MQ_USER";
	public static String UPLOAD_VIDEO_MQ_PWD="UPLOAD_VIDEO_MQ_PWD";
	public static String ZP_LSSC_VIDEO_MQDestination="ZP_LSSC_VIDEO_MQDestination";
	public static String ZP_WFSC_VIDEO_MQURL="ZP_WFSC_VIDEO_MQURL";
	//过车
	public static String GCJK="MQ_JKSJ_YS";
	//获取配置文件
	public static Properties getProperties(){
		Properties pro=null;
		try {
			InputStream file = new FileInputStream(Common.class.getClassLoader().getResource("config.properties").getFile());
			 pro = new Properties();
			 pro.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return pro;
	}
	public static String RedisAddress="redis_add";
	public static String RedisPort="redis_port";
	public static String RedisNum="redis_num";
	//并发开启线程数
	public static String DahuaWaterThread="dahuaWaterThread";
	public static String DahuaIllegalThread="dahuaIllegalThread";
	public static String VideoDahuaWaterThread="videoDahuaWaterThread";
	
	public static String HikvisonWaterThread="hikvisonWaterThread";
	public static String HikvisonIllegalThread="hikvisonIllegalThread";
	public static String VideoHikvisonWaterThread="videoHikvisonWaterThread";
	
	public static String WAIT="wait";
	
}



