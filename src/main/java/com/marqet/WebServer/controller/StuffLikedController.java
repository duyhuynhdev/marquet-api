package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.StuffLikedDao;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.StuffLikedEntity;
import com.marqet.WebServer.util.ActivityUtil;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by hpduy17 on 3/22/15.
 */
public class StuffLikedController {
    private StuffLikedDao dao = new StuffLikedDao();
    private Database database = Database.getInstance();

    public JSONObject likeProduct(String email, long productId) {
        if(database.getStuffLikedRFbyEmailAndProductId().containsKey(email+"#"+productId))
            return ResponseController.createFailJSON("You liked this product");
        JSONObject responseJSON;
        StuffLikedEntity stuffLiked = new StuffLikedEntity();
        stuffLiked.setId(IdGenerator.getStuffLikedId());
        stuffLiked.setBuyerEmail(email);
        stuffLiked.setProductId(productId);
        database.getStuffLikedEntityHashMap().put(stuffLiked.getId(), stuffLiked);
        //put to stuffLikedRFProductId;
        HashSet<Long> stuffLikedList = database.getStuffLikedRFbyProductId().get(productId);
        if (stuffLikedList == null)
            stuffLikedList = new HashSet<>();
        stuffLikedList.add(stuffLiked.getId());
        database.getStuffLikedRFbyProductId().put(productId, stuffLikedList);
        //put to stuffLikedByEmail
        HashSet<Long> stuffLikedList2 = database.getStuffLikedRFbyEmail().get(email);
        if (stuffLikedList2 == null)
            stuffLikedList2 = new HashSet<>();
        stuffLikedList2.add(stuffLiked.getId());
        database.getStuffLikedRFbyEmail().put(email, stuffLikedList2);
        database.getStuffLikedRFbyEmailAndProductId().put(email+"#"+productId,stuffLiked.getId());
        if (dao.insert(stuffLiked)) {
            responseJSON = ResponseController.createSuccessJSON();
            try{
                ProductEntity product = database.getProductEntityHashMap().get(productId);
                new ActivityController().insertActivity(email, product.getEmail(), ActivityUtil.LIKE_PRODUCT,product.getId());
            }catch (Exception ignored){

            }
        }
        else
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");

        return responseJSON;

    }

    public JSONObject unlikeProduct(String email, long productId) {
        try {
            if(!database.getStuffLikedRFbyEmailAndProductId().containsKey(email+"#"+productId))
                return ResponseController.createFailJSON("You have not like this product yet");
            JSONObject responseJSON;
            long id = database.getStuffLikedRFbyEmailAndProductId().get(email + "#" + productId);
            database.getStuffLikedRFbyEmailAndProductId().remove(email + "#" + productId);
            database.getStuffLikedRFbyProductId().get(productId).remove(id);
            database.getStuffLikedRFbyEmail().get(email).remove(id);
            StuffLikedEntity stuffLiked = new StuffLikedEntity(database.getStuffLikedEntityHashMap().get(id));
            database.getStuffLikedEntityHashMap().remove(id);
            if (dao.delete(stuffLiked))
                responseJSON = ResponseController.createSuccessJSON();
            else
                responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
            return responseJSON;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }

    }


}
