package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
@Entity
@Table(name = "Offer", schema = "", catalog = "marqet")
public class OfferEntity {
    private long id;
    private String buyerEmail;
    private long productId;
    private double offerPrice;
    private int status;
    public OfferEntity() {
    }

    public OfferEntity(OfferEntity o) {
        this.id = o.id;
        this.buyerEmail = o.buyerEmail;
        this.productId = o.productId;
        this.offerPrice = o.offerPrice;
        this.status = o.status;
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

    @Basic
    @Column(name = "offerPrice", nullable = false, insertable = true, updatable = true)
    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferEntity that = (OfferEntity) o;

        if (id != that.id) return false;
        if (offerPrice != that.offerPrice) return false;
        if (productId != that.productId) return false;
        if (buyerEmail != null ? !buyerEmail.equals(that.buyerEmail) : that.buyerEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long temp;
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (buyerEmail != null ? buyerEmail.hashCode() : 0);
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        ;
        temp = Double.doubleToLongBits(offerPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("buyerEmail", this.buyerEmail);
        jsonObject.put("productId", this.productId);
        jsonObject.put("offerPrice", this.offerPrice);
        return jsonObject;
    }
}
