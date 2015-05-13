package com.marqet.WebServer.controller;

import org.json.JSONObject;

/**
 * Created by hpduy17 on 3/17/15.
 */
public class ResponseController {
    public static final String RESULT ="result";
    public static final String SUCCESS ="success";
    public static final String FAIL ="fail";
    public static final String ERROR ="error";
    public static final String CONTENT ="content";
    public static JSONObject createSuccessJSON(){
        JSONObject object = new JSONObject();
        object.put(RESULT,SUCCESS);
        return object;
    }
    public static JSONObject createFailJSON(String content){
        JSONObject object = new JSONObject();
        object.put(RESULT,FAIL);
        object.put(CONTENT,content);
        return object;
    }
    public static JSONObject createErrorJSON(String error){
        JSONObject object = new JSONObject();
        object.put(RESULT,ERROR);
        object.put(CONTENT,error);
        return object;
    }
    public static boolean isSuccess(JSONObject result){
        if(result.get(RESULT).equals(SUCCESS))
            return true;
        return false;
    }
    public static boolean isFail(JSONObject result){
        if(result.get(RESULT).equals(FAIL))
            return true;
        return false;
    }
    public static boolean isError(JSONObject result){
        if(result.get(RESULT).equals(ERROR))
            return true;
        return false;
    }
}
