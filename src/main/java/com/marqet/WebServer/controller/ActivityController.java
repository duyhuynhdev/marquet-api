package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.ActivityDao;
import com.marqet.WebServer.pojo.ActivityEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.ActivityUtil;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.DateTimeUtil;
import com.marqet.WebServer.util.IdGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 4/1/15.
 */
public class ActivityController {
    private ActivityDao dao = new ActivityDao();
    private Database database = Database.getInstance();

    public JSONObject getListActivity(String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<Long> activityIdList = database.getActivityRFEmail().get(email);
            JSONArray activitiesJSONArray = new JSONArray();
            if (activityIdList != null)
                activitiesJSONArray = activityProcess(email, activityIdList);
            return result.put(ResponseController.CONTENT, activitiesJSONArray);
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    private JSONArray activityProcess(String email, List<Long> activityIdList) {

        JSONArray jsonArray = new JSONArray();
        try {
            //process case only 1 activity
            if (activityIdList.size() == 1) {
                ActivityEntity activity = database.getActivityEntityHashMap().get(activityIdList.get(0));
                if (activity != null) {
                    List<ActivityEntity> actList =new ArrayList<>();
                    actList.add(activity);
                    return jsonArray.put(parseActivityInfo(email, actList));
                }
            }
            for (int i = 0; i < activityIdList.size(); i++) {
                ActivityEntity activity = database.getActivityEntityHashMap().get(activityIdList.get(i));
                ActivityEntity activityNext = database.getActivityEntityHashMap().get(activityIdList.get(i + 1));
                List<ActivityEntity> sameActionList = new ArrayList<>();
                // check case
                int caseAction = checkSameActionCase(activity, activityNext);
                if (caseAction != 0) {
                    sameActionList.add(activityNext);
                    // scan same action
                    for (int j = i + 2; j < activityIdList.size(); j++) {
                        ActivityEntity activityAfterNext = database.getActivityEntityHashMap().get(activityIdList.get(j));
                        if (checkSameActionCase(activity, activityAfterNext) != caseAction)
                            break;
                        //add to sameAction list
                        sameActionList.add(activityAfterNext);
                        i++;
                    }
                    jsonArray.put(parseActivityInfo(email, sameActionList, caseAction));
                } else {
                    List<ActivityEntity> actList =new ArrayList<>();
                    actList.add(activity);
                    return jsonArray.put(parseActivityInfo(email, actList));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private int checkSameActionCase(ActivityEntity act1, ActivityEntity act2) {
        if(!act1.getObject().trim().equals("")) {
            if (act1.getSubjectEmail().equals(act2.getSubjectEmail())
                    && act1.getObjectEmail().equals(act2.getObjectEmail())
                    && act1.getAction().equals(act2.getAction()))
                return 1;
            //2.same activeUser and same passiveUser
//        if (act1.getSubjectEmail().equals(act2.getSubjectEmail())
//                && act1.getObjectEmail().equals(act2.getObjectEmail()))
//            return 2;
            //3.same activeUser and same action
            if (act1.getSubjectEmail().equals(act2.getSubjectEmail())
                    && act1.getAction().equals(act2.getAction()))
                return 3;
            //4.same action and same passiveUser
            if (act1.getAction().equals(act2.getAction())
                    && act1.getObjectEmail().equals(act2.getObjectEmail()))
                return 4;
        }
        return 0;
    }

    private JSONObject parseActivityInfo(String email, List<ActivityEntity> actList) {
        //JSONObject Structure
        // {activeUsers:[{email:"xx@xx", name:"xx"},{email:"xx@xx", name:"xx"}] ,
        // activity:"",passiveUser:[{email:"yy@xx", name:"yy"},{email:"yy@xx", name:"yy"}], time: 12445}
        ActivityEntity act = actList.get(0);
        JSONObject result = new JSONObject();
        //get Active User info
        JSONObject activeUserJSONObject = new JSONObject();
        UserEntity activeUser = database.getUserEntityHashMap().get(act.getSubjectEmail());
        if (activeUser != null) {
            activeUserJSONObject.put("email", activeUser.getEmail());
            activeUserJSONObject.put("name", activeUser.getEmail().equals(email) ? "You" : activeUser.getUserName());
        } else {
            activeUserJSONObject.put("email", "unknown@marqet.com");
            activeUserJSONObject.put("name", "Unknown User");
        }
        //get action info
        JSONObject action = getAction(actList);
        //get Passive User info
        JSONObject passiveUserJSONObject = new JSONObject();
        if (act.getObjectEmail().equals("")) {
            passiveUserJSONObject.put("email", "");
            passiveUserJSONObject.put("name", "");
        } else {
            UserEntity passiveUser = database.getUserEntityHashMap().get(act.getObjectEmail());
            if (activeUser != null) {
                passiveUserJSONObject.put("email", passiveUser.getEmail());
                passiveUserJSONObject.put("name", passiveUser.getEmail().equals(email) ? "You" : passiveUser.getUserName());
            } else {
                passiveUserJSONObject.put("email", "unknown@marqet.com");
                passiveUserJSONObject.put("name", "Unknown User");
            }
        }
        //put result
        result.put("activeUsers", new JSONArray().put(activeUserJSONObject));
        result.put("action", action);
        result.put("passiveUsers", new JSONArray().put(passiveUserJSONObject));
        result.put("time", act.getDate());
        return result;
    }

    private JSONObject parseActivityInfo(String email, List<ActivityEntity> actList, int caseAction) {
        switch (caseAction) {
            case 1:
                return parseActivityInfo(email,actList);
            case 2:
                //return parseActivityInfoCase2(email, actList);
            case 3:
                return parseActivityInfoCase3(email, actList);
            case 4:
                return parseActivityInfoCase4(email, actList);
        }
        return new JSONObject();
    }

    private JSONObject parseActivityInfoCase3(String email, List<ActivityEntity> actList) {
        //JSONObject Structure
        // {activeUsers:[{email:"xx@xx", name:"xx"},{email:"xx@xx", name:"xx"}] ,
        // activity:"",passiveUser:[{email:"yy@xx", name:"yy"},{email:"yy@xx", name:"yy"}], time: 12445}
        ActivityEntity act = actList.get(0);
        JSONObject result = new JSONObject();
        //get Active User info
        JSONObject activeUserJSONObject = new JSONObject();
        UserEntity activeUser = database.getUserEntityHashMap().get(act.getSubjectEmail());
        if (activeUser != null) {
            activeUserJSONObject.put("email", activeUser.getEmail());
            activeUserJSONObject.put("name", activeUser.getEmail().equals(email) ? "You" : activeUser.getUserName());
        } else {
            activeUserJSONObject.put("email", "unknown@marqet.com");
            activeUserJSONObject.put("name", "Unknown User");
        }
        //get action info
        JSONObject action = getAction(actList);
        //get Passive User info
        JSONArray passiveUserJSONArray = new JSONArray();
        for (ActivityEntity a : actList) {
            JSONObject passiveUserJSONObject = new JSONObject();
            if (a.getObjectEmail().equals("")) {
                passiveUserJSONObject.put("email", "");
                passiveUserJSONObject.put("name", "");
            } else {
                UserEntity passiveUser = database.getUserEntityHashMap().get(a.getObjectEmail());
                if (passiveUser != null) {
                    passiveUserJSONObject.put("email", passiveUser.getEmail());
                    passiveUserJSONObject.put("name", passiveUser.getEmail().equals(email) ? "You" : passiveUser.getUserName());
                } else {
                    passiveUserJSONObject.put("email", "unknown@marqet.com");
                    passiveUserJSONObject.put("name", "Unknown User");
                }
            }
            passiveUserJSONArray.put(passiveUserJSONObject);
        }
        //put result
        result.put("activeUsers", new JSONArray().put(activeUserJSONObject));
        result.put("action", action);
        result.put("passiveUsers", passiveUserJSONArray);
        result.put("time", act.getDate());
        return result;
    }

    private JSONObject parseActivityInfoCase4(String email, List<ActivityEntity> actList) {
        //JSONObject Structure
        // {activeUsers:[{email:"xx@xx", name:"xx"},{email:"xx@xx", name:"xx"}] ,
        // activity:"",passiveUser:[{email:"yy@xx", name:"yy"},{email:"yy@xx", name:"yy"}], time: 12445}
        ActivityEntity act = actList.get(0);
        JSONObject result = new JSONObject();
        //get Active User info
        JSONArray activeUserJSONArray = new JSONArray();
        for (ActivityEntity a : actList) {
            JSONObject activeUserJSONObject = new JSONObject();
            UserEntity activeUser = database.getUserEntityHashMap().get(a.getSubjectEmail());
            if (activeUser != null) {
                activeUserJSONObject.put("email", activeUser.getEmail());
                activeUserJSONObject.put("name", activeUser.getEmail().equals(email) ? "You" : activeUser.getUserName());
            } else {
                activeUserJSONObject.put("email", "unknown@marqet.com");
                activeUserJSONObject.put("name", "Unknown User");
            }
            activeUserJSONArray.put(activeUserJSONObject);
        }
        //get action info
        JSONObject action = getAction(actList);
        //get Passive User info
        JSONObject passiveUserJSONObject = new JSONObject();
        if (act.getObjectEmail().equals("")) {
            passiveUserJSONObject.put("email", "");
            passiveUserJSONObject.put("name", "");
        } else {
            UserEntity passiveUser = database.getUserEntityHashMap().get(act.getObjectEmail());
            if (passiveUser != null) {
                passiveUserJSONObject.put("email", passiveUser.getEmail());
                passiveUserJSONObject.put("name", passiveUser.getEmail().equals(email) ? "You" : passiveUser.getUserName());
            } else {
                passiveUserJSONObject.put("email", "unknown@marqet.com");
                passiveUserJSONObject.put("name", "Unknown User");
            }
        }
        //put result
        result.put("activeUsers", activeUserJSONArray);
        result.put("action", action);
        result.put("passiveUsers", new JSONArray().put(passiveUserJSONObject));
        result.put("time", act.getDate());
        return result;
    }

    private JSONObject getAction(List<ActivityEntity> activityEntityList) {

        JSONObject result = new JSONObject();
        result.put("action", activityEntityList.get(0).getAction());
        switch (activityEntityList.get(0).getAction()) {
            case ActivityUtil.LABEL_GOT_POINT_ACTION:
                long point = 0;
                for (ActivityEntity act : activityEntityList) {
                    point += Long.parseLong(act.getObject());
                }
                result.put("object", point);
                break;
            case ActivityUtil.LABEL_UPLOAD_ACTION:
                result.put("object", activityEntityList.size());
                break;
            default:
                result.put("object", activityEntityList.get(0).getObject());

        }
        return result;
    }
    public JSONObject insertActivity(String action,String subEmail,String object, String objEmail){
        try {
            ActivityEntity obj = new ActivityEntity();
            obj.setAction(action);
            obj.setDate(new DateTimeUtil().getNow());
            obj.setId(IdGenerator.getActivityId());
            obj.setSubjectEmail(subEmail);
            obj.setObject(object);
            obj.setObjectEmail(objEmail);
            database.getActivityEntityHashMap().put(obj.getId(), obj);
            // put activity to : 1/ +person 2/ -person 3/ follower of +person
            //+person
            //-----check exist user
            UserEntity userActive = database.getUserEntityHashMap().get(obj.getSubjectEmail());
            if(userActive!=null){
                List<Long> activityList = database.getActivityRFEmail().get(userActive.getEmail());
                if(activityList==null)
                    activityList = new ArrayList<>();
                activityList.add(obj.getId());
                database.getActivityRFEmail().put(userActive.getEmail(),activityList);
                //--follower of +person ( just do with upload activity)
                if(obj.getAction().equals(ActivityUtil.LABEL_UPLOAD_ACTION)) {
                    List<String> followerList = database.getFollowerRF().get(userActive.getEmail());
                    if (followerList != null) {
                        for (String u : followerList) {
                            List<Long> activityFollowerList = database.getActivityRFEmail().get(u);
                            if (activityFollowerList == null)
                                activityFollowerList = new ArrayList<>();
                            activityFollowerList.add(obj.getId());
                            database.getActivityRFEmail().put(u, activityFollowerList);
                        }
                    }
                }
            }
            //-person
            //-----check exist user
            UserEntity userPassive = database.getUserEntityHashMap().get(obj.getObjectEmail());
            if(userPassive!=null){
                List<Long> activityList = database.getActivityRFEmail().get(userPassive.getEmail());
                if(activityList==null)
                    activityList = new ArrayList<>();
                activityList.add(obj.getId());
                database.getActivityRFEmail().put(userPassive.getEmail(),activityList);
            }
            if (dao.insert(obj)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
