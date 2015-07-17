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
    public JSONObject getInformation(int type){
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            switch (type){
                case 1:
                    result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON().get("communityGuidelines"));
                    break;
                case 2:
                    result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON().get("helpFAQ"));
                    break;
                case 3:
                    result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON().get("about"));
                    break;
                case 4:
                    result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON().get("emailSupport"));
                    break;
                default:
                    result.put(ResponseController.CONTENT, database.getInformationEntity().toJSON());
            }

            return result;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject updateInformation(String about,String communityGuidelines,String emailSupport,String helpFAQ){
        try {
            database.getInformationEntity().setAbout(about);
            database.getInformationEntity().setCommunityRule(communityGuidelines);
            database.getInformationEntity().setEmailSupport(emailSupport);
            database.getInformationEntity().setHelpFAQ(helpFAQ);
            if (dao.update(database.getInformationEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        }catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
