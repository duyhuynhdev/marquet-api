package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.Path;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "User", schema = "", catalog = "marqet")
public class UserEntity {
    private String userName;
    private String password;
    private String email;
    private String telephone;
    private long joinDate;
    private String profilePicture;
    private long point;
    private String countryCode;
    private String cityCode;
    private String broadcastId;
    private String stateCode;

    public UserEntity() {
    }

    public UserEntity(UserEntity u) {
        this.userName = u.userName;
        this.password = u.password;
        this.email = u.email;
        this.telephone = u.telephone;
        this.joinDate = u.joinDate;
        this.profilePicture = u.profilePicture;
        this.point = u.point;
        this.countryCode = u.countryCode;
        this.cityCode = u.cityCode;
        this.broadcastId = u.broadcastId;
        this.stateCode = u.stateCode;
    }

    @Basic
    @Column(name = "joinDate", nullable = false, insertable = true, updatable = true)
    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    @Basic
    @Column(name = "userName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "telephone", nullable = false, insertable = true, updatable = true, length = 20)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "profilePicture", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getProfilePicture() {
        if (profilePicture.contains(".com") || profilePicture.contains("http") || profilePicture.contains("www") || profilePicture.contains("//"))
            return profilePicture;
        return Path.getServerAddress() + (profilePicture.equals("") ? Database.getInstance().getElementEntity().getDefaultAvatar() : profilePicture);
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Basic
    @Column(name = "point", nullable = false, insertable = true, updatable = true)
    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    @Basic
    @Column(name = "broadcastId", nullable = true, insertable = true, updatable = true, length = 100)
    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }


    @Basic
    @Column(name = "countryCode", nullable = false, insertable = true, updatable = true, length = 10)
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "cityCode", nullable = false, insertable = true, updatable = true, length = 10)
    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Basic
    @Column(name = "stateCode", nullable = false, insertable = true, updatable = true, length = 10)
    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (point != that.point) return false;
        if (cityCode != null ? !cityCode.equals(that.cityCode) : that.cityCode != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (stateCode != null ? !stateCode.equals(that.stateCode) : that.stateCode != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (broadcastId != null ? !broadcastId.equals(that.broadcastId) : that.broadcastId != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (profilePicture != null ? !profilePicture.equals(that.profilePicture) : that.profilePicture != null)
            return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (joinDate != that.joinDate) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (profilePicture != null ? profilePicture.hashCode() : 0);
        result = 31 * result + (int) (point ^ (point >>> 32));
        result = 31 * result + (broadcastId != null ? broadcastId.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (stateCode != null ? stateCode.hashCode() : 0);
        result = 31 * result + (int) (joinDate ^ (point >>> 32));
        return result;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", this.userName);
        jsonObject.put("password", this.password);
        jsonObject.put("email", this.email);
        jsonObject.put("telephone", this.telephone);
        jsonObject.put("profilePicture", this.profilePicture);
        jsonObject.put("point", this.point);
        jsonObject.put("broadcastId", this.broadcastId);
        jsonObject.put("countryCode", this.countryCode);
        jsonObject.put("cityCode", this.cityCode);
        jsonObject.put("stateCode", this.stateCode);
        jsonObject.put("joinDate", this.joinDate);
        return jsonObject;
    }

    public JSONObject toUserDetailJSON() {
        Database database = Database.getInstance();
        int numPossitive, numNeutron, numNegative, postCount, followerCount, followingCount;
        numPossitive = numNeutron = numNegative = postCount = followerCount = followingCount = 0;
        //get post count
        if (database.getProductRFbyEmail().get(this.email) != null)
            postCount = database.getProductRFbyEmail().get(this.email).size();
        //get num follower
        if (database.getFollowerRF().get(this.email) != null)
            followerCount = database.getFollowerRF().get(this.email).size();
        //get num following
        if (database.getFollowingRF().get(this.email) != null)
            followingCount = database.getFollowingRF().get(this.email).size();
        List<Long> feedbackIds = database.getSellerFeedbackRFEmail().get(this.email);
        JSONArray feedbackArray = new JSONArray();
        if (feedbackIds != null) {
            for (long id : feedbackIds) {
                FeedbackEntity feedback = database.getFeedbackEntityHashMap().get(id);
                if (feedback != null) {
                    switch (feedback.getStatus()) {
                        case 1:
                            numPossitive++;
                            break;
                        case 2:
                            numNeutron++;
                            break;
                        case 3:
                            numNegative++;
                            break;
                    }
                }
            }
        }
        feedbackArray.put(numPossitive).put(numNeutron).put(numNegative);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", this.userName);
        jsonObject.put("email", this.email);
        jsonObject.put("telephone", this.telephone);
        jsonObject.put("profilePicture", getProfilePicture());
        jsonObject.put("point", this.point);
        String verify = "email";
        if (this.email.contains("facebook_"))
            verify = "facebook";
        else if (this.email.contains("google_"))
            verify = "google";
        jsonObject.put("isVerify", verify);
        jsonObject.put("country", database.getCountryEntityHashMap().get(this.countryCode).getName());
        jsonObject.put("countryCode", this.countryCode);
        jsonObject.put("city", database.getCityEntityHashMap().get(this.cityCode).getName());
        jsonObject.put("cityCode", this.cityCode);
        jsonObject.put("joinDate", this.joinDate);
        jsonObject.put("postCount", postCount);
        jsonObject.put("followerCount", followerCount);
        jsonObject.put("followingCount", followingCount);
        jsonObject.put("feedback",feedbackArray);
        return jsonObject;
    }

    public JSONObject toSortDetailJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", this.userName);
        jsonObject.put("email", this.email);
        jsonObject.put("profilePicture", getProfilePicture());
        jsonObject.put("joinDate", this.joinDate);
        return jsonObject;
    }
}
