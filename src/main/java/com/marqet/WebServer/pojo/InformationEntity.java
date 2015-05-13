package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/17/15.
 */
@Entity
@Table(name = "Information", schema = "", catalog = "marqet")
public class InformationEntity {
    private String communityRule;
    private String pointSystem;
    private String emailSupport;
    private String about;
    private long id;

    public InformationEntity() {
    }

    public InformationEntity(InformationEntity i) {
        this.communityRule = i.communityRule;
        this.pointSystem = i.pointSystem;
        this.emailSupport = i.emailSupport;
        this.about = i.about;
        this.id = i.id;
    }

    @Basic
    @Column(name = "communityRule", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getCommunityRule() {
        return communityRule;
    }

    public void setCommunityRule(String communityRule) {
        this.communityRule = communityRule;
    }

    @Basic
    @Column(name = "pointSystem", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getPointSystem() {
        return pointSystem;
    }

    public void setPointSystem(String pointSystem) {
        this.pointSystem = pointSystem;
    }

    @Basic
    @Column(name = "emailSupport", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getEmailSupport() {
        return emailSupport;
    }

    public void setEmailSupport(String emailSupport) {
        this.emailSupport = emailSupport;
    }

    @Basic
    @Column(name = "about", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

        InformationEntity that = (InformationEntity) o;

        if (id != that.id) return false;
        if (about != null ? !about.equals(that.about) : that.about != null) return false;
        if (communityRule != null ? !communityRule.equals(that.communityRule) : that.communityRule != null)
            return false;
        if (emailSupport != null ? !emailSupport.equals(that.emailSupport) : that.emailSupport != null) return false;
        if (pointSystem != null ? !pointSystem.equals(that.pointSystem) : that.pointSystem != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = communityRule != null ? communityRule.hashCode() : 0;
        result = 31 * result + (pointSystem != null ? pointSystem.hashCode() : 0);
        result = 31 * result + (emailSupport != null ? emailSupport.hashCode() : 0);
        result = 31 * result + (about != null ? about.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("communityRule",this.communityRule);
        jsonObject.put("pointSystem",this.pointSystem);
        jsonObject.put("emailSupport",this.emailSupport);
        jsonObject.put("about",this.about);
        return jsonObject;
    }
}
