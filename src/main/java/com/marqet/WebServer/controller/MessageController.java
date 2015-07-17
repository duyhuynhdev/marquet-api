package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.MessageDao;
import com.marqet.WebServer.pojo.MessageEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by hpduy17 on 3/28/15.
 */
public class MessageController {
    private MessageDao dao = new MessageDao();
    private Database database = Database.getInstance();

    public JSONObject getListConversationAll(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            HashSet<Long> listBuyerMessage = database.getBuyerMessRFEmail().get(email);
            HashSet<Long> listSellerMessage = database.getSellerMessRFEmail().get(email);
            if (listBuyerMessage == null)
                listBuyerMessage = new HashSet<>();
            if (listSellerMessage == null)
                listSellerMessage = new HashSet<>();
            listBuyerMessage.addAll(listSellerMessage);
            JSONArray arrAll = getListMessage(email, new ArrayList<>(listBuyerMessage), startIdx, numMessage, 0);
            return result.put(ResponseController.CONTENT, arrAll);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    public JSONObject getListConversationSeller(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            HashSet<Long> listSellerMessage = database.getSellerMessRFEmail().get(email);
            if (listSellerMessage == null)
                listSellerMessage = new HashSet<>();
            JSONArray arrSeller = getListMessage(email, new ArrayList<>(listSellerMessage), startIdx, numMessage,0);
            return result.put(ResponseController.CONTENT, arrSeller);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    public JSONObject getListConversationBuyer(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            HashSet<Long> listBuyerMessage = database.getBuyerMessRFEmail().get(email);
            if (listBuyerMessage == null)
                listBuyerMessage = new HashSet<>();
            JSONArray arrBuyer = getListMessage(email, new ArrayList<>(listBuyerMessage), startIdx, numMessage, 0);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }
    public  JSONObject getListConversationArchived(String email, int startIdx, int numMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            HashSet<Long> listBuyerMessage = database.getBuyerMessRFEmail().get(email);
            HashSet<Long> listSellerMessage = database.getSellerMessRFEmail().get(email);
            if (listBuyerMessage == null)
                listBuyerMessage = new HashSet<>();
            if (listSellerMessage == null)
                listSellerMessage = new HashSet<>();
            listBuyerMessage.addAll(listSellerMessage);
            JSONArray arrBuyer = getListMessage(email, new ArrayList<Long>(listBuyerMessage), startIdx, numMessage, 1);
            return result.put(ResponseController.CONTENT, arrBuyer);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    private JSONArray getListMessage(String email, List<Long> messIds, int startIdx, int numMess, int isArchived) {
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
            int count = 0;
            List<MessageEntity> temp = new ArrayList<>();
            for(long id : messIds){
                MessageEntity mess = database.getMessageEntityHashMap().get(id);
                if (mess != null && mess.getIsArchive() == isArchived) {
                    temp.add(mess);
                }
            }
            for (int i = startIdx; i < temp.size() && count < numMess; i++) {
                if(temp.get(i).getIsArchive()== isArchived){
                    array.put(temp.get(i).toDetailJSON(email));
                    count++;
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
    public void updateStatusMessage(long messageId, int status) {
        try {
            MessageEntity mess = database.getMessageEntityHashMap().get(messageId);
            mess.setStatus(status);
            database.getMessageEntityHashMap().put(mess.getId(), mess);
            dao.update(mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject insertMessage(String senderEmail, String receiverEmail, long productId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(IdGenerator.getMessageId());
            messageEntity.setSenderEmail(senderEmail);
            messageEntity.setOfferId(0);
            messageEntity.setReceiverEmail(receiverEmail);
            messageEntity.setStatus(1);
            messageEntity.setDate(new DateTimeUtil().getNow());
            messageEntity.setProductId(productId);
            messageEntity.setIsArchive(0);
            messageEntity.setContent("New message is created");
            database.getMessageEntityHashMap().put(messageEntity.getId(), messageEntity);
            HashSet<Long> messBuyerList = database.getBuyerMessRFEmail().get(messageEntity.getSenderEmail());
            if (messBuyerList == null)
                messBuyerList = new HashSet<>();
            if (!messBuyerList.contains(messageEntity.getId())) {
                messBuyerList.add(messageEntity.getId());
            }
            database.getBuyerMessRFEmail().put(messageEntity.getSenderEmail(), messBuyerList);
            //put to sellerMessRFEmail
            HashSet<Long> messSellerList = database.getSellerMessRFEmail().get(messageEntity.getReceiverEmail());
            if (messSellerList == null)
                messSellerList = new HashSet<>();
            if (!messSellerList.contains(messageEntity.getId())) {
                messSellerList.add(messageEntity.getId());
            }
            database.getSellerMessRFEmail().put(messageEntity.getReceiverEmail(), messSellerList);
            database.getDistinctMessage().put(messageEntity.getSenderEmail() + "#" + messageEntity.getReceiverEmail() + "#" + messageEntity.getProductId(), messageEntity.getId());
            if (dao.insert(messageEntity)) {
                return result.put(ResponseController.CONTENT, messageEntity.toDetailJSON(senderEmail));
            } else {
                return ResponseController.createFailJSON("Cannot insert to database");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

    public synchronized JSONObject archiveMessage(long messId,int type) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            MessageEntity messageEntity = new MessageEntity(database.getMessageEntityHashMap().get(messId));
            messageEntity.setIsArchive(type);
            database.getMessageEntityHashMap().put(messageEntity.getId(), messageEntity);
            if (dao.update(messageEntity)) {
                return result;
            } else {
                return ResponseController.createFailJSON("Cannot insert to database");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }
    public synchronized JSONObject deleteMessage(long messageId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            if(!database.getMessageEntityHashMap().containsKey(messageId)){
                return ResponseController.createSuccessJSON();
            }
            MessageEntity messageEntity = new MessageEntity(database.getMessageEntityHashMap().get(messageId));
            database.getMessageEntityHashMap().remove(messageEntity.getId());
            HashSet<Long> messBuyerList = database.getBuyerMessRFEmail().get(messageEntity.getSenderEmail());
            if (messBuyerList != null)
                messBuyerList.remove(messageEntity.getId());
            database.getBuyerMessRFEmail().put(messageEntity.getSenderEmail(), messBuyerList);
            //put to sellerMessRFEmail
            HashSet<Long> messSellerList = database.getSellerMessRFEmail().get(messageEntity.getReceiverEmail());
            if (messSellerList != null)
                messSellerList.remove(messageEntity.getId());
            database.getSellerMessRFEmail().put(messageEntity.getReceiverEmail(), messSellerList);
            database.getDistinctMessage().remove(messageEntity.getSenderEmail() + "#" + messageEntity.getReceiverEmail() + "#" + messageEntity.getProductId());
            // delete All submessage
            HashSet<Long> subMessageList = database.getSubMessagesRFMessageId().get(messageId);
            if(subMessageList != null) {
                for (long sub : new ArrayList<>(subMessageList)) {
                    new SubMessageController().deleteSubMessage(sub);
                }
            }
            database.getSubMessagesRFMessageId().remove(messageId);
            if (dao.delete(messageEntity)) {
                return result;
            } else {
                return ResponseController.createFailJSON("Cannot insert to database");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }

}
