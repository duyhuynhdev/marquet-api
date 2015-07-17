package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.ActivityDao;
import com.marqet.WebServer.pojo.ActivityEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by hpduy17 on 4/1/15.
 */
public class ActivityController {
    private ActivityDao dao = new ActivityDao();
    private Database database = Database.getInstance();

    public JSONObject getListActivity(String email, long lastestId, int numActivity) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> activityIdList = new ArrayList<>();
            try{
                activityIdList = new ArrayList<>(database.getActivityRFEmail().get(email));
            }catch (Exception ignored){}

            if(activityIdList == null){
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            Collections.sort(activityIdList, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    ActivityEntity a1 = database.getActivityEntityHashMap().get(o1);
                    ActivityEntity a2 = database.getActivityEntityHashMap().get(o2);
                    if (a1 != null && a2 != null) {
                        if (a1.getDate() < a2.getDate()) {
                            return -1;
                        }
                        return 1;
                    }
                    return 0;
                }
            });
            int endIdx = activityIdList.size();
            try {
                if(lastestId>0) {
                    endIdx = activityIdList.indexOf(lastestId);
                }
            }catch (Exception ignored){

            }
            int startIdx = endIdx - numActivity;
            if (startIdx < 0)
                startIdx = 0;
            List<Long> subList = activityIdList.subList(startIdx, endIdx);
            JSONArray activitiesJSONArray = new JSONArray();
            for (int i = subList.size()-1; i >= 0; i--) {
                activitiesJSONArray.put(database.getActivityEntityHashMap().get(subList.get(i)).toDetailJSON());
            }
            return result.put(ResponseController.CONTENT, activitiesJSONArray);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject insertActivity(String subEmail, String objEmail, int action, long ref) {
        UserEntity userActive = database.getUserEntityHashMap().get(subEmail);
        JSONObject resultActive = ResponseController.createFailJSON("");
        JSONObject resultPassive = ResponseController.createFailJSON("");
        if (userActive != null) {
            resultActive = insertActivity(subEmail, subEmail, objEmail, action, ref);
            //TODO insert activity list following
            //notify
            List<String> content = new ArrayList<>();
            content.add(resultActive.toString());
            List<String> email = new ArrayList<>();
            email.add(subEmail);
            Thread thread = new Thread(new BroadcastCenterUtil(content,email));
            thread.start();
        }
        UserEntity userPassive = database.getUserEntityHashMap().get(objEmail);
        if (userPassive != null) {
            resultPassive = insertActivity(objEmail, subEmail, objEmail, action, ref);

        }
        if (resultActive.getString(ResponseController.RESULT).equals(ResponseController.SUCCESS)
                && resultPassive.getString(ResponseController.RESULT).equals(ResponseController.SUCCESS))
            return ResponseController.createSuccessJSON();
        else {
            String content = "";
            if (resultActive.keySet().contains(ResponseController.CONTENT)) {
                content += resultActive.getString(ResponseController.CONTENT) + "\n";
            }
            if (resultPassive.keySet().contains(ResponseController.CONTENT)) {
                content += resultPassive.getString(ResponseController.CONTENT);
            }
            return ResponseController.createFailJSON(content);
        }
    }

    private JSONObject insertActivity(String ownerEmail, String subEmail, String objEmail, int action, long ref) {
        try {
            ActivityEntity obj = new ActivityEntity();
            obj.setId(IdGenerator.getActivityId());
            obj.setAction(action);
            obj.setDate(new DateTimeUtil().getNow());
            obj.setSubjectEmail(subEmail);
            obj.setRef(ref);
            obj.setObjectEmail(objEmail);
            obj.setOwnerEmail(ownerEmail);
            obj.setIsRead(0);
            database.getActivityEntityHashMap().put(obj.getId(), obj);
            UserEntity users = database.getUserEntityHashMap().get(obj.getOwnerEmail());
            if (users != null) {
                HashSet<Long> activityList = database.getActivityRFEmail().get(users.getEmail());
                if (activityList == null)
                    activityList = new HashSet<>();
                activityList.add(obj.getId());
                database.getActivityRFEmail().put(users.getEmail(), activityList);
            }
            if (obj.getAction() == ActivityUtil.WATCHING_PRODUCT && obj.getRef() > 0) {
                HashMap<Long, Long> watchProductLog = database.getWatchingProductLogHashMap().get(users.getEmail());
                if (watchProductLog == null)
                    watchProductLog = new HashMap<>();
                watchProductLog.put(obj.getRef(), obj.getId());
                database.getWatchingProductLogHashMap().put(users.getEmail(), watchProductLog);
            }
            if (dao.insert(obj)) {
                JSONObject result = ResponseController.createSuccessJSON();
                result.put(ResponseController.CONTENT,obj.toDetailJSON());
                return result;
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject updateTimeWatchProducActivity(long activitiId) {
        try {
            ActivityEntity obj = database.getActivityEntityHashMap().get(activitiId);
            if(obj==null)
                return ResponseController.createFailJSON("Activity Cannot Found");
            if(obj.getAction() != ActivityUtil.WATCHING_PRODUCT)
                return ResponseController.createFailJSON("Have Something Wrong, action is not watching");
            obj.setDate(new DateTimeUtil().getNow());
            database.getActivityEntityHashMap().put(obj.getId(), obj);
            if (dao.update(obj)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot update in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject deleteActivity(String ownerEmail, long activityId) {
        try {
            ActivityEntity obj = new ActivityEntity(database.getActivityEntityHashMap().get(activityId));
            database.getActivityEntityHashMap().remove(activityId);
            UserEntity users = database.getUserEntityHashMap().get(ownerEmail);
            if (users != null) {
                HashSet<Long> activityList = database.getActivityRFEmail().get(users.getEmail());
                if (activityList != null){
                    activityList.remove(activityId);
                }
                database.getActivityRFEmail().put(users.getEmail(), activityList);
            }
            if (dao.delete(obj)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
