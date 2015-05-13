package com.marqet.WebServer.pojo;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/27/15.
 */
@Entity
@Table(name = "WatchList", schema = "", catalog = "marqet")
public class WatchListEntity {
    private long id;
    private String email;
    private String pattern;

    public WatchListEntity() {
    }

    public WatchListEntity(WatchListEntity w) {
        this.id = w.id;
        this.email = w.email;
        this.pattern = w.pattern;
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
    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "pattern", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WatchListEntity that = (WatchListEntity) o;

        if (id != that.id) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }
}
