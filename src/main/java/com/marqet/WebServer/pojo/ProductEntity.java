package com.marqet.WebServer.pojo;

import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.Path;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by hpduy17 on 3/16/15.
 */
@Entity
@Table(name = "Product", schema = "", catalog = "marqet")
public class ProductEntity {
    private long id;
    private String name;
    private long date;
    private double price;
    private String status;
    private String description;
    private String productImages;
    private String email;
    private String cityCode;
    private long subCategoryId;
    public static final String STATUS_AVAILABLE = "available";
    public static final String STATUS_OFFER = "offer";
    public static final String STATUS_SOLD = "sold";

    public ProductEntity() {
    }

    public ProductEntity(ProductEntity p) {
        this.id = p.id;
        this.name = p.name;
        this.date = p.date;
        this.price = p.price;
        this.status = p.status;
        this.description = p.description;
        this.productImages = p.productImages;
        this.email = p.email;
        this.cityCode = p.cityCode;
        this.subCategoryId = p.subCategoryId;
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
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true, precision = 0)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "description", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "productImages", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
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
    @Column(name = "cityCode", nullable = false, insertable = true, updatable = true, length = 10)
    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Basic
    @Column(name = "subCategoryId", nullable = false, insertable = true, updatable = true)
    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (date != that.date) return false;
        if (id != that.id) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (subCategoryId != that.subCategoryId) return false;
        if (cityCode != null ? !cityCode.equals(that.cityCode) : that.cityCode != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (productImages != null ? !productImages.equals(that.productImages) : that.productImages != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (date ^ (date >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (productImages != null ? productImages.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (int) (subCategoryId ^ (subCategoryId >>> 32));
        ;
        return result;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("date", this.date);
        jsonObject.put("price", this.price);
        jsonObject.put("status", this.status);
        jsonObject.put("description", this.description);
        jsonObject.put("productImages", this.productImages);
        jsonObject.put("email", this.email);
        jsonObject.put("cityCode", this.cityCode);
        jsonObject.put("subCategoryId", subCategoryId);
        return jsonObject;
    }

    public JSONObject toProductSortDetailJSON(String email) {
        Database database = Database.getInstance();
        UserEntity owner = database.getUserEntityHashMap().get(this.email);
        int numStuffLiked = 0;
        if (database.getStuffLikedRFbyProductId().get(this.id) != null)
            numStuffLiked = database.getStuffLikedRFbyProductId().get(this.id).size();
        boolean isLiked = database.getStuffLikedRFbyEmailAndProductId().containsKey(email+"#"+this.id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("date", this.date);
        jsonObject.put("price", this.price);
        int status = 0;
        if (this.status.equals(ProductEntity.STATUS_SOLD))
            status = 1;
        if (this.status.equals(ProductEntity.STATUS_OFFER))
            status = 2;
        jsonObject.put("status", status);
        String productImage = "";
        try {
            JSONObject productImages = new JSONObject(this.productImages);
            productImage = productImages.getJSONArray("thumbnail").getString(0);

        } catch (Exception ignored) {
        }
        jsonObject.put("productImage", Path.getServerAddress() + (productImage.equals("") ? Path.getDefaultSmallBannerImagePath() : productImage));
        jsonObject.put("userName", owner.getUserName());
        jsonObject.put("profilePicture", owner.getProfilePicture());
        jsonObject.put("numStuffLiked", numStuffLiked);
        jsonObject.put("isLiked", isLiked);
        return jsonObject;
    }

    public JSONObject toProductDetailJSON() {
        Database database = Database.getInstance();
        //get user detail
        UserEntity owner = database.getUserEntityHashMap().get(this.email);
        //get city name
        CityEntity city = database.getCityEntityHashMap().get(this.cityCode);
        //get category
        SubCategoryEntity subCategory = database.getSubCategoryEntityHashMap().get(this.subCategoryId);
        CategoryEntity category = database.getCategoryEntityHashMap().get(subCategory.getCategoryId());
        //get num liked
        int numStuffLiked = 0;
        if (database.getStuffLikedRFbyProductId().get(this.id) != null)
            numStuffLiked = database.getStuffLikedRFbyProductId().get(this.id).size();
        //result
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("name", this.name);
        jsonObject.put("date", this.date);
        jsonObject.put("price", this.price);
        int status = 0;
        if (this.status.equals(ProductEntity.STATUS_SOLD))
            status = 1;
        if (this.status.equals(ProductEntity.STATUS_OFFER))
            status = 2;
        jsonObject.put("status", status);
        jsonObject.put("description", this.description);
        JSONArray productImage = new JSONArray();
        try {
            JSONObject productImages = new JSONObject(this.productImages);
            for (int i = 0; i < productImages.getJSONArray("full").length(); i++) {
                productImage.put(Path.getServerAddress() + productImages.getJSONArray("full").get(i));
            }
        } catch (Exception ignored) {
        }
        JSONArray defaultImg = new JSONArray();
        for (int i = 0; i < 5; i++)
            defaultImg.put(Path.getServerAddress() + Path.getDefaultBigBannerImagePath());
        jsonObject.put("productImage", (productImage.length() == 0 ? defaultImg : productImage));
        jsonObject.put("userName", owner.getUserName());
        String image = owner.getProfilePicture();
        jsonObject.put("profilePicture", (image.equals("") ? Database.getInstance().getElementEntity().getDefaultAvatar() : image));
        jsonObject.put("email", this.email);
        jsonObject.put("city", city.getName());
        jsonObject.put("category", category.getName());
        jsonObject.put("numStuffLiked", numStuffLiked);
        return jsonObject;
    }
}
