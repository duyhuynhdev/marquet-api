package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Country", schema = "", catalog = "marqet")
public class CountryEntity {
    private String code;
    private String name;
    private String phonePrefix;

    public CountryEntity() {
    }

    public CountryEntity(CountryEntity c) {
        this.code = c.code;
        this.name = c.name;
        this.phonePrefix = c.phonePrefix;
    }

    @Id
    @Column(name = "code", nullable = false, insertable = true, updatable = true, length = 5)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "phonePrefix", nullable = false, insertable = true, updatable = true, length = 5)
    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryEntity that = (CountryEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phonePrefix != null ? !phonePrefix.equals(that.phonePrefix) : that.phonePrefix != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phonePrefix != null ? phonePrefix.hashCode() : 0);
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",this.code);
        jsonObject.put("name",this.name);
        jsonObject.put("phonePrefix",this.phonePrefix);
        return jsonObject;
    }
}
