package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.FeedbackEntity;
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
public class FeedbackDao {
    public boolean insert(FeedbackEntity object) {
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

    public boolean update(FeedbackEntity object) {
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

    public boolean delete(FeedbackEntity object) {
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
            List<FeedbackEntity> list = (List<FeedbackEntity>) session
                    .createCriteria(FeedbackEntity.class).list();
            for (FeedbackEntity obj : list) {
                database.getFeedbackEntityHashMap().put(obj.getId(), obj);
                //put to buyerFeedbackRFEmail
                List<Long> feedBuyerList = database.getBuyerFeedbackRFEmail().get(obj.getBuyerEmail());
                if (feedBuyerList == null)
                    feedBuyerList = new ArrayList<>();
                feedBuyerList.add(obj.getId());
                database.getBuyerFeedbackRFEmail().put(obj.getBuyerEmail(), feedBuyerList);
                //put to sellerFeedbackRFEmail
                List<Long> feedSellerList = database.getSellerFeedbackRFEmail().get(obj.getSellerEmail());
                if (feedSellerList == null)
                    feedSellerList = new ArrayList<>();
                feedSellerList.add(obj.getId());
                database.getSellerFeedbackRFEmail().put(obj.getSellerEmail(), feedSellerList);
                //put to required feedback
                if (obj.getStatus() < 0) {
                    List<Long> requireFeedbackList = database.getRequiredFeedback().get(obj.getBuyerEmail());
                    if (requireFeedbackList == null)
                        requireFeedbackList = new ArrayList<>();
                    requireFeedbackList.add(obj.getId());
                    database.getSellerFeedbackRFEmail().put(obj.getBuyerEmail(), requireFeedbackList);

                }
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
