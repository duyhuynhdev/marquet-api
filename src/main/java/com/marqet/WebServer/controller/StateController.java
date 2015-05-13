package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.StateDao;
import com.marqet.WebServer.pojo.StateEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class StateController {
    private StateDao dao = new StateDao();
    private Database database = Database.getInstance();
    public JSONObject getListState(String countryCode){
        try{
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<String> stateCodes = database.getStateRFbyCountryCode().get(countryCode);
            if(stateCodes!=null) {
                for (String c : stateCodes) {
                    jsonArray.put(database.getStateEntityHashMap().get(c).toJSON());
                }
            }
            result.put(ResponseController.CONTENT,jsonArray);
            return result;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject addState(String name, String countryCode,String code) {
        try {
            StateEntity state = new StateEntity();
            if(code.replaceAll(" ","").equals("") || database.getStateEntityHashMap().containsKey(code))
                code = IdGenerator.getCodeFromName(name);
            state.setCode(code);
            state.setName(name);
            if (database.getStateEntityHashMap().containsKey(state.getCode()))
                return ResponseController.createFailJSON("State is exist\n");
            state.setCountryCode(countryCode);
            List<String> listStateCode = database.getStateRFbyCountryCode().get(countryCode);
            if(listStateCode == null)
                listStateCode = new ArrayList<>();
            listStateCode.add(state.getCode());
            database.getStateRFbyCountryCode().put(countryCode, listStateCode);
            database.getStateEntityHashMap().put(state.getCode(), state);
            if (dao.insert(state))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, state.toJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject updateState(String stateCode, String name, String countryCode) {
        try {
            StateEntity state = database.getStateEntityHashMap().get(stateCode);
            state.setName(name);
            //remove in old country
            database.getStateRFbyCountryCode().get(state.getCountryCode()).remove(stateCode);
            //add in new country
            state.setCountryCode(countryCode);
            List<String> listStateCode = database.getStateRFbyCountryCode().get(countryCode);
            if(listStateCode == null)
                listStateCode = new ArrayList<>();
            listStateCode.add(stateCode);
            database.getStateRFbyCountryCode().put(countryCode,listStateCode);
            database.getStateEntityHashMap().put(state.getCode(), state);
            if (dao.update(state))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, state.toJSON());
            else
                return ResponseController.createFailJSON("Cannot update in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteState(String stateCode) {
        try {
            StateEntity state = new StateEntity(database.getStateEntityHashMap().get(stateCode));
            database.getStateEntityHashMap().remove(stateCode);
            //remove state in country
            database.getStateRFbyCountryCode().get(state.getCountryCode()).remove(stateCode);
            if (dao.delete(state))
                return ResponseController.createSuccessJSON();
            else
                return ResponseController.createFailJSON("Cannot delete in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
