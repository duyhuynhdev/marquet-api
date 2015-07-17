package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.SubMessageEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class SubMessageDao {
    public boolean insert(SubMessageEntity object){
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
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public boolean update(SubMessageEntity object){
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
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public boolean delete(SubMessageEntity object){
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
            ex.printStackTrace();
        }finally {
            session.close();
        }
        return result;
    }
    public static void backUpDatabase() {
        try {
            Database database = Database.getInstance();
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<SubMessageEntity> list = (List<SubMessageEntity>) session
                    .createCriteria(SubMessageEntity.class).list();
            for (SubMessageEntity obj : list) {
                database.getSubMessageEntityHashMap().put(obj.getId(), obj);
                //put to subMessRFMessage
                HashSet<Long> subMessList = database.getSubMessagesRFMessageId().get(obj.getMessageId());
                if(subMessList==null)
                    subMessList = new HashSet<>();
                subMessList.add(obj.getId());
                database.getSubMessagesRFMessageId().put(obj.getMessageId(),subMessList);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
