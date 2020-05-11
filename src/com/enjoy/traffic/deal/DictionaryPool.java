package com.enjoy.traffic.deal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enjoy.traffic.dao.DataBaseDao;
import com.zhitec.busibase.zclx.CKZCLX;
@Component
public class DictionaryPool {
//	dataBaseDao dataBaseDao=new dataBaseDao();
	@Autowired
	private DataBaseDao dataBaseDao;

	private int m_v=0;//定时计算
//	private int []m_={2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58,60};
	private Map map=new HashMap();
	private Map mapSql=new HashMap();
//	private static Map map_ip=null;
	public DictionaryPool() {
		Logger.getRootLogger().info("Dictionary init");
	}
	private static Map map_ip_db=new HashMap<String,String>();
	public Map getMap() {
		return map;
	}

	public Map getMapSql() {
		return mapSql;
	}
	public void setMapSql(Map mapSql) {
		this.mapSql = mapSql;
	}
	public void setMap(Map map) {
		this.map = map;
	}

	public static final String BLACKLIST="BLACKLIST";
	public static final String Black_alert_device="black_alert_device";
	public static final String bkl_bkjb="bkl_bkjb";//布控级别
	public static final String bkl_bklx="bkl_bklx";//黑名单布控类型
	public static final String com_cdfx="com_cdfx";//方向
	public static final String com_cbbh="com_cbbh";//车标
	public static final String com_cllx="com_cllx";//车辆类型
	public static final String com_hplx="com_hplx";//号牌类型
	public static final String com_csys="com_csys";//车身颜色
	public static final String com_cdbh="com_cdbh";//车道编号
	public static final String eqp_type="eqp_type";//数据来源
	public static final String bkl_all_area="bkl_all_area";//全局作用域
	public static final String bkl_status="bkl_status";//状态控制
	@PostConstruct
	public void initDictionaryPool(){	
		try{//共简化4个 clear标识
			//黑名单车辆 CARNO
			String sql=" select * from BLACKLIST t ";
			List<Map<String,Object>> list= dataBaseDao.executeQuery(sql);	
			map.put(BLACKLIST,list);
			mapSql.put(BLACKLIST,sql);
			//卡口点位 DEVICENAME DEVICECODE
			sql=" select * from black_alert_device ";
			list= dataBaseDao.executeQuery(sql);
			map.put(Black_alert_device,list);	
			mapSql.put(Black_alert_device,sql);
			//布控级别
			sql=" select * from SYS_DICT where type='bkl_bkjb' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(bkl_bkjb,list);	
			mapSql.put(bkl_bkjb,sql);
			//黑名单布控类型
			sql=" select * from SYS_DICT where type='bkl_bklx' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(bkl_bklx,list);	
			mapSql.put(bkl_bklx,sql);
			//方向
			sql=" select * from SYS_DICT where type='com_cdfx' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_cdfx,list);	
			mapSql.put(com_cdfx,sql);
			//车标
			sql=" select * from SYS_DICT where type='com_cbbh' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_cbbh,list);	
			mapSql.put(com_cbbh,sql);
			//车辆类型
			sql=" select * from SYS_DICT where type='com_cllx' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_cllx,list);	
			mapSql.put(com_cllx,sql);
			//布控级别
			sql=" select * from SYS_DICT where type='bkl_bkjb' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(bkl_bkjb,list);	
			mapSql.put(bkl_bkjb,sql);
			//号牌类型
			sql=" select * from SYS_DICT where type='com_hplx' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_hplx,list);	
			mapSql.put(com_hplx,sql);
			//车身颜色
			sql=" select * from SYS_DICT where type='com_csys' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_csys,list);	
			mapSql.put(com_csys,sql);
			//车道编号
			sql=" select * from SYS_DICT where type='com_cdbh' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_cdbh,list);	
			mapSql.put(com_cdbh,sql);
			//数据来源
			sql=" select * from SYS_DICT where type='eqp_type' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(eqp_type,list);	
			mapSql.put(eqp_type,sql);
			//全区域控制
			sql=" select * from SYS_DICT a where a.type='bkl_all_area' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(bkl_all_area,list);	
			mapSql.put(bkl_all_area,sql);
			//布控状态
			sql=" select * from SYS_DICT a where a.type='bkl_status' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(bkl_status,list);	
			mapSql.put(bkl_status,sql);
			
			System.out.println("字典数据加载...");
		}		
		catch(Exception e){
			e.printStackTrace();
			System.out.println("字典数据加载出现错误："+e.getMessage());
		}
	}
	/**
	 * 线程同步，防止冲突
	 * @param tableName
	 * @param sql
	 */
	public synchronized void resetTable(){
		String []tables={BLACKLIST,Black_alert_device,bkl_all_area,bkl_status};
	    Logger.getRootLogger().info("dictionary begin query...");
		for(int i=0;i<tables.length;i++){
			String s=String.valueOf(mapSql.get(tables[i]));
			List list = dataBaseDao.executeQuery(s);
			//System.out.println(s);
//			System.out.println(list);
			map.put(tables[i], list);
		}
	}
	
	/**
	 * 获取字典列表
	 * @param tableName
	 * @return
	 */
	public List getElementList(String tableName){
		List list=(List)map.get(tableName);
		return list;
	}
	/**
	 * 获取字典值 
	 * 首先查询初始化中是否有数据,如果没有从数据库中查找,没有直接返回.
	 * @param tableName
	 * @param queryField
	 * @param queryValue
	 * @param resultFeild
	 * @return
	 */
	public String getElementValue(String tableName,String queryField,String queryValue,String resultFeild){
		String value=null;
		if(queryValue!=null && queryValue.length()>0){				
			List list=(List)map.get(tableName);
			if(list!=null && list.size()>0){				
				int count=list.size();
				for(int i=0;i<count;i++){
					Map element=(Map)list.get(i);
					if(element.get(queryField).toString().equals(queryValue)){
						value=element.get(resultFeild).toString();
						break;
					}
				}
				if(!"".equals(value)){return value;}
			}
		}
		return value;
	}
	/**
	 * reset find from db
	 * @param tableName
	 * @param queryField
	 * @param queryValue
	 * @param resultFeild
	 * @return
	 */
	public String getElementValueReset(String tableName,String queryField,String queryValue,String resultFeild){
		String value="";
		if(queryValue!=null && queryValue.length()>0){				
			List list=(List)map.get(tableName);
			if(list!=null && list.size()>0){				
				int count=list.size();
				for(int i=0;i<count;i++){
					Map element=(Map)list.get(i);
					if(element.get(queryField).toString().equals(queryValue)){
						value=element.get(resultFeild).toString();
						break;
					}
				}
			}
		}
		return value;
	}

	/**
	 * 获取单条记录
	 * @param tableName
	 * @param queryField
	 * @param queryValue
	 * @return
	 */
	public Map getRecordInfo(String tableName,String queryField,String queryValue){
		Map mapReturn=null;
		List list=(List)map.get(tableName);
		if(list!=null && list.size()>0){
			int count=list.size();
			for(int i=0;i<count;i++){
				Map temp=(Map)list.get(i);
				if(temp.get(queryField).toString().equals(queryValue)){
					mapReturn=temp;
					break;
				}
			}
		}
		return mapReturn;
	}
	
}
