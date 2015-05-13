package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
@Entity
@Table(name = "Follow", schema = "", catalog = "marqet")
public class FollowEntity {
    private long id;
    private String follower;
    private String beFollower;
    private long time;

    public FollowEntity() {
    }

    public FollowEntity(FollowEntity f) {
        this.id = f.id;
        this.follower = f.follower;
        this.beFollower = f.beFollower;
        this.time = f.time;
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "follower", nullable = false, insertable = true, updatable = true, length = 100)
    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    @Basic
    @Column(name = "beFollower", nullable = false, insertable = true, updatable = true, length = 100)
    public String getBeFollower() {
        return beFollower;
    }

    public void setBeFollower(String beFollower) {
        this.beFollower = beFollower;
    }

    @Basic
    @Column(name = "time", nullable = false, insertable = true, updatable = true)
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FollowEntity that = (FollowEntity) o;

        if (id != that.id) return false;
        if (time != that.time) return false;
        if (beFollower != null ? !beFollower.equals(that.beFollower) : that.beFollower != null) return false;
        if (follower != null ? !follower.equals(that.follower) : that.follower != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (follower != null ? follower.hashCode() : 0);
        result = 31 * result + (beFollower != null ? beFollower.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("follower",this.follower);
        jsonObject.put("beFollower",this.beFollower);
        jsonObject.put("time",this.time);
        return jsonObject;
    }
}
