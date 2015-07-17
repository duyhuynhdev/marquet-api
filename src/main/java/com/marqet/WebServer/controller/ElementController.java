package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.ElementDao;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.Path;
import com.marqet.WebServer.util.UploadImageUtil;
import org.json.JSONObject;

import javax.servlet.http.Part;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class ElementController {
    private ElementDao dao = new ElementDao();
    private Database database = Database.getInstance();

    public JSONObject getElement() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            result.put(ResponseController.CONTENT, database.getElementEntity().toElementDetailJSON());
            return result;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject changeLogo(Part imagePart) {
        try {
            if (imagePart != null && imagePart.getSize() > 0) {
                String newLogo = new UploadImageUtil().upload("logo" + new DateTimeUtil().getNow(), Path.getOtherPath(), imagePart);
                database.getElementEntity().setLogo(newLogo);
            }
            if (dao.update(database.getElementEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        }catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject changeAvatar(Part imagePart) {
        try {
            if (imagePart != null && imagePart.getSize() > 0) {
                String newAvatar = new UploadImageUtil().upload("defaultAvatar" + new DateTimeUtil().getNow(), Path.getOtherPath(), imagePart);
                database.getElementEntity().setDefaultAvatar(newAvatar);
            }
            if (dao.update(database.getElementEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        }catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject changeBigBannerInfo(long newPoint, long newDuration, int num) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ElementDao.POINT, newPoint);
            jsonObject.put(ElementDao.DURATION, newDuration);
            jsonObject.put(ElementDao.NUM, num);
            database.getElementEntity().setBigBannerInfo(jsonObject.toString());
            if (dao.update(database.getElementEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        }catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject changeSmallBannerInfo(long newPoint, long newDuration, int num) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ElementDao.POINT, newPoint);
            jsonObject.put(ElementDao.DURATION, newDuration);
            jsonObject.put(ElementDao.NUM, num);
            database.getElementEntity().setSmallBannerInfo(jsonObject.toString());
            if (dao.update(database.getElementEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject changePointLevel(long level1, long level2, long level3, long level4) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ElementDao.LEVEL1, level1);
            jsonObject.put(ElementDao.LEVEL2, level2);
            jsonObject.put(ElementDao.LEVEL3, level3);
            jsonObject.put(ElementDao.LEVEL4, level4);
            database.getElementEntity().setPointLevel(jsonObject.toString());
            if (dao.update(database.getElementEntity())) {
                return ResponseController.createSuccessJSON();
            }
            return ResponseController.createFailJSON("Cannot update in database\n");
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

}
