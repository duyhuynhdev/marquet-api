package com.marqet.WebServer.pojo;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/27/15.
 */
@Entity
@Table(name = "SubMessage", schema = "", catalog = "marqet")
public class SubMessageEntity {
    private long id;
    private long messageId;
    private String content;
    private long date;
    private String senderEmail;

    public SubMessageEntity() {
    }

    public SubMessageEntity(SubMessageEntity s) {
        this.id = s.id;
        this.messageId = s.messageId;
        this.content = s.content;
        this.date = s.date;
        this.senderEmail = s.senderEmail;
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
    @Column(name = "messageId", nullable = false, insertable = true, updatable = true)
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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
    @Column(name = "senderEmail", nullable = false, insertable = true, updatable = true, length = 100)
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubMessageEntity that = (SubMessageEntity) o;

        if (date != that.date) return false;
        if (id != that.id) return false;
        if (messageId != that.messageId) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (senderEmail != null ? !senderEmail.equals(that.senderEmail) : that.senderEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (messageId ^ (messageId >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (senderEmail != null ? senderEmail.hashCode() : 0);
        return result;
    }
}
