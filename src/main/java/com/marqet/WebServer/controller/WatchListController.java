package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.WatchListDao;
import com.marqet.WebServer.pojo.ActivityEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.WatchListEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/27/15.
 */
public class WatchListController {
    private WatchListDao dao = new WatchListDao();
    private Database database = Database.getInstance();

    public void noticeWatchListOwner(long productId) {
        List<String> emails = new ArrayList<>();
        try {
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            if (product != null) {
                for (long id : database.getWatchListEntityHashMap().keySet()) {
                    WatchListEntity watchList = database.getWatchListEntityHashMap().get(id);
                    if (new SearchAndFilterUtil().textSearch(watchList.getPattern(), product.getName()) == 0) {
                        emails.add(watchList.getEmail());
                    }
                }
            }
            //notify
            ActivityEntity activityEntity = new ActivityEntity();
            activityEntity.setId(0);
            activityEntity.setOwnerEmail("");
            activityEntity.setObjectEmail("");
            activityEntity.setSubjectEmail("");
            activityEntity.setRef(productId);
            activityEntity.setAction(ActivityUtil.WATCHLIST);
            List<String> content = new ArrayList<>();
            content.add(ResponseController.createSuccessJSON().put(ResponseController.CONTENT, activityEntity.toDetailJSON()).toString());
            Thread thread = new Thread(new BroadcastCenterUtil(content, emails));
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject addWatchList(String email, String pattern) {
        try {
            HashSet<String> patternList = database.getWatchListPatternRFEmail().get(email);
            if (patternList != null) {
                if (patternList.contains(pattern)) {
                    return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, "Pattern is exits");
                }
            }
            WatchListEntity watchList = new WatchListEntity();
            long id = IdGenerator.getWatchListId();
            watchList.setId(id);
            watchList.setEmail(email);
            watchList.setPattern(pattern);
            patternList = database.getWatchListPatternRFEmail().get(watchList.getEmail());
            if (patternList == null)
                patternList = new HashSet<>();
            patternList.add(watchList.getPattern());
            database.getWatchListPatternRFEmail().put(watchList.getEmail(), patternList);
            HashSet<Long> watchLists = database.getWatchListRFEmail().get(watchList.getEmail());
            if (watchLists == null)
                watchLists = new HashSet<>();
            watchLists.add(watchList.getId());
            database.getWatchListRFEmail().put(watchList.getEmail(), watchLists);
            database.getWatchListEntityHashMap().put(id, watchList);

            if (dao.insert(watchList)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject getWatchList(String email) {
        try {
            JSONArray result = new JSONArray();
            List<Long> watchLists = new ArrayList<>();
            try {
                watchLists = new ArrayList<>(database.getWatchListRFEmail().get(email));
            } catch (Exception ignored) {
            }
            for (long id : watchLists) {
                WatchListEntity wl = database.getWatchListEntityHashMap().get(id);
                if (wl != null)
                    result.put(wl.toDetailJSON());
            }
            return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, result);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteWatchList(long watchListId) {
        try {
            WatchListEntity watchList = new WatchListEntity(database.getWatchListEntityHashMap().get(watchListId));
            HashSet<Long> watchLists = database.getWatchListRFEmail().get(watchList.getEmail());
            if (watchLists != null) {
                watchLists.remove(watchListId);
                database.getWatchListRFEmail().put(watchList.getEmail(), watchLists);
            }
            HashSet<String> patternList = database.getWatchListPatternRFEmail().get(watchList.getEmail());
            if (patternList != null) {
                patternList.remove(watchList.getPattern());
                database.getWatchListPatternRFEmail().put(watchList.getEmail(), patternList);
            }
            database.getWatchListEntityHashMap().remove(watchListId);
            if (dao.delete(watchList)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
