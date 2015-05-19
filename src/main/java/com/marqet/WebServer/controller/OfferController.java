package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.OfferDao;
import com.marqet.WebServer.pojo.OfferEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 5/9/15.
 */
public class OfferController {
    OfferDao dao = new OfferDao();
    Database database = Database.getInstance();

    public JSONObject offerProduct(String buyerEmail, long productId, double price, long messageId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = new OfferEntity();
            offerEntity.setBuyerEmail(buyerEmail);
            offerEntity.setId(IdGenerator.getOfferId());
            offerEntity.setOfferPrice(price);
            offerEntity.setProductId(productId);
            offerEntity.setStatus(-1);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);
            // put to rf map
            List<Long> offerList = database.getOfferRFbyEmail().get(buyerEmail);
            if (offerList == null)
                offerList = new ArrayList<>();
            offerList.add(offerEntity.getId());
            database.getOfferRFbyEmail().put(buyerEmail, offerList);
            // put to wait reply map
            ProductEntity productEntity = database.getProductEntityHashMap().get(productId);
            if (productEntity != null) {
                List<Long> waitList = database.getWaitOfferReply().get(productEntity.getEmail());
                if (waitList == null)
                    waitList = new ArrayList<>();
                waitList.add(offerEntity.getId());
            }
            //update for message
            new MessageController().updateOfferOfMessage(messageId, offerEntity.getId());
            //TODO insert sub message
            //
            // put into database
            if (dao.insert(offerEntity)) {
                return result.put(ResponseController.CONTENT, offerEntity.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot insert database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject replyOffer(long offerId, int status) { // status 0 deny, 1 accept
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = database.getOfferEntityHashMap().get(offerId);
            offerEntity.setStatus(status);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);
            // remove from wait reply map
            ProductEntity productEntity = database.getProductEntityHashMap().get(offerEntity.getProductId());
            if (productEntity != null) {
                List<Long> waitList = database.getWaitOfferReply().get(productEntity.getEmail());
                if (waitList != null) {
                    waitList.remove(offerId);
                    waitList.add(offerEntity.getId());
                }
            }
            //TODO insert more subMessage
            // update in database
            if (dao.update(offerEntity)) {
                return result.put(ResponseController.CONTENT, offerEntity.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot update database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject editOffer(long offerId, double price) { // status 0 deny, 1 accept
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = database.getOfferEntityHashMap().get(offerId);
            offerEntity.setOfferPrice(price);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);
            //TODO insert more subMessage
            if (dao.update(offerEntity)) {
                return result.put(ResponseController.CONTENT, offerEntity.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot update database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListOfferWaitReply(String email, int startIdx, int numOffer) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray offerArray = new JSONArray();
            List<Long> offerList = database.getWaitOfferReply().get(email);
            if(offerList!=null){
                int endIdx = startIdx+numOffer;
                if(endIdx>offerList.size())
                    endIdx=offerList.size();
                List<Long> subList = offerList.subList(startIdx,endIdx);
                for(long id : subList){
                    OfferEntity offerEntity = database.getOfferEntityHashMap().get(id);
                    if(offerEntity!=null)
                        offerArray.put(offerEntity.toDetailJSON());
                }
            }
            return result.put(ResponseController.CONTENT, offerArray);

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }
}
