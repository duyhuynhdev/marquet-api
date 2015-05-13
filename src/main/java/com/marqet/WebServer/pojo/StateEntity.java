package com.marqet.WebServer.pojo;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 4/6/15.
 */
@Entity
@Table(name = "State", schema = "", catalog = "marqet")
public class StateEntity {
    private String code;
    private String name;
    private String countryCode;

    public StateEntity() {
    }

    public StateEntity(StateEntity s) {
        this.code = s.code;
        this.name = s.name;
        this.countryCode = s.countryCode;
    }

    @Id
    @Column(name = "code", nullable = false, insertable = true, updatable = true, length = 10)
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
    @Column(name = "countryCode", nullable = false, insertable = true, updatable = true, length = 10)
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateEntity that = (StateEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",this.code);
        jsonObject.put("name",this.name);
        jsonObject.put("countryCode",this.countryCode);
        return jsonObject;
    }
}
