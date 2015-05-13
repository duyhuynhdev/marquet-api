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

import java.util.ArrayList;
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
                // put activity to : 1/ +person 2/ -person 3/ follower of +person
                //+person
                //-----check exist user
                UserEntity userActive = database.getUserEntityHashMap().get(obj.getSubjectEmail());
                if(userActive!=null){
                    List<Long> activityList = database.getActivityRFEmail().get(userActive.getEmail());
                    if(activityList==null)
                        activityList = new ArrayList<>();
                    activityList.add(obj.getId());
                    database.getActivityRFEmail().put(userActive.getEmail(),activityList);
                    //--follower of +person ( just do with upload activity)
                    if(obj.getAction().equals(ActivityUtil.LABEL_UPLOAD_ACTION)) {
                        List<String> followerList = database.getFollowerRF().get(userActive.getEmail());
                        if (followerList != null) {
                            for (String u : followerList) {
                                List<Long> activityFollowerList = database.getActivityRFEmail().get(u);
                                if (activityFollowerList == null)
                                    activityFollowerList = new ArrayList<>();
                                activityFollowerList.add(obj.getId());
                                database.getActivityRFEmail().put(u, activityFollowerList);
                            }
                        }
                    }
                }
                //-person
                //-----check exist user
                UserEntity userPassive = database.getUserEntityHashMap().get(obj.getObjectEmail());
                if(userPassive!=null){
                    List<Long> activityList = database.getActivityRFEmail().get(userPassive.getEmail());
                    if(activityList==null)
                        activityList = new ArrayList<>();
                    activityList.add(obj.getId());
                    database.getActivityRFEmail().put(userPassive.getEmail(),activityList);
                }

            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
