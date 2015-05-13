package com.marqet.WebServer.util;

import com.marqet.WebServer.controller.ResponseController;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class ApiParameterChecker {
    public static JSONObject check(Set<String> keySet, String parameters){
        String [] parameterArray = parameters.split(",");
        JSONObject exJSON = new JSONObject();
        for(String ep : parameterArray){
            exJSON.put(ep,"#value");
        }
        for(String p : parameterArray){
            if(!keySet.contains(p)){
                return ResponseController.createFailJSON("Parameter @"+p+" is not found.\n Right format:"+exJSON);
            }
        }
        return ResponseController.createSuccessJSON();
    }

}
