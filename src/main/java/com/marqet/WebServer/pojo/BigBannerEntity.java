package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Path;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "BigBanner", schema = "", catalog = "marqet")
public class BigBannerEntity {
    private long id;
    private String coverImg;
    private String status;
    private long setTime;
    private String email;
    private long productId;

    public BigBannerEntity() {
    }

    public BigBannerEntity(BigBannerEntity b) {
        this.id = b.id;
        this.coverImg = b.coverImg;
        this.status = b.status;
        this.setTime = b.setTime;
        this.email = b.email;
        this.productId = b.productId;
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
    @Column(name = "coverImg", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "setTime", nullable = false, insertable = true, updatable = true)
    public long getSetTime() {
        return setTime;
    }

    public void setSetTime(long setTime) {
        this.setTime = setTime;
    }

    @Basic
    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Basic
    @Column(name = "productId", nullable = false, insertable = true, updatable = true)
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigBannerEntity that = (BigBannerEntity) o;

        if (id != that.id) return false;
        if (setTime != that.setTime) return false;
        if (coverImg != null ? !coverImg.equals(that.coverImg) : that.coverImg != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (productId != that.productId) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (coverImg != null ? coverImg.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (setTime ^ (setTime >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        return result;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("coverImg", this.coverImg);
        jsonObject.put("status",this.status);
        jsonObject.put("setTime",this.setTime);
        jsonObject.put("email",this.email);
        jsonObject.put("productId",this.productId);
        return jsonObject;
    }
    public JSONObject toDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coverImg", Path.getServerAddress()+(this.coverImg.equals("") ? Path.getDefaultBigBannerImagePath(): this.coverImg));
        jsonObject.put("productId",this.productId);
        return jsonObject;
    }
    public static JSONObject toDefaultDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coverImg", Path.getServerAddress()+Path.getDefaultBigBannerImagePath());
        jsonObject.put("productId",0);
        return jsonObject;
    }


}
