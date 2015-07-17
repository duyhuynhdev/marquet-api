package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.ActivityEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.ActivityUtil;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class ActivityDao {
    public boolean insert(ActivityEntity object) {
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

    public boolean update(ActivityEntity object) {
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

    public boolean delete(ActivityEntity object) {
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
            List<ActivityEntity> list = (List<ActivityEntity>) session
                    .createCriteria(ActivityEntity.class).list();
            for (ActivityEntity obj : list) {
                database.getActivityEntityHashMap().put(obj.getId(), obj);
                UserEntity users = database.getUserEntityHashMap().get(obj.getOwnerEmail());
                if (users != null) {
                    HashSet<Long> activityList = database.getActivityRFEmail().get(users.getEmail());
                    if (activityList == null)
                        activityList = new HashSet<>();
                    activityList.add(obj.getId());
                    database.getActivityRFEmail().put(users.getEmail(), activityList);
                    if (obj.getAction() == ActivityUtil.WATCHING_PRODUCT && obj.getRef() > 0) {
                        HashMap<Long, Long> watchProductLog = database.getWatchingProductLogHashMap().get(users.getEmail());
                        if (watchProductLog == null)
                            watchProductLog = new HashMap<>();
                        watchProductLog.put(obj.getRef(), obj.getId());
                        database.getWatchingProductLogHashMap().put(users.getEmail(), watchProductLog);
                    }
                }
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
