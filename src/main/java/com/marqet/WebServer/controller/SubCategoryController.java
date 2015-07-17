package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.SubCategoryDao;
import com.marqet.WebServer.pojo.SubCategoryEntity;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.IdGenerator;
import com.marqet.WebServer.util.Path;
import com.marqet.WebServer.util.UploadImageUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class SubCategoryController {
    private SubCategoryDao dao = new SubCategoryDao();
    private Database database = Database.getInstance();
    public JSONObject getListSubCategoryByCategoryId(long categoryId) {
        JSONObject object = ResponseController.createSuccessJSON();
        JSONArray jsonArray = new JSONArray();
        List<Long> lstSubCategory = new ArrayList<>();
        try {
             lstSubCategory = new ArrayList<>(database.getSubCategoryRFbyCategoryId().get(categoryId));
        }catch (Exception ignore){

        }
        if(lstSubCategory == null)
            return object.put(ResponseController.CONTENT, jsonArray);
        for (long id : lstSubCategory) {
            jsonArray.put(database.getSubCategoryEntityHashMap().get(id).toDetailJSON());
        }
        object.put(ResponseController.CONTENT, jsonArray);
        return object;
    }
    public JSONObject getListSubCategory() {
        JSONObject object = ResponseController.createSuccessJSON();
        JSONArray jsonArray = new JSONArray();
        for (long id : database.getSubCategoryEntityHashMap().keySet()) {
            jsonArray.put(database.getSubCategoryEntityHashMap().get(id).toDetailJSON());
        }
        object.put(ResponseController.CONTENT, jsonArray);
        return object;
    }
    public JSONObject addSubCategory(String name, Part imagePart, long categoryId) {
        try {
            SubCategoryEntity subCategory = new SubCategoryEntity();
            long id = IdGenerator.getSubCategoryId();
            subCategory.setId((int) id);
            String coverImagePath = "";
            if (imagePart != null && imagePart.getSize() > 0) {
                coverImagePath = new UploadImageUtil().upload("category_" + id, Path.getSubCategoriesPath(), imagePart);
            }
            subCategory.setCoverImg(coverImagePath);
            subCategory.setName(name);
            subCategory.setCategoryId(categoryId);
            HashSet<Long> listSubCategoryId = database.getSubCategoryRFbyCategoryId().get(categoryId);
            if(listSubCategoryId==null)
                listSubCategoryId = new HashSet<>();
            listSubCategoryId.add(id);
            database.getSubCategoryRFbyCategoryId().put(categoryId,listSubCategoryId);
            database.getSubCategoryEntityHashMap().put(id, subCategory);
            if (dao.insert(subCategory)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, subCategory.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot insert in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject editSubCategory(long id, String name, Part imagePart, long categoryId) {
        try {
            SubCategoryEntity subCategory = Database.getInstance().getSubCategoryEntityHashMap().get(id);
            if (imagePart != null && imagePart.getSize() > 0) {
                String coverImagePath = new UploadImageUtil().upload("category_" + id, Path.getSubCategoriesPath(), imagePart);
                subCategory.setCoverImg(coverImagePath);
            }
            subCategory.setName(name);
            //remove in old category
            database.getSubCategoryRFbyCategoryId().get(categoryId).remove(id);
            //add in new category
            subCategory.setCategoryId(categoryId);
            HashSet<Long> listSubCategoryId = database.getSubCategoryRFbyCategoryId().get(categoryId);
            if(listSubCategoryId==null)
                listSubCategoryId = new HashSet<>();
            listSubCategoryId.add(id);
            database.getSubCategoryRFbyCategoryId().put(categoryId,listSubCategoryId);
            database.getSubCategoryEntityHashMap().put(id, subCategory);
            if (dao.update(subCategory)) {
                return ResponseController.createSuccessJSON().put(ResponseController.CONTENT, subCategory.toDetailJSON());
            } else {
                return ResponseController.createFailJSON("Cannot update in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject deleteSubCategory(long id) {
        try {
            SubCategoryEntity subCategory = new SubCategoryEntity(Database.getInstance().getSubCategoryEntityHashMap().get(id));
            database.getSubCategoryEntityHashMap().remove(id);
            database.getSubCategoryRFbyCategoryId().get(subCategory.getCategoryId()).remove(id);
            if (dao.delete(subCategory)) {
                return ResponseController.createSuccessJSON();
            } else {
                return ResponseController.createFailJSON("Cannot delete in database\n");
            }
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }


}
