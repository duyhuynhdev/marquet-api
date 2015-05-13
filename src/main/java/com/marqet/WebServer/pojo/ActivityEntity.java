package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Activity", schema = "", catalog = "marqet")
public class ActivityEntity {
    private long id;
    private long date;
    private String action;
    private String object;
    private String subjectEmail;
    private byte isRead;
    private String objectEmail;

    public ActivityEntity() {
    }

    public ActivityEntity(ActivityEntity a) {
        this.id = a.id;
        this.date = a.date;
        this.action = a.action;
        this.object = a.object;
        this.subjectEmail = a.subjectEmail;
        this.isRead = a.isRead;
        this.objectEmail = a.objectEmail;
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
    @Column(name = "action", nullable = false, insertable = true, updatable = true, length = 100)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Basic
    @Column(name = "object", nullable = false, insertable = true, updatable = true, length = 100)
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
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
    @Column(name = "objectEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getObjectEmail() {
        return objectEmail;
    }

    public void setObjectEmail(String objectEmail) {
        this.objectEmail = objectEmail;
    }
    @Basic
    @Column(name = "isRead", nullable = false, insertable = true, updatable = true)
    public byte getIsRead() {
        return isRead;
    }

    public void setIsRead(byte isRead) {
        this.isRead = isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityEntity that = (ActivityEntity) o;

        if (date != that.date) return false;
        if (id != that.id) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (subjectEmail != null ? !subjectEmail.equals(that.subjectEmail) : that.subjectEmail != null) return false;
        if (objectEmail != null ? !objectEmail.equals(that.objectEmail) : that.objectEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (subjectEmail != null ? subjectEmail.hashCode() : 0);
        result = 31 * result + (objectEmail != null ? objectEmail.hashCode() : 0);
        return result;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("action",this.action);
        jsonObject.put("object",this.object);
        jsonObject.put("subjectEmail",this.subjectEmail);
        jsonObject.put("objectEmail",this.objectEmail);
        return jsonObject;
    }


}
