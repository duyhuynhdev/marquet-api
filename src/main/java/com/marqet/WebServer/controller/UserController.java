package com.marqet.WebServer.controller;


import com.marqet.WebServer.dao.UserDao;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/17/15.
 */
public class UserController {
    private UserDao dao = new UserDao();
    private Database database = Database.getInstance();
    public UserEntity anonymous = new UserEntity();

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
        newUser.setStateCode(cityCode);
        newUser.setJoinDate(joinDate);
        newUser.setGender(0);
        newUser.setWebsite("marqet.com/");
        newUser.setBio("marqet.com/");
        newUser.setFirstname("first name");
        newUser.setLastname("last name");
        newUser.setBirthday(0);
        newUser.setPoint(20000);
        newUser.setBroadcastId("");
        //put social id
        String prefix = email.split("_")[0];
        String id = "";
        try {
            id = email.split("_")[1];
        } catch (Exception ignored) {
        }
        switch (prefix) {
            case "facebook":
                newUser.setFacebookId(id);
                newUser.setGoogleId("");
                newUser.setTwitterId("");
                break;
            case "google":
                newUser.setGoogleId(id);
                newUser.setFacebookId("");
                newUser.setTwitterId("");
                break;
            case "twitter":
                newUser.setTwitterId(id);
                newUser.setFacebookId("");
                newUser.setGoogleId("");
                break;
        }
        //put into cache
        database.getUserEntityHashMap().put(newUser.getEmail(), newUser);

        //put into reference
        if (newUser.getFacebookId() != null && !newUser.getFacebookId().replaceAll(" ", "").equals("")) {
            database.getFacebookRF().put(newUser.getFacebookId(), newUser.getEmail());
        }
        if (newUser.getGoogleId() != null && !newUser.getGoogleId().replaceAll(" ", "").equals("")) {
            database.getGoogleplushRF().put(newUser.getGoogleId(), newUser.getEmail());
        }
        if (newUser.getTwitterId() != null && !newUser.getTwitterId().replaceAll(" ", "").equals("")) {
            database.getTwitterRF().put(newUser.getTwitterId(), newUser.getEmail());
        }
        //save into database
        if (dao.insert(newUser)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, newUser.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
        }
        return responseJSON;
    }

    public JSONObject updateUserDetail(String username, String email,
                                       String countryCode, String cityCode) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        user.setUserName(username);
        user.setCountryCode(countryCode);
        if (user.getCityCode() == null) {
            user.setCityCode(cityCode);
        }
        if (user.getPassword() == null) {
            user.setPassword(Database.DEFAULT_PASSWORD);
        }
        if (user.getTelephone() == null) {
            user.setTelephone("");
        }
        if (user.getProfilePicture() == null) {
            user.setProfilePicture("");
        }
        if (user.getStateCode() == null) {
            user.setStateCode(cityCode);
        }
        if (user.getJoinDate() == 0) {
            user.setJoinDate(new DateTimeUtil().getNow());
        }
        if (user.getGender() == -1) {
            user.setGender(0);
        }
        if (user.getWebsite() == null)
            user.setWebsite("marqet.com/");
        if (user.getBio() == null)
            user.setBio("marqet.com/");
        if (user.getFirstname() == null)
            user.setFirstname("first name");
        if (user.getLastname() == null)
            user.setLastname("last name");
        if (user.getBroadcastId() == null)
            user.setBroadcastId("");
        if (user.getFacebookId() == null)
            user.setFacebookId("");
        if (user.getGoogleId() == null)
            user.setGoogleId("");
        if (user.getTwitterId() == null)
            user.setTwitterId("");
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
        if (imagePart != null && imagePart.getSize() > 0) {
            profilePicturePath = new UploadImageUtil().upload(fileName, Path.getUsersPath(), imagePart);
            new UploadImageUtil().uploadThumbnail(fileName, imagePart);
        }
        newUser.setProfilePicture(profilePicturePath);
        newUser.setCountryCode(countryCode);
        newUser.setCityCode(cityCode);
        newUser.setStateCode(cityCode);
