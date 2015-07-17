package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.BigBannerDao;
import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.BigBannerEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/21/15.
 */
public class BigBannerController {
    private BigBannerDao dao = new BigBannerDao();
    private Database database = Database.getInstance();
    public static boolean isThreadRun = false;

    public JSONObject getListBigBanner() {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray array = new JSONArray();
            List<BigBannerEntity> bigBannerEntities = new ArrayList<>(database.getBigBannerShowQueue().values());
            for (int i = bigBannerEntities.size() - 1; i >= 0; i--) {
                BigBannerEntity b = bigBannerEntities.get(i);
                if (b.getStatus().equals(BigBannerDao.SHOW)) {
                    array.put(b.toDetailJSON());
                }
                if (array.length() == BigBannerDao.NUMBER_OF_BIGBANNER)
                    break;
            }
            if (array.length() < BigBannerDao.NUMBER_OF_BIGBANNER) {
                array = getDefaultBigBanner(array);
            }
            return result.put(ResponseController.CONTENT, array);
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListWaitingBigBanner(String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray array = new JSONArray();
            List<BigBannerEntity> bigBannerEntities = new ArrayList<>(database.getBigBannerWaitingStack().values());
            for (int i = bigBannerEntities.size() - 1; i >= 0; i--) {
                BigBannerEntity b = bigBannerEntities.get(i);
                if (b.getStatus().equals(BigBannerDao.WAITING)) {
                    array.put(b.toDetailWithProductJSON(email));
                }
                if (array.length() == BigBannerDao.NUMBER_OF_BIGBANNER)
                    break;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", database.getBigPopTime());
            jsonObject.put("banners", array);
            return result.put(ResponseController.CONTENT, array);
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject deleteBigBanner(long bigBannerId) {
        try {
            BigBannerEntity bigBanner = new BigBannerEntity(database.getBigBannerEntityHashMap().get(bigBannerId));
            database.getBigBannerEntityHashMap().remove(bigBannerId);
            database.getBigBannerShowQueue().remove(bigBannerId);
            database.getBigBannerWaitingStack().remove(bigBannerId);
            database.getBigBannerExist().remove(bigBanner.getEmail() + "#" + bigBanner.getProductId());
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
            if (database.getBigBannerExist().containsKey(email + "#" + productId)) {
                return updateBigBanner(email, productId);
            }
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getBigBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            // process big banner
            BigBannerEntity bigBanner = new BigBannerEntity();
            bigBanner.setId(IdGenerator.getBigBannerId());
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            String img = "";
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = new JSONArray(product.getProductVideo());
            } catch (Exception ex) {

            }
            if (jsonArray.length() == 0) {
                if (!product.getProductImages().equals("") && new JSONObject(product.getProductImages()).getJSONArray("full").length() > 0) {
                    try {
                        img = new JSONObject(product.getProductImages()).getJSONArray("full").getString(0);
                    } catch (Exception ignored) {
                    }
                }
            } else {
                try {
                    img = new JSONArray(product.getProductVideo()).getString(1);
                } catch (Exception ignored) {
                }
            }
            bigBanner.setCoverImg(img);
            bigBanner.setEmail(email);
            bigBanner.setSetTime(new DateTimeUtil().getNow());
            bigBanner.setProductId(productId);
            //-----put queue and stack
            if (database.getBigBannerShowQueue().size() < BigBannerDao.NUMBER_OF_BIGBANNER) {
                bigBanner.setStatus(BigBannerDao.SHOW);
                database.getBigBannerShowQueue().remove(bigBanner.getId());
                database.getBigBannerShowQueue().put(bigBanner.getId(), bigBanner);
                database.getBigBannerExist().put(email + "#" + productId, bigBanner.getId());
            } else {
                bigBanner.setStatus(BigBannerDao.WAITING);
                database.getBigBannerWaitingStack().remove(bigBanner.getId());
                database.getBigBannerWaitingStack().put(bigBanner.getId(), bigBanner);
                database.getBigBannerExist().put(email + "#" + productId, bigBanner.getId());
                if (!isThreadRun) {
                    database.setBigPopTime(new DateTimeUtil().getNow());
                    Thread bigBannerTimer = new Thread(new BigBannerTimer());
                    bigBannerTimer.start();
                    isThreadRun = true;
                }
            }
            //-- save big banner
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

    public JSONObject updateBigBanner(String email, long productId) {
        try {
            //process user's point
            UserEntity user = database.getUserEntityHashMap().get(email);
            long point = new JSONObject(database.getElementEntity().getBigBannerInfo()).getLong("point");
            if (user.getPoint() < point)
                return ResponseController.createFailJSON("Not enough points");
            long newPoint = user.getPoint() - point;
            user.setPoint(newPoint);
            new UserDao().update(user);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            // process big banner
            BigBannerEntity bigBanner = database.getBigBannerEntityHashMap().get(database.getBigBannerExist().get(email + "#" + productId));
            bigBanner.setSetTime(new DateTimeUtil().getNow());
            //-----put queue and stack
            if (database.getBigBannerShowQueue().size() < BigBannerDao.NUMBER_OF_BIGBANNER) {
                bigBanner.setStatus(BigBannerDao.SHOW);
                database.getBigBannerShowQueue().remove(bigBanner.getId());
                database.getBigBannerShowQueue().put(bigBanner.getId(), bigBanner);
            } else {
                bigBanner.setStatus(BigBannerDao.WAITING);
                database.getBigBannerWaitingStack().remove(bigBanner.getId());
                database.getBigBannerWaitingStack().put(bigBanner.getId(), bigBanner);
            }
            //-- save big banner
            database.getBigBannerEntityHashMap().put(bigBanner.getId(), bigBanner);
            if (dao.update(bigBanner)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot update in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    private JSONArray getDefaultBigBanner(JSONArray jsonArray) {
        List<Long> products = null;
        try {
            products = new ArrayList<>(database.getProductEntityHashMap().keySet());
            products = new SearchAndFilterUtil().sortProductByLastest(products);
        } catch (Exception ignored) {
        }
        int length = jsonArray.length();
        for (int i = 0; i < (BigBannerDao.NUMBER_OF_BIGBANNER - length); i++) {
            if (products != null) {
                ProductEntity product = database.getProductEntityHashMap().get(products.get(i % products.size()));
                BigBannerEntity bb = new BigBannerEntity();
                String img = "";
                JSONArray jsonVideo = new JSONArray();
                try {
                    jsonVideo = new JSONArray(product.getProductVideo());
                } catch (Exception ex) {

                }
                if (jsonVideo.length() == 0) {
                    if (!product.getProductImages().equals("") && new JSONObject(product.getProductImages()).getJSONArray("full").length() > 0) {
                        try {
                            img = new JSONObject(product.getProductImages()).getJSONArray("full").getString(0);
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    try {
                        img = new JSONArray(product.getProductVideo()).getString(1);
                    } catch (Exception ignored) {
                    }
                }
                bb.setCoverImg(img);
                bb.setProductId(product.getId());
                jsonArray.put(bb.toDetailJSON());
            } else {
                jsonArray.put(BigBannerEntity.toDefaultDetailJSON());
            }
        }
        return jsonArray;
    }
}
