package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.SubCategoryEntity;
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
public class SubCategoryDao {
    public boolean insert(SubCategoryEntity object){
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
    public boolean update(SubCategoryEntity object){
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
    public boolean delete(SubCategoryEntity object){
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
            List<SubCategoryEntity> list = (List<SubCategoryEntity>) session
                    .createCriteria(SubCategoryEntity.class).list();
            for (SubCategoryEntity obj : list) {
                Database.getInstance().getSubCategoryEntityHashMap().put(obj.getId(), obj);
                //put into SubCategoryRFbyCategoryId
                HashSet<Long> lstSubCategory = Database.getInstance().getSubCategoryRFbyCategoryId().get(obj.getCategoryId());
                if(lstSubCategory==null){
                    lstSubCategory = new HashSet<>();
                }
                lstSubCategory.add(obj.getId());
                Database.getInstance().getSubCategoryRFbyCategoryId().put(obj.getCategoryId(),lstSubCategory);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
