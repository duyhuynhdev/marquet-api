package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.ProductDao;
import com.marqet.WebServer.pojo.*;
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
            List<Long> stuffLikedList = new ArrayList<>();
            try{
                stuffLikedList = new ArrayList<>(database.getStuffLikedRFbyEmail().get(email));
            }catch (Exception ignored){}
            if (stuffLikedList == null) {
                return responseJSON.put(ResponseController.CONTENT, new JSONArray());
            }
            JSONArray jsonArray = new JSONArray();
            int endIdx = startIdx + numProduct;
            if (endIdx > stuffLikedList.size() - 1)
                endIdx = stuffLikedList.size();
            List<Long> subList = stuffLikedList.subList(startIdx, endIdx);
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

    public JSONObject getListProductByOffer(String email, int startIdx, int numProduct) {
        try {
            JSONObject responseJSON = ResponseController.createSuccessJSON();
            List<Long> offerList = new ArrayList<>();
            try{
                offerList = new ArrayList<>(database.getOfferRFbyEmail().get(email));
            }catch (Exception ex){}
            if (offerList == null) {
                return responseJSON.put(ResponseController.CONTENT, new JSONArray());
            }
            JSONArray jsonArray = new JSONArray();
            int endIdx = startIdx + numProduct;
            if (endIdx > offerList.size() - 1)
                endIdx = offerList.size();
            List<Long> subList = offerList.subList(startIdx, endIdx);
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

    public JSONObject getListProductByCountryCode(String countryCode, long subCategoryId, String typeSort, String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = new ArrayList<>();
            try{
                productList = new ArrayList<>(database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId));
            }catch (Exception ignored){}
            if (productList == null) {
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

    public JSONObject getListProductByCountryCode(String countryCode, long subCategoryId, String typeSort, int startIdx, int numProduct, String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = new ArrayList<>();
            try{
                productList = new ArrayList<>(database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId));
            }catch (Exception ignored){}
            if (productList == null) {
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            if (productList != null) {
                if (typeSort.equals(SearchAndFilterUtil.LASTEST))
                    productList = new SearchAndFilterUtil().sortProductByLastest(productList);
                else
                    productList = new SearchAndFilterUtil().sortProductByPopular(productList);
                int count = 0;
                for (int i = startIdx; count < numProduct && i < productList.size(); i++) {
                    ProductEntity product = database.getProductEntityHashMap().get(productList.get(i));
                    if (product != null && product.getStatus().equals(ProductEntity.STATUS_AVAILABLE)) {
                        jsonArray.put(product.toProductSortDetailJSON(email));
                        count++;
                    }
                }
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByCategory(String countryCode, long categoryId, String typeSort, int startIdx, int numProduct, String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = new ArrayList<>();
            List<Long> subCategoryList = new ArrayList<>();
            try{
                subCategoryList = new ArrayList<>(database.getSubCategoryRFbyCategoryId().get(categoryId));
            }catch (Exception ignored){}
            if (subCategoryList != null) {
                for (long subCategoryId : subCategoryList) {
                    List<Long> subProductList = null;
                    try {
                        subProductList = new ArrayList<>(database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId));
                    } catch (Exception ignored) {

                    }
                    if (subProductList != null) {
                        productList.addAll(subProductList);
                    }
                }
            }
            if (productList == null) {
                return result.put(ResponseController.CONTENT, new JSONArray());
            }
            if (productList != null) {
                if (typeSort.equals(SearchAndFilterUtil.LASTEST))
                    productList = new SearchAndFilterUtil().sortProductByLastest(productList);
                else
                    productList = new SearchAndFilterUtil().sortProductByPopular(productList);
                int count = 0;
                for (int i = startIdx; count < numProduct && i < productList.size(); i++) {
                    ProductEntity product = database.getProductEntityHashMap().get(productList.get(i));
                    if (product != null && product.getStatus().equals(ProductEntity.STATUS_AVAILABLE)) {
                        jsonArray.put(product.toProductSortDetailJSON(email));
                        count++;
                    }
                }
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getProductDetail(String email, long productId) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            result.put(ResponseController.CONTENT, product.toProductDetailJSON(email));
            if (!product.getEmail().equals(email)) {
                HashMap<Long, Long> watchProductLog = database.getWatchingProductLogHashMap().get(email);
                if (watchProductLog != null && watchProductLog.containsKey(productId)) {
                    System.out.println(new ActivityController().updateTimeWatchProducActivity(watchProductLog.get(productId)));
                } else {
                    System.out.println(new ActivityController().insertActivity(email, product.getEmail(), ActivityUtil.WATCHING_PRODUCT, product.getId()));
                }
            }
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject addProduct(String email, String title, String cityCode, long price, long subCategoryId, String description, List<Part> productImages, List<Part> videoAndThumbnail) {
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
                int idx = 1;
                for (Part p : productImages) {
                    if (p.getSize() > 0) {
                        String fileName = "product_" + idx + "_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                        String imgName = new UploadImageUtil().uploadProduct(fileName, Path.getProductPath(), p);
                        productImagesFull.put(imgName);
                        productImagesThumbnail.put(new UploadImageUtil().uploadThumbnail(fileName, p));
                        idx++;
                    } else {
                        productImagesFull.put(Path.getDefaultBigBannerImagePath());
                        productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                        idx++;
                    }
                }
            } else {
                productImagesFull.put(Path.getDefaultBigBannerImagePath());
                productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
            }
            JSONArray productVideo = new JSONArray();
            if(videoAndThumbnail != null && videoAndThumbnail.size() >=2){
                Part videoPart = videoAndThumbnail.get(0);
                String fileName = "product_video_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                String videoName = new VideoProcessor().convertVideoFromMOVToMP4(fileName, Path.getProductPath(), videoPart);
                Part videoThumbPart = videoAndThumbnail.get(1);
                String thumbfileName = "product_videoThumb_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                String videoThumbName = new UploadImageUtil().uploadProduct(thumbfileName, Path.getProductPath(), videoThumbPart);
                productVideo.put(videoName);
                productVideo.put(videoThumbName);
            }
            productImagesJSON.put("full", productImagesFull);
            productImagesJSON.put("thumbnail", productImagesThumbnail);
            product.setProductImages(productImagesJSON.toString());
            product.setProductVideo(productVideo.toString());
            product.setEmail(email);
            database.getProductEntityHashMap().put(product.getId(), product);
            //put to productRFCountry
            HashMap<String, CityEntity> cityHashMap = database.getCityEntityHashMap();
            String countryCode = cityHashMap.get(product.getCityCode()).getCountryCode();
            HashMap<Long, HashSet<Long>> productFRSubCategory = database.getProductRFbyCountryCode().get(countryCode);
            if (productFRSubCategory == null)
                productFRSubCategory = new HashMap<>();
            HashSet<Long> productList = productFRSubCategory.get(product.getSubCategoryId());
            if (productList == null) {
                productList = new HashSet<>();
            }
            productList.add(product.getId());
            productFRSubCategory.put(product.getSubCategoryId(), productList);
            database.getProductRFbyCountryCode().put(countryCode, productFRSubCategory);
            //put to productRFEmail
            HashSet<Long> productRFEmail = database.getProductRFbyEmail().get(product.getEmail());
            if (productRFEmail == null)
                productRFEmail = new HashSet<>();
            productRFEmail.add(product.getId());
            database.getProductRFbyEmail().put(email, productRFEmail);
            //save to database
            if (dao.insert(product)) {
                responseJSON = ResponseController.createSuccessJSON();
                responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON(email));
                try {
                    new ActivityController().insertActivity(email, "", ActivityUtil.UPLOAD_PRODUCT, product.getId());
                } catch (Exception ignored) {

                }
                try{
                    new WatchListController().noticeWatchListOwner(product.getId());
                }catch (Exception ignored){}
            } else {
                responseJSON = ResponseController.createFailJSON("Cannot insert to database\n");
            }
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject editProduct(long productId, String newTitle, String newCityCode, long newPrice, long newSubCategoryId, String newDescription, List<Part> newProductImages, JSONArray oldProductImagesIdx,List<Part> videoAndThumbnail) {
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
                    int idx = 0;
                    for (Part p : newProductImages) {
                        if (p.getSize() > 0) {
                            String fileName = "product_" + idx + "_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                            String imgName = new UploadImageUtil().uploadProduct(fileName, Path.getProductPath(), p);
                            productImagesFull.put(imgName);
                            String[] temp = imgName.split("[.]");
                            if (!temp[temp.length - 1].contains("mp4")) {
                                productImagesThumbnail.put(new UploadImageUtil().uploadThumbnail(fileName, p));
                            }
                            idx++;
                        } else {
                            productImagesFull.put(Path.getDefaultBigBannerImagePath());
                            productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                            idx++;
                        }
                    }
                } else {
                    productImagesFull.put(Path.getDefaultBigBannerImagePath());
                    productImagesThumbnail.put(Path.getDefaultSmallBannerImagePath());
                }
                JSONArray productVideo = new JSONArray();
                if(videoAndThumbnail != null && videoAndThumbnail.size() >=2){
                    Part videoPart = videoAndThumbnail.get(0);
                    String fileName = "product_video_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                    String videoName = new VideoProcessor().convertVideoFromMOVToMP4(fileName, Path.getProductPath(), videoPart);
                    Part videoThumbPart = videoAndThumbnail.get(1);
                    String thumbfileName = "product_videoThumb_" + product.getId() + "_" + AccentRemoveAlgorithm.removeAccent(product.getName()).replace(" ", "");
                    String videoThumbName = new UploadImageUtil().uploadProduct(thumbfileName, Path.getProductPath(), videoThumbPart);
                    productVideo.put(videoName);
                    productVideo.put(videoThumbName);
                }
                productImagesJSON.put("full", productImagesFull);
                productImagesJSON.put("thumbnail", productImagesFull);
                product.setProductImages(productImagesJSON.toString());
                product.setProductVideo(productVideo.toString());
                database.getProductEntityHashMap().put(productId, product);
                //put to productRFCountry
                //1. remove old RF
                String oldCountryCode = database.getCityEntityHashMap().get(oldCityCode).getCountryCode();
                database.getProductRFbyCountryCode().get(oldCountryCode).get(oldSubcategory).remove(product.getId());
                //2. add new RF
                HashMap<String, CityEntity> cityHashMap = database.getCityEntityHashMap();
                String countryCode = cityHashMap.get(product.getCityCode()).getCountryCode();
                HashMap<Long, HashSet<Long>> productFRSubCategory = database.getProductRFbyCountryCode().get(countryCode);
                if (productFRSubCategory == null)
                    productFRSubCategory = new HashMap<>();
                HashSet<Long> productList = productFRSubCategory.get(product.getSubCategoryId());
                if (productList == null) {
                    productList = new HashSet<>();
                }
                productList.add(product.getId());
                productFRSubCategory.put(product.getSubCategoryId(), productList);
                database.getProductRFbyCountryCode().put(countryCode, productFRSubCategory);
                if (dao.update(product)) {
                    try{
                        new WatchListController().noticeWatchListOwner(product.getId());
                    }catch (Exception ignored){}
                    responseJSON = ResponseController.createSuccessJSON();
                    responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON(product.getEmail()));
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
            List<Long> productList = new ArrayList<>();
            try{
                productList = new ArrayList<>(database.getProductRFbyEmail().get(email));
            }catch (Exception ignored){}
            if (productList == null) {
                jsonObject.put("numProduct", 0);
                jsonObject.put("listProduct", jsonArray);
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
            int endIdx = startIdx + numProduct;
            if (endIdx > productList.size() - 1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx, endIdx);
            for (Long id : subList) {
                ProductEntity product = database.getProductEntityHashMap().get(id);
                jsonArray.put(product.toProductSortDetailJSON(email));
            }
            jsonObject.put("numProduct", productList.size());
            jsonObject.put("listProduct", jsonArray);
            result.put(ResponseController.CONTENT, jsonObject);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

    public JSONObject getListProductByFilter(String keySearch, String typeSort, double minimumPrice, double maximumPrice, long subCategoryId, String email, int startIdx, int numProduct) {
        try {
            UserEntity user = database.getUserEntityHashMap().get(email);
            SearchAndFilterUtil searchAndFilterUtil = new SearchAndFilterUtil(keySearch, typeSort, user.getCountryCode(), minimumPrice, maximumPrice, subCategoryId);
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            List<Long> productList = searchAndFilterUtil.filterProduct();
            if (productList == null) {
                if (TempData.isTemp) {
                    productList = TempData.tempProduct();
                } else {
                    return result.put(ResponseController.CONTENT, new JSONArray());
                }
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
            int endIdx = startIdx + numProduct;
            if (endIdx > productList.size() - 1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx, endIdx);
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

    public JSONObject searchProduct(String keySearch, String typeSort, String email, int startIdx, int numProduct) {
        try {
            SearchAndFilterUtil searchAndFilterUtil = new SearchAndFilterUtil();
            searchAndFilterUtil.setKeySearch(keySearch);
            searchAndFilterUtil.setTypeSort(typeSort);
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            List<Long> productList = searchAndFilterUtil.getProductByKeySearch(new ArrayList<>(database.getProductEntityHashMap().keySet()));
            if (typeSort.equals(SearchAndFilterUtil.POPULAR))
                productList = searchAndFilterUtil.sortProductByPopular(productList);
            else
                productList = searchAndFilterUtil.sortProductByLastest(productList);
            if (productList == null) {
                jsonObject.put("numProduct", 0);
                jsonObject.put("listProduct", jsonArray);
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
            int endIdx = startIdx + numProduct;
            if (endIdx > productList.size() - 1)
                endIdx = productList.size();
            List<Long> subList = productList.subList(startIdx, endIdx);
            for (Long id : subList) {
                ProductEntity product = database.getProductEntityHashMap().get(id);
                jsonArray.put(product.toProductSortDetailJSON(email));
            }
            jsonObject.put("numProduct", productList.size());
            jsonObject.put("listProduct", jsonArray);
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

    public JSONObject markAsSold(String email, long productId) {
        try {
            JSONObject responseJSON;
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            if (product != null) {
                if (product.getEmail().equals(email)) {
                    product.setStatus(ProductEntity.STATUS_SOLD);
                    database.getProductEntityHashMap().put(productId, product);
                    if (dao.update(product)) {
                        responseJSON = ResponseController.createSuccessJSON();
                        responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON(email));
                    } else {
                        responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
                    }
                } else {
                    responseJSON = ResponseController.createFailJSON("This is not your product\n");
                }
            } else {
                responseJSON = ResponseController.createFailJSON("Product not found\n");
            }
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public synchronized JSONObject markAsSold(String email, long productId, long messageId, long tempSubMessageId, String receiverEmail) {
        try {
            JSONObject responseJSON;
            ProductEntity product = database.getProductEntityHashMap().get(productId);
            if (product != null) {
                if (product.getEmail().equals(email)) {
                    product.setStatus(ProductEntity.STATUS_SOLD);
                    database.getProductEntityHashMap().put(productId, product);
                    if (dao.update(product)) {
                        try {
                            return new SubMessageController().markAsSoldMessage(email, messageId, tempSubMessageId, receiverEmail);
                        } catch (Exception ignored) {
                        }
                        responseJSON = ResponseController.createSuccessJSON();
                        responseJSON.put(ResponseController.CONTENT, product.toProductDetailJSON(email));
                    } else {
                        responseJSON = ResponseController.createFailJSON("Cannot update to database\n");
                    }
                } else {
                    responseJSON = ResponseController.createFailJSON("This is not your product\n");
                }
            } else {
                responseJSON = ResponseController.createFailJSON("Product not found\n");
            }
            return responseJSON;
        } catch (Exception ex) {
            return ResponseController.createErrorJSON(ex.getMessage());
        }
    }

    public JSONObject getList30ProductNearest(String email) {
        try {
            JSONObject result = ResponseController.createSuccessJSON();
            JSONArray jsonArray = new JSONArray();
            HashMap<Long, Long> watchingProductLog = database.getWatchingProductLogHashMap().get(email);
            if (watchingProductLog != null) {
                List<Long> listActivity = new ArrayList<>();
                try{
                    listActivity = new ArrayList<>(watchingProductLog.values());
                }catch (Exception ignored){}
                Collections.sort(listActivity, new Comparator<Long>() {
                    @Override
                    public int compare(Long o1, Long o2) {
                        ActivityEntity a1 = database.getActivityEntityHashMap().get(o1);
                        ActivityEntity a2 = database.getActivityEntityHashMap().get(o2);
                        if(a1!=null && a2 !=null) {
                            if (a1.getDate() > a2.getDate()) {
                                return -1;
                            }
                            return 1;
                        }
                        return 0;
                    }
                });
                int numProduct = 0;
                for(int i = 0 ; i < listActivity.size() && numProduct < 30 ; i++){
                    ActivityEntity act = database.getActivityEntityHashMap().get(listActivity.get(i));
                    ProductEntity productEntity = database.getProductEntityHashMap().get(act.getRef());
                    if(productEntity!=null){
                        jsonArray.put(productEntity.toProductSortDetailJSON(email));
                        numProduct++;
                    }
                }
            }
            result.put(ResponseController.CONTENT, jsonArray);
            return result;
        } catch (Exception e) {
            return ResponseController.createErrorJSON(e.getMessage());
        }
    }

}
