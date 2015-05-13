package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Path;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/17/15.
 */
@Entity
@Table(name = "Element", schema = "", catalog = "marqet")
public class ElementEntity {
    private String logo;
    private String defaultAvatar;
    private String bigBannerInfo;
    private String smallBannerInfo;
    private String pointLevel;
    private long id;

    public ElementEntity() {
    }

    public ElementEntity(ElementEntity e) {
        this.logo = e.logo;
        this.defaultAvatar = e.defaultAvatar;
        this.bigBannerInfo = e.bigBannerInfo;
        this.smallBannerInfo = e.smallBannerInfo;
        this.pointLevel = e.pointLevel;
        this.id = e.id;
    }

    @Basic
    @Column(name = "logo", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Basic
    @Column(name = "defaultAvatar", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getDefaultAvatar() {
        return defaultAvatar;
    }

    public void setDefaultAvatar(String defaultAvatar) {
        this.defaultAvatar = defaultAvatar;
    }

    @Basic
    @Column(name = "bigBannerInfo", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getBigBannerInfo() {
        return bigBannerInfo;
    }

    public void setBigBannerInfo(String bigBannerInfo) {
        this.bigBannerInfo = bigBannerInfo;
    }

    @Basic
    @Column(name = "smallBannerInfo", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getSmallBannerInfo() {
        return smallBannerInfo;
    }

    public void setSmallBannerInfo(String smallBannerInfo) {
        this.smallBannerInfo = smallBannerInfo;
    }

    @Basic
    @Column(name = "pointLevel", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getPointLevel() {
        return pointLevel;
    }

    public void setPointLevel(String pointLevel) {
        this.pointLevel = pointLevel;
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementEntity that = (ElementEntity) o;

        if (id != that.id) return false;
        if (bigBannerInfo != null ? !bigBannerInfo.equals(that.bigBannerInfo) : that.bigBannerInfo != null)
            return false;
        if (defaultAvatar != null ? !defaultAvatar.equals(that.defaultAvatar) : that.defaultAvatar != null)
            return false;
        if (logo != null ? !logo.equals(that.logo) : that.logo != null) return false;
        if (pointLevel != null ? !pointLevel.equals(that.pointLevel) : that.pointLevel != null) return false;
        if (smallBannerInfo != null ? !smallBannerInfo.equals(that.smallBannerInfo) : that.smallBannerInfo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = logo != null ? logo.hashCode() : 0;
        result = 31 * result + (defaultAvatar != null ? defaultAvatar.hashCode() : 0);
        result = 31 * result + (bigBannerInfo != null ? bigBannerInfo.hashCode() : 0);
        result = 31 * result + (smallBannerInfo != null ? smallBannerInfo.hashCode() : 0);
        result = 31 * result + (pointLevel != null ? pointLevel.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("defaultAvatar",this.defaultAvatar);
        jsonObject.put("bigBannerInfo",this.bigBannerInfo);
        jsonObject.put("smallBannerInfo",this.smallBannerInfo);
        jsonObject.put("logo",this.logo);
        jsonObject.put("pointLevel",this.pointLevel);
        return jsonObject;
    }
    public JSONObject toElementDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("defaultAvatar",Path.getServerAddress()+this.defaultAvatar);
        jsonObject.put("bigBannerInfo",this.bigBannerInfo);
        jsonObject.put("smallBannerInfo",this.smallBannerInfo);
        jsonObject.put("logo", Path.getServerAddress()+this.logo);
        jsonObject.put("pointLevel",this.pointLevel);
        return jsonObject;
    }
}
