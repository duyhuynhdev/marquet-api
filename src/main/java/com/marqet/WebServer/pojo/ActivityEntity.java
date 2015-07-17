package com.marqet.WebServer.pojo;

import com.marqet.WebServer.controller.UserController;
import com.marqet.WebServer.util.Database;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 5/29/15.
 */
@Entity
@Table(name = "Activity", schema = "", catalog = "marqet")
public class ActivityEntity {
    private long id;
    private long date;
    private int action;
    private String subjectEmail;
    private int isRead;
    private String objectEmail;
    private String ownerEmail;
    private long ref;

    public ActivityEntity() {
    }

    public ActivityEntity(ActivityEntity that) {
        this.id = that.id;
        this.date = that.date;
        this.action = that.action;
        this.subjectEmail = that.subjectEmail;
        this.isRead = that.isRead;
        this.objectEmail = that.objectEmail;
        this.ownerEmail = that.ownerEmail;
        this.ref = that.ref;
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
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Basic
    @Column(name = "action", nullable = false, insertable = true, updatable = true)
    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Basic
    @Column(name = "subjectEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    @Basic
    @Column(name = "isRead", nullable = false, insertable = true, updatable = true)
    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Basic
    @Column(name = "objectEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getObjectEmail() {
        return objectEmail;
    }

    public void setObjectEmail(String objectEmail) {
        this.objectEmail = objectEmail;
    }

    @Basic
    @Column(name = "ownerEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    @Basic
    @Column(name = "ref", nullable = false, insertable = true, updatable = true)
    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityEntity that = (ActivityEntity) o;

        if (id != that.id) return false;
        if (date != that.date) return false;
        if (action != that.action) return false;
        if (isRead != that.isRead) return false;
        if (ref != that.ref) return false;
        if (subjectEmail != null ? !subjectEmail.equals(that.subjectEmail) : that.subjectEmail != null) return false;
        if (objectEmail != null ? !objectEmail.equals(that.objectEmail) : that.objectEmail != null) return false;
        if (ownerEmail != null ? !ownerEmail.equals(that.ownerEmail) : that.ownerEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (int) (ref ^ (ref >>> 32));
        result = 31 * result + action;
        result = 31 * result + (subjectEmail != null ? subjectEmail.hashCode() : 0);
        result = 31 * result + isRead;
        result = 31 * result + (objectEmail != null ? objectEmail.hashCode() : 0);
        result = 31 * result + (ownerEmail != null ? ownerEmail.hashCode() : 0);

        return result;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("date",date);
        jsonObject.put("action",action);
        jsonObject.put("isRead",isRead);
        jsonObject.put("ref",ref);
        jsonObject.put("subjectEmail",subjectEmail);
        jsonObject.put("objectEmail",objectEmail);
        jsonObject.put("ownerEmail",ownerEmail);
        return jsonObject;
    }

    public JSONObject toDetailJSON(){
        Database database = Database.getInstance();
        UserEntity subUser = database.getUserEntityHashMap().get(subjectEmail);
        UserEntity objUser = database.getUserEntityHashMap().get(objectEmail);
        if(subUser == null){
            subUser = new UserEntity(new UserController().getAnonymous());
        }
        if(objUser == null){
            objUser = new UserEntity(new UserController().getAnonymous());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("date",date);
        jsonObject.put("action",action);
        jsonObject.put("ref",ref);
        jsonObject.put("subjectEmail",subjectEmail);
        jsonObject.put("subjectName",subUser.getUserName());
        jsonObject.put("subjectAvatar",subUser.getProfilePicture());
        jsonObject.put("objectEmail",objectEmail);
        jsonObject.put("objectName",objUser.getUserName());
        jsonObject.put("objectAvatar",objUser.getProfilePicture());
        return jsonObject;
    }
}
