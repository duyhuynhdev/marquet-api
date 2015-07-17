package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.WatchListEntity;
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
public class WatchListDao {
    public boolean insert(WatchListEntity object) {
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

    public boolean update(WatchListEntity object) {
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

    public boolean delete(WatchListEntity object) {
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
            List<WatchListEntity> list = (List<WatchListEntity>) session
                    .createCriteria(WatchListEntity.class).list();
            for (WatchListEntity obj : list) {
                database.getWatchListEntityHashMap().put(obj.getId(), obj);
                HashSet<Long> watchList = database.getWatchListRFEmail().get(obj.getEmail());
                if (watchList == null)
                    watchList = new HashSet<>();
                watchList.add(obj.getId());
                database.getWatchListRFEmail().put(obj.getEmail(), watchList);
                HashSet<String> patternList = database.getWatchListPatternRFEmail().get(obj.getEmail());
                if (patternList == null)
                    patternList = new HashSet<>();
                patternList.add(obj.getPattern());
                database.getWatchListPatternRFEmail().put(obj.getEmail(), patternList);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
