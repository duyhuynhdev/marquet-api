package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Path;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Category", schema = "", catalog = "marqet")
public class CategoryEntity {
    private long id;
    private String name;
    private String coverImg;

    public CategoryEntity() {
    }

    public CategoryEntity(CategoryEntity c) {
        this.id = c.id;
        this.name = c.name;
        this.coverImg = c.coverImg;
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
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "coverImg", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntity that = (CategoryEntity) o;

        if (id != that.id) return false;
        if (coverImg != null ? !coverImg.equals(that.coverImg) : that.coverImg != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coverImg != null ? coverImg.hashCode() : 0);
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("name",this.name);
        jsonObject.put("coverImg", Path.getServerAddress()+this.coverImg);
        return jsonObject;
    }
    public JSONObject toDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("name",this.name);
        jsonObject.put("coverImg", Path.getServerAddress()+(this.coverImg.equals("")?Path.getDefaultCategoryImagePath():this.coverImg));
        return jsonObject;
    }
}
