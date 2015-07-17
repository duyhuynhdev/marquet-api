package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.CityDao;
import com.marqet.WebServer.pojo.CityEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class CityController {
    private CityDao dao = new CityDao();
    private Database database = Database.getInstance();
    public JSONObject getListCity(String countryCode){
        try{
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<String> cityCodes = new ArrayList<>();
            try{
                cityCodes = new ArrayList<>(database.getCityRFbyCountryCode().get(countryCode));
            }catch (Exception ignored){}
            if(cityCodes!=null) {
                for (String c : cityCodes) {
                    jsonArray.put(database.getCityEntityHashMap().get(c).toJSON());
                }
            }
            result.put(ResponseController.CONTENT,jsonArray);
            return result;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject getListCity(){
        try{
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            for(String c : database.getCityEntityHashMap().keySet() ){
                jsonArray.put(database.getCityEntityHashMap().get(c).toJSON());
            }
            result.put(ResponseController.CONTENT,jsonArray);
            return result;
        }catch (Exception ex){
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject addCity(String name, String countryCode, String code) {
        try {
            CityEntity city = new CityEntity();
            if(code.replaceAll(" ","").equals("")|| database.getCityEntityHashMap().containsKey(code))
                code = IdGenerator.getCodeFromName(name);
            city.setCode(code);
            city.setName(name);
            if (database.getStateEntityHashMap().containsKey(city.getCode()))
                return ResponseController.createFailJSON("City is exist\n");
            city.setCountryCode(countryCode);
            HashSet<String> listCityCode = database.getCityRFbyCountryCode().get(countryCode);
            if(listCityCode == null)
                listCityCode = new HashSet<>();
            listCityCode.add(city.getCode());
            database.getCityRFbyCountryCode().put(countryCode,listCityCode);
            database.getCityEntityHashMap().put(city.getCode(), city);
            if (dao.insert(city))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, city.toJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject updateCity(String cityCode, String name, String countryCode) {
        try {
            CityEntity city = database.getCityEntityHashMap().get(cityCode);
            city.setName(name);
            //remove in old country
            database.getCityRFbyCountryCode().get(city.getCountryCode()).remove(cityCode);
            //add in new country
            city.setCountryCode(countryCode);
            HashSet<String> listCityCode = database.getCityRFbyCountryCode().get(countryCode);
            if(listCityCode == null)
                listCityCode = new HashSet<>();
            listCityCode.add(cityCode);
            database.getCityRFbyCountryCode().put(countryCode,listCityCode);
            database.getCityEntityHashMap().put(city.getCode(), city);
            if (dao.update(city))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, city.toJSON());
            else
                return ResponseController.createFailJSON("Cannot update in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteCity(String cityCode) {
        try {
            CityEntity city = new CityEntity(Database.getInstance().getCityEntityHashMap().get(cityCode));
            database.getCityEntityHashMap().remove(cityCode);
            //remove city in country
            database.getCityRFbyCountryCode().get(city.getCountryCode()).remove(cityCode);
            if (dao.delete(city))
                return ResponseController.createSuccessJSON();
            else
                return ResponseController.createFailJSON("Cannot delete in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
