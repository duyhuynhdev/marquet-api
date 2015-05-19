package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.FollowDao;
import com.marqet.WebServer.pojo.FollowEntity;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
            List<String> followers = database.getFollowerRF().get(emailView);
            List<String> followings = database.getFollowingRF().get(emailMe);
            JSONArray followerList = new JSONArray();
            if (followers == null) {
                if (TempData.isTemp) {
                    followers = TempData.tempFollow(emailView);
                    if (followers.size() > 3)
                        followings = followers.subList(0, 3);
                } else {
                    return result.put(ResponseController.CONTENT, followerList);
                }

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
            List<String> followings = database.getFollowingRF().get(emailView);
            List<String> followingsMe = database.getFollowingRF().get(emailMe);
            JSONArray followingList = new JSONArray();
            if (followings == null) {
                if (TempData.isTemp) {
                    followings = TempData.tempFollow(emailView);
                    if (followings.size() > 3)
                        followingsMe = TempData.tempFollow(emailMe).subList(0, 3);
                } else {
                    return result.put(ResponseController.CONTENT, followingList);
                }

            }
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
            List<String> followers = database.getFollowerRF().get(follow.getBeFollower());
            if (followers == null)
                followers = new ArrayList<>();
            followers.add(follow.getFollower());
            database.getFollowerRF().put(follow.getBeFollower(), followers);
            //put to followingRF
            List<String> beFollowers = database.getFollowingRF().get(follow.getFollower());
            if (beFollowers == null)
                beFollowers = new ArrayList<>();
            beFollowers.add(follow.getBeFollower());
            database.getFollowingRF().put(follow.getFollower(), beFollowers);
            if (dao.insert(follow)) {
                responseJSON = ResponseController.createSuccessJSON();
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
            }
            // insert Activity
            new ActivityController().insertActivity(ActivityUtil.LABEL_FOLLOWING_ACTION, email, "", beFollowedEmail);
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
}
