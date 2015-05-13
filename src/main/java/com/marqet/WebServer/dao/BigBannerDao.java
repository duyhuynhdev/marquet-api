package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.BigBannerEntity;
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
public class BigBannerDao {
    public static final int NUMBER_OF_BIGBANNER = 5;
    public boolean insert(BigBannerEntity object){
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
    public boolean update(BigBannerEntity object){
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
    public boolean delete(BigBannerEntity object){
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
            List<BigBannerEntity> list = (List<BigBannerEntity>) session
                    .createCriteria(BigBannerEntity.class).list();
            for (BigBannerEntity obj : list) {
                obj.setSetTime(getTimeForBigBanner());
                Database.getInstance().getBigBannerEntityHashMap().put(obj.getId(), obj);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
    public static long getTimeForBigBanner(){
        List<BigBannerEntity> bigBannerEntities = new ArrayList<>(Database.getInstance().getBigBannerEntityHashMap().values());
        int idx = bigBannerEntities.size();
        long duration = new JSONObject(Database.getInstance().getElementEntity().getBigBannerInfo()).getLong(ElementDao.DURATION);
        long lastTurn = new DateTimeUtil().getNow();
        if(idx >= NUMBER_OF_BIGBANNER) {
            int oldBannerIdx = idx / NUMBER_OF_BIGBANNER;
            BigBannerEntity oldBanner = bigBannerEntities.get(oldBannerIdx);
            if (oldBanner.getSetTime() > lastTurn)
                lastTurn = oldBanner.getSetTime();
        }
        return lastTurn+duration;
    }

}
