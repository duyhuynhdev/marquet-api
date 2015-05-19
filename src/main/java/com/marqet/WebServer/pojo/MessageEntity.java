package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Message", schema = "", catalog = "marqet")
public class MessageEntity {
    private long id;
    private String content;
    private long date;
    private int isArchive;
    private int status;
    private String senderEmail;
    private String receiverEmail;
    private long productId;
    private long offerId = 0;

    public MessageEntity() {
    }

    public MessageEntity(MessageEntity m) {
        this.id = m.id;
        this.content = m.content;
        this.date = m.date;
        this.isArchive = m.isArchive;
        this.status = m.status;
        this.senderEmail = m.senderEmail;
        this.receiverEmail = m.receiverEmail;
        this.productId = m.productId;
        this.offerId = m.offerId;
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
    @Column(name = "productId", nullable = false, insertable = true, updatable = true)
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
    @Basic
    @Column(name = "offerId", nullable = false, insertable = true, updatable = true)
    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }
    @Basic
    @Column(name = "content", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Basic
    @Column(name = "isArchive", nullable = false, insertable = true, updatable = true)
    public int getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(int isArchive) {
        this.isArchive = isArchive;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "senderEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Basic
    @Column(name = "receiverEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (date != that.date) return false;
        if (id != that.id) return false;
        if (isArchive != that.isArchive) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (receiverEmail != null ? !receiverEmail.equals(that.receiverEmail) : that.receiverEmail != null)
            return false;
        if (senderEmail != null ? !senderEmail.equals(that.senderEmail) : that.senderEmail != null) return false;
        if (status != that.status) return false;;
        if (productId != that.productId) return false;
        if (offerId != that.offerId) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (int) (offerId ^ (offerId >>> 32));
        result = 31 * result + isArchive;
        result = 31 * result + status;
        result = 31 * result + (senderEmail != null ? senderEmail.hashCode() : 0);
        result = 31 * result + (receiverEmail != null ? receiverEmail.hashCode() : 0);
        return result;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("productId",this.productId);
        jsonObject.put("content",this.content);
        jsonObject.put("date",this.date);
        jsonObject.put("offerId",this.offerId);
        jsonObject.put("isArchive",this.isArchive);
        jsonObject.put("status",this.status);
        jsonObject.put("senderEmail",this.senderEmail);
        jsonObject.put("receiverEmail",this.receiverEmail);
        return jsonObject;
    }
    public JSONObject toDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("productId",this.productId);
        jsonObject.put("content",this.content);
        jsonObject.put("date",this.date);
        jsonObject.put("offerId",this.offerId);
        jsonObject.put("isArchive",this.isArchive);
        jsonObject.put("status",this.status);
        jsonObject.put("senderEmail",this.senderEmail);
        jsonObject.put("receiverEmail",this.receiverEmail);
        jsonObject.put("isOffered",(this.offerId > 0));
        return jsonObject;
    }
}
