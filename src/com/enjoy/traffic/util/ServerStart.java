package com.enjoy.traffic.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.enjoy.traffic.deal.DahuaWaterDeal;
public class ServerStart implements ServletContextListener{
	private final Logger logger= LogManager.getLogger(this.getClass());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		WebApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
		//大华
		DahuaWaterDeal dahuaWaterDeal = (DahuaWaterDeal)application.getBean("dahuaWaterDeal");
		//1
		//大华过车
		ExecutorService exec_dahuaWater = Executors.newCachedThreadPool();// 获得一个线程池
		for(int i=0;i<Integer.parseInt((String) Common.getProperties().get(Common.DahuaWaterThread));i++){
			try {
				exec_dahuaWater.submit(dahuaWaterDeal);
				logger.info("大华过车线程："+i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
