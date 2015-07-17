package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class UserDao {
    Logger logger = Logger.getLogger(this.getClass());
    public boolean insert(UserEntity object){
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
            result = true;
        }catch (HibernateException ex){
            transaction.rollback();
            logger.error(ex.getStackTrace());
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public boolean update(UserEntity object){
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.update(object);
            transaction.commit();
            result = true;
        }catch (HibernateException ex){
            transaction.rollback();
            logger.error(ex.getStackTrace());
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public boolean delete(UserEntity object){
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();
            result = true;
        }catch (HibernateException ex){
            transaction.rollback();
            logger.error(ex.getStackTrace());
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public static void backUpDatabase() {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<UserEntity> list = (List<UserEntity>) session
                    .createCriteria(UserEntity.class).list();
            for (UserEntity obj : list) {
                Database database = Database.getInstance();
                database.getUserEntityHashMap().put(obj.getEmail(), obj);
                if(obj.getFacebookId()!=null && !obj.getFacebookId().replaceAll(" ","").equals("")) {
                    database.getFacebookRF().put(obj.getFacebookId(), obj.getEmail());
                }
                if(obj.getGoogleId()!=null && !obj.getGoogleId().replaceAll(" ","").equals("")) {
                    database.getGoogleplushRF().put(obj.getGoogleId(), obj.getEmail());
                }
                if(obj.getTwitterId()!=null && !obj.getTwitterId().replaceAll(" ","").equals("")) {
                    database.getTwitterRF().put(obj.getTwitterId(), obj.getEmail());
                }
                try {
                    JSONArray broadcastArray = new JSONArray();
                    try{
                        broadcastArray = new JSONArray(obj.getBroadcastId());
                    }catch (Exception ignored){}
                    for(int i = 0 ; i < broadcastArray.length(); i++){
                        String pair = broadcastArray.getString(i);
                        String temp [] = pair.split("#");
                        HashMap<String,String> broadcastMap = database.getBroadcastLogRFEmail().get(obj.getEmail());
                        if(broadcastMap == null){
                            broadcastMap = new HashMap<>();
                        }
                        broadcastMap.put(temp[0],temp[1]);
                        database.getBroadcastLogRFEmail().put(obj.getEmail(),broadcastMap);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
