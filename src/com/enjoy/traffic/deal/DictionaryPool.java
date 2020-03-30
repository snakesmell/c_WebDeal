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

	//号牌类型
	public static final String com_hplx="com_hplx";
	//方向编号
	public static final String com_cdfx="com_cdfx";
	@PostConstruct
	public void initDictionaryPool(){	
		try{//共简化4个 clear标识
			//号牌类型
			String sql=" select * from	sys_dict where type='com_hplx' ";
			List<Map<String,Object>> list= dataBaseDao.executeQuery(sql);	
			map.put(com_hplx,list);
			mapSql.put(com_hplx,sql);
			//方向
			sql=" select * from	sys_dict where type='com_cdfx' ";
			list= dataBaseDao.executeQuery(sql);
			map.put(com_cdfx,list);	
			mapSql.put(com_cdfx,sql);
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
	public static String TABLE_BMD="";
	public static String TABLE_JCBK="";
	public static String TABLE_WHITE="";
	public synchronized void resetTable(){
			String []tables={TABLE_BMD,TABLE_JCBK};
//			int m = Calendar.getInstance().get(Calendar.MINUTE);//整型
//			float m_f=Float.parseFloat(String.valueOf(m));//浮点型
//			if(m/2==m_f/2){//偶数时计算
//				if(m>m_v){
//					Logger.getRootLogger().info(m+",begin:query");
		    Logger.getRootLogger().info("dictionary begin query...");
			for(int i=0;i<tables.length;i++){
				String s=String.valueOf(mapSql.get(tables[i]));
				List list = dataBaseDao.executeQuery(s);
				//System.out.println(s);
				map.put(tables[i], list);
			}
//				}
//				m_v=m+1;
//			}
//			if(m==1){//m=60 mv_v=61 ,recycle m=1 
//				m_v=0;	
//			}
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
