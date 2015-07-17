package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.CommentDao;
import com.marqet.WebServer.pojo.CommentEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.util.ActivityUtil;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
public class CommentController {
    private CommentDao dao = new CommentDao();
    private Database database = Database.getInstance();

    public JSONObject getListCommentByProductId(long productId){
        try {
            JSONObject object = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> lstComment = new ArrayList<>();
            try{
                lstComment = new ArrayList<>(database.getCommentRFbyProductId().get(productId));
            }catch (Exception ignored){}
            for (long id : lstComment) {
                jsonArray.put(database.getCommentEntityHashMap().get(id).toCommentDetailJSON());
            }
            object.put(ResponseController.CONTENT, jsonArray);
            return object;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListCommentByProductId(long productId, int startIdx , int numComment){
        try {
            JSONObject object = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> lstComment = new ArrayList<>();
            try{
                lstComment = new ArrayList<>(database.getCommentRFbyProductId().get(productId));
            }catch (Exception ex){}
            if(lstComment == null) {
                JSONObject result = new JSONObject();
                result.put("commentList",jsonArray);
                result.put("numComment",0);
                result.put("currentTime",new DateTimeUtil().getNow());
                return object.put(ResponseController.CONTENT, result);
            }
            Collections.sort(lstComment, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    CommentEntity c1 = database.getCommentEntityHashMap().get(o1);
                    CommentEntity c2 = database.getCommentEntityHashMap().get(o2);
                    if (c1.getDate() > c2.getDate())
                        return -1;
                    return 0;
                }
            });
            int endIdx = startIdx+numComment;
            if(endIdx>lstComment.size()-1)
                endIdx = lstComment.size();
            for (long id : lstComment.subList(startIdx,endIdx)) {
                jsonArray.put(database.getCommentEntityHashMap().get(id).toCommentDetailJSON());
            }
            JSONObject result = new JSONObject();
            result.put("commentList",jsonArray);
            result.put("numComment",lstComment.size());
            result.put("currentTime",new DateTimeUtil().getNow());
            object.put(ResponseController.CONTENT, result);
            return object;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject commentProduct(String content, String email, long productId) {
        JSONObject responseJSON;
        CommentEntity comment= new CommentEntity();
        comment.setId(IdGenerator.getCommentId());
        comment.setContent(content);
        comment.setEmail(email);
        comment.setDate(new DateTimeUtil().getNow());
        comment.setProductId(productId);
        database.getCommentEntityHashMap().put(comment.getId(),comment);
        //put to commentRFProductId
        HashSet<Long> commentList = database.getCommentRFbyProductId().get(productId);
        if(commentList==null)
            commentList = new HashSet<>();
        commentList.add(comment.getId());
        database.getCommentRFbyProductId().put(productId,commentList);
        if (dao.insert(comment)) {
            responseJSON = ResponseController.createSuccessJSON();
            try {
                ProductEntity product = database.getProductEntityHashMap().get(productId);
                new ActivityController().insertActivity(email, product.getEmail(), ActivityUtil.COMMENT_PRODUCT, productId);
            } catch (Exception ignored) {
            }
        }
        else
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
        return responseJSON;
    }
}
