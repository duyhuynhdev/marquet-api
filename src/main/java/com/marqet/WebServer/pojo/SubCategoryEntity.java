package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Path;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "SubCategory", schema = "", catalog = "marqet")
public class SubCategoryEntity {
    private long id;
    private String name;
    private String coverImg;
    private long categoryId;

    public SubCategoryEntity() {
    }

    public SubCategoryEntity(SubCategoryEntity s) {
        this.id = s.id;
        this.name = s.name;
        this.coverImg = s.coverImg;
        this.categoryId = s.categoryId;
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

    @Basic
    @Column(name = "categoryId", nullable = false, insertable = true, updatable = true)
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCategoryEntity that = (SubCategoryEntity) o;

        if (categoryId != that.categoryId) return false;
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
        result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));;
        return result;
    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("name",this.name);
        jsonObject.put("coverImg", Path.getServerAddress()+this.coverImg);
        jsonObject.put("categoryId",this.categoryId);
        return jsonObject;
    }
    public JSONObject toDetailJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",this.id);
        jsonObject.put("name",this.name);
        jsonObject.put("coverImg", Path.getServerAddress()+(this.coverImg.equals("")?Path.getDefaultCategoryImagePath():this.coverImg));
        jsonObject.put("categoryId",this.categoryId);
        return jsonObject;
    }
}
