package com.marqet.WebServer.util;

import com.marqet.WebServer.pojo.FeedbackEntity;
import com.marqet.WebServer.pojo.MessageEntity;
import com.marqet.WebServer.pojo.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 4/24/15.
 */
public class TempData {

    public static boolean isTemp = false;
    public static List<String> tempFollow(String email){
        List<String> result = new ArrayList<>(Database.getInstance().getUserEntityHashMap().keySet());
        result.remove(email);
        return result ;
    }
    public static void createTemper(){
        UserEntity temper = new UserEntity();
        temper.setEmail("MarQetTemper");
        temper.setPassword("MarQetTemper");
        temper.setUserName("MarQetTemper");
        temper.setJoinDate(new DateTimeUtil().getNow());
        temper.setCityCode("S");
        temper.setCountryCode("S");
        temper.setStateCode("S");
        temper.setProfilePicture(Database.getInstance().getElementEntity().getDefaultAvatar());
        temper.setPoint(100000);
        temper.setTelephone("12345");
        Database.getInstance().getUserEntityHashMap().put("MarQetTemper",temper);
    }
    public static List<Long> tempFeedback(String email,boolean isBuyer){
        List<Long> result = new ArrayList<>();
        List<Long> productEntities = tempProduct();
        for(int i = 0; i< 20; i++){
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setId(i);
            feedbackEntity.setStatus(i % 3 + 1);
            feedbackEntity.setBuyerEmail(isBuyer ? email : "MarQetTemper");
            feedbackEntity.setDate(new DateTimeUtil().getNow());
            feedbackEntity.setContent("temp data " + i);
            feedbackEntity.setProductId(productEntities.get(i % productEntities.size()));
            Database.getInstance().getFeedbackEntityHashMap().put(feedbackEntity.getId(),feedbackEntity);
            result.add((long)i);
        }
        return result;
    }
    public static List<Long> tempMessage(String email, boolean isSender){
        List<Long> result = new ArrayList<>();
        List<Long> productEntities = tempProduct();
        for(int i = 0; i< 20; i++){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(i);
            messageEntity.setOfferId(0);
            messageEntity.setContent("Product ABC conversation " + i);
            messageEntity.setDate(new DateTimeUtil().getNow());
            messageEntity.setProductId(productEntities.get(i % productEntities.size()));
            messageEntity.setIsArchive(-1);
            if(isSender){
                messageEntity.setSenderEmail(email);
                messageEntity.setReceiverEmail("MarqetTemper");
            }else{
                messageEntity.setReceiverEmail(email);
                messageEntity.setSenderEmail("MarqetTemper");
            }
            messageEntity.setStatus(-1);
            Database.getInstance().getMessageEntityHashMap().put(messageEntity.getId(),messageEntity);
            result.add((long)i);
        }
        return result;
    }
    public static List<Long> tempProduct(){
        return new ArrayList<>(Database.getInstance().getProductEntityHashMap().keySet());
    }

}
