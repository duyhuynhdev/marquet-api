package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.Path;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
@Entity
@Table(name = "Feedback", schema = "", catalog = "marqet")
public class FeedbackEntity {
    private long id;
    private String buyerEmail;
    private String sellerEmail;
    private long productId;
    private int status;
    private long date;
    private String content;

    public FeedbackEntity() {
    }

    public FeedbackEntity(FeedbackEntity f) {
        this.id = f.id;
        this.buyerEmail = f.buyerEmail;
        this.sellerEmail = f.sellerEmail;
        this.productId = f.productId;
        this.status = f.status;
        this.date = f.date;
        this.content = f.content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
    @Column(name = "sellerEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
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
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackEntity that = (FeedbackEntity) o;

        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (status != that.status) return false;
        if (buyerEmail != null ? !buyerEmail.equals(that.buyerEmail) : that.buyerEmail != null) return false;
        if (sellerEmail != null ? !sellerEmail.equals(that.sellerEmail) : that.sellerEmail != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (buyerEmail != null ? buyerEmail.hashCode() : 0);
        result = 31 * result + (sellerEmail != null ? sellerEmail.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + status;
        return result;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("buyerEmail",this.buyerEmail);
        jsonObject.put("sellerEmail",this.sellerEmail);
        jsonObject.put("productId",this.productId);
        jsonObject.put("status",this.status);
        jsonObject.put("content",this.content);
        return jsonObject;
    }
    public JSONObject toDetailJSON(String email){
        Database database = Database.getInstance();
        UserEntity buyer = database.getUserEntityHashMap().get(this.buyerEmail);
        UserEntity seller = database.getUserEntityHashMap().get(this.sellerEmail);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("productId",this.productId);
        jsonObject.put("status",this.status);
        jsonObject.put("content",this.content);
        if(!buyerEmail.equals(email)) {
            jsonObject.put("email",buyer == null ? "Anonymous" : buyer.getEmail());
            jsonObject.put("userName", buyer == null ? "Anonymous" : buyer.getUserName());
            jsonObject.put("userAvatar", buyer == null ? Path.getServerAddress() + database.getElementEntity().getDefaultAvatar() : buyer.getProfilePicture());
            jsonObject.put("isBuyer",true);
        }else {
            jsonObject.put("email",buyer == null ? "Anonymous" : buyer.getEmail());
            jsonObject.put("userName", buyer == null ? "Anonymous" : buyer.getUserName());
            jsonObject.put("userAvatar", seller == null ? Path.getServerAddress() + database.getElementEntity().getDefaultAvatar() : seller.getProfilePicture());
            jsonObject.put("isBuyer",false);
        }
        return jsonObject;
    }
}
