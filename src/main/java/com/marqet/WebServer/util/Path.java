package com.marqet.WebServer.util;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Created by hpduy17 on 3/17/15.
 */
public class Path {

    @NotNull
    private static final String dbPathRootWindows      = "C:\\MarqetData";
    @NotNull
    private static final String dbPathRootUNIX         = "/marqet/production";
    private static String dataPath, usersPath, productPath, imagePath, categoriesPath, subCategoriesPath ,thumbnailPath,otherPath;
    private static String serverAddress = "";
    private static String defaultBigBannerImagePath ="";
    private static String defaultSmallBannerImagePath ="";
    private static String defaultCategoryImagePath ="";
    public static String getRoot()
    {
        if (File.separator.equals("\\"))
            return dbPathRootWindows;
        else
            return dbPathRootUNIX;
    }

    public static void buildRoot() throws IOException {
        String root;
        if (File.separator.equals("\\"))
            root = dbPathRootWindows;
        else
            root =  dbPathRootUNIX ;
        dataPath = root + File.separator + "data";
        imagePath =root + File.separator + "images";
        usersPath = imagePath + File.separator + "users";
        productPath = imagePath + File.separator + "products";
        thumbnailPath = imagePath + File.separator + "thumbnail";
        categoriesPath = imagePath + File.separator + "categories";
        subCategoriesPath = imagePath + File.separator + "subCategories";
        otherPath = imagePath + File.separator + "other";
        File fileData = new File(dataPath);
        File fileUsers = new File(usersPath);
        File fileProducts = new File(productPath);
        File fileImages = new File(imagePath);
        File fileThumbnail = new File(thumbnailPath);
        File fileCategories = new File(categoriesPath);
        File fileSubCategories = new File(subCategoriesPath);
        File fileOther = new File(otherPath);
        if(!fileData.exists()){
            fileData.mkdirs();
        }
        if (!fileUsers.exists()){
            fileUsers.mkdirs();
        }
        if(!fileImages.exists()){
            fileImages.mkdirs();
        }
        if(!fileThumbnail.exists()){
            fileThumbnail.mkdirs();
        }
        if(!fileProducts.exists()){
            fileProducts.mkdirs();
        }
        if(!fileCategories.exists()){
            fileCategories.mkdirs();
        }
        if(!fileSubCategories.exists()){
            fileSubCategories.mkdirs();
        }
        if(!fileOther.exists()){
            fileOther.mkdirs();
        }
        defaultBigBannerImagePath = otherPath+File.separator+"default-bigbanner.png";
        defaultSmallBannerImagePath = otherPath+File.separator+"default-smallbanner.png";
        defaultCategoryImagePath = otherPath+File.separator+"default-category.png";


    }

    @NotNull
    public static String getDbPathRootWindows() {
        return dbPathRootWindows;
    }

    @NotNull
    public static String getDbPathRootUNIX() {
        return dbPathRootUNIX;
    }

    public static String getDataPath() {
        return dataPath;
    }

    public static String getUsersPath() {
        return usersPath;
    }

    public static String getProductPath() {
        return productPath;
    }

    public static String getImagePath() {
        return imagePath;
    }

    public static String getCategoriesPath() {
        return categoriesPath;
    }

    public static String getSubCategoriesPath() {
        return subCategoriesPath;
    }

    public static String getThumbnailPath() {
        return thumbnailPath;
    }

    public static void setThumbnailPath(String thumbnailPath) {
        Path.thumbnailPath = thumbnailPath;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(String serverAddress) {
        Path.serverAddress = serverAddress;
    }

    public static String getOtherPath() {
        return otherPath;
    }

    public static String getDefaultBigBannerImagePath() {
        return defaultBigBannerImagePath;
    }

    public static String getDefaultSmallBannerImagePath() {
        return defaultSmallBannerImagePath;
    }

    public static String getDefaultCategoryImagePath() {
        return defaultCategoryImagePath;
    }
}

