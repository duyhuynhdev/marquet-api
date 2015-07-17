package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.BigBannerEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.HibernateUtil;
import com.marqet.WebServer.util.MapUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class BigBannerDao {
    public static final int NUMBER_OF_BIGBANNER =
            Integer.parseInt(new JSONObject(Database.getInstance().getElementEntity().getBigBannerInfo()).get("num").toString());
    public static final String DELETED = "deleted";
    public static final String SHOW = "show";
    public static final String WAITING = "waiting";

    public boolean insert(BigBannerEntity object) {
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

    public boolean update(BigBannerEntity object) {
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

    public boolean delete(BigBannerEntity object) {
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
            List<BigBannerEntity> list = (List<BigBannerEntity>) session
                    .createCriteria(BigBannerEntity.class).list();
            for (BigBannerEntity obj : list) {
                if (obj.getStatus().equals(SHOW)) {
                    database.getBigBannerShowQueue().put(obj.getId(), obj);
                    database.getBigBannerExist().put(obj.getEmail()+"#"+obj.getProductId(),obj.getId());
                }
                if (obj.getStatus().equals(WAITING)) {
                    database.getBigBannerWaitingStack().put(obj.getId(), obj);
                    database.getBigBannerExist().put(obj.getEmail()+"#"+obj.getProductId(),obj.getId());
                }
                //old
                database.getBigBannerEntityHashMap().put(obj.getId(), obj);
            }
            if (database.getBigBannerShowQueue().size() > NUMBER_OF_BIGBANNER ||
                    (database.getBigBannerShowQueue().size() == NUMBER_OF_BIGBANNER && database.getBigBannerWaitingStack().size() > 0))
            database.setBigPopTime(new DateTimeUtil().getNow());
            database.setBigBannerShowQueue((LinkedHashMap<Long, BigBannerEntity>) MapUtil.sortByValue(database.getBigBannerShowQueue()));
            database.setBigBannerWaitingStack((LinkedHashMap<Long, BigBannerEntity>) MapUtil.sortByValue(database.getBigBannerWaitingStack()));
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

    public static long getTimeForBigBanner() {
        List<BigBannerEntity> bigBannerEntities = new ArrayList<>(Database.getInstance().getBigBannerEntityHashMap().values());
        int idx = bigBannerEntities.size();
        long duration = new JSONObject(Database.getInstance().getElementEntity().getBigBannerInfo()).getLong(ElementDao.DURATION);
        long lastTurn = new DateTimeUtil().getNow();
        if (idx >= NUMBER_OF_BIGBANNER) {
            int oldBannerIdx = idx / NUMBER_OF_BIGBANNER;
            BigBannerEntity oldBanner = bigBannerEntities.get(oldBannerIdx);
            if (oldBanner.getSetTime() > lastTurn)
                lastTurn = oldBanner.getSetTime();
        }
        return lastTurn + duration;
    }

}
