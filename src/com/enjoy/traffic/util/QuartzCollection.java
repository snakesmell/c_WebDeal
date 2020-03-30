package com.enjoy.traffic.util;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.enjoy.traffic.dao.DataBaseDao;
@Component
public class QuartzCollection  {
	@Autowired
	DataBaseDao dataBaseDao;
	public void setGCMQ(){
		String sql="select * from tr_zp_gcmq a where a.updatetime>sysdate";
		List list = dataBaseDao.executeQuery(sql);
		WebApplicationContext  webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		webApplicationContext.getServletContext().setAttribute("",list.size());
		
	}
}
