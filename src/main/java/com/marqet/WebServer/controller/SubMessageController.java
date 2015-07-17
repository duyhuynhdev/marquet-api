package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.MessageDao;
import com.marqet.WebServer.dao.SubMessageDao;
import com.marqet.WebServer.pojo.MessageEntity;
import com.marqet.WebServer.pojo.SubMessageEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 5/18/15.
 */
public class SubMessageController {
    private SubMessageDao dao = new SubMessageDao();
    private Database database = Database.getInstance();

    public JSONObject getSubMessageByMessageId(String email, long messageId, long firstestSubmessageId, int numSubMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray offerArray = new JSONArray();
            List<Long> subMessageList = new ArrayList<>();
            try{
                subMessageList = new ArrayList<>(database.getSubMessagesRFMessageId().get(messageId));
            }catch (Exception ignored){}
//            Collections.sort(subMessageList, new Comparator<Long>() {
//                @Override
//                public int compare(Long o1, Long o2) {
//                    SubMessageEntity s1 = database.getSubMessageEntityHashMap().get(o1);
//                    SubMessageEntity s2 = database.getSubMessageEntityHashMap().get(o2);
//                    if (s1 != null && s2 != null) {
//                        return (int) (s2.getDate() - s1.getDate());
//                    }
//                    return 0;
//                }
//            });
            int endIdx = subMessageList.size();
            if (subMessageList.contains(firstestSubmessageId)) {
                endIdx = subMessageList.indexOf(firstestSubmessageId);
            }
            if (subMessageList != null) {
                int startIdx = endIdx - numSubMessage;
                if (startIdx < 0)
                    startIdx = 0;
                List<Long> subList = subMessageList.subList(startIdx, endIdx);
                for (long id : subList) {
                    SubMessageEntity subMessageEntity = database.getSubMessageEntityHashMap().get(id);
                    if (subMessageEntity != null)
                        offerArray.put(subMessageEntity.toDetailJSON(subList.indexOf(id), -1));
                }
            }
            return result.put(ResponseController.CONTENT, offerArray);

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getNewSubMessageByMessageId(String email, long messageId, long lastestSubMessageId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray offerArray = new JSONArray();
            List<Long> subMessageList = new ArrayList<>();
            try{
                subMessageList = new ArrayList<>(database.getSubMessagesRFMessageId().get(messageId));
            }catch (Exception ignored){}
//            Collections.sort(subMessageList, new Comparator<Long>() {
//                @Override
//                public int compare(Long o1, Long o2) {
//                    SubMessageEntity s1 = database.getSubMessageEntityHashMap().get(o1);
//                    SubMessageEntity s2 = database.getSubMessageEntityHashMap().get(o2);
//                    if (s1 != null && s2 != null) {
//                        return (int) (s2.getDate() - s1.getDate());
//                    }
//                    return 0;
//                }
//            });
            if (subMessageList != null) {
                List<Long> subList = new ArrayList<>();
                try {
                    subList = subMessageList.subList(subMessageList.indexOf(lastestSubMessageId) + 1, subMessageList.size());
                } catch (Exception ignored) {

                }
                for (long id : subList) {
                    SubMessageEntity subMessageEntity = database.getSubMessageEntityHashMap().get(id);
                    if (subMessageEntity != null)
                        offerArray.put(subMessageEntity.toDetailJSON(subList.indexOf(id), -1));
                }
            }
            return result.put(ResponseController.CONTENT, offerArray);

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject deleteSubMessage(long subMessageId) {
        try {
            //delete submessage
            JSONObject result = ResponseController.createSuccessJSON();
            SubMessageEntity subMessageEntity = new SubMessageEntity(database.getSubMessageEntityHashMap().get(subMessageId));
            database.getSubMessageEntityHashMap().remove(subMessageEntity.getId());
            HashSet<Long> subMessageList = database.getSubMessagesRFMessageId().get(subMessageEntity.getMessageId());
            if (subMessageList != null)
                subMessageList.remove(subMessageEntity.getId());
            database.getSubMessagesRFMessageId().put(subMessageEntity.getMessageId(), subMessageList);
            if (dao.delete(subMessageEntity)) {
                return result;
            } else {
                return ResponseController.createFailJSON("Cannot delete in database");
            }
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    private JSONObject insertSubMessage(String email, long messageId, String content, int status, int type, long ref, long tempSubMessageId, String receiverEmail, int currentStep) {
        try {
            // change message status
            new MessageController().updateStatusMessage(messageId,currentStep);
            //insert submessage
            JSONObject result = ResponseController.createSuccessJSON();
            SubMessageEntity subMessageEntity = new SubMessageEntity();
            subMessageEntity.setId(IdGenerator.getSubMessageId());
            subMessageEntity.setContent(content);
            subMessageEntity.setDate(new DateTimeUtil().getNow());
            subMessageEntity.setMessageId(messageId);
            subMessageEntity.setSenderEmail(email);
            subMessageEntity.setStatus(status);
            subMessageEntity.setRef(ref);
            subMessageEntity.setType(type);
            subMessageEntity.setReceiverEmail(receiverEmail);
            database.getSubMessageEntityHashMap().put(subMessageEntity.getId(), subMessageEntity);
            HashSet<Long> subMessageList = database.getSubMessagesRFMessageId().get(messageId);
            if (subMessageList == null)
                subMessageList = new HashSet<>();
            subMessageList.add(subMessageEntity.getId());
            database.getSubMessagesRFMessageId().put(messageId, subMessageList);
            //update content message
            MessageEntity messageEntity = database.getMessageEntityHashMap().get(messageId);
            if (messageEntity != null) {
                messageEntity.setContent(subMessageEntity.getContent());
                database.getMessageEntityHashMap().put(messageId, messageEntity);
                new MessageDao().update(messageEntity);
            }
            if (dao.insert(subMessageEntity)) {
                return result.put(ResponseController.CONTENT, subMessageEntity.toDetailJSON(new ArrayList<>(subMessageList).indexOf(subMessageEntity.getId()), tempSubMessageId));
            } else {
                return ResponseController.createFailJSON("Cannot insert database");
            }

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject sendOfferMessage(String email, long messageId, double offerPrice, long offerId, long tempSubMessageId, String receiverEmail) {
        String content = "Made an Offer\n$" + offerPrice;
        int status = 0;
        int type = 4;
        int currentStep = 2;
        long ref = offerId;
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject cancelOfferMessage(String email, long messageId, long tempSubMessageId, String receiverEmail) {
        String content = "Cancel an Offer\n";
        int status = 0;
        int type = 6;
        int currentStep = 1;
        long ref = 0;
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject replyOfferMessage(String email, long messageId, double offerPrice, long offerId, boolean isAccept, long tempSubMessageId, String receiverEmail) {
        String content = isAccept ? "Accept an Offer\n$" : "Deny an Offer\n$" + offerPrice;
        int status = isAccept ? 1 : 0;
        int type = 5;
        int currentStep = isAccept ? 3 : 1;
        long ref = offerId;
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject requireFeedbackMessage(String email, long messageId, long tempSubMessageId, String receiverEmail) {
        String content = "Let's feedback my product.";
        int status = 0;
        int type = 3;
        long ref = 0;
        int currentStep = database.getMessageEntityHashMap().get(messageId) == null ? 3 : database.getMessageEntityHashMap().get(messageId).getStatus();
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject markAsSoldMessage(String email, long messageId, long tempSubMessageId, String receiverEmail) {
        String content = "Product marked as sold.";
        int status = 0;
        int type = 7;
        long ref = 0;
        int currentStep = 4;
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject feedbackProduct(String email, long messageId, long tempSubMessageId, String receiverEmail) {
        String content = "Feed back this product.";
        int status = 0;
        int type = 6;
        long ref = 0;
        int currentStep = 5;
        return insertSubMessage(email, messageId, content, status, type, ref, tempSubMessageId, receiverEmail, currentStep);
    }

    public JSONObject sendSubMessage(String email, long messageId, String content, int type, long tempSubMessageId, String receiverEmail) {
        int currentStep = database.getMessageEntityHashMap().get(messageId) == null ? 1 : database.getMessageEntityHashMap().get(messageId).getStatus();
        return insertSubMessage(email, messageId, content, 0, type, 0, tempSubMessageId, receiverEmail, currentStep);
    }


}
