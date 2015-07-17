package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.OfferDao;
import com.marqet.WebServer.pojo.MessageEntity;
import com.marqet.WebServer.pojo.OfferEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 5/9/15.
 */
public class OfferController {
    OfferDao dao = new OfferDao();
    Database database = Database.getInstance();

    public synchronized JSONObject offerProduct(String buyerEmail, double price, long messageId, long tempSubMessageId, String receiverEmail) {
        try {
            MessageEntity messageEntity = database.getMessageEntityHashMap().get(messageId);
            if (messageEntity.getOfferId() > 0) {
                return editOffer(messageId, messageEntity.getOfferId(), price, tempSubMessageId, receiverEmail);
            }
            long productId = messageEntity.getProductId();
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = new OfferEntity();
            offerEntity.setBuyerEmail(buyerEmail);
            offerEntity.setId(IdGenerator.getOfferId());
            offerEntity.setOfferPrice(price);
            offerEntity.setProductId(productId);
            offerEntity.setStatus(1);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);
            // put to rf map
            HashSet<Long> offerList = database.getOfferRFbyEmail().get(buyerEmail);
            if (offerList == null)
                offerList = new HashSet<>();
            offerList.add(offerEntity.getId());
            database.getOfferRFbyEmail().put(buyerEmail, offerList);
            // put to wait reply map
            ProductEntity productEntity = database.getProductEntityHashMap().get(productId);
            if (productEntity != null) {
                HashSet<Long> waitList = database.getWaitOfferReply().get(productEntity.getEmail());
                if (waitList == null)
                    waitList = new HashSet<>();
                waitList.add(offerEntity.getId());
            }
            //update for message
            new MessageController().updateOfferOfMessage(messageId, offerEntity.getId());

            //
            // put into database
            if (dao.insert(offerEntity)) {
                return new SubMessageController().sendOfferMessage(buyerEmail, messageId, price, offerEntity.getId(), tempSubMessageId, receiverEmail);
            } else {
                return ResponseController.createFailJSON("Cannot insert database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject replyOffer(String email, long messageId, int status, long tempSubMessageId, String receiverEmail) { // status 0 deny, 1 accept
        try {
            long offerId = database.getMessageEntityHashMap().get(messageId).getOfferId();
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = database.getOfferEntityHashMap().get(offerId);
            offerEntity.setStatus(2);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);
            // remove from wait reply map
            ProductEntity productEntity = database.getProductEntityHashMap().get(offerEntity.getProductId());
            if (productEntity != null) {
                HashSet<Long> waitList = database.getWaitOfferReply().get(productEntity.getEmail());
                if (waitList != null) {
                    waitList.remove(offerId);
                    waitList.add(offerEntity.getId());
                }
            }
            // update in database
            if (dao.update(offerEntity)) {
                return new SubMessageController().replyOfferMessage(email, messageId, offerEntity.getOfferPrice(), offerId, status == 1, tempSubMessageId, receiverEmail);
            } else {
                return ResponseController.createFailJSON("Cannot update database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject editOffer(long messageId, long offerId, double price, long tempSubMessageId, String receiverEmail) { // status 0 deny, 1 accept
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            OfferEntity offerEntity = database.getOfferEntityHashMap().get(offerId);
            offerEntity.setOfferPrice(price);
            //put to offer map
            database.getOfferEntityHashMap().put(offerEntity.getId(), offerEntity);

            if (dao.update(offerEntity)) {
                return new SubMessageController().sendOfferMessage(offerEntity.getBuyerEmail(), messageId, price, offerEntity.getId(), tempSubMessageId, receiverEmail);
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
            List<Long> offerList = new ArrayList<>();
            try{
                offerList = new ArrayList<>(database.getWaitOfferReply().get(email));
            }catch (Exception ignored){}
            if (offerList != null) {
                int endIdx = startIdx + numOffer;
                if (endIdx > offerList.size())
                    endIdx = offerList.size();
                List<Long> subList = offerList.subList(startIdx, endIdx);
                for (long id : subList) {
                    OfferEntity offerEntity = database.getOfferEntityHashMap().get(id);
                    if (offerEntity != null)
                        offerArray.put(offerEntity.toDetailJSON());
                }
            }
            return result.put(ResponseController.CONTENT, offerArray);

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject cancelOffer(String email, long messageId, long tempSubMessageId, String receiverEmail) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            MessageEntity mess = database.getMessageEntityHashMap().get(messageId);
            if (mess != null) {
                try {
//                    dao.delete(database.getOfferEntityHashMap().get(mess.getOfferId()));
                    OfferEntity offer = database.getOfferEntityHashMap().get(mess.getOfferId());
                    offer.setStatus(0);
                    database.getOfferEntityHashMap().put(offer.getId(),offer);
                    dao.update(offer);
                } catch (Exception ignored) {

                }
//                database.getOfferEntityHashMap().remove(mess.getOfferId());
//                mess.setOfferId(0);
//                database.getMessageEntityHashMap().put(messageId, mess);
//                new MessageDao().update(mess);
                return new SubMessageController().cancelOfferMessage(email, messageId, tempSubMessageId, receiverEmail);
            } else {
                return ResponseController.createFailJSON("Message is not exist");
            }

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }
}
