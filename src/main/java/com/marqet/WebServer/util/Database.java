package com.marqet.WebServer.util;

import com.marqet.WebServer.dao.*;
import com.marqet.WebServer.pojo.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by hpduy17 on 3/15/15.
 */
public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }

    /*DATABASE-TABLE*/
    private LinkedHashMap<Long, ActivityEntity> activityEntityHashMap = new LinkedHashMap<>();
    private LinkedHashMap<Long, BigBannerEntity> bigBannerEntityHashMap = new LinkedHashMap<>();
    private LinkedHashMap<Long, CategoryEntity> categoryEntityHashMap = new LinkedHashMap<>();
    private HashMap<String, CityEntity> cityEntityHashMap = new HashMap<>();
    private HashMap<String, StateEntity> stateEntityHashMap = new HashMap<>();
    private HashMap<Long, CommentEntity> commentEntityHashMap = new HashMap<>();
    private HashMap<String, CountryEntity> countryEntityHashMap = new HashMap<>();
    private LinkedHashMap<Long, FeedbackEntity> feedbackEntityHashMap = new LinkedHashMap<>();
    private HashMap<Long, FollowEntity> followEntityList = new HashMap<>();
    private HashMap<Long, MessageEntity> messageEntityHashMap = new HashMap<>();
    private HashMap<Long, ProductEntity> productEntityHashMap = new HashMap<>();
    private LinkedHashMap<Long, SmallBannerEntity> smallBannerEntityHashMap = new LinkedHashMap<>();
    private HashMap<Long, SubCategoryEntity> subCategoryEntityHashMap = new HashMap<>();
    private HashMap<String, UserEntity> userEntityHashMap = new HashMap<>();
    private HashMap<String, AccountEntity> accountEntityHashMap = new HashMap<>();
    private HashMap<Long, OfferEntity> offerEntityHashMap = new HashMap<>();
    private HashMap<Long, StuffLikedEntity> stuffLikedEntityHashMap = new HashMap<>();
    private HashMap<Long, WatchListEntity> watchListEntityHashMap = new HashMap<>();
    private HashMap<Long, SubMessageEntity> subMessageEntityHashMap = new HashMap<>();
    private ElementEntity elementEntity = new ElementEntity();
    private InformationEntity informationEntity = new InformationEntity();

    /*REFERENCE AND QUERY*/
    private HashMap<String, String> facebookRF = new HashMap<>();//<fbId, email>
    private HashMap<String, String> googleplushRF = new HashMap<>();//<ggId, email>
    private HashMap<Long, List<Long>> subCategoryRFbyCategoryId = new HashMap<>();//<categoryId,List<subCategory>>
    private HashMap<String, List<String>> cityRFbyCountryCode = new HashMap<>();//<countryCode,List<cityCode>>
    private HashMap<String, List<String>> stateRFbyCountryCode = new HashMap<>();//<countryCode,List<cityCode>>
    private HashMap<String, HashMap<Long,List<Long>>> productRFbyCountryCode = new HashMap<>();//<countryCode,hashMap<subCategoryId,List<productId>>
    private HashMap<String, List<Long>> productRFbyEmail = new HashMap<>();//<email,List<productId>>
    private HashMap<Long, List<Long>> commentRFbyProductId = new HashMap<>();//<productId,List<commentId>>
    private HashMap<Long, List<Long>> stuffLikedRFbyProductId = new HashMap<>();//<productId,List<stuffLikeId>>
    private HashMap<String, List<Long>> stuffLikedRFbyEmail = new HashMap<>();//<email,List<stuffLikeId>>
    private HashMap<String, Long> stuffLikedRFbyEmailAndProductId = new HashMap<>();//<email#productId,List<stuffLikeId>>
    private HashMap<String, List<Long>> offerRFbyEmail = new HashMap<>();//<email,List<offerId>>
    private HashMap<String, Long> followMapIdRF = new HashMap<>();//<follower#befollower,followId>
    private HashMap<String, List<String>> followerRF = new HashMap<>();//<beFollower,List<follower>>
    private HashMap<String, List<String>> followingRF = new HashMap<>();//<follower, List<beFollower>>
    private HashMap<String,List<Long>> buyerMessRFEmail = new HashMap<>();//<email,List<MessageId>>
    private HashMap<String,List<Long>> sellerMessRFEmail = new HashMap<>();//<email, List<MessageId>
    private HashMap<String,List<Long>> buyerFeedbackRFEmail = new HashMap<>();//<email, List<MessageId>>
    private HashMap<String,List<Long>> sellerFeedbackRFEmail = new HashMap<>();//<email, List<MessageId>>
    private HashMap<Long,List<Long>> subMessagesRFMessageId = new HashMap<>();//<MessageId,List<SubMessageId>>
    private HashMap<String,List<Long>> activityRFEmail = new HashMap<>();//<email, List<activityId>>
    private HashMap<String,List<Long>> requiredFeedback = new HashMap<>();//<email,List<feedbackId>>
    private HashMap<String,List<Long>> waitOfferReply = new HashMap<>();//<email,List<OfferId>>
    /*FINAL INSTANCE*/
    public static final String DEFAULT_PASSWORD = "MarqetMobileAppDevelopByHeroTeam";
    /*PERSONAL FUNCTION*/

    public static void restore() {
        //restore table from database
        ElementDao.backUpDatabase();
        InformationDao.backUpDatabase();
        AccountDao.backUpDatabase();
        CountryDao.backUpDatabase();
        CityDao.backUpDatabase();
        StateDao.backUpDatabase();
        UserDao.backUpDatabase();
        BigBannerDao.backUpDatabase();
        SmallBannerDao.backUpDatabase();
        CategoryDao.backUpDatabase();
        SubCategoryDao.backUpDatabase();
        ProductDao.backUpDatabase();
        StuffLikedDao.backUpDatabase();
        OfferDao.backUpDatabase();
        FollowDao.backUpDatabase();
        ActivityDao.backUpDatabase();
        FeedbackDao.backUpDatabase();
        MessageDao.backUpDatabase();
        CommentDao.backUpDatabase();
        SubMessageDao.backUpDatabase();
        WatchListDao.backUpDatabase();


    }

    public static void backup() {

    }

    public HashMap<String,String> convertArrStringToHashMap(String[] src, String charSplit){
        HashMap<String,String> result= new HashMap<>();
        for(String temp :src){
            try {
                String [] x = temp.split(charSplit);
                result.put(x[0],x[1]);
            }catch (Exception ignored){

            }
        }
        return result;
    }
    /*GET-SET Function*/

    public LinkedHashMap<Long, ActivityEntity> getActivityEntityHashMap() {
        return activityEntityHashMap;
    }

    public void setActivityEntityHashMap(LinkedHashMap<Long, ActivityEntity> activityEntityHashMap) {
        this.activityEntityHashMap = activityEntityHashMap;
    }

    public LinkedHashMap<Long, BigBannerEntity> getBigBannerEntityHashMap() {
        return bigBannerEntityHashMap;
    }

    public void setBigBannerEntityHashMap(LinkedHashMap<Long, BigBannerEntity> bigBannerEntityHashMap) {
        this.bigBannerEntityHashMap = bigBannerEntityHashMap;
    }

    public LinkedHashMap<Long, CategoryEntity> getCategoryEntityHashMap() {
        return categoryEntityHashMap;
    }

    public void setCategoryEntityHashMap(LinkedHashMap<Long, CategoryEntity> categoryEntityHashMap) {
        this.categoryEntityHashMap = categoryEntityHashMap;
    }

    public HashMap<String, CityEntity> getCityEntityHashMap() {
        return cityEntityHashMap;
    }

    public void setCityEntityHashMap(HashMap<String, CityEntity> cityEntityHashMap) {
        this.cityEntityHashMap = cityEntityHashMap;
    }

    public HashMap<Long, CommentEntity> getCommentEntityHashMap() {
        return commentEntityHashMap;
    }

    public void setCommentEntityHashMap(HashMap<Long, CommentEntity> commentEntityHashMap) {
        this.commentEntityHashMap = commentEntityHashMap;
    }

    public HashMap<String, CountryEntity> getCountryEntityHashMap() {
        return countryEntityHashMap;
    }

    public void setCountryEntityHashMap(HashMap<String, CountryEntity> countryEntityHashMap) {
        this.countryEntityHashMap = countryEntityHashMap;
    }

    public HashMap<String, StateEntity> getStateEntityHashMap() {
        return stateEntityHashMap;
    }

    public LinkedHashMap<Long, FeedbackEntity> getFeedbackEntityHashMap() {
        return feedbackEntityHashMap;
    }

    public void setFeedbackEntityHashMap(LinkedHashMap<Long, FeedbackEntity> feedbackEntityHashMap) {
        this.feedbackEntityHashMap = feedbackEntityHashMap;
    }

    public HashMap<Long, FollowEntity> getFollowEntityList() {
        return followEntityList;
    }

    public void setFollowEntityList(HashMap<Long, FollowEntity> followEntityList) {
        this.followEntityList = followEntityList;
    }

    public HashMap<Long, MessageEntity> getMessageEntityHashMap() {
        return messageEntityHashMap;
    }

    public void setMessageEntityHashMap(HashMap<Long, MessageEntity> messageEntityHashMap) {
        this.messageEntityHashMap = messageEntityHashMap;
    }

    public HashMap<Long, ProductEntity> getProductEntityHashMap() {
        return productEntityHashMap;
    }

    public void setProductEntityHashMap(HashMap<Long, ProductEntity> productEntityHashMap) {
        this.productEntityHashMap = productEntityHashMap;
    }

    public LinkedHashMap<Long, SmallBannerEntity> getSmallBannerEntityHashMap() {
        return smallBannerEntityHashMap;
    }

    public void setSmallBannerEntityHashMap(LinkedHashMap<Long, SmallBannerEntity> smallBannerEntityHashMap) {
        this.smallBannerEntityHashMap = smallBannerEntityHashMap;
    }

    public HashMap<Long, SubCategoryEntity> getSubCategoryEntityHashMap() {
        return subCategoryEntityHashMap;
    }

    public void setSubCategoryEntityHashMap(HashMap<Long, SubCategoryEntity> subCategoryEntityHashMap) {
        this.subCategoryEntityHashMap = subCategoryEntityHashMap;
    }

    public HashMap<String, UserEntity> getUserEntityHashMap() {
        return userEntityHashMap;
    }

    public void setUserEntityHashMap(HashMap<String, UserEntity> userEntityHashMap) {
        this.userEntityHashMap = userEntityHashMap;
    }

    public HashMap<Long, OfferEntity> getOfferEntityHashMap() {
        return offerEntityHashMap;
    }

    public void setOfferEntityHashMap(HashMap<Long, OfferEntity> offerEntityHashMap) {
        this.offerEntityHashMap = offerEntityHashMap;
    }

    public HashMap<Long, StuffLikedEntity> getStuffLikedEntityHashMap() {
        return stuffLikedEntityHashMap;
    }

    public void setStuffLikedEntityHashMap(HashMap<Long, StuffLikedEntity> stuffLikedEntityHashMap) {
        this.stuffLikedEntityHashMap = stuffLikedEntityHashMap;
    }

    public HashMap<Long, WatchListEntity> getWatchListEntityHashMap() {
        return watchListEntityHashMap;
    }

    public HashMap<Long, SubMessageEntity> getSubMessageEntityHashMap() {
        return subMessageEntityHashMap;
    }

    public ElementEntity getElementEntity() {
        return elementEntity;
    }

    public void setElementEntity(ElementEntity elementEntity) {
        this.elementEntity = elementEntity;
    }

    public InformationEntity getInformationEntity() {
        return informationEntity;
    }

    public void setInformationEntity(InformationEntity informationEntity) {
        this.informationEntity = informationEntity;
    }

    public HashMap<String, String> getFacebookRF() {
        return facebookRF;
    }

    public void setFacebookRF(HashMap<String, String> facebookRF) {
        this.facebookRF = facebookRF;
    }

    public HashMap<String, String> getGoogleplushRF() {
        return googleplushRF;
    }

    public void setGoogleplushRF(HashMap<String, String> googleplushRF) {
        this.googleplushRF = googleplushRF;
    }

    public HashMap<Long, List<Long>> getSubCategoryRFbyCategoryId() {
        return subCategoryRFbyCategoryId;
    }

    public void setSubCategoryRFbyCategoryId(HashMap<Long, List<Long>> subCategoryRFbyCategoryId) {
        this.subCategoryRFbyCategoryId = subCategoryRFbyCategoryId;
    }

    public HashMap<String, List<String>> getCityRFbyCountryCode() {
        return cityRFbyCountryCode;
    }

    public void setCityRFbyCountryCode(HashMap<String, List<String>> cityRFbyCountryCode) {
        this.cityRFbyCountryCode = cityRFbyCountryCode;
    }

    public HashMap<String, HashMap<Long, List<Long>>> getProductRFbyCountryCode() {
        return productRFbyCountryCode;
    }

    public HashMap<Long, List<Long>> getCommentRFbyProductId() {
        return commentRFbyProductId;
    }

    public void setCommentRFbyProductId(HashMap<Long, List<Long>> commentRFbyProductId) {
        this.commentRFbyProductId = commentRFbyProductId;
    }

    public HashMap<Long, List<Long>> getStuffLikedRFbyProductId() {
        return stuffLikedRFbyProductId;
    }

    public void setStuffLikedRFbyProductId(HashMap<Long, List<Long>> stuffLikedRFbyProductId) {
        this.stuffLikedRFbyProductId = stuffLikedRFbyProductId;
    }

    public HashMap<String, List<Long>> getStuffLikedRFbyEmail() {
        return stuffLikedRFbyEmail;
    }

    public HashMap<String, List<Long>> getOfferRFbyEmail() {
        return offerRFbyEmail;
    }

    public HashMap<String, List<String>> getFollowerRF() {
        return followerRF;
    }

    public HashMap<String, List<String>> getFollowingRF() {
        return followingRF;
    }

    public HashMap<String, Long> getFollowMapIdRF() {
        return followMapIdRF;
    }

    public HashMap<String, List<Long>> getProductRFbyEmail() {
        return productRFbyEmail;
    }

    public HashMap<String, List<Long>> getBuyerMessRFEmail() {
        return buyerMessRFEmail;
    }

    public HashMap<String, List<Long>> getSellerMessRFEmail() {
        return sellerMessRFEmail;
    }

    public HashMap<Long, List<Long>> getSubMessagesRFMessageId() {
        return subMessagesRFMessageId;
    }

    public HashMap<String, List<Long>> getActivityRFEmail() {
        return activityRFEmail;
    }

    public HashMap<String, List<String>> getStateRFbyCountryCode() {
        return stateRFbyCountryCode;
    }

    public HashMap<String, Long> getStuffLikedRFbyEmailAndProductId() {
        return stuffLikedRFbyEmailAndProductId;
    }

    public HashMap<String, List<Long>> getBuyerFeedbackRFEmail() {
        return buyerFeedbackRFEmail;
    }

    public HashMap<String, List<Long>> getSellerFeedbackRFEmail() {
        return sellerFeedbackRFEmail;
    }

    public HashMap<String, AccountEntity> getAccountEntityHashMap() {
        return accountEntityHashMap;
    }

    public HashMap<String, List<Long>> getRequiredFeedback() {
        return requiredFeedback;
    }

    public HashMap<String, List<Long>> getWaitOfferReply() {
        return waitOfferReply;
    }
}