//        newUser.setFacebookId(facebookId);
//        newUser.setGoogleplusId(googlePlusId);
        newUser.setJoinDate(joinDate);
        newUser.setGender(0);
        newUser.setWebsite("marqet.com/");
        newUser.setBio("marqet.com/");
        newUser.setFirstname("first name");
        newUser.setLastname("last name");
        newUser.setBirthday(0);
        newUser.setPoint(20000);
        newUser.setBroadcastId("");
        newUser.setFacebookId("");
        newUser.setGoogleId("");
        newUser.setTwitterId("");
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

    public JSONObject changeCountry(String email, String newCountryCode, String newCityCode) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            user.setCountryCode(newCountryCode);
            user.setCityCode(newCityCode);
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
            // TODO insert Activity
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject getPoint(String email) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            database.getUserEntityHashMap().put(email, user);
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, user.getPoint());
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
        if (giveUser.getPoint() < points)
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
        // TODO insert Activity
        return ResponseController.createSuccessJSON();
    }

    public JSONObject findFriendsViaFacebook(String email, String yourSocialId, JSONArray facebookFriendList, int startIdx, int numUser) {
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user == null) {
            return ResponseController.createFailJSON("User cannot found");
        }
        if (user.getFacebookId().replaceAll(" ", "").equals("")) {
            user.setFacebookId(yourSocialId);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            try {
                new UserDao().update(user);
            } catch (Exception ignored) {

            }
        }
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        JSONArray friendList = new JSONArray();
        List<String> listUser = new ArrayList<>();
        for (int i = 0; i < facebookFriendList.length(); i++) {
            if (database.getFacebookRF().keySet().contains(facebookFriendList.getString(i))) {
                String friendEmail = database.getFacebookRF().get(facebookFriendList.getString(i));
                listUser.add(friendEmail);
            }
        }
        int endIdx = startIdx + numUser;
        if (endIdx >= listUser.size()) {
            endIdx = listUser.size();
        }
        List<String> subList = listUser.subList(startIdx, endIdx);
        for (String friendEmail : subList) {
            friendList.put(database.getUserEntityHashMap().get(friendEmail).toSortFriendDetailJSON(email));
        }
        responseJSON.put(ResponseController.CONTENT, friendList);
        return responseJSON;
    }

    public JSONObject findFriendsViaGoogle(String email, String yourSocialId, JSONArray googleFriendList, int startIdx, int numUser) {
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user == null) {
            return ResponseController.createFailJSON("User cannot found");
        }
        if (user.getGoogleId().replaceAll(" ", "").equals("")) {
            user.setGoogleId(yourSocialId);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            try {
                new UserDao().update(user);
            } catch (Exception ignored) {

            }
        }
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        JSONArray friendList = new JSONArray();
        List<String> listUser = new ArrayList<>();
        for (int i = 0; i < googleFriendList.length(); i++) {
            if (database.getGoogleplushRF().keySet().contains(googleFriendList.getString(i))) {
                String friendEmail = database.getGoogleplushRF().get(googleFriendList.getString(i));
                listUser.add(friendEmail);
            }
        }
        int endIdx = startIdx + numUser;
        if (endIdx >= listUser.size()) {
            endIdx = listUser.size();
        }
        List<String> subList = listUser.subList(startIdx, endIdx);
        for (String friendEmail : subList) {
            friendList.put(database.getUserEntityHashMap().get(friendEmail).toSortFriendDetailJSON(email));
        }
        responseJSON.put(ResponseController.CONTENT, friendList);
        return responseJSON;
    }

    public JSONObject findFriendsViaTwitter(String email, String yourSocialId, JSONArray twitterFriendList, int startIdx, int numUser) {
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user == null) {
            return ResponseController.createFailJSON("User cannot found");
        }
        if (user.getTwitterId().replaceAll(" ", "").equals("")) {
            user.setTwitterId(yourSocialId);
            database.getUserEntityHashMap().put(user.getEmail(), user);
            try {
                new UserDao().update(user);
            } catch (Exception ignored) {

            }
        }
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        JSONArray friendList = new JSONArray();
        List<String> listUser = new ArrayList<>();
        for (int i = 0; i < twitterFriendList.length(); i++) {
            if (database.getTwitterRF().keySet().contains(twitterFriendList.getString(i))) {
                String friendEmail = database.getTwitterRF().get(twitterFriendList.getString(i));
                listUser.add(friendEmail);
            }
        }
        int endIdx = startIdx + numUser;
        if (endIdx >= listUser.size()) {
            endIdx = listUser.size();
        }
        List<String> subList = listUser.subList(startIdx, endIdx);
        for (String friendEmail : subList) {
            friendList.put(database.getUserEntityHashMap().get(friendEmail).toSortFriendDetailJSON(email));
        }
        responseJSON.put(ResponseController.CONTENT, friendList);
        return responseJSON;
    }

    public JSONObject getUserDetail(String email, String emailUser) {
        JSONObject responseJSON = ResponseController.createSuccessJSON();
        UserEntity user = database.getUserEntityHashMap().get(emailUser);
        if (user == null)
            user = anonymous;
        responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON(email));
        return responseJSON;
    }

    public JSONObject forgetPassword(String email) {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user != null) {
            try {
                new SendMail(user.getEmail(), "Your MarQet Password", "<p>Have a guys require get back your password, if this is you, please view your password below.</p><p>Password:<b>" + user.getPassword() + "</b></p>");
                responseJSON = ResponseController.createSuccessJSON().put(ResponseController.CONTENT, "Your password have sent to your mail (maybe in your Spam)");
            } catch (Exception ex) {
                responseJSON = ResponseController.createErrorJSON(ex.getMessage());
            }
        } else {
            responseJSON = ResponseController.createFailJSON("User not found\n");
        }
        return responseJSON;
    }

    public JSONObject editProfile(String username, String email, String telephone, Part imagePart, String countryCode,
                                  String cityCode, int gender, long birthday, String website, String bio, String lastname, String firstname) throws IOException {
        JSONObject responseJSON;
        UserEntity user = database.getUserEntityHashMap().get(email);
        if (user == null)
            return ResponseController.createFailJSON("User cannot found");
        user.setUserName(username);
        user.setTelephone(telephone);
        user.setGender(gender);
        user.setBio(bio);
        user.setBirthday(birthday);
        user.setWebsite(website);
        String fileName = email.split("@")[0] + new DateTimeUtil().getNow();
        if (imagePart != null && imagePart.getSize() > 0) {
            String profilePicturePath = new UploadImageUtil().upload(fileName, Path.getUsersPath(), imagePart);
            new UploadImageUtil().uploadThumbnail(fileName, imagePart);
            user.setProfilePicture(profilePicturePath);
        }
        user.setCountryCode(countryCode);
        user.setCityCode(cityCode);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        if (user.getBroadcastId() == null)
            user.setBroadcastId("");
        if (user.getFacebookId() == null)
            user.setFacebookId("");
        if (user.getGoogleId() == null)
            user.setGoogleId("");
        if (user.getTwitterId() == null)
            user.setTwitterId("");
        //put into cache
        database.getUserEntityHashMap().put(user.getEmail(), user);
        if (dao.update(user)) {
            responseJSON = ResponseController.createSuccessJSON();
            responseJSON.put(ResponseController.CONTENT, user.toUserDetailJSON());
        } else {
            responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
        }
        return responseJSON;
    }

    public JSONObject ProcessBroardcast(String email, String deviceId, String broadcast, int type) {
        try {
            UserEntity user = database.getUserEntityHashMap().get(email);
            if (user == null) {
                return ResponseController.createFailJSON("User cannot found");
            }
            HashMap<String, String> broadcastMap = database.getBroadcastLogRFEmail().get(email);
            if (broadcastMap == null) {
                broadcastMap = new HashMap<>();
            }
            switch (type) {
                //add or update
                case 1:
                    broadcastMap.put(deviceId, broadcast);
                    break;
                //remove
                case 2:
                    broadcastMap.remove(deviceId);
                    break;
            }
            user.setBroadcastId(convertHashMapBroadcastToArray(broadcastMap).toString());
            if (dao.update(user)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, user.toUserDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot update to database\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    private JSONArray convertHashMapBroadcastToArray(HashMap<String, String> map) {
        JSONArray jsonArray = new JSONArray();
        for (String deviceId : map.keySet()) {
            jsonArray.put(deviceId + "#" + map.get(deviceId));
        }
        return jsonArray;
    }

    public UserEntity getAnonymous() {
        return anonymous;
    }


}
