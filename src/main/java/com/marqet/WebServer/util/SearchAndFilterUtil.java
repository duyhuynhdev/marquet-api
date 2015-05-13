package com.marqet.WebServer.util;

import com.marqet.WebServer.pojo.ProductEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hpduy17 on 3/25/15.
 */
public class SearchAndFilterUtil {
    public static final String POPULAR = "popular";
    public static final String LASTEST = "lastest";
    private String keySearch = "";
    private String typeSort = LASTEST;
    private String countryCode = "";
    private double minimumPrice = 0;
    private double maximumPrice = 0;
    private long subCategoryId = 0;
    private Database database = Database.getInstance();

    public SearchAndFilterUtil() {
    }

    public SearchAndFilterUtil(String keySearch, String typeSort, String countryCode, double minimumPrice, double maximumPrice, long subCategoryId) {
        this.keySearch = keySearch;
        this.typeSort = typeSort;
        this.countryCode = countryCode;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.subCategoryId = subCategoryId;
    }

    // return number of different word
    public long textSearch(String pattern, String text) {
        long result = 0;
        String[] patternElements = pattern.split(" ");
        for (String pE : patternElements) {
            if (!text.contains(pE))
                result++;
        }
        return result;
    }

    public List<Long> filterProduct() {
        // get product by country
        List<Long> productCandidates = database.getProductRFbyCountryCode().get(countryCode).get(subCategoryId);
        try {
            if (productCandidates != null) {
                //get product by ket search
                productCandidates = getProductByKeySearch(productCandidates);
                //get product by price
                productCandidates = getProductByPrice(productCandidates);
                //sort product
                if(typeSort.equals(POPULAR))
                    productCandidates = sortProductByPopular(productCandidates);
                else
                    productCandidates = sortProductByLastest(productCandidates);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return productCandidates;
    }

    public List<Long> getProductByPrice(List<Long> productList) {
        if (maximumPrice == 0 && minimumPrice == 0)
            return productList;
        for (long id : new ArrayList<>(productList)) {
            ProductEntity product = database.getProductEntityHashMap().get(id);
            if (product.getPrice() < minimumPrice || product.getPrice() > maximumPrice)
                productList.remove(id);
        }
        return productList;
    }

    public List<Long> getProductByKeySearch(List<Long> productList) {
        for (long id : new ArrayList<>(productList)) {
            ProductEntity product = database.getProductEntityHashMap().get(id);
            if (textSearch(keySearch, product.getName()) == keySearch.length())
                productList.remove(id);
        }
        return productList;
    }

    public List<Long> sortProductByLastest(List<Long> productList) {
        Collections.sort(productList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                ProductEntity product1 = database.getProductEntityHashMap().get(o1);
                ProductEntity product2 = database.getProductEntityHashMap().get(o2);
                if (product1.getDate() > product2.getDate())
                    return -1;
                else if (product1.getDate() < product2.getDate())
                    return 1;
                return 0;
            }
        });
        return productList;
    }

    public List<Long> sortProductByPopular(List<Long> productList) {
        Collections.sort(productList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                int numLike1 = database.getStuffLikedRFbyProductId().get(o1).size();
                int numLike2 = database.getStuffLikedRFbyProductId().get(o2).size();
                if (numLike1 > numLike2)
                    return -1;
                else if (numLike2 < numLike1)
                    return 1;
                return 0;
            }
        });
        return productList;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public void setTypeSort(String typeSort) {
        this.typeSort = typeSort;
    }
}
