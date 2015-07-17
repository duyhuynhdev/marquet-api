package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.FeedbackDao;
import com.marqet.WebServer.pojo.FeedbackEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import com.marqet.WebServer.util.TempData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by hpduy17 on 3/28/15.
 */
public class FeedbackController {
    private FeedbackDao dao = new FeedbackDao();
    private Database database = Database.getInstance();

    public JSONObject getListFeedbackAll(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerFeedback = new ArrayList<>();
            List<Long> listSellerFeedback = new ArrayList<>();
            try{
                listBuyerFeedback = new ArrayList<>(database.getBuyerFeedbackRFEmail().get(email));
                listSellerFeedback = new ArrayList<>(database.getSellerFeedbackRFEmail().get(email));
            }catch (Exception ignored){}
            if (listBuyerFeedback == null)
                listBuyerFeedback = new ArrayList<>();
            if (listSellerFeedback == null)
                listSellerFeedback = new ArrayList<>();
            if(listBuyerFeedback.isEmpty()&&listSellerFeedback.isEmpty()&& TempData.isTemp){
                listSellerFeedback = TempData.tempFeedback(email, false);
            }
            listBuyerFeedback.addAll(listSellerFeedback);
            JSONArray arrAll = getListFeedback(email,listBuyerFeedback,startIdx,numFeedback);
            return result.put(ResponseController.CONTENT, arrAll);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListFeedbackSeller(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listSellerFeedback = new ArrayList<>();
            try{
                listSellerFeedback = new ArrayList<>(database.getSellerFeedbackRFEmail().get(email));
            }catch (Exception ignored){}
            if (listSellerFeedback == null)
                listSellerFeedback = new ArrayList<>();
            if(listSellerFeedback.isEmpty()&& TempData.isTemp){
                listSellerFeedback = TempData.tempFeedback(email,false);
            }
            JSONArray arrSeller = getListFeedback(email, listSellerFeedback, startIdx, numFeedback);
            return result.put(ResponseController.CONTENT, arrSeller);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListFeedbackBuyer(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerFeedback = new ArrayList<>();
            try{
                listBuyerFeedback = new ArrayList<>(database.getBuyerFeedbackRFEmail().get(email));
            }catch (Exception ignored){}
            if (listBuyerFeedback == null)
                listBuyerFeedback = new ArrayList<>();
            if(listBuyerFeedback.isEmpty()&& TempData.isTemp){
                listBuyerFeedback = TempData.tempFeedback(email,true);
            }
            JSONArray arrBuyer = getListFeedback(email, listBuyerFeedback, startIdx, numFeedback);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    private JSONArray getListFeedback(String email , List<Long> feedbackIds, int startIdx, int numFeed) {
        JSONArray array = new JSONArray();
        try{
            Collections.sort(feedbackIds, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    try {
                        FeedbackEntity feed1 = database.getFeedbackEntityHashMap().get(o1);
                        FeedbackEntity feed2 = database.getFeedbackEntityHashMap().get(o2);
                        if (feed1.getDate() < feed2.getDate())
                            return -1;
                        return 1;
                    } catch (Exception ex) {
                        return 0;
                    }

                }
            });
            int endIdx = startIdx+numFeed;
            if(endIdx>feedbackIds.size()-1)
                endIdx = feedbackIds.size();
            List<Long> subList = feedbackIds.subList(startIdx, endIdx);
            for(long id : subList){
                FeedbackEntity feed = database.getFeedbackEntityHashMap().get(id);
                if(feed!=null){
                    array.put(feed.toDetailJSON(email));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return array;
    }
    public JSONObject feedbackProduct(String buyerEmail, String sellerEmail, String content, int status, long productId) {
        try {
            if(database.getFeedbackRFEmailAndProduct().containsKey(buyerEmail+"#"+productId)){
                return ResponseController.createFailJSON("You have feedback this product already");
            }
            long feedbackId = IdGenerator.getFeedbackId();
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setId(feedbackId);
            feedback.setBuyerEmail(buyerEmail);
            feedback.setSellerEmail(sellerEmail);
            feedback.setDate(new DateTimeUtil().getNow());
            feedback.setProductId(productId);
            feedback.setContent(content);
            feedback.setStatus(status);
            //put to database
            database.getFeedbackEntityHashMap().put(feedbackId, feedback);
            //put to buyerFeedbackRFEmail
            HashSet<Long> feedBuyerList = database.getBuyerFeedbackRFEmail().get(buyerEmail);
            if (feedBuyerList == null)
                feedBuyerList = new HashSet<>();
            feedBuyerList.add(feedbackId);
            database.getBuyerFeedbackRFEmail().put(buyerEmail, feedBuyerList);
            //put to sellerFeedbackRFEmail
            HashSet<Long> feedSellerList = database.getSellerFeedbackRFEmail().get(sellerEmail);
            if (feedSellerList == null)
                feedSellerList = new HashSet<>();
            feedSellerList.add(feedbackId);
            database.getSellerFeedbackRFEmail().put(sellerEmail, feedSellerList);
            database.getFeedbackRFEmailAndProduct().put(buyerEmail+"#"+feedback.getProductId(),feedbackId);
            if (dao.insert(feedback))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, feedback.toDetailJSON(buyerEmail));
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public synchronized JSONObject feedbackProduct(String buyerEmail, String sellerEmail, String content, int status, long productId, long messageId, long tempSubMessageId) {
        try {
            if(database.getFeedbackRFEmailAndProduct().containsKey(buyerEmail+"#"+productId)){
                return ResponseController.createFailJSON("You have feedback this product already");
            }
            long feedbackId = IdGenerator.getFeedbackId();
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setId(feedbackId);
            feedback.setBuyerEmail(buyerEmail);
            feedback.setSellerEmail(sellerEmail);
            feedback.setDate(new DateTimeUtil().getNow());
            feedback.setProductId(productId);
            feedback.setContent(content);
            feedback.setStatus(status);
            //put to database
            database.getFeedbackEntityHashMap().put(feedbackId, feedback);
            //put to buyerFeedbackRFEmail
            HashSet<Long> feedBuyerList = database.getBuyerFeedbackRFEmail().get(buyerEmail);
            if (feedBuyerList == null)
                feedBuyerList = new HashSet<>();
            feedBuyerList.add(feedbackId);
            database.getBuyerFeedbackRFEmail().put(buyerEmail, feedBuyerList);
            //put to sellerFeedbackRFEmail
            HashSet<Long> feedSellerList = database.getSellerFeedbackRFEmail().get(sellerEmail);
            if (feedSellerList == null)
                feedSellerList = new HashSet<>();
            feedSellerList.add(feedbackId);
            database.getSellerFeedbackRFEmail().put(sellerEmail, feedSellerList);
            database.getFeedbackRFEmailAndProduct().put(buyerEmail+"#"+feedback.getProductId(),feedbackId);
            if (dao.insert(feedback))
                return new SubMessageController().feedbackProduct(buyerEmail, messageId, tempSubMessageId, sellerEmail);
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

}
