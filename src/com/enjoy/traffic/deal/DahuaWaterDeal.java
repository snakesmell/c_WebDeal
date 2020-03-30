package com.enjoy.traffic.deal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.enjoy.traffic.dao.DataBaseDao;
import com.enjoy.traffic.dao.DataBaseDao2;
import com.enjoy.traffic.redisManger.RedisFactory;
import com.enjoy.traffic.util.Common;
import com.enjoy.traffic.util.MqHelper;

import redis.clients.jedis.Jedis;

@Component("dahuaWaterDeal")
public class DahuaWaterDeal implements Runnable{
	public DahuaWaterDeal() {
		Logger.getRootLogger().info("dahuaWaterDeal init");
	}
	private final Logger logger= LogManager.getLogger(this.getClass());
	@Autowired
	private RedisFactory redisFactory;
	@Autowired
	private DictionaryPool dictionaryPool;
	@Autowired
	private DataBaseDao dataBaseDao;
	@Autowired
	private DataBaseDao2 dataBaseDao2;
	@Autowired
	private WaterDeal waterDeal;
	@Override
	public void run() {
		dictionaryPool.initDictionaryPool();
		Jedis jedis = redisFactory.gerListRedis(1);//redisUtil.getJedis();//2 reids
		ArrayList<String> list=null;
		MqHelper mq=null;
		MqHelper mq_gc=null;
		String user="";
		String pw="";
		String topic="";
		String url="";
		try {
//			WebApplicationContext  webApplicationContext = ContextLoader.getCurrentWebApplicationContext();//全局作用域变量
			List list_mq = dataBaseDao.executeQuery("");
//			List list_mq_gc = dataBaseDao.executeQuery(Common.WEB_Param);
			if(list_mq!=null && list_mq.size()>1){
				for(int i=0;i<list_mq.size();i++){
					Map mqMap = (Map)list_mq.get(i);//海信MQ
					System.out.println(mqMap.get(Common.PARAMKEY).toString());
					if("GC_HISENSE_MQ_PASSWORD".equals(mqMap.get(Common.PARAMKEY).toString())){
						user=mqMap.get(Common.PARAMVALUE).toString();
					}
					if("GC_HISENSE_MQ_ACCOUNT".equals(mqMap.get(Common.PARAMKEY).toString())){
						pw=mqMap.get(Common.PARAMVALUE).toString();
					}
					if("GC_HISENSE_MQ_ADDR".equals(mqMap.get(Common.PARAMKEY).toString())){
						url=mqMap.get(Common.PARAMVALUE).toString();
					}
					if("GC_HISENSE_MQ_TOPIC".equals(mqMap.get(Common.PARAMKEY).toString())){
						topic=mqMap.get(Common.PARAMVALUE).toString();
					}
				}
				mq=new MqHelper(user,pw,url);//海信MQ
			}
//			
//			if(list_mq!=null && list_mq.size()>0){
//				Map mqMap=null;//银江过车MQ
//				mqMap = (Map)list_mq.get(0);
//				mq_gc=new MqHelper(mqMap.get("USERNAME").toString(), mqMap.get("PASSWORD").toString(),"tcp://"+mqMap.get("WWIP").toString()+":"+mqMap.get("WWPORT").toString());
//			}
			
			while(true){
				try {
					List ipList=dataBaseDao.executeQuery("");
//					Object obj = webApplicationContext.getServletContext().getAttribute(Common.MQ_COUNT_FLAG);
//					Integer mqFlag=0;
//					if(obj!=null){
//						mqFlag=(Integer)obj;
//					}
					String json = jedis.rpop("");
					if(json==null){
						Thread.sleep(500);//no data wating...
					}else{
						logger.info(json);
						Map map=(Map)JSONValue.parse(json);
						//将消息转成海信格式
//						//MQ发送海信
						boolean ret=mq.sendTopicMessage(topic, "water_text");
						if(ret){
//						if(true){
							waterDeal.dealWaterRecord(map, dictionaryPool, mq_gc);//过车入库
							//数据配置银江
							//list = dealWaterRecord(map,dictionaryPool);//3 to list
							//int id=dataBaseDao.executeQueryProc2(Common.SP_TR_ZP_TX101ZPSYJL_ADD, list);//4 save
//使用公共包							dealWaterRecord(map,dictionaryPool);
//使用公共包							  sendJKSJ(mq_gc,map);
//							if(mqFlag>0){
//								waterDeal.sendJKSJ(mq_gc, map, dictionaryPool);//MQ过车数据监控 银江
//							}
						}else{
							Logger.getRootLogger().error("MQ upload error!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedis.disconnect();
		}
	}
//	public void dealWaterRecord(Map map,DictionaryPool dictionaryPool) {
//		   Calendar cal = Calendar.getInstance();	
//		   dealHMD(map,dictionaryPool);//2
//		   StringBuffer s=new StringBuffer();
//		   String cjsj=map.get("cjsj")==null?Common.SIGN_NULL:map.get("cjsj").toString();
//		   s.append(map.get("sjly")==null?Common.SIGN_NULL:map.get("sjly").toString()+Common.SIGN_COMMA);
//		   s.append(map.get("zpxtid")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_ZPXT_JC","CODE",map.get("zpxtid").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(Common.SIGN_NULL+Common.SIGN_COMMA);
//		   s.append(map.get("zpxtjlid")==null?Common.SIGN_NULL:"'"+map.get("zpxtjlid").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("hplxidzdsb")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_HPLX_JC","CODE",map.get("hplxidzdsb").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("hphmzdsb")==null?Common.SIGN_NULL:"'"+map.get("hphmzdsb").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("hplxidzdsb")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_HPLX_JC","CODE",map.get("hplxidzdsb").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("hphmzdsb")==null?Common.SIGN_NULL:"'"+map.get("hphmzdsb").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("hpysid")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_HPYS_JC","CODE",map.get("hpysid").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("csysid")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_CSYS_JC","CODE",map.get("csysid").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("cllxid")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_CLLX_JC","CODE",map.get("cllxid").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("cbid")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_CL_CB_JC","CODE",map.get("cbid").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(SystemUtil.getDateOracleByString(cjsj)+Common.SIGN_COMMA);
//		   s.append((cal.get(Calendar.MONTH)+1)+Common.SIGN_COMMA);
//		   s.append(cal.get(Calendar.DAY_OF_MONTH)+Common.SIGN_COMMA);
//		   s.append(cal.get(Calendar.HOUR_OF_DAY)+Common.SIGN_COMMA);
//		   s.append(map.get("cjdbh")==null?Common.SIGN_NULL:"'"+map.get("cjdbh").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("cjdz")==null?Common.SIGN_NULL:"'"+map.get("cjdz").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("cjjgbh")==null?Common.SIGN_NULL:"'"+map.get("cjjgbh").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("sbbh")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("XT_CK_SB","CODE",map.get("sbbh").toString(),"DEPTID")+Common.SIGN_COMMA);
//		   s.append(map.get("fxbh")==null?Common.SIGN_NULL:map.get("fxbh").toString()+Common.SIGN_COMMA);
//		   s.append(map.get("cdxh")==null?Common.SIGN_NULL:"'"+map.get("cdxh").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(Common.SIGN_NULL+Common.SIGN_COMMA);
//		   s.append(map.get("clsd")==null?Common.SIGN_NULL:map.get("clsd").toString()+Common.SIGN_COMMA);
//		   s.append(map.get("zjtp1")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjtp1"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjtp2")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjtp2"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjtp3")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjtp3"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjtp4")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjtp4"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjtp4info")==null?Common.SIGN_NULL:"'"+map.get("zjtp4info").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjrltp")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjrltp"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zjsp")==null?Common.SIGN_NULL:"'"+dictionaryPool.ipTransform((String) map.get("zjsp"))+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("zplx")==null?Common.SIGN_NULL:map.get("zplx").toString()+Common.SIGN_COMMA);
//		   s.append(map.get("zpmblx")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("TR_ZP_MBLX_JC","CODE",map.get("zpmblx").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append(map.get("sbbh")==null?Common.SIGN_NULL:"'"+map.get("sbbh").toString()+"'"+Common.SIGN_COMMA);
//		   s.append(map.get("sbbh")==null?Common.SIGN_NULL:dictionaryPool.getElementValue("XT_CK_SB","CODE",map.get("sbbh").toString(),"ID")+Common.SIGN_COMMA);
//		   s.append("sysdate"+Common.SIGN_COMMA);
//		   s.append(Common.CANCEL_FIELD[1]+Common.SIGN_COMMA);
//		   s.append(Common.CANCEL_FIELD[0]+Common.SIGN_COMMA);
//		   s.append(Common.CANCEL_FIELD[0]+Common.SIGN_COMMA);
//		   s.append(Common.CANCEL_FIELD[0]+Common.SIGN_COMMA);
//		   s.append(Common.CANCEL_FIELD[0]+Common.SIGN_COMMA);
//		   s.append(Common.SIGN_NULL);
//		   try {
//			   String date=Common.stringTransforArray(cjsj,EnumUtil.YMD);
//			   String sql=Common.getWaterTableSaveSql(date, s.toString());
//			   logger.getRootLogger().info("water:"+sql);
//			   dataBaseDao2.save(sql);
//		   } catch (Exception e) {
//				e.printStackTrace();
//		   }
//	}
	/**
	 * 发送过车数据
	 * @param activeMQUtil
	 * @param map
	 * @return
	 */
//	private void sendJKSJ(MqHelper mq,Map map){
//		//发送到过车监控通道
//		map.put("zpxtname",map.get("zpxtid")==null?"":dictionaryPool.getElementValue("TR_CL_ZPXT_JC","INORDER",map.get("zpxtid").toString(),"NAME"));
//		map.put("hplxname",map.get("hplxidzdsb")==null?"":dictionaryPool.getElementValue("TR_CL_HPLX_JC","CODE",map.get("hplxidzdsb").toString(),"NAME"));
//		map.put("fxname",map.get("fxbh")==null?"":dictionaryPool.getElementValue("TR_ZP_FXBM_JC","CODE",map.get("fxbh").toString(),"NAME"));
//		map.put("cdname",map.get("cdxh")==null?"":dictionaryPool.getElementValue("XT_JZ_DL_CDSX_JC","CODE",map.get("cdxh").toString(),"NAME"));				
//		map.put("hpysname", map.get("hpysid")==null?"":dictionaryPool.getElementValue("TR_CL_HPYS_JC","CODE",map.get("hpysid").toString(),"NAME"));
//		map.put("csysname",map.get("csysid")==null?"":dictionaryPool.getElementValue("TR_CL_CSYS_JC","INORDER",map.get("csysid").toString(),"NAME"));
//		map.put("sjlyname",map.get("sjly")==null?"":dictionaryPool.getElementValue("TR_ZP_ZPSJLYLX_JC","INORDER",map.get("sjly").toString(),"NAME"));
//		map.put("cbname",map.get("cbid")==null?"":dictionaryPool.getElementValue("TR_CL_CB_JC","CODE",map.get("cbid").toString(),"NAME"));
//		map.put("zplxname",map.get("zplx")==null?"":dictionaryPool.getElementValue("TR_ZP_ZPLX_JC","CODE",map.get("zplx").toString(),"NAME"));
//		map.put("cjjgname",map.get("cjjgbh")==null?"":dictionaryPool.getElementValue("TR_GXDWJGDM_JC","CODE",map.get("cjjgbh").toString(),"NAME"));
//		map.put("cllxname",map.get("cllxid")==null?"":dictionaryPool.getElementValue("TR_CL_CLLX_JC","CODE",map.get("cllxid").toString(),"NAME"));
//		Map filterMap=new HashMap();
//		filterMap.put("hphm", map.get("hphmzdsb")==null?"":map.get("hphmzdsb").toString());
//		filterMap.put("cjdbh", map.get("cjdbh")==null?"":map.get("cjdbh").toString());
//		mq.sendTopicMessage(Common.GCJK, filterMap, com.json.simple.JSONValue.toJSONString(map));
//	}
	/**
	 * 黑名单违法判断
	 * @param map
	 * @param dictionaryPool
	 * @return
	 */
//	private boolean dealHMD(Map map,DictionaryPool dictionaryPool){
//		String hmdId = dictionaryPool.getElementValue("V_TR_MD_HEIMD_BKZ", "HPHM", map.get("hphmzdsb").toString(),"ID");
//		if(hmdId!=null&&!"".equals(hmdId.trim())){
//			if(sendMq(map)){
//				Logger.getRootLogger().info("send hmd");
//				StringBuffer sb=new StringBuffer();
//				sb.append(map.get("hplxidzdsb")==null?"":dictionaryPool.getElementValue("TR_CL_HPLX_JC","CODE",map.get("hplxidzdsb").toString(),"ID"));sb.append(",");
//				sb.append(map.get("hphmzdsb")==null?"''":"'"+map.get("hphmzdsb").toString()+"'");sb.append(",");
//				sb.append(map.get("hpysid")==null?"":dictionaryPool.getElementValue("TR_CL_HPYS_JC","CODE",map.get("hpysid").toString(),"ID"));sb.append(",");
//				sb.append(map.get("cllxid")==null?"":dictionaryPool.getElementValue("TR_CL_CLLX_JC","CODE",map.get("cllxid").toString(),"ID"));sb.append(",");
//				sb.append(map.get("cjsj")==null?"":"to_date('"+map.get("cjsj").toString()+"', 'yyyy-mm-dd hh24:mi:ss')");sb.append(",");
//				sb.append(map.get("cjdz")==null?"''":"'"+map.get("cjdz").toString()+"'");sb.append(",");
//				sb.append(map.get("fxbh")==null?"":map.get("fxbh").toString());sb.append(",");
//				sb.append(map.get("clsd")==null?"":map.get("clsd").toString());sb.append(",");
//				sb.append(map.get("zjtp1")==null?"''":"'"+dictionaryPool.ipTransform((String) map.get("zjtp1"))+"'");sb.append(",");
//				sb.append(map.get("zjtp2")==null?"''":"'"+dictionaryPool.ipTransform((String) map.get("zjtp2"))+"'");sb.append(",");
//				sb.append(map.get("zjtp3")==null?"''":"'"+dictionaryPool.ipTransform((String) map.get("zjtp3"))+"'");sb.append(",");
//				sb.append(map.get("zjtp4")==null?"''":"'"+dictionaryPool.ipTransform((String) map.get("zjtp4"))+"'");sb.append(",");
//				sb.append(hmdId);sb.append(",");
//				String sql=Common.HMD_TABLE_SAVE.replace(Common.SIGN_QUESTION, sb.toString());
//				try {
//					int f = dataBaseDao.save(sql);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Logger.getRootLogger().info("hmd save error");
//				}
//				return Common.True;
//			}else{
//				Logger.getRootLogger().info("hmd sendMq error");
//			}
//		}
//		return Common.False;
//	}
	/**
	 * 黑名单报警
	 * @param map
	 */
//	private  boolean sendMq(Map map){
//		List list = dataBaseDao.executeQuery(Common.SQL_MQ);
//		if(list!=null && list.size()>0){
//		Map mqMap = (Map)list.get(0);	
//		try {
//            //创建一个链接工厂
//			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mqMap.get("USERNAME").toString(), mqMap.get("PASSWORD").toString(),"tcp://"+mqMap.get("WWIP").toString()+":"+mqMap.get("WWPORT").toString());
//             //从工厂中创建一个链接
//            Connection connection = connectionFactory.createConnection();
//            //启动链接,不启动不影响消息的发送，但影响消息的接收
//            connection.start();
//            //创建一个事物session
//            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//            //获取消息发送的目的地，指消息发往那个地方
//            //Queue queue = session.createQueue(Common.HMD);
//            Topic queue = session.createTopic(Common.HMD);
//            //获取消息发送的生产者
//            MessageProducer producer = session.createProducer(queue);
//            TextMessage msg = session.createTextMessage(com.json.simple.JSONValue.toJSONString(map));
//            producer.send(msg);
//            session.commit();
//            //close connection
//            producer.close();
//            session.close();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Common.False;
//        }
//	  }
//	 return Common.True;
//	}
	/**
	 * Alter procedures to insert sql
	 * This function have been invalid!
	 * @param map
	 * @param dictionaryPool
	 * @return
	 */
//	public  ArrayList<String> dealWaterRecord_(Map map,DictionaryPool dictionaryPool) {
//		Boolean isHei=Common.False;
//		ArrayList<String> listPar = new ArrayList<String>();
//		try{
//			isHei=dealHMD(map,dictionaryPool);//2
//			// 调用存储过程
//			//1
//			listPar.add(map.get("zpxtid")==null?"":dictionaryPool.getElementValue("TR_CL_ZPXT_JC","CODE",map.get("zpxtid").toString(),"ID") );
//			//2
//			listPar.add(map.get("zpxtjlid")==null?"":map.get("zpxtjlid").toString());		
//			//3
//			listPar.add(map.get("hplxidzdsb")==null?"":dictionaryPool.getElementValue("TR_CL_HPLX_JC","CODE",map.get("hplxidzdsb").toString(),"ID"));
//			//4
//			listPar.add(map.get("hphmzdsb")==null?"":map.get("hphmzdsb").toString());
//			//5
//			listPar.add(map.get("hpysid")==null?"":dictionaryPool.getElementValue("TR_CL_HPYS_JC","CODE",map.get("hpysid").toString(),"ID"));
//			//6
//			listPar.add(map.get("cllxid")==null?"":dictionaryPool.getElementValue("TR_CL_CLLX_JC","CODE",map.get("cllxid").toString(),"ID"));
//			//7
//			listPar.add(map.get("cbid")==null?"":dictionaryPool.getElementValue("TR_CL_CB_JC","CODE",map.get("cbid").toString(),"ID"));
//			//8
//			listPar.add(map.get("csysid")==null?"":dictionaryPool.getElementValue("TR_CL_CSYS_JC","CODE",map.get("csysid").toString(),"ID"));
//			//9
//			listPar.add(map.get("cjsj")==null?"":map.get("cjsj").toString());
//			//10
//			listPar.add(map.get("cjdbh")==null?"":map.get("cjdbh").toString());
//			//11
//			listPar.add(map.get("cjdz")==null?"":map.get("cjdz").toString());
//			//12
//			listPar.add(map.get("cjjgbh")==null?"":map.get("cjjgbh").toString());			
//			//13
//			listPar.add(map.get("sjly")==null?"":map.get("sjly").toString());
//			//14
//			listPar.add(map.get("zplx")==null?"":map.get("zplx").toString());
//			//15
//			listPar.add(map.get("sbbh")==null?"":dictionaryPool.getElementValue("XT_CK_SB","CODE",map.get("sbbh").toString(),"ID"));
//			//16
//			listPar.add(map.get("sbbh")==null?"":map.get("sbbh").toString());
//			//17
//			listPar.add(map.get("sbbh")==null?"":dictionaryPool.getElementValue("XT_CK_SB","CODE",map.get("sbbh").toString(),"DEPTID"));
//			//18
//			listPar.add(map.get("fxbh")==null?"":map.get("fxbh").toString());
//			//19
//			listPar.add(map.get("cdxh")==null?"":map.get("cdxh").toString());		
//			//20
//			listPar.add(map.get("clsd")==null?"":map.get("clsd").toString());		
//			//21
//			listPar.add(map.get("zjtp1")==null?"":dictionaryPool.ipTransform((String) map.get("zjtp1")));
//			//22
//			listPar.add(map.get("zjtp2")==null?"":dictionaryPool.ipTransform((String) map.get("zjtp2")));
//			//23
//			listPar.add(map.get("zjtp3")==null?"":dictionaryPool.ipTransform((String) map.get("zjtp3")));
//			//24
//			listPar.add(map.get("zpmblx")==null?"":dictionaryPool.getElementValue("TR_ZP_MBLX_JC","CODE",map.get("zpmblx").toString(),"ID"));
//			//25
//			listPar.add(map.get("zjtp4")==null?"":dictionaryPool.ipTransform((String) map.get("zjtp4")));
//			//26
//			listPar.add(map.get("zjtp4info")==null?"":map.get("zjtp4info").toString());
//			//27
//			listPar.add(map.get("zjrltp")==null?"":dictionaryPool.ipTransform((String) map.get("zjrltp")));
//			//28
//			listPar.add(map.get("zjsp")==null?"":dictionaryPool.ipTransform((String) map.get("zjsp")));	
//			if(isHei){
//				//29
//				listPar.add("1");
//				//30
//				listPar.add(map.get("hphmzdsb")==null?"":dictionaryPool.getElementValue("V_TR_MD_HEIMD_BKZ","HPHM",map.get("hphmzdsb").toString(),"ID"));
//			}
//			else{
//				//29
//				listPar.add("0");
//				//30
//				listPar.add("");
//			}
//			//31
//			listPar.add("0");
//			//32
//			listPar.add("");
//			//33
//			listPar.add("0");
//			//34
//			listPar.add("");	
//			//35
//			listPar.add("-1");
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}
////		String parm="1_zpxtid,2_msgid,3_hplxidzdsb,4_hphmzdsb,5_hpysid,6_cllxid,7_cbid,8_csysid,9_cjsj,10_cjdbh,11_cjdz,12_cjjgbh,13_sjly,14_zplx,15_sbid,16_sbbh,17_deptid,18_fxbh,19_cdxh,20_clsd,21_zjtp1,22_zjtp2,23_zjtp3,24_mblxid,25_zjtp4,26_zjtp4info,27_zjrltp,28_zjsp,29_heibj,30_heimdID,31_baibj,32_baimdid,33_hongbj,34_hongmdid,35_AID";
////		String tableColumn="ID ,SJLY,ZPXTID,ZPXTJLID,MSGID,HPLXIDZDSB,HPHMZDSB,HPLXID    ,HPHM    ,HPYSID,CSYSID,CLLXID,CBID,CJSJ ,CJSJMonth,CJSJDay,CJSJHour,CJDBH,CJDZ,CJJGBH,DeptID,FXBH,CDXH,TDID,CLSD,ZJTP1,ZJTP2,ZJTP3,ZJTP4,ZJTP4INFO,ZJRLTP,ZJSP,ZPLX           ,ZPMBID,SBBH,SBID,DRSJ   ,WFBJ,SCZT,HONGBJ,HEIBJ,BAIBJ,Memo";
////		String tableValue= "AID,sjly,zpxtid,null    ,msgid,hplxidzdsb,hphmzdsb,hplxidzdsb,hphmzdsb,hpysid,csysid,cllxid,cbid,DCJSJ,IMONTH   ,IDAY   ,IHOUR   ,cjdbh,cjdz,cjjgbh,deptid,fxbh,cdxh,NULL,clsd,zjtp1,zjtp2,zjtp3,zjtp4,zjtp4info,zjrltp,zjsp,to_number(zplx),mblxid,sbbh,sbid,CURDATE,1   ,0   ,hongbj,heibj,baibj,MEMO";
////		String temp="		    13    1               2       3          4          3         4       5      8      6     7    9_需转                                                                          	10	 11    12     17	18	 19			20  21     22    23     25    26        27   28          14         24   16   15                       33    29    31                        ";
//		return listPar;
//	}
}
