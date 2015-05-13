package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.WatchListDao;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.WatchListEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import com.marqet.WebServer.util.SearchAndFilterUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 3/27/15.
 */
public class WatchListController {
    private WatchListDao dao = new WatchListDao();
    private Database database = Database.getInstance();
    //TODO notification
    public List<String> noticeWatchListOwner(long productId) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }

    public JSONObject addWatchList(String email, String pattern) {
        try {
            WatchListEntity watchList = new WatchListEntity();
            long id = IdGenerator.getWatchListId();
            watchList.setId(id);
            watchList.setEmail(email);
            watchList.setPattern(pattern);
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
}
