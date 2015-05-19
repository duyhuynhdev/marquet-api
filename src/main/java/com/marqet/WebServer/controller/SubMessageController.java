package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.SubMessageDao;
import com.marqet.WebServer.pojo.SubMessageEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hpduy17 on 5/18/15.
 */
public class SubMessageController {
    private SubMessageDao dao = new SubMessageDao();
    private Database database = Database.getInstance();
    public JSONObject getSubMessageByMessageId(String email,long messageId, int startIdx, int numSubMessage) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray offerArray = new JSONArray();
            List<Long> subMessageList = database.getSubMessagesRFMessageId().get(messageId);
            Collections.sort(subMessageList, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    SubMessageEntity s1 = database.getSubMessageEntityHashMap().get(o1);
                    SubMessageEntity s2 = database.getSubMessageEntityHashMap().get(o2);
                    if(s1!=null&&s2!=null){
                        return (int)(s2.getDate()-s1.getDate());
                    }
                    return 0;
                }
            });
            if(subMessageList!=null){
                int endIdx = startIdx+numSubMessage;
                if(endIdx>subMessageList.size())
                    endIdx=subMessageList.size();
                List<Long> subList = subMessageList.subList(startIdx,endIdx);
                for(long id : subList){
                    SubMessageEntity subMessageEntity = database.getSubMessageEntityHashMap().get(id);
                    if(subMessageEntity!=null)
                        offerArray.put(subMessageEntity.toDetailJSON());
                }
            }
            return result.put(ResponseController.CONTENT, offerArray);

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }
    private JSONObject insertSubMessage(String email,long messageId,String content,int status, int type, long ref) {
        try {
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
            database.getSubMessageEntityHashMap().put(subMessageEntity.getId(), subMessageEntity);
            List<Long> subMessageList = database.getSubMessagesRFMessageId().get(messageId);
            if(subMessageList!=null)
                subMessageList = new ArrayList<>();
            subMessageList.add(subMessageEntity.getId());
            database.getSubMessagesRFMessageId().put(messageId,subMessageList);
            if(dao.insert(subMessageEntity)) {
                return result.put(ResponseController.CONTENT, subMessageEntity.toDetailJSON());
            }else {
                return ResponseController.createFailJSON("Cannot insert database");
            }

        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject sendOfferMessage(String email,long messageId, long offerPrice, long offerId){
        String content = "Made an Offer\n" + offerPrice;
        int status = 0;
        int type = 4;
        long ref = offerId;
       return insertSubMessage(email,messageId,content,status,type,ref);
    }
    public JSONObject replyOfferMessage(String email,long messageId, long offerPrice, long offerId, boolean isAccept){
        String content = isAccept?"Accept an Offer\n":"Deny an Offer\n" + offerPrice;
        int status = 0;
        int type = 5;
        long ref = offerId;
        return insertSubMessage(email,messageId,content,status,type,ref);
    }
    public JSONObject sendSubMessage(String email,long messageId,String content, int type){
        return insertSubMessage(email,messageId,content,0,type,0);
    }
}
