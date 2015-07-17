package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.MessageEntity;
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
public class MessageDao {
    public boolean insert(MessageEntity object) {
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
            result = true;
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean update(MessageEntity object) {
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(object);
            transaction.commit();
            result = true;
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean delete(MessageEntity object) {
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();
            result = true;
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    public static void backUpDatabase() {
        try {
            Database database = Database.getInstance();
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<MessageEntity> list = (List<MessageEntity>) session
                    .createCriteria(MessageEntity.class).list();
            for (MessageEntity obj : list) {
                database.getMessageEntityHashMap().put(obj.getId(), obj);
                //put to buyerMessRFEmail
                HashSet<Long> messBuyerList = database.getBuyerMessRFEmail().get(obj.getSenderEmail());
                if (messBuyerList == null)
                    messBuyerList = new HashSet<>();
                messBuyerList.add(obj.getId());
                database.getBuyerMessRFEmail().put(obj.getSenderEmail(), messBuyerList);
                //put to sellerMessRFEmail
                HashSet<Long> messSellerList = database.getSellerMessRFEmail().get(obj.getReceiverEmail());
                if (messSellerList == null)
                    messSellerList = new HashSet<>();
                messSellerList.add(obj.getId());
                database.getSellerMessRFEmail().put(obj.getReceiverEmail(), messSellerList);
                //distinct message
                database.getDistinctMessage().put(obj.getSenderEmail()+"#"+obj.getReceiverEmail()+"#"+obj.getProductId(),obj.getId());

            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
