package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.FeedbackDao;
import com.marqet.WebServer.pojo.FeedbackEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import com.marqet.WebServer.util.TempData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hpduy17 on 3/28/15.
 */
public class FeedbackController {
    private FeedbackDao dao = new FeedbackDao();
    private Database database = Database.getInstance();

    public JSONObject getListFeedbackAll(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerFeedback = database.getBuyerFeedbackRFEmail().get(email);
            List<Long> listSellerFeedback = database.getSellerFeedbackRFEmail().get(email);
            if (listBuyerFeedback == null)
                listBuyerFeedback = new ArrayList<>();
            if (listSellerFeedback == null)
                listSellerFeedback = new ArrayList<>();
            if(listBuyerFeedback.isEmpty()&&listSellerFeedback.isEmpty()&& TempData.isTemp){
                listSellerFeedback = TempData.tempFeedback(email, false);
            }
            listBuyerFeedback.addAll(listSellerFeedback);
            JSONArray arrAll = getListFeedback(listBuyerFeedback,startIdx,numFeedback);
            return result.put(ResponseController.CONTENT, arrAll);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListFeedbackSeller(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listSellerFeedback = database.getSellerFeedbackRFEmail().get(email);
            if (listSellerFeedback == null)
                listSellerFeedback = new ArrayList<>();
            if(listSellerFeedback.isEmpty()&& TempData.isTemp){
                listSellerFeedback = TempData.tempFeedback(email,false);
            }
            JSONArray arrSeller = getListFeedback(listSellerFeedback, startIdx, numFeedback);
            return result.put(ResponseController.CONTENT, arrSeller);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListFeedbackBuyer(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerFeedback = database.getBuyerFeedbackRFEmail().get(email);
            if (listBuyerFeedback == null)
                listBuyerFeedback = new ArrayList<>();
            if(listBuyerFeedback.isEmpty()&& TempData.isTemp){
                listBuyerFeedback = TempData.tempFeedback(email,true);
            }
            JSONArray arrBuyer = getListFeedback(listBuyerFeedback,startIdx,numFeedback);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListFeedbackRequired(String email, int startIdx, int numFeedback) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listRequired = database.getRequiredFeedback().get(email);
            if (listRequired == null)
                listRequired = new ArrayList<>();
            JSONArray arrBuyer = getListFeedback(listRequired, startIdx, numFeedback);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    private JSONArray getListFeedback(List<Long> feedbackIds, int startIdx, int numFeed) {
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
                    array.put(feed.toDetailJSON());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return array;
    }
    public JSONObject feedbackProduct(String buyerEmail, String sellerEmail, String content, int status, long productId) {
        try {
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
            List<Long> feedBuyerList = database.getBuyerFeedbackRFEmail().get(buyerEmail);
            if (feedBuyerList == null)
                feedBuyerList = new ArrayList<>();
            feedBuyerList.add(feedbackId);
            database.getBuyerFeedbackRFEmail().put(buyerEmail, feedBuyerList);
            //put to sellerFeedbackRFEmail
            List<Long> feedSellerList = database.getSellerFeedbackRFEmail().get(sellerEmail);
            if (feedSellerList == null)
                feedSellerList = new ArrayList<>();
            feedSellerList.add(feedbackId);
            database.getSellerFeedbackRFEmail().put(sellerEmail, feedSellerList);
            if (dao.insert(feedback))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, feedback.toDetailJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject feedbackProduct(long feedbackId, String content, int status) {
        try {
            FeedbackEntity feedback = database.getFeedbackEntityHashMap().get(feedbackId);
            feedback.setDate(new DateTimeUtil().getNow());
            feedback.setContent(content);
            feedback.setStatus(status);
            //put to database
            database.getFeedbackEntityHashMap().put(feedbackId, feedback);
            //remove from required feedback
            List<Long> requireFeedbackList = database.getRequiredFeedback().get(feedback.getBuyerEmail());
            if (requireFeedbackList != null) {
                requireFeedbackList = new ArrayList<>();
                requireFeedbackList.remove(feedback.getId());
                database.getSellerFeedbackRFEmail().put(feedback.getBuyerEmail(), requireFeedbackList);
            }
            if (dao.update(feedback))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, feedback.toDetailJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject requiredFeedbackProduct(String buyerEmail, String sellerEmail, long productId) {
        try {
            long feedbackId = IdGenerator.getFeedbackId();
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setId(feedbackId);
            feedback.setBuyerEmail(buyerEmail);
            feedback.setSellerEmail(sellerEmail);
            feedback.setDate(new DateTimeUtil().getNow());
            feedback.setProductId(productId);
            feedback.setStatus(-1);
            //put to database
            database.getFeedbackEntityHashMap().put(feedbackId, feedback);
            //put to buyerFeedbackRFEmail
            List<Long> feedBuyerList = database.getBuyerFeedbackRFEmail().get(buyerEmail);
            if (feedBuyerList == null)
                feedBuyerList = new ArrayList<>();
            feedBuyerList.add(feedbackId);
            database.getBuyerFeedbackRFEmail().put(buyerEmail, feedBuyerList);
            //put to sellerFeedbackRFEmail
            List<Long> feedSellerList = database.getSellerFeedbackRFEmail().get(sellerEmail);
            if (feedSellerList == null)
                feedSellerList = new ArrayList<>();
            feedSellerList.add(feedbackId);
            database.getSellerFeedbackRFEmail().put(sellerEmail, feedSellerList);
            //put to required feedback
            List<Long> requireFeedbackList = database.getRequiredFeedback().get(feedback.getBuyerEmail());
            if (requireFeedbackList == null)
                requireFeedbackList = new ArrayList<>();
            requireFeedbackList.add(feedback.getId());
            database.getSellerFeedbackRFEmail().put(feedback.getBuyerEmail(), requireFeedbackList);
            //TODO:Notify user
            if (dao.insert(feedback))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, feedback.toDetailJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

}
