package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.FollowDao;
import com.marqet.WebServer.pojo.FollowEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/26/15.
 */
public class FollowController {
    private FollowDao dao = new FollowDao();
    private Database database = Database.getInstance();

    public JSONObject getListFollower(String emailMe, String emailView, int startIdx, int numUser) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<String> followers = new ArrayList<>();
            List<String> followings = new ArrayList<>();
            try{
                followers = new ArrayList<>(database.getFollowerRF().get(emailView));
                followings = new ArrayList<>(database.getFollowingRF().get(emailMe));
            }catch (Exception ignored){}
            JSONArray followerList = new JSONArray();
            if (followers == null) {
                return result.put(ResponseController.CONTENT, followerList);
            }
            int endIdx = startIdx + numUser;
            if (endIdx > followers.size() - 1)
                endIdx = followers.size();
            List<String> subList = followers.subList(startIdx, endIdx);
            for (String f : subList) {
                UserEntity follower = database.getUserEntityHashMap().get(f);
                if (follower != null) {
                    boolean isFollow = false;
                    if (followings != null && followings.contains(follower.getEmail())) {
                        isFollow = true;
                    }
                    FollowEntity fInfo = database.getFollowEntityList().get(
                            database.getFollowMapIdRF().get(emailView + "#" + f));
                    JSONObject jsonObject = follower.toSortDetailJSON();
                    if (fInfo != null)
                        jsonObject.put("time", fInfo.getTime());
                    else
                        jsonObject.put("time", new DateTimeUtil().getNow());
                    jsonObject.put("isFollowing", isFollow);
                    followerList.put(jsonObject);
                }
            }
            result.put(ResponseController.CONTENT, followerList);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListFollowing(String emailMe, String emailView, int startIdx, int numUser) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            List<String> followings = new ArrayList<>();
            List<String> followingsMe = new ArrayList<>();
            try{
                followingsMe = new ArrayList<>(database.getFollowingRF().get(emailMe));
                followings = new ArrayList<>(database.getFollowingRF().get(emailView));
            }catch (Exception ignored){}
            JSONArray followingList = new JSONArray();
            int endIdx = startIdx + numUser;
            if (endIdx > followings.size() - 1)
                endIdx = followings.size();
            List<String> subList = followings.subList(startIdx, endIdx);
            for (String f : subList) {
                UserEntity following = database.getUserEntityHashMap().get(f);
                if (following != null) {
                    boolean isFollow = false;
                    if (followingsMe != null && followingsMe.contains(following.getEmail())) {
                        isFollow = true;
                    }
                    FollowEntity fInfo = database.getFollowEntityList().get(
                            database.getFollowMapIdRF().get(emailView + "#" + f));
                    JSONObject jsonObject = following.toSortDetailJSON();
                    if (fInfo != null)
                        jsonObject.put("time", fInfo.getTime());
                    else
                        jsonObject.put("time", new DateTimeUtil().getNow());
                    jsonObject.put("isFollowing", isFollow);
                    followingList.put(jsonObject);
                }
            }
            result.put(ResponseController.CONTENT, followingList);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject follow(String email, String beFollowedEmail) {
        try {
            JSONObject responseJSON;
            FollowEntity follow = new FollowEntity();
            follow.setId(IdGenerator.getFollowId());
            follow.setFollower(email);
            follow.setBeFollower(beFollowedEmail);
            follow.setTime(new DateTimeUtil().getNow());
            database.getFollowEntityList().put(follow.getId(), follow);
            //put to followMapIdRF;
            database.getFollowMapIdRF().put(follow.getFollower() + "#" + follow.getBeFollower(), follow.getId());
            //put to followerRF
            HashSet<String> followers = database.getFollowerRF().get(follow.getBeFollower());
            if (followers == null)
                followers = new HashSet<>();
            followers.add(follow.getFollower());
            database.getFollowerRF().put(follow.getBeFollower(), followers);
            //put to followingRF
            HashSet<String> beFollowers = database.getFollowingRF().get(follow.getFollower());
            if (beFollowers == null)
                beFollowers = new HashSet<>();
            beFollowers.add(follow.getBeFollower());
            database.getFollowingRF().put(follow.getFollower(), beFollowers);
            if (dao.insert(follow)) {
                responseJSON = ResponseController.createSuccessJSON();
                new ActivityController().insertActivity(email, beFollowedEmail, ActivityUtil.FOLLOWING, follow.getId());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
            }
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject unfollow(String email, String beFollowedEmail) {
        try {
            long followId = database.getFollowMapIdRF().get(email + "#" + beFollowedEmail);
            JSONObject responseJSON;
            if (database.getFollowEntityList().get(followId) == null) {
                return ResponseController.createSuccessJSON();
            }
            FollowEntity follow = new FollowEntity(database.getFollowEntityList().get(followId));
            database.getFollowEntityList().remove(followId);
            //rm to followMapIdRF;
            database.getFollowMapIdRF().remove(email + "#" + beFollowedEmail);
            //rm to followerRF
            HashSet<String> followers = database.getFollowerRF().get(follow.getBeFollower());
            if (followers != null) {
                followers.remove(follow.getFollower());
                database.getFollowerRF().put(follow.getBeFollower(), followers);
            }
            //rm to followingRF
            HashSet<String> beFollowers = database.getFollowingRF().get(follow.getFollower());
            if (beFollowers != null) {
                beFollowers.remove(follow.getBeFollower());
                database.getFollowingRF().put(follow.getFollower(), beFollowers);
            }
            if (dao.delete(follow)) {
                responseJSON = ResponseController.createSuccessJSON();
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot delete from database\n");
            }
            // insert Activity
            //new ActivityController().insertActivity(ActivityUtil.LABEL_FOLLOWING_ACTION, email, "", beFollowedEmail);
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
