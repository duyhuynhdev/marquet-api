package com.marqet.WebServer.util;

import com.marqet.WebServer.controller.BigBannerController;
import com.marqet.WebServer.controller.SmallBannerController;
import com.marqet.WebServer.dao.*;
import com.marqet.WebServer.pojo.*;

import java.util.*;

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
    private HashMap<String, String> twitterRF = new HashMap<>();//<twId, email>
    private HashMap<Long, HashSet<Long>> subCategoryRFbyCategoryId = new HashMap<>();//<categoryId,List<subCategory>>
    private HashMap<String, HashSet<String>> cityRFbyCountryCode = new HashMap<>();//<countryCode,List<cityCode>>
    private HashMap<String, HashSet<String>> stateRFbyCountryCode = new HashMap<>();//<countryCode,List<cityCode>>
    private HashMap<String, HashMap<Long, HashSet<Long>>> productRFbyCountryCode = new HashMap<>();//<countryCode,hashMap<subCategoryId,List<productId>>
    private HashMap<String, HashSet<Long>> productRFbyEmail = new HashMap<>();//<email,List<productId>>
    private HashMap<Long, HashSet<Long>> commentRFbyProductId = new HashMap<>();//<productId,List<commentId>>
    private HashMap<Long, HashSet<Long>> stuffLikedRFbyProductId = new HashMap<>();//<productId,List<stuffLikeId>>
    private HashMap<String, HashSet<Long>> stuffLikedRFbyEmail = new HashMap<>();//<email,List<stuffLikeId>>
    private HashMap<String, Long> stuffLikedRFbyEmailAndProductId = new HashMap<>();//<email#productId,List<stuffLikeId>>
    private HashMap<String, HashSet<Long>> offerRFbyEmail = new HashMap<>();//<email,List<offerId>>
    private HashMap<String, Long> followMapIdRF = new HashMap<>();//<follower#befollower,followId>
    private HashMap<String, HashSet<String>> followerRF = new HashMap<>();//<beFollower,List<follower>>
    private HashMap<String, HashSet<String>> followingRF = new HashMap<>();//<follower, List<beFollower>>
    private HashMap<String, HashSet<Long>> buyerMessRFEmail = new HashMap<>();//<email,List<MessageId>>
    private HashMap<String, HashSet<Long>> sellerMessRFEmail = new HashMap<>();//<email, List<MessageId>
    private HashMap<String, HashSet<Long>> buyerFeedbackRFEmail = new HashMap<>();//<email, List<MessageId>>
    private HashMap<String, HashSet<Long>> sellerFeedbackRFEmail = new HashMap<>();//<email, List<MessageId>>
    private HashMap<Long, HashSet<Long>> subMessagesRFMessageId = new HashMap<>();//<MessageId,List<SubMessageId>>
    private HashMap<String, HashSet<Long>> activityRFEmail = new HashMap<>();//<email, List<activityId>>
    private HashMap<String, Long> feedbackRFEmailAndProduct = new HashMap<>();//<email#product,feedbackId>
    private HashMap<String, HashSet<Long>> waitOfferReply = new HashMap<>();//<email,List<OfferId>>
    private HashMap<String, Long> distinctMessage = new HashMap<>();//<email#email#productId,messageId>
    private HashMap<String, HashMap<Long, Long>> watchingProductLogHashMap = new HashMap<>(); //<email,<productId,activityId>>
    private HashMap<String, HashMap<String, String>> broadcastLogRFEmail = new HashMap<>(); //<email,<deviceId,boadcastId>>
    private HashMap<String, HashSet<Long>> watchListRFEmail = new HashMap<>(); //<email,<watchListId>>
    private HashMap<String,HashSet<String>> watchListPatternRFEmail = new HashMap<>(); //<email,<pattern>>
    //QUEUE FOR BANNER
    private LinkedHashMap<Long, SmallBannerEntity> smallBannerShowQueue = new LinkedHashMap<>();
    private LinkedHashMap<Long, SmallBannerEntity> smallBannerWaitingStack = new LinkedHashMap<>();
    private long smallPopTime;
    private LinkedHashMap<Long, BigBannerEntity> bigBannerShowQueue = new LinkedHashMap<>();
    private LinkedHashMap<Long, BigBannerEntity> bigBannerWaitingStack = new LinkedHashMap<>();
    private long bigPopTime;
    private LinkedHashMap<String, Long> bigBannerExist = new LinkedHashMap<>();
    private LinkedHashMap<String, Long> smallBannerExist = new LinkedHashMap<>();
    /*FINAL INSTANCE*/
    public static final String DEFAULT_PASSWORD = "MarqetMobileAppDevelopByHeroTeam";
    /*PERSONAL FUNCTION*/

    public void restore() {
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
        // small and big banner process
        if (bigBannerShowQueue.size() >= BigBannerDao.NUMBER_OF_BIGBANNER && bigBannerWaitingStack.size() > 0) {
            if (!BigBannerController.isThreadRun) {
                bigPopTime = new DateTimeUtil().getNow();
                Thread bigBannerTimer = new Thread(new BigBannerTimer());
                bigBannerTimer.start();
                BigBannerController.isThreadRun = true;
            }
        }
        if (smallBannerShowQueue.size() >= SmallBannerDao.NUMBER_OF_SMALLBANNER && smallBannerWaitingStack.size() > 0) {
            if (!SmallBannerController.isThreadRun) {
                smallPopTime = new DateTimeUtil().getNow();
                Thread smallBannerTimer = new Thread(new SmallBannerTimer());
                smallBannerTimer.start();
                SmallBannerController.isThreadRun = true;
            }
        }

    }

    public void backup() {

    }

    public HashMap<String, String> convertArrStringToHashMap(String[] src, String charSplit) {
        HashMap<String, String> result = new HashMap<>();
        for (String temp : src) {
            try {
                String[] x = temp.split(charSplit);
                result.put(x[0], x[1]);
            } catch (Exception ignored) {

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

    public HashMap<Long, HashSet<Long>> getSubCategoryRFbyCategoryId() {
        return subCategoryRFbyCategoryId;
    }

    public void setSubCategoryRFbyCategoryId(HashMap<Long, HashSet<Long>> subCategoryRFbyCategoryId) {
        this.subCategoryRFbyCategoryId = subCategoryRFbyCategoryId;
    }

    public HashMap<String, HashSet<String>> getCityRFbyCountryCode() {
        return cityRFbyCountryCode;
    }

    public void setCityRFbyCountryCode(HashMap<String, HashSet<String>> cityRFbyCountryCode) {
        this.cityRFbyCountryCode = cityRFbyCountryCode;
    }

    public HashMap<String, HashMap<Long, HashSet<Long>>> getProductRFbyCountryCode() {
        for (String s : productRFbyCountryCode.keySet()) {
            System.out.println(s);
            for (long id : productRFbyCountryCode.get(s).keySet()) {
                System.out.println("sub-" + id + ":" + productRFbyCountryCode.get(s).get(id).size());
            }

        }
        return productRFbyCountryCode;
    }

    public HashMap<Long, HashSet<Long>> getCommentRFbyProductId() {
        return commentRFbyProductId;
    }

    public void setCommentRFbyProductId(HashMap<Long, HashSet<Long>> commentRFbyProductId) {
        this.commentRFbyProductId = commentRFbyProductId;
    }

    public HashMap<Long, HashSet<Long>> getStuffLikedRFbyProductId() {
        return stuffLikedRFbyProductId;
    }

    public void setStuffLikedRFbyProductId(HashMap<Long, HashSet<Long>> stuffLikedRFbyProductId) {
        this.stuffLikedRFbyProductId = stuffLikedRFbyProductId;
    }

    public HashMap<String, HashSet<Long>> getStuffLikedRFbyEmail() {
        return stuffLikedRFbyEmail;
    }

    public HashMap<String, HashSet<Long>> getOfferRFbyEmail() {
        return offerRFbyEmail;
    }

    public HashMap<String, HashSet<String>> getFollowerRF() {
        return followerRF;
    }

    public HashMap<String, HashSet<String>> getFollowingRF() {
        return followingRF;
    }

    public HashMap<String, Long> getFollowMapIdRF() {
        return followMapIdRF;
    }

    public HashMap<String, HashSet<Long>> getProductRFbyEmail() {
        return productRFbyEmail;
    }

    public HashMap<String, HashSet<Long>> getBuyerMessRFEmail() {
        return buyerMessRFEmail;
    }

    public HashMap<String, HashSet<Long>> getSellerMessRFEmail() {
        return sellerMessRFEmail;
    }

    public HashMap<Long, HashSet<Long>> getSubMessagesRFMessageId() {
        return subMessagesRFMessageId;
    }

    public HashMap<String, HashSet<Long>> getActivityRFEmail() {
        return activityRFEmail;
    }

    public HashMap<String, HashSet<String>> getStateRFbyCountryCode() {
        return stateRFbyCountryCode;
    }

    public HashMap<String, Long> getStuffLikedRFbyEmailAndProductId() {
        return stuffLikedRFbyEmailAndProductId;
    }

    public HashMap<String, HashSet<Long>> getBuyerFeedbackRFEmail() {
        return buyerFeedbackRFEmail;
    }

    public HashMap<String, HashSet<Long>> getSellerFeedbackRFEmail() {
        return sellerFeedbackRFEmail;
    }

    public HashMap<String, AccountEntity> getAccountEntityHashMap() {
        return accountEntityHashMap;
    }

    public HashMap<String, Long> getFeedbackRFEmailAndProduct() {
        return feedbackRFEmailAndProduct;
    }

    public HashMap<String, HashSet<Long>> getWaitOfferReply() {
        return waitOfferReply;
    }

    public HashMap<String, Long> getDistinctMessage() {
        return distinctMessage;
    }

    public HashMap<String, String> getTwitterRF() {
        return twitterRF;
    }

    public HashMap<String, HashMap<Long, Long>> getWatchingProductLogHashMap() {
        return watchingProductLogHashMap;
    }

    public HashMap<String, HashMap<String, String>> getBroadcastLogRFEmail() {
        return broadcastLogRFEmail;
    }

    public LinkedHashMap<Long, SmallBannerEntity> getSmallBannerShowQueue() {
        return smallBannerShowQueue;
    }

    public LinkedHashMap<Long, SmallBannerEntity> getSmallBannerWaitingStack() {
        return smallBannerWaitingStack;
    }

    public LinkedHashMap<Long, BigBannerEntity> getBigBannerShowQueue() {
        return bigBannerShowQueue;
    }

    public LinkedHashMap<Long, BigBannerEntity> getBigBannerWaitingStack() {
        return bigBannerWaitingStack;
    }

    public long getSmallPopTime() {
        return smallPopTime;
    }

    public long getBigPopTime() {
        return bigPopTime;
    }

    public HashMap<String, Long> getBigBannerExist() {
        return bigBannerExist;
    }

    public HashMap<String, Long> getSmallBannerExist() {
        return smallBannerExist;
    }

    public HashMap<String, HashSet<Long>> getWatchListRFEmail() {
        return watchListRFEmail;
    }

    public HashMap<String, HashSet<String>> getWatchListPatternRFEmail() {
        return watchListPatternRFEmail;
    }

    //SET
    public void setSmallBannerShowQueue(LinkedHashMap<Long, SmallBannerEntity> smallBannerShowQueue) {
        this.smallBannerShowQueue = smallBannerShowQueue;
    }

    public void setSmallBannerWaitingStack(LinkedHashMap<Long, SmallBannerEntity> smallBannerWaitingStack) {
        this.smallBannerWaitingStack = smallBannerWaitingStack;
    }

    public void setSmallPopTime(long smallPopTime) {
        this.smallPopTime = smallPopTime;
    }

    public void setBigBannerShowQueue(LinkedHashMap<Long, BigBannerEntity> bigBannerShowQueue) {
        this.bigBannerShowQueue = bigBannerShowQueue;
    }

    public void setBigBannerWaitingStack(LinkedHashMap<Long, BigBannerEntity> bigBannerWaitingStack) {
        this.bigBannerWaitingStack = bigBannerWaitingStack;
    }

    public void setBigPopTime(long bigPopTime) {
        this.bigPopTime = bigPopTime;
    }


}
