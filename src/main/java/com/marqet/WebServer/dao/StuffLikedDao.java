package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.StuffLikedEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class StuffLikedDao {
    public boolean insert(StuffLikedEntity object){
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
    public boolean update(StuffLikedEntity object){
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
    public boolean delete(StuffLikedEntity object){
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
            List<StuffLikedEntity> list = (List<StuffLikedEntity>) session
                    .createCriteria(StuffLikedEntity.class).list();
            for (StuffLikedEntity obj : list) {
                database.getStuffLikedEntityHashMap().put(obj.getId(), obj);
                // put to product RF
                List<Long> stuffLikedList = Database.getInstance().getStuffLikedRFbyProductId().get(obj.getProductId());
                if(stuffLikedList==null)
                    stuffLikedList = new ArrayList<>();
                stuffLikedList.add(obj.getId());
                database.getStuffLikedRFbyProductId().put(obj.getProductId(),stuffLikedList);
                // put to email RF
                List<Long> stuffLikedList2 = Database.getInstance().getStuffLikedRFbyEmail().get(obj.getBuyerEmail());
                if(stuffLikedList2==null)
                    stuffLikedList2 = new ArrayList<>();
                stuffLikedList2.add(obj.getId());
                database.getStuffLikedRFbyEmail().put(obj.getBuyerEmail(),stuffLikedList2);
                database.getStuffLikedRFbyEmailAndProductId().put(obj.getBuyerEmail()+"#"+obj.getProductId(),obj.getId());
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
