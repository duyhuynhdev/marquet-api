package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.CountryDao;
import com.marqet.WebServer.pojo.CountryEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class CountryController {
    private CountryDao dao = new CountryDao();
    private Database database = Database.getInstance();

    public JSONObject getListCountry() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            for (String c : database.getCountryEntityHashMap().keySet()) {
                jsonArray.put(database.getCountryEntityHashMap().get(c).toJSON());
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject addCountry(String name, String phonePrefix,String code) {
        try {
            CountryEntity country = new CountryEntity();
            if(code.replaceAll(" ","").equals("")|| database.getCountryEntityHashMap().containsKey(code))
                code = IdGenerator.getCodeFromName(name);
            country.setCode(code);
            country.setName(name);
            country.setPhonePrefix(phonePrefix);
            if (database.getCountryEntityHashMap().containsKey(country.getCode()))
                return ResponseController.createFailJSON("Country is exist\n");
            database.getCountryEntityHashMap().put(country.getCode(), country);
            if (dao.insert(country))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, country.toJSON());
            else
                return ResponseController.createFailJSON("Cannot insert in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject updateCountry(String countryCode, String name, String phonePrefix) {
        try {
            CountryEntity country = database.getCountryEntityHashMap().get(countryCode);
            country.setName(name);
            country.setPhonePrefix(phonePrefix);
            database.getCountryEntityHashMap().put(country.getCode(), country);
            if (dao.update(country))
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, country.toJSON());
            else
                return ResponseController.createFailJSON("Cannot update in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteCountry(String countryCode) {
        try {
            CountryEntity country = new CountryEntity(Database.getInstance().getCountryEntityHashMap().get(countryCode));
            database.getCountryEntityHashMap().remove(countryCode);
            //remove city in country
            List<String> listCityCode = database.getCityRFbyCountryCode().get(countryCode);
            if(listCityCode!=null) {
                for (String code : new ArrayList<>(listCityCode)) {
                    new CityController().deleteCity(code);
                }
            }
            database.getCityRFbyCountryCode().remove(countryCode);
            // remove state in country
            List<String> listStateCode = database.getStateRFbyCountryCode().get(countryCode);
            if(listStateCode!=null) {
                for (String code : new ArrayList<>(listStateCode)) {
                    new StateController().deleteState(code);
                }
            }
            database.getStateRFbyCountryCode().remove(countryCode);
            if (dao.delete(country))
                return ResponseController.createSuccessJSON();
            else
                return ResponseController.createFailJSON("Cannot delete in database\n");

        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
