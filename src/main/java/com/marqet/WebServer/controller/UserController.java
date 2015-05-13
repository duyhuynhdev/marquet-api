package com.marqet.WebServer.controller;


import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by hpduy17 on 3/17/15.
 */
public class UserController {
    private UserDao dao = new UserDao();
    private Database database = Database.getInstance();
    private UserEntity anonymous = new UserEntity();

    public UserController() {
        anonymous.setUserName("Anonymous");
        anonymous.setPassword(Database.DEFAULT_PASSWORD);
        anonymous.setEmail("no-reply@anonymous.com");
        anonymous.setTelephone("100-000-222");
        anonymous.setProfilePicture(database.getElementEntity().getDefaultAvatar());
        anonymous.setCountryCode("S");
        anonymous.setCityCode("S");
        anonymous.setJoinDate(new DateTimeUtil().getNow());
        anonymous.setPoint(0);
    }

    public JSONObject loginViaEmail(String email, String password) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Password is wrong\n");
            }
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public boolean loginViaSocialId(String socialId) {
        return database.getUserEntityHashMap().containsKey(socialId);
    }

    public JSONObject register(String username, String password, String email, String telephone, String profilePicture,
                               String countryCode, String cityCode, long joinDate) {
        JSONObject responseJSON;
        //validation
        String content = "";
        if (database.getUserEntityHashMap().containsKey(email)) {
            content += "1 - Email is registered by other people\n";
        }
        if (password.length() < 8) {
            content += "2 - Password at least 8 characters\n";
        }
        if (!content.equals("")) {
            responseJSON = ResponseController.createFailJSON(content);
            return responseJSON;
        }
        UserEntity newUser = new UserEntity();
        newUser.setUserName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setTelephone(telephone);
        newUser.setProfilePicture(profilePicture);
        newUser.setCountryCode(countryCode);
        newUser.setCityCode(cityCode);
        newUser.setJoinDate(joinDate);
        newUser.setPoint(0);
        //put into cache
        database.getUserEntityHashMap().put(newUser.getEmail(), newUser);
//
//        //put into reference
//        if (!newUser.getFacebookId().equals("")) {
//            database.getFacebookRF().put(newUser.getFacebookId(), newUser.getEmail());
//        }
//        if (!newUser.getGoogleplusId().equals("")) {
//            database.getGoogleplushRF().put(newUser.getGoogleplusId(), newUser.getEmail());
//        }
        //save into database
        if (dao.insert(newUser)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, newUser.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
        }
        return responseJSON;
    }
    public JSONObject updateUserDetail(String username, String email, String profilePicture,
                               String countryCode, String cityCode) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        user.setUserName(username);
        user.setProfilePicture(profilePicture);
        user.setCountryCode(countryCode);
        user.setCityCode(cityCode);
        //put into cache
        database.getUserEntityHashMap().put(user.getEmail(), user);
        if (dao.update(user)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
        }
        return responseJSON;
    }

    public JSONObject register(String username, String password, String email, String telephone, Part imagePart,
                               String countryCode, String cityCode, long joinDate) throws IOException {
        JSONObject responseJSON;
        //validation
        String content = "";
        if (database.getUserEntityHashMap().containsKey(email)) {
            content += "1 - Email is registered by other people\n";
        }
        if (password.length() < 8) {
            content += "2 - Password at least 8 characters\n";
        }
        if (!content.equals("")) {
            responseJSON = ResponseController.createFailJSON(content);
            return responseJSON;
        }
        UserEntity newUser = new UserEntity();
        newUser.setUserName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setTelephone(telephone);
        String fileName = email.split("@")[0] + new DateTimeUtil().getNow();
        String profilePicturePath = Database.getInstance().getElementEntity().getDefaultAvatar();
        if(imagePart != null&&imagePart.getSize()>0){
            profilePicturePath = new UploadImageUtil().upload(fileName, Path.getUsersPath(), imagePart);
            new UploadImageUtil().uploadThumbnail(fileName, imagePart);
        }
        newUser.setProfilePicture(profilePicturePath);
        newUser.setCountryCode(countryCode);
        newUser.setCityCode(cityCode);
//        newUser.setFacebookId(facebookId);
//        newUser.setGoogleplusId(googlePlusId);
        newUser.setJoinDate(joinDate);
        newUser.setPoint(0);
        //put into cache
        database.getUserEntityHashMap().put(newUser.getEmail(), newUser);
//        //put into reference
//        if (!newUser.getFacebookId().equals("")) {
//            database.getFacebookRF().put(newUser.getFacebookId(), newUser.getEmail());
//        }
//        if (!newUser.getGoogleplusId().equals("")) {
//            database.getGoogleplushRF().put(newUser.getGoogleplusId(), newUser.getEmail());
//        }
        //save into database
        if (dao.insert(newUser)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, newUser.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
        }
        return responseJSON;
    }

    public JSONObject verifyViaFacebook(String email, String facebookId) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
