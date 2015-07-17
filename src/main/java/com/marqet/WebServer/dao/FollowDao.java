package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.FollowEntity;
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
public class FollowDao {
    public boolean insert(FollowEntity object) {
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

    public boolean update(FollowEntity object) {
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

    public boolean delete(FollowEntity object) {
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
            List<FollowEntity> list = (List<FollowEntity>) session
                    .createCriteria(FollowEntity.class).list();
            for (FollowEntity obj : list) {
                database.getFollowEntityList().put(obj.getId(), obj);
                //backup followMapIdRF;
                database.getFollowMapIdRF().put(obj.getFollower() + "#" + obj.getBeFollower(), obj.getId());
                //backup followerRF
                HashSet<String> followers = database.getFollowerRF().get(obj.getBeFollower());
                if (followers == null)
                    followers = new HashSet<>();
                followers.add(obj.getFollower());
                database.getFollowerRF().put(obj.getBeFollower(), followers);
                //backup followingRF
                HashSet<String> beFollowers = database.getFollowingRF().get(obj.getFollower());
                if (beFollowers == null)
                    beFollowers = new HashSet<>();
                beFollowers.add(obj.getBeFollower());
                database.getFollowingRF().put(obj.getFollower(), beFollowers);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
