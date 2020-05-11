package com.enjoy.traffic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Repository;

@Repository
public class DataBaseDao2Impl implements DataBaseDao2 {
	public DataBaseDao2Impl() {
		// TODO Auto-generated constructor stub
		Logger.getRootLogger().info("dataBaseDao2 init");
	}
	@Resource
	private SessionFactory sessionFactory2;
	
	@Override
	public List executeQuery(String sql) {
		Session session = sessionFactory2.openSession(); // 相当于得到一个Connection
	    Query query = session.createSQLQuery(sql);
	    query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
	    List list = query.list();
	    session.close();
	    return list;
	}
	@Override
	public int save(String sql) {
		// TODO Auto-generated method stub
		int result=-1;
		Connection conn=null;
		Statement stt=null;
//		PreparedStatement prepare=null;
		try {
			Session session = sessionFactory2.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			conn=((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider().getConnection();
//			prepare=conn.prepareStatement(sql);
//			result = prepare.executeUpdate();
			stt = conn.createStatement();
			result = stt.executeUpdate(sql);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
//			if(prepare !=null){
//				try {
//					prepare.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			if(stt!=null){
				try {
					stt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn !=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        return result;  
	}
	@Override
	public boolean create(String sql){
		// TODO Auto-generated method stub
		Connection conn=null;
		Statement stt=null;
		boolean result=false;
		try {
			Session session = sessionFactory2.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			conn = ((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider().getConnection();
			stt = conn.createStatement();
			result = stt.execute(sql);
			transaction.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stt !=null){
				try {
					stt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn !=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        return result;  
	}
}
