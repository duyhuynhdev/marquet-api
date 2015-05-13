package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.CityEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
public class ProductDao {
    public boolean insert(ProductEntity object) {
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

    public boolean update(ProductEntity object) {
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

    public boolean delete(ProductEntity object) {
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
            List<ProductEntity> list = (List<ProductEntity>) session
                    .createCriteria(ProductEntity.class).list();
            for (ProductEntity obj : list) {
                database.getProductEntityHashMap().put(obj.getId(), obj);
                //backup ProductRFCountryCode
                HashMap<String, CityEntity> cityHashMap = database.getCityEntityHashMap();
                String countryCode = cityHashMap.get(obj.getCityCode()).getCountryCode();
                HashMap <Long,List<Long>> productFRSubCategory = database.getProductRFbyCountryCode().get(countryCode);
                if(productFRSubCategory==null)
                    productFRSubCategory = new HashMap<>();
                List<Long> productList = productFRSubCategory.get(obj.getSubCategoryId());
                if (productList == null) {
                    productList = new ArrayList<>();
                }
                productList.add(obj.getId());
                productFRSubCategory.put(obj.getSubCategoryId(),productList);
                database.getProductRFbyCountryCode().put(countryCode, productFRSubCategory);
                //backup ProductRFEmail
                List<Long> productList2 = database.getProductRFbyEmail().get(obj.getEmail());
                if (productList2 == null) {
                    productList2 = new ArrayList<>();
                }
                productList2.add(obj.getId());
                database.getProductRFbyEmail().put(obj.getEmail(), productList2);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
