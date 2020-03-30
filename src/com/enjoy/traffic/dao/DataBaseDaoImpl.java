package com.enjoy.traffic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.stereotype.Repository;

@Repository
public class DataBaseDaoImpl implements DataBaseDao{
	public DataBaseDaoImpl() {
		// TODO Auto-generated constructor stub
		Logger.getRootLogger().info("dataBaseDao init");
	}
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public List executeQuery(String sql) {
		Session session = sessionFactory.openSession(); // 相当于得到一个Connection
	    Query query = session.createSQLQuery(sql);
	    query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
	    List list = query.list();
	    session.close();
	    return list;
	}
	/**
	 * fase
	 */
	public int executeQueryProc2(String proc, List listPar)
    {
        Connection conn;
        CallableStatement pstmt;
        Transaction transaction;
        int keyid;
        Session session = null;
        conn = null;
        pstmt = null;
        transaction = null;
        keyid = -1;
        try
        {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
            conn = ((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider().getConnection();
            String sqlproc = (new StringBuilder("call ")).append(proc).append("(").toString();
            if(listPar != null && listPar.size() > 0)
            {
                for(int i = 0; i < listPar.size(); i++)
                    if(i == 0)
                        sqlproc = (new StringBuilder(String.valueOf(sqlproc))).append("?").toString();
                    else
                        sqlproc = (new StringBuilder(String.valueOf(sqlproc))).append(",?").toString();

            }
            sqlproc = (new StringBuilder(String.valueOf(sqlproc))).append(")").toString();
            pstmt = conn.prepareCall(sqlproc);
            if(listPar != null && listPar.size() > 0)
            {
                for(int i = 0; i < listPar.size() - 1; i++)
                {
                    String value = (String)listPar.get(i);
                    pstmt.setString(i + 1, value);
                }

                pstmt.registerOutParameter(listPar.size(), 4);
            }
            pstmt.execute();
            keyid = Integer.parseInt(pstmt.getString(listPar.size()));
            transaction.commit();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            if(transaction != null)
                transaction.rollback();
        }
        try
        {
            if(transaction != null)
                transaction = null;
            if(pstmt != null)
            {
                pstmt.close();
                pstmt = null;
            }
            if(conn != null)
            {
                conn.close();
                conn = null;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        Exception exception1;
        try
        {
            if(transaction != null)
                transaction = null;
            if(pstmt != null)
            {
                pstmt.close();
                pstmt = null;
            }
            if(conn != null)
            {
                conn.close();
                conn = null;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        try
        {
            if(transaction != null)
                transaction = null;
            if(pstmt != null)
            {
                pstmt.close();
                pstmt = null;
            }
            if(conn != null)
            {
                conn.close();
                conn = null;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return keyid;
    }
	/**
	 * slow
	 */
//	@Override
//	public int executeQueryProc(String procName, List list) {
//		ConnectionProvider cp = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
//		int id=Common.PRO_DEFAULE;
//		try {
//			StringBuilder sql=new StringBuilder();
//			sql.append("{call ");
//			sql.append(procName);
//			sql.append("(?");
//			for(int i=1;i<list.size();i++){
//				sql.append(",?");
//			}
//			sql.append(")}");
//			sql.toString();
////		    CallableStatement statement = cp.getConnection().prepareCall("{call SP_TR_ZP_TX101ZPSYJL_ADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");  
//			CallableStatement statement = cp.getConnection().prepareCall(sql.toString());
//		    for(int i=1;i<=list.size()-1;i++){
//		    	statement.setString(i, String.valueOf(list.get(i-1)));
//		    }
//		    statement.setInt(list.size(), -1);//last parameter is out value
//		    statement.registerOutParameter(list.size(),Types.INTEGER);//set out value type
//		    statement.execute();
//		    id = statement.getInt(list.size());
//		   } catch (Exception e){
//			   e.printStackTrace();
//		   }finally{
//			   
//			   cp.close();
//		   }
//		return id;
//	}
	@Override
	public int save(String sql) {
		// TODO Auto-generated method stub
		int result=-1;
		Connection conn=null;
		Statement stt=null;
//		PreparedStatement prepare=null;
		try {
			Session session = sessionFactory.getCurrentSession();
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
//	public int save(String sql) throws Exception{
//		// TODO Auto-generated method stub
//		Session session = sessionFactory.openSession(); // 相当于得到一个Connection
//		Transaction transaction = session.beginTransaction();
//		Connection conn = session.connection();
//		Statement stt = conn.createStatement();
//		int result = stt.executeUpdate(sql);
//		transaction.commit();
//		stt.close();
//		conn.close();
//        return result;  
//	}
	@Override
	public int saveWater(List list){
		// TODO Auto-generated method stub
		int result=0;
		try {
			Session session = sessionFactory.openSession(); // 相当于得到一个Connection
			Transaction transaction = session.beginTransaction();
			Connection conn = session.connection();
			Statement stt = conn.createStatement();
			result = stt.executeUpdate("");
			transaction.commit();
			stt.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;  
	}
}
