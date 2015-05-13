package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Database;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Comment", schema = "", catalog = "marqet")
public class CommentEntity {
    private long id;
    private String content;
    private long date;
    private String email;
    private long productId;

    public CommentEntity() {
    }

    public CommentEntity(CommentEntity c) {
        this.id = c.id;
        this.content = c.content;
        this.date = c.date;
        this.email = c.email;
        this.productId = c.productId;
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

        CommentEntity that = (CommentEntity) o;

        if (date != that.date) return false;
        if (id != that.id) return false;
        if (productId != that.productId) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("content",this.content);
        jsonObject.put("date",this.date);
        jsonObject.put("email",this.email);
        jsonObject.put("productId",this.productId);
        return jsonObject;
    }
    public JSONObject toCommentDetailJSON(){
        Database database = Database.getInstance();
        UserEntity owner = database.getUserEntityHashMap().get(this.email);
        String userName = "Anonymous";
        String image =  "";
        if(owner != null){
            userName = owner.getUserName();
            image = owner.getProfilePicture();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("content",this.content);
        jsonObject.put("date",this.date);
        jsonObject.put("email",this.email);
        jsonObject.put("userName", userName);
        jsonObject.put("profilePicture",(image.equals("") ? Database.getInstance().getElementEntity().getDefaultAvatar(): image));
        return jsonObject;
    }
}
