package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.InformationDao;
import com.marqet.WebServer.util.Database;
import org.json.JSONObject;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class InformationController {
    private InformationDao dao = new InformationDao();
    private Database database = Database.getInstance();
    public JSONObject getInformation(){
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON());
            return result;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject updateInformation(String about,String communityRule,String emailSupport,String pointSystem){
        try {
            database.getInformationEntity().setAbout(about);
            database.getInformationEntity().setCommunityRule(communityRule);
            database.getInformationEntity().setEmailSupport(emailSupport);
            database.getInformationEntity().setPointSystem(pointSystem);
            if (dao.update(database.getInformationEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        }catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
