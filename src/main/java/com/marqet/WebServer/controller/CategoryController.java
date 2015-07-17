package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.CategoryDao;
import com.marqet.WebServer.pojo.CategoryEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import com.marqet.WebServer.util.Path;
import com.marqet.WebServer.util.UploadImageUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class CategoryController {
    private CategoryDao dao = new CategoryDao();
    private Database database = Database.getInstance();

    public JSONObject getListCategory() {
        try {
            JSONObject object = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            for (long id : database.getCategoryEntityHashMap().keySet()) {
                jsonArray.put(database.getCategoryEntityHashMap().get(id).toDetailJSON());
            }
            object.put(ResponseController.CONTENT, jsonArray);
            return object;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }
    public JSONObject addCategory(String name, Part imagePart) {
        try {
            CategoryEntity category = new CategoryEntity();
            long id = IdGenerator.getCategoryId();
            category.setId((int) id);
            String coverImagePath = "";
            if (imagePart != null && imagePart.getSize() > 0) {
                coverImagePath = new UploadImageUtil().upload("category_" + id, Path.getCategoriesPath(), imagePart);
            }
            category.setCoverImg(coverImagePath);
            category.setName(name);
            database.getCategoryEntityHashMap().put(id, category);
            if (dao.insert(category)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, category.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject editCategory(long id, String name, Part imagePart) {
        try {
            CategoryEntity category = Database.getInstance().getCategoryEntityHashMap().get(id);
            if (imagePart != null && imagePart.getSize() > 0) {
                String coverImagePath = new UploadImageUtil().upload("category_" + id, Path.getCategoriesPath(), imagePart);
                category.setCoverImg(coverImagePath);
            }
            category.setName(name);
            database.getCategoryEntityHashMap().put(id, category);
            if (dao.update(category)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, category.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot update in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteCategory(long id) {
        try {
            CategoryEntity category = new CategoryEntity(Database.getInstance().getCategoryEntityHashMap().get(id));
            database.getCategoryEntityHashMap().remove(id);
            HashSet<Long> lstSubCategoryId = database.getSubCategoryRFbyCategoryId().get(id);
            if(lstSubCategoryId!=null) {
                for (long subId : new ArrayList<>(lstSubCategoryId)) {
                    new SubCategoryController().deleteSubCategory(subId);
                }
            }
            database.getSubCategoryRFbyCategoryId().remove(id);
            if (dao.delete(category)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, category.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

}