//            user.setFacebookId(facebookId);
            database.getUserEntityHashMap().put(email, user);
//            database.getFacebookRF().put(user.getFacebookId(), user.getEmail());
            if (dao.update(user)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
            }

        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject verifyViaGooglePlus(String email, String googleplusId) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
//            user.setGoogleplusId(googleplusId);
            database.getUserEntityHashMap().put(email, user);
//            database.getGoogleplushRF().put(user.getGoogleplusId(), user.getEmail());
            if (dao.update(user)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
            }
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject changeCountry(String email, String newCountryCode) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            user.setCityCode(newCountryCode);
            database.getUserEntityHashMap().put(email, user);
            if (dao.update(user)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
            }
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject changePassword(String email, String oldPassword, String newPassword) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            if (!oldPassword.equals(Database.DEFAULT_PASSWORD) && oldPassword.equals(user.getPassword())) {
                user.setPassword(newPassword);
                database.getUserEntityHashMap().put(email, user);
                if (dao.update(user)) {
                    responseJSON = ResponseController.createSuccessJSON();
                    responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
                } else {
                    responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
                }
            } else {
                responseJSON = ResponseController.createFailJSON("Current password is wrong\n");
            }
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject addPoint(String email, long points) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            long newPoint = user.getPoint() + points;
            user.setPoint(newPoint);
            database.getUserEntityHashMap().put(email, user);
            if (dao.update(user)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
            }
            // insert Activity
            new ActivityController().insertActivity(ActivityUtil.LABEL_GOT_POINT_ACTION, email, ""+points, "");
            responseJSON = ResponseController.createSuccessJSON();
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject gotPoint(String email, long points, String giveEmail) {
        JSONObject responseJSON;
        UserEntity giveUser = database.getUserEntityHashMap().get(giveEmail);
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (giveUser == null || user == null) {
            return ResponseController.createFailJSON("User not found\n");
        }
        if(giveUser.getPoint()<points)
            return ResponseController.createFailJSON("You are not enough point\n");
        long newPoint = user.getPoint() + points;
        long minusPoint = giveUser.getPoint() - points;
        user.setPoint(newPoint);
        giveUser.setPoint(minusPoint);
        database.getUserEntityHashMap().put(email, user);
        database.getUserEntityHashMap().put(giveEmail, giveUser);
        if (dao.update(user) && dao.update(giveUser)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
        }
        // insert Activity
        new ActivityController().insertActivity(ActivityUtil.LABEL_GOT_POINT_ACTION, email,""+points, giveEmail);
        return ResponseController.createSuccessJSON();
    }

    public JSONObject findFriendsViaFacebook(JSONArray facebookFriendList) {
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        JSONArray friendList = new JSONArray();
        for (int i = 0; i < facebookFriendList.length(); i++) {
            if (database.getFacebookRF().keySet().contains(facebookFriendList.getString(i))) {
                String friendEmail = database.getFacebookRF().get(facebookFriendList.getString(i));
                friendList.put(database.getUserEntityHashMap().get(friendEmail).toSortDetailJSON());
            }
        }
        responseJSON.put(ResponseController.CONTENT, friendList);
        return responseJSON;
    }

    public JSONObject findFriendsViaGoogle(JSONArray googleFriendList) {
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        JSONArray friendList = new JSONArray();
        for (int i = 0; i < googleFriendList.length(); i++) {
            if (database.getGoogleplushRF().keySet().contains(googleFriendList.getString(i))) {
                String friendEmail = database.getGoogleplushRF().get(googleFriendList.getString(i));
                friendList.put(database.getUserEntityHashMap().get(friendEmail).toSortDetailJSON());
            }
        }
        responseJSON.put(ResponseController.CONTENT, friendList);
        return responseJSON;
    }
    public JSONObject getUserDetail(String email){
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        UserEntity user = database.getUserEntityHashMap().get(email);
        if(user==null)
            user = anonymous;
        responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
        return responseJSON;
    }

}
