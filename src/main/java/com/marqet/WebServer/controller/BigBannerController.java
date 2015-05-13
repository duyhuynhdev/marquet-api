package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.BigBannerDao;
import com.marqet.WebServer.dao.ElementDao;
import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.BigBannerEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class BigBannerController {
    private BigBannerDao dao = new BigBannerDao();
    private Database database = Database.getInstance();

    public JSONObject getListBigBanner() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            long now = new DateTimeUtil().getNow();
            JSONArray jsonArray = new JSONArray();
            //copying hash map for deleting old banner
            long duration = new JSONObject(database.getElementEntity().getBigBannerInfo()).getLong(ElementDao.DURATION);
            HashMap<Long, BigBannerEntity> bigBannerHashMap = new HashMap<>(database.getBigBannerEntityHashMap());
            for (long bb : bigBannerHashMap.keySet()) {
                BigBannerEntity bigBanner = bigBannerHashMap.get(bb);
                if (bigBanner.getSetTime() < now) {
                    deleteBigBanner(bigBanner.getId());
                } else {
                    jsonArray.put(bigBanner.toDetailJSON());
                    if (jsonArray.length() == BigBannerDao.NUMBER_OF_BIGBANNER) {
                        break;
                    }
                }
            }

            result.put(ResponseController.CONTENT, getDefaultBigBanner(jsonArray));
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject deleteBigBanner(long bigBannerId) {
        try {
            BigBannerEntity bigBanner = new BigBannerEntity(database.getBigBannerEntityHashMap().get(bigBannerId));
            if (bigBanner == null)
                return ResponseController.createFailJSON("BigBanner is not found\n");
            database.getBigBannerEntityHashMap().remove(bigBannerId);
            if (dao.delete(bigBanner)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject putBigBanner(String email, long productId) {
        try {
            BigBannerEntity bigBanner = new BigBannerEntity();
            bigBanner.setId(IdGenerator.getBigBannerId());
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            if(!product.getProductImages().equals("")&&new JSONArray(product.getProductImages()).length()>0) {
                bigBanner.setCoverImg(new JSONArray(product.getProductImages()).getString(0));
            }else{
                bigBanner.setCoverImg("");
            }
            bigBanner.setEmail(email);
            bigBanner.setStatus("");
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getBigBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            //-----
            bigBanner.setSetTime(BigBannerDao.getTimeForBigBanner());
            database.getBigBannerEntityHashMap().put(bigBanner.getId(), bigBanner);
            if (dao.insert(bigBanner)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    private JSONArray getDefaultBigBanner(JSONArray jsonArray){
        for(int i = 0 ; i < (BigBannerDao.NUMBER_OF_BIGBANNER-jsonArray.length()); i++ )
            jsonArray.put(BigBannerEntity.toDefaultDetailJSON());
        return jsonArray;
    }
}
