package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.MessageDao;
import com.marqet.WebServer.pojo.MessageEntity;
import com.marqet.WebServer.util.Database;
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
public class MessageController {
    private MessageDao dao = new MessageDao();
    private Database database = Database.getInstance();

    public JSONObject getListConversationAll(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerMessage = database.getBuyerMessRFEmail().get(email);
            List<Long> listSellerMessage = database.getSellerMessRFEmail().get(email);
            if (listBuyerMessage == null)
                listBuyerMessage = new ArrayList<>();
            if (listSellerMessage == null)
                listSellerMessage = new ArrayList<>();
            if (listBuyerMessage.isEmpty() && listSellerMessage.isEmpty() && TempData.isTemp) {
                listSellerMessage = TempData.tempMessage(email, false);
            }
            listBuyerMessage.addAll(listSellerMessage);
            JSONArray arrAll = getListMessage(listBuyerMessage, startIdx, numMessage);
            return result.put(ResponseController.CONTENT, arrAll);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    public JSONObject getListConversationSeller(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listSellerMessage = database.getSellerMessRFEmail().get(email);
            if (listSellerMessage == null)
                listSellerMessage = new ArrayList<>();
            if (listSellerMessage.isEmpty() && TempData.isTemp) {
                listSellerMessage = TempData.tempMessage(email, false);
            }
            JSONArray arrSeller = getListMessage(listSellerMessage, startIdx, numMessage);
            return result.put(ResponseController.CONTENT, arrSeller);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    public JSONObject getListConversationBuyer(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> listBuyerMessage = database.getBuyerMessRFEmail().get(email);
            if (listBuyerMessage == null)
                listBuyerMessage = new ArrayList<>();
            if (listBuyerMessage.isEmpty() && TempData.isTemp) {
                listBuyerMessage = TempData.tempMessage(email, true);
            }
            JSONArray arrBuyer = getListMessage(listBuyerMessage, startIdx, numMessage);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    private JSONArray getListMessage(List<Long> messIds, int startIdx, int numMess) {
        JSONArray array = new JSONArray();
        try {
            Collections.sort(messIds, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    try {
                        MessageEntity mess1 = database.getMessageEntityHashMap().get(o1);
                        MessageEntity mess2 = database.getMessageEntityHashMap().get(o2);
                        if (mess1.getDate() < mess2.getDate())
                            return -1;
                        return 1;
                    } catch (Exception ex) {
                        return 0;
                    }

                }
            });
            int endIdx = startIdx + numMess;
            if (endIdx > messIds.size() - 1)
                endIdx = messIds.size();
            List<Long> subList = messIds.subList(startIdx, endIdx);
            for (long id : subList) {
                MessageEntity mess = database.getMessageEntityHashMap().get(id);
                if (mess != null) {
                    array.put(mess.toDetailJSON());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return array;
    }

    public void updateOfferOfMessage(long messageId, long offerId) {
        try {
            MessageEntity mess = database.getMessageEntityHashMap().get(messageId);
            mess.setOfferId(offerId);
            database.getMessageEntityHashMap().put(mess.getId(), mess);
            dao.update(mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
