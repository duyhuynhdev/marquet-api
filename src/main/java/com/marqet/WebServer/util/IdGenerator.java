package com.marqet.WebServer.util;

import java.util.Set;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class IdGenerator {
    private static long activityId = 0;
    private static long bigBannerId = 0;
    private static long categoryId = 0;
    private static long commentId = 0;
    private static long feedbackId = 0;
    private static long messageId = 0;
    private static long productId = 0;
    private static long smallBannerId = 0;
    private static long subCategoryId = 0;
    private static long followId = 0;
    private static long offerId = 0;
    private static long stuffLikedId = 0;
    private static long watchListId = 0;
    private static long subMessageId = 0;
    private static Database database = Database.getInstance();

    public static synchronized long getActivityId() {
        Set<Long> idSet = database.getActivityEntityHashMap().keySet();
        do {
            activityId++;
        }
        while (idSet.contains(activityId));
        return activityId;
    }

    public static synchronized long getBigBannerId() {
        Set<Long> idSet = database.getBigBannerEntityHashMap().keySet();
        do {
            bigBannerId++;
        }
        while (idSet.contains(bigBannerId));
        return bigBannerId;
    }

    public static synchronized long getCategoryId() {
        Set<Long> idSet = database.getCategoryEntityHashMap().keySet();
        do {
            categoryId++;
        }
        while (idSet.contains(categoryId));
        return categoryId;
    }

    public static synchronized long getCommentId() {
        Set<Long> idSet = database.getCommentEntityHashMap().keySet();
        do {
            commentId++;
        }
        while (idSet.contains(commentId));
        return commentId;
    }

    public static synchronized long getFeedbackId() {
        Set<Long> idSet = database.getFeedbackEntityHashMap().keySet();
        do {
            feedbackId++;
        }
        while (idSet.contains(feedbackId));
        return feedbackId;
    }

    public static synchronized long getMessageId() {
        Set<Long> idSet = database.getMessageEntityHashMap().keySet();
        do {
            messageId++;
        }
        while (idSet.contains(messageId));
        return messageId;
    }

    public static synchronized long getProductId() {
        Set<Long> idSet = database.getProductEntityHashMap().keySet();
        do {
            productId++;
        }
        while (idSet.contains(productId));
        return productId;
    }

    public static synchronized long getSmallBannerId() {
        Set<Long> idSet = database.getSmallBannerEntityHashMap().keySet();
        do {
            smallBannerId++;
        }
        while (idSet.contains(smallBannerId));
        return smallBannerId;
    }

    public static synchronized long getSubCategoryId() {
        Set<Long> idSet = database.getSubCategoryEntityHashMap().keySet();
        do {
            subCategoryId++;
        }
        while (idSet.contains(subCategoryId));
        return subCategoryId;
    }

    public static synchronized long getFollowId() {
        Set<Long> idSet = database.getFollowEntityList().keySet();
        do {
            followId++;
        }
        while (idSet.contains(followId));
        return followId;
    }

    public static synchronized long getOfferId() {
        Set<Long> idSet = database.getOfferEntityHashMap().keySet();
        do {
            offerId++;
        }
        while (idSet.contains(offerId));
        return offerId;
    }

    public static synchronized long getStuffLikedId() {
        Set<Long> idSet = database.getStuffLikedEntityHashMap().keySet();
        do {
            stuffLikedId++;
        }
        while (idSet.contains(stuffLikedId));
        return stuffLikedId;
    }

    public static synchronized long getWatchListId() {
        Set<Long> idSet = database.getWatchListEntityHashMap().keySet();
        do {
            watchListId++;
        }
        while (idSet.contains(watchListId));
        return watchListId;
    }

    public static synchronized long getSubMessageId() {
        Set<Long> idSet = database.getSubMessageEntityHashMap().keySet();
        do {
            subMessageId++;
        }
        while (idSet.contains(subMessageId));
        return subMessageId;
    }

    public synchronized static String getCodeFromName(String name) {
        String temp [] = AccentRemoveAlgorithm.removeAccent(name).split(" ");
        String result ="";
        for(String t : temp){
            result+=t.charAt(0);
        }
        return result;
    }
}
