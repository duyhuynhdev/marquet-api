package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.SmallBannerEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class SmallBannerDao {
    public static final int NUMBER_OF_SMALLBANNER = 4;
    public boolean insert(SmallBannerEntity object){
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
    public boolean update(SmallBannerEntity object){
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
    public boolean delete(SmallBannerEntity object){
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
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<SmallBannerEntity> list = (List<SmallBannerEntity>) session
                    .createCriteria(SmallBannerEntity.class).list();
            for (SmallBannerEntity obj : list) {
                obj.setSetTime(getTimeForSmallBanner());
                Database.getInstance().getSmallBannerEntityHashMap().put((long)obj.getId(), obj);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
    public static long getTimeForSmallBanner(){
        List<SmallBannerEntity> smallBannerEntities = new ArrayList<>(Database.getInstance().getSmallBannerEntityHashMap().values());
        int idx = smallBannerEntities.size();
        long duration = new JSONObject(Database.getInstance().getElementEntity().getSmallBannerInfo()).getLong(ElementDao.DURATION);
        long lastTurn = new DateTimeUtil().getNow();
        if(idx >= NUMBER_OF_SMALLBANNER) {
            int oldBannerIdx = idx / NUMBER_OF_SMALLBANNER;
            SmallBannerEntity oldBanner = smallBannerEntities.get(oldBannerIdx);
            if (oldBanner.getSetTime() > lastTurn)
                lastTurn = oldBanner.getSetTime();
        }
        return lastTurn+duration;
    }
}
