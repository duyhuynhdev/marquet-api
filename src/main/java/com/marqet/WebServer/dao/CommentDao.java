package com.marqet.WebServer.dao;

import com.marqet.WebServer.pojo.CommentEntity;
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
public class CommentDao {
    public boolean insert(CommentEntity object){
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
    public boolean update(CommentEntity object){
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
    public boolean delete(CommentEntity object){
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
            List<CommentEntity> list = (List<CommentEntity>) session
                    .createCriteria(CommentEntity.class).list();
            for (CommentEntity obj : list) {
                Database.getInstance().getCommentEntityHashMap().put((long)obj.getId(), obj);
                HashSet<Long> commentList = Database.getInstance().getCommentRFbyProductId().get((long)obj.getProductId());
                if(commentList==null)
                    commentList = new HashSet<>();
                commentList.add(obj.getId());
                Database.getInstance().getCommentRFbyProductId().put((long)obj.getProductId(),commentList);
            }
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }
}
