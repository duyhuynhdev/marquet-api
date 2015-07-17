package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.OfferEntity;
import com.marqet.WebServer.pojo.ProductEntity;
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
public class OfferDao {
    public boolean insert(OfferEntity object){
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
    public boolean update(OfferEntity object){
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
    public boolean delete(OfferEntity object){
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
            Database database = Database.getInstance();
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            List<OfferEntity> list = (List<OfferEntity>) session
                    .createCriteria(OfferEntity.class).list();
            for (OfferEntity obj : list) {
                database.getOfferEntityHashMap().put(obj.getId(), obj);
                //put to email RF
                HashSet<Long> offerList = database.getOfferRFbyEmail().get(obj.getBuyerEmail());
                if(offerList==null)
                    offerList = new HashSet<>();
                offerList.add(obj.getId());
                database.getOfferRFbyEmail().put(obj.getBuyerEmail(),offerList);
                //put to wait Reply offer
                if(obj.getStatus()<0){
                    ProductEntity product = database.getProductEntityHashMap().get(obj.getProductId());
                    if(product!=null) {
                        HashSet<Long> waitingList = database.getOfferRFbyEmail().get(product.getEmail());
                        if (waitingList == null)
                            waitingList = new HashSet<>();
                        waitingList.add(obj.getId());
                        database.getOfferRFbyEmail().put(product.getEmail(), waitingList);
                    }
                }
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
