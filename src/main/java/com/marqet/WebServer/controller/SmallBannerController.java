package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.SmallBannerDao;
import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.SmallBannerEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class SmallBannerController {
    private SmallBannerDao dao = new SmallBannerDao();
    private Database database = Database.getInstance();
    public static boolean isThreadRun = false;

    public JSONObject getListSmallBanner() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray array = new JSONArray();
            List<SmallBannerEntity> smallBannerEntities = new ArrayList<>(database.getSmallBannerShowQueue().values());
            for (int i = smallBannerEntities.size() - 1; i >= 0; i--) {
                SmallBannerEntity b = smallBannerEntities.get(i);
                if (b.getStatus().equals(SmallBannerDao.SHOW)) {
                    array.put(b.toDetailJSON());
                }
                if (array.length() == SmallBannerDao.NUMBER_OF_SMALLBANNER)
                    break;
            }
            if (array.length() < SmallBannerDao.NUMBER_OF_SMALLBANNER) {
                array = getDefaultSmallBanner(array);
            }
            result.put(ResponseController.CONTENT, array);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListWaitingSmallBanner(String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray array = new JSONArray();
            List<SmallBannerEntity> smallBannerEntities = new ArrayList<>(database.getSmallBannerWaitingStack().values());
            for (int i = smallBannerEntities.size() - 1; i >= 0; i--) {
                SmallBannerEntity b = smallBannerEntities.get(i);
                if (b.getStatus().equals(SmallBannerDao.WAITING)) {
                    array.put(b.toDetailWithProductJSON(email));
                }
                if (array.length() == SmallBannerDao.NUMBER_OF_SMALLBANNER)
                    break;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", database.getSmallPopTime());
            jsonObject.put("banners", array);
            result.put(ResponseController.CONTENT, jsonObject);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject deleteSmallBanner(long smallBannerId) {
        try {
            SmallBannerEntity smallBanner = new SmallBannerEntity(database.getSmallBannerEntityHashMap().get(smallBannerId));
            database.getSmallBannerEntityHashMap().remove(smallBannerId);
            database.getSmallBannerShowQueue().remove(smallBannerId);
            database.getSmallBannerWaitingStack().remove(smallBannerId);
            database.getSmallBannerExist().remove(smallBanner.getEmail() + "#" + smallBanner.getProductId());
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
            if (database.getSmallBannerExist().containsKey(email + "#" + productId)) {
                return updateSmallBanner(email, productId);
            }
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getSmallBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            // small banner process
            SmallBannerEntity smallBanner = new SmallBannerEntity();
            smallBanner.setId(IdGenerator.getSmallBannerId());
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            String img = "";
            JSONArray jsonVideo = new JSONArray();
            try {
                jsonVideo = new JSONArray(product.getProductVideo());
            } catch (Exception ex) {

            }
            if (jsonVideo.length() == 0) {
                if (!product.getProductImages().equals("") && new JSONObject(product.getProductImages()).getJSONArray("thumbnail").length() > 0) {
                    try {
                        img = new JSONObject(product.getProductImages()).getJSONArray("thumbnail").getString(0);
                    } catch (Exception ignored) {
                    }
                }
            } else {
                try {
                    img = new JSONArray(product.getProductVideo()).getString(1);
                } catch (Exception ignored) {
                }
            }
            smallBanner.setCoverImg(img);
            smallBanner.setEmail(email);
            smallBanner.setSetTime(new DateTimeUtil().getNow());
            smallBanner.setProductId(productId);
            //-----put queue and stack
            if (database.getSmallBannerShowQueue().size() < SmallBannerDao.NUMBER_OF_SMALLBANNER) {
                smallBanner.setStatus(SmallBannerDao.SHOW);
                database.getSmallBannerShowQueue().remove(smallBanner.getId());
                database.getSmallBannerShowQueue().put(smallBanner.getId(), smallBanner);
                database.getSmallBannerExist().put(email + "#" + productId, smallBanner.getId());
            } else {
                smallBanner.setStatus(SmallBannerDao.WAITING);
                database.getSmallBannerWaitingStack().remove(smallBanner.getId());
                database.getSmallBannerWaitingStack().put(smallBanner.getId(), smallBanner);
                database.getSmallBannerExist().put(email + "#" + productId, smallBanner.getId());
                if (!isThreadRun) {
                    database.setSmallPopTime(new DateTimeUtil().getNow());
                    Thread smallBannerTimer = new Thread(new SmallBannerTimer());
                    smallBannerTimer.start();
                    isThreadRun = true;
                }
            }
            //-- save small banner
            database.getSmallBannerEntityHashMap().put(smallBanner.getId(), smallBanner);
            if (dao.insert(smallBanner)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, "");
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject updateSmallBanner(String email, long productId) {
        try {
            long smallBannerId = database.getSmallBannerExist().get(email + "#" + productId);
            if (database.getSmallBannerShowQueue().containsKey(smallBannerId)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, "hot");
            }
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getSmallBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            // small banner process
            SmallBannerEntity smallBanner = database.getSmallBannerEntityHashMap().get(smallBannerId);
            smallBanner.setSetTime(new DateTimeUtil().getNow());
            //-----put queue and stack
            if (database.getSmallBannerShowQueue().containsKey(smallBanner.getId())) {
                smallBanner.setStatus(SmallBannerDao.SHOW);
                database.getSmallBannerShowQueue().remove(smallBanner.getId());
                database.getSmallBannerShowQueue().put(smallBanner.getId(), smallBanner);
                database.getSmallBannerExist().put(email + "#" + productId, smallBanner.getId());
            } else {
                smallBanner.setStatus(SmallBannerDao.WAITING);
                database.getSmallBannerWaitingStack().remove(smallBanner.getId());
                database.getSmallBannerWaitingStack().put(smallBanner.getId(), smallBanner);
                database.getSmallBannerExist().put(email + "#" + productId, smallBanner.getId());
            }
            //-- save small banner
            database.getSmallBannerEntityHashMap().put(smallBanner.getId(), smallBanner);
            if (dao.update(smallBanner)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, "");
            } else {
                return ResponseController.createFailJSON("Cannot update in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    private JSONArray getDefaultSmallBanner(JSONArray jsonArray) {
        List<Long> products = null;
        try {
            products = new ArrayList<>(database.getProductEntityHashMap().keySet());
            products = new SearchAndFilterUtil().sortProductByLastest(products);
        } catch (Exception ignored) {
        }
        int length = jsonArray.length();
        for (int i = 0; i < (SmallBannerDao.NUMBER_OF_SMALLBANNER - length); i++) {
            if (products != null) {
                ProductEntity product = database.getProductEntityHashMap().get(products.get(i % products.size()));
                SmallBannerEntity sm = new SmallBannerEntity();
                String img = "";
                JSONArray jsonVideo = new JSONArray();
                try {
                    jsonVideo = new JSONArray(product.getProductVideo());
                } catch (Exception ex) {

                }
                if (jsonVideo.length() == 0) {
                    if (!product.getProductImages().equals("") && new JSONObject(product.getProductImages()).getJSONArray("thumbnail").length() > 0) {
                        try {
                            img = new JSONObject(product.getProductImages()).getJSONArray("thumbnail").getString(0);
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    try {
                        img = new JSONArray(product.getProductVideo()).getString(1);
                    } catch (Exception ignored) {
                    }
                }
                sm.setCoverImg(img);
                sm.setProductId(product.getId());
                jsonArray.put(sm.toDetailJSON());
            } else {
                jsonArray.put(SmallBannerEntity.toDefaultDetailJSON());
            }
        }
        return jsonArray;
    }

}
