package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.CityEntity;
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
public class CityDao {
    public boolean insert(CityEntity object){
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
    public boolean update(CityEntity object){
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
    public boolean delete(CityEntity object){
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
            List<CityEntity> list = (List<CityEntity>) session
                    .createCriteria(CityEntity.class).list();
            for (CityEntity obj : list) {
                Database.getInstance().getCityEntityHashMap().put(obj.getCode(), obj);
                //put into cityRFcountryCode
                HashSet<String> cityList = Database.getInstance().getCityRFbyCountryCode().get(obj.getCountryCode());
                if(cityList==null){
                    cityList = new HashSet<>();
                }
                cityList.add(obj.getCode());
                Database.getInstance().getCityRFbyCountryCode().put(obj.getCountryCode(),cityList);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
