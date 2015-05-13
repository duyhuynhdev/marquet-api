package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.ProductDao;
import com.marqet.WebServer.pojo.CityEntity;
import com.marqet.WebServer.pojo.OfferEntity;
import com.marqet.WebServer.pojo.ProductEntity;
import com.marqet.WebServer.pojo.StuffLikedEntity;
import com.marqet.WebServer.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.util.*;

/**
 * Created by hpduy17 on 3/22/15.
 */
public class ProductController {
    private ProductDao dao = new ProductDao();
    private Database database = Database.getInstance();

    public JSONObject getListProductByStuffLiked(String email, int startIdx, int numProduct) {
        try {
            JSONObject responseJSON = ResponseController.createSuccessJSON();
            List<Long> stuffLikedList = database.getStuffLikedRFbyEmail().get(email);
            if(stuffLikedList== null){
                return responseJSON.put(ResponseController.CONTENT, new JSONArray());
            }
            JSONArray jsonArray = new JSONArray();
            int endIdx = startIdx+numProduct;
            if(endIdx>stuffLikedList.size()-1)
                endIdx = stuffLikedList.size();
            List<Long> subList = stuffLikedList.subList(startIdx,endIdx);
            for (long slId : subList) {
                StuffLikedEntity stuffLiked = database.getStuffLikedEntityHashMap().get(slId);
                ProductEntity product = database.getProductEntityHashMap().get(stuffLiked.getProductId());
                if (product != null) {
                    jsonArray.put(product.toProductSortDetailJSON(email));
                }
            }
            responseJSON.put(ResponseController.CONTENT, jsonArray);
            return responseJSON;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByOffer(String email,int startIdx,int numProduct) {
        try {
            JSONObject responseJSON = ResponseController.createSuccessJSON();
            List<Long> offerList = database.getOfferRFbyEmail().get(email);
            if(offerList== null){
                return responseJSON.put(ResponseController.CONTENT, new JSONArray());
            }
            JSONArray jsonArray = new JSONArray();
            int endIdx = startIdx+numProduct;
            if(endIdx>offerList.size()-1)
                endIdx = offerList.size();
            List<Long> subList = offerList.subList(startIdx,endIdx);
            for (long offerId : subList) {
                OfferEntity offer = database.getOfferEntityHashMap().get(offerId);
                ProductEntity product = database.getProductEntityHashMap().get(offer.getProductId());
                if (product != null) {
                    jsonArray.put(product.toProductSortDetailJSON(email));
                }
            }
            responseJSON.put(ResponseController.CONTENT, jsonArray);
            return responseJSON;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByCountryCode(String countryCode, long subCategoryId, String typeSort,String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId);
            if(productList== null){
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            if (productList != null) {
                if (typeSort.equals(SearchAndFilterUtil.LASTEST))
                    productList = new SearchAndFilterUtil().sortProductByLastest(productList);
                else
                    productList = new SearchAndFilterUtil().sortProductByPopular(productList);
                for (long pId : productList) {
                    ProductEntity product = database.getProductEntityHashMap().get(pId);
                    if (product != null && product.getStatus().equals(ProductEntity.STATUS_AVAILABLE)) {
                        jsonArray.put(product.toProductSortDetailJSON(email));
                    }
                }
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByCountryCode(String countryCode, long subCategoryId, String typeSort, int startIdx, int numProduct,String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId);
            if(productList== null){
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            if (productList != null) {
                if (typeSort.equals(SearchAndFilterUtil.LASTEST))
                    productList = new SearchAndFilterUtil().sortProductByLastest(productList);
                else
                    productList = new SearchAndFilterUtil().sortProductByPopular(productList);
                int endIdx = startIdx+numProduct;
                if(endIdx>productList.size()-1)
                    endIdx = productList.size();
                List<Long> subList = productList.subList(startIdx,endIdx);
                for (long pId : subList) {
                    ProductEntity product = database.getProductEntityHashMap().get(pId);
                    if (product != null && product.getStatus().equals(ProductEntity.STATUS_AVAILABLE)) {
                        jsonArray.put(product.toProductSortDetailJSON(email));
                    }
                }
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getProductDetail(long productId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            result.put(ResponseController.CONTENT,
                    database.getProductEntityHashMap().get(productId).toProductDetailJSON());
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    //TODO notify watchlist owner
    public JSONObject addProduct(String email, String title, String cityCode, long price, long subCategoryId, String description, List<Part> productImages) {
        try {
            JSONObject responseJSON;
            ProductEntity product = new ProductEntity();
            product.setId(IdGenerator.getProductId());
            product.setName(title);
            product.setCityCode(cityCode);
            product.setPrice(price);
            product.setSubCategoryId(subCategoryId);
            product.setDescription(description);
            product.setStatus(ProductEntity.STATUS_AVAILABLE);
            product.setDate(new DateTimeUtil().getNow());
            JSONObject productImagesJSON = new JSONObject();
            JSONArray productImagesFull = new JSONArray();
            JSONArray productImagesThumbnail = new JSONArray();
            if (productImages != null) {
                for (Part p : productImages) {
                    if (p.getSize() > 0) {
                        String fileName = "product_" + product.getId() + "_" + product.getName() + new DateTimeUtil().getNow();
                        productImagesFull.put(new UploadImageUtil().upload(fileName, Path.getProductPath(), p));
                        productImagesThumbnail.put(new UploadImageUtil().uploadThumbnail(fileName, p));
                    } else {
                        productImagesFull.put(Path.getDefaultBigBannerImagePath());
                        productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                    }
                }
            } else {
                productImagesFull.put(Path.getDefaultBigBannerImagePath());
                productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
            }
            productImagesJSON.put("full", productImagesFull);
            productImagesJSON.put("thumbnail", productImagesFull);
            product.setProductImages(productImagesJSON.toString());
            product.setEmail(email);
            database.getProductEntityHashMap().put(product.getId(), product);
            //put to productRFCountry
            HashMap<String, CityEntity> cityHashMap = database.getCityEntityHashMap();
            String countryCode = cityHashMap.get(product.getCityCode()).getCountryCode();
            HashMap<Long, List<Long>> productFRSubCategory = database.getProductRFbyCountryCode().get(countryCode);
            if (productFRSubCategory == null)
                productFRSubCategory = new HashMap<>();
            List<Long> productList = productFRSubCategory.get(product.getSubCategoryId());
            if (productList == null) {
                productList = new ArrayList<>();
            }
            productList.add(product.getId());
            productFRSubCategory.put(product.getSubCategoryId(), productList);
            database.getProductRFbyCountryCode().put(countryCode, productFRSubCategory);
            //put to productRFEmail
            List<Long> productRFEmail = database.getProductRFbyEmail().get(product.getEmail());
            if (productRFEmail == null)
                productRFEmail = new ArrayList<>();
            productRFEmail.add(product.getId());
            database.getProductRFbyEmail().put(countryCode, productRFEmail);
            //save to database
            if (dao.insert(product)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON());
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
            }
            // insert Activity
            new ActivityController().insertActivity(ActivityUtil.LABEL_UPLOAD_ACTION, email, "1", "");
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject editProduct(long productId, String newTitle, String newCityCode, long newPrice, long newSubCategoryId, String newDescription, List<Part> newProductImages, JSONArray oldProductImagesIdx) {
        try {
            JSONObject responseJSON;
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            if (product != null) {
                String oldCityCode = product.getCityCode();
                long oldSubcategory = product.getSubCategoryId();
                product.setName(newTitle);
                product.setCityCode(newCityCode);
                product.setPrice(newPrice);
                product.setSubCategoryId(newSubCategoryId);
                product.setDescription(newDescription);
                JSONObject productImagesJSON = new JSONObject(product.getProductImages());
                JSONArray productImagesFull = productImagesJSON.getJSONArray("full");
                JSONArray productImagesThumbnail = productImagesJSON.getJSONArray("thumbnail");
                for (int i = 0; i < oldProductImagesIdx.length(); i++) {
                    productImagesFull.remove(oldProductImagesIdx.getInt(i));
                    productImagesThumbnail.remove(oldProductImagesIdx.getInt(i));
                }
                if (newProductImages != null) {
                    for (Part p : newProductImages) {
                        if (p.getSize() > 0) {
                            String fileName = "product_" + product.getId() + "_" + product.getName() + new DateTimeUtil().getNow();
                            productImagesFull.put(new UploadImageUtil().upload(fileName, Path.getProductPath(), p));
                            productImagesThumbnail.put(new UploadImageUtil().uploadThumbnail(fileName, p));
                        } else {
                            productImagesFull.put(Path.getDefaultBigBannerImagePath());
                            productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                        }
                    }
                } else {
                    productImagesFull.put(Path.getDefaultBigBannerImagePath());
                    productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                }
                productImagesJSON.put("full", productImagesFull);
                productImagesJSON.put("thumbnail", productImagesFull);
                product.setProductImages(productImagesJSON.toString());
                database.getProductEntityHashMap().put(productId, product);
                //put to productRFCountry
                //1. remove old RF
                String oldCountryCode = database.getCityEntityHashMap().get(oldCityCode).getCountryCode();
                database.getProductRFbyCountryCode().get(oldCountryCode).get(oldSubcategory).remove(product.getId());
                //2. add new RF
                HashMap<String, CityEntity> cityHashMap = database.getCityEntityHashMap();
                String countryCode = cityHashMap.get(product.getCityCode()).getCountryCode();
                HashMap<Long, List<Long>> productFRSubCategory = database.getProductRFbyCountryCode().get(countryCode);
                if (productFRSubCategory == null)
                    productFRSubCategory = new HashMap<>();
                List<Long> productList = productFRSubCategory.get(product.getSubCategoryId());
                if (productList == null) {
                    productList = new ArrayList<>();
                }
                productList.add(product.getId());
                productFRSubCategory.put(product.getSubCategoryId(), productList);
                database.getProductRFbyCountryCode().put(countryCode, productFRSubCategory);
                if (dao.update(product)) {
                    responseJSON = ResponseController.createSuccessJSON();
                    responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON());
                } else {
                    responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
                }
            } else {
                responseJSON = ResponseController.createFailJSON("Product not found\n");
            }
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject getListProductByEmail(String email, String typeSort, int startIdx, int numProduct) {
        try {
            SearchAndFilterUtil searchAndFilterUtil = new SearchAndFilterUtil();
            JSONObject result = ResponseController.createSuccessJSON();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = database.getProductRFbyEmail().get(email);
            if(productList== null){
                jsonObject.put("numProduct",0);
                jsonObject.put("listProduct",jsonArray);
                return result.put(ResponseController.CONTENT, jsonObject);
            }
            if (typeSort.equals(SearchAndFilterUtil.LASTEST))
                productList = searchAndFilterUtil.sortProductByLastest(productList);
            else
                productList = searchAndFilterUtil.sortProductByPopular(productList);
            List<Long> soldProducts = new ArrayList<>();

            for (long pId : new ArrayList<>(productList)) {
                ProductEntity product = database.getProductEntityHashMap().get(pId);
                if (product != null && product.getStatus().equals(ProductEntity.STATUS_SOLD)) {
                    soldProducts.add(pId);
                    productList.remove(pId);
                }
            }
            productList.addAll(soldProducts);
            int endIdx = startIdx+numProduct;
            if(endIdx>productList.size()-1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx,endIdx);
            for (Long id : subList) {
                ProductEntity product = database.getProductEntityHashMap().get(id);
                jsonArray.put(product.toProductSortDetailJSON(email));
            }
            jsonObject.put("numProduct",productList.size());
            jsonObject.put("listProduct",jsonArray);
            result.put(ResponseController.CONTENT, jsonObject);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByFilter(String keySearch, String typeSort, String countryCode, double minimumPrice, double maximumPrice, long subCategoryId,String email, int startIdx, int numProduct) {
        try {
            SearchAndFilterUtil searchAndFilterUtil = new SearchAndFilterUtil(keySearch, typeSort, countryCode, minimumPrice, maximumPrice, subCategoryId);
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = searchAndFilterUtil.filterProduct();
            if(productList== null){
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            List<Long> soldProducts = new ArrayList<>();
            for (long pId : new ArrayList<>(productList)) {
                ProductEntity product = database.getProductEntityHashMap().get(pId);
                if (product != null && product.getStatus().equals(ProductEntity.STATUS_SOLD)) {
                    soldProducts.add(pId);
                    productList.remove(pId);
                }
            }
            productList.addAll(soldProducts);
            int endIdx = startIdx+numProduct;
            if(endIdx>productList.size()-1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx,endIdx);
            for (Long id : subList) {
                ProductEntity product = database.getProductEntityHashMap().get(id);
                jsonArray.put(product.toProductSortDetailJSON(email));
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }
    public JSONObject searchProduct(String keySearch, String typeSort,String email, int startIdx, int numProduct) {
        try {
            SearchAndFilterUtil searchAndFilterUtil = new SearchAndFilterUtil();
            searchAndFilterUtil.setKeySearch(keySearch);
            searchAndFilterUtil.setTypeSort(typeSort);
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            List<Long> productList = searchAndFilterUtil.getProductByKeySearch(new ArrayList<>(database.getProductEntityHashMap().keySet()));
            if(typeSort.equals(SearchAndFilterUtil.POPULAR))
                productList = searchAndFilterUtil.sortProductByPopular(productList);
            else
                productList = searchAndFilterUtil.sortProductByLastest(productList);
            if(productList== null){
                jsonObject.put("numProduct",0);
                jsonObject.put("listProduct",jsonArray);
                return result.put(ResponseController.CONTENT, jsonObject);
            }
            List<Long> soldProducts = new ArrayList<>();
            for (long pId : new ArrayList<>(productList)) {
                ProductEntity product = database.getProductEntityHashMap().get(pId);
                if (product != null && product.getStatus().equals(ProductEntity.STATUS_SOLD)) {
                    soldProducts.add(pId);
                    productList.remove(pId);
                }
            }
            productList.addAll(soldProducts);
            int endIdx = startIdx+numProduct;
            if(endIdx>productList.size()-1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx,endIdx);
            for (Long id : subList) {
                ProductEntity product = database.getProductEntityHashMap().get(id);
                jsonArray.put(product.toProductSortDetailJSON(email));
            }
            jsonObject.put("numProduct",productList.size());
            jsonObject.put("listProduct",jsonArray);
            result.put(ResponseController.CONTENT, jsonObject);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }
    public List<Long> getTopBestProduct() {
        List<Long> lstProductId = new ArrayList<>(Database.getInstance().getStuffLikedRFbyProductId().keySet());
        Collections.sort(lstProductId, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                int size1 = Database.getInstance().getStuffLikedRFbyProductId().get(o1).size();
                int size2 = Database.getInstance().getStuffLikedRFbyProductId().get(o1).size();
                if (size1 < size2)
                    return -1;
                return 0;
            }
        });
        return lstProductId;
    }

}
