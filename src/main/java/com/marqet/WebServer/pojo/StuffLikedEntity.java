package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
@Entity
@Table(name = "StuffLiked", schema = "", catalog = "marqet")
public class StuffLikedEntity {
    private long id;
    private String buyerEmail;
    private long productId;

    public StuffLikedEntity(StuffLikedEntity s) {
        this.id = s.id;
        this.buyerEmail = s.buyerEmail;
        this.productId = s.productId;
    }

    public StuffLikedEntity() {
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
    @Column(name = "buyerEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
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

        StuffLikedEntity that = (StuffLikedEntity) o;

        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (buyerEmail != null ? !buyerEmail.equals(that.buyerEmail) : that.buyerEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (buyerEmail != null ? buyerEmail.hashCode() : 0);
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("buyerEmail",this.buyerEmail);
        jsonObject.put("productId",this.productId);
        return jsonObject;
    }
}
