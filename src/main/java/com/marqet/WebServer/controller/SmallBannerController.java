package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.BigBannerDao;
import com.marqet.WebServer.dao.SmallBannerDao;
import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.SmallBannerEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class SmallBannerController {
    private SmallBannerDao dao = new SmallBannerDao();
    private Database database = Database.getInstance();

    public JSONObject getListSmallBanner() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            long now = new DateTimeUtil().getNow();
            JSONArray jsonArray = new JSONArray();
            //copying hash map for deleting old banner
            HashMap<Long, SmallBannerEntity> smallBannerHashMap = new HashMap<>(database.getSmallBannerEntityHashMap());
            for (long sb : smallBannerHashMap.keySet()) {
                SmallBannerEntity smallBanner = smallBannerHashMap.get(sb);
                if (smallBanner.getSetTime() < now) {
                    deleteSmallBanner(smallBanner.getId());
                } else {
                    jsonArray.put(smallBanner.toDetailJSON());
                    if (jsonArray.length() == SmallBannerDao.NUMBER_OF_SMALLBANNER) {
                        break;
                    }
                }
            }
            if (jsonArray.length() < SmallBannerDao.NUMBER_OF_SMALLBANNER) {
                List<Long> lstProductId = new ProductController().getTopBestProduct();
                if(lstProductId!=null) {
                    for (int i = 0; i < SmallBannerDao.NUMBER_OF_SMALLBANNER - jsonArray.length(); i++) {
                        ProductEntity product = database.getProductEntityHashMap().get(lstProductId.get(i));
                        jsonArray.put(SmallBannerEntity.convertToDetailJSON(product));
                    }
                }
            }
            result.put(ResponseController.CONTENT, getDefaultSmallBanner(jsonArray));
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject deleteSmallBanner(long smallBannerId) {
        try {
            SmallBannerEntity smallBanner = new SmallBannerEntity(database.getSmallBannerEntityHashMap().get(smallBannerId));
            if (smallBanner == null)
                return ResponseController.createFailJSON("Small Banner is not found\n");
            database.getSmallBannerEntityHashMap().remove(smallBannerId);
            if (dao.delete(smallBanner)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject putSmallBanner(String email, long productId) {
        try {
            SmallBannerEntity smallBanner = new SmallBannerEntity();
            smallBanner.setId(IdGenerator.getBigBannerId());
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            String productImage = "";
            try {
                JSONObject productImages = new JSONObject(product.getProductImages());
                productImage = productImages.getJSONArray("thumbnail").getString(0);
            } catch (Exception ex) {

            }
            smallBanner.setCoverImg(productImage);
            smallBanner.setEmail(email);
            smallBanner.setStatus("");
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getSmallBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            //-----
            smallBanner.setSetTime(BigBannerDao.getTimeForBigBanner());
            database.getSmallBannerEntityHashMap().put(smallBanner.getId(), smallBanner);
            if (dao.insert(smallBanner)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    private JSONArray getDefaultSmallBanner(JSONArray jsonArray){
        for(int i = 0 ; i < (SmallBannerDao.NUMBER_OF_SMALLBANNER-jsonArray.length()); i++ )
            jsonArray.put(SmallBannerEntity.toDefaultDetailJSON());
        return jsonArray;
    }

}
