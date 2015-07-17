package com.marqet.WebServer.util;

import com.marqet.WebServer.controller.SmallBannerController;
import com.marqet.WebServer.dao.ElementDao;
import com.marqet.WebServer.dao.SmallBannerDao;
import com.marqet.WebServer.pojo.SmallBannerEntity;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hpduy17 on 6/13/15.
 */
public class SmallBannerTimer implements Runnable {
    private Database database = Database.getInstance();
    private boolean firstTime = true;

    @Override
    public void run() {
        try {
            while (true) {
                long duration = new JSONObject(database.getElementEntity().getSmallBannerInfo()).getLong(ElementDao.DURATION);
                if(!firstTime) {
                    long now = new DateTimeUtil().getNow();
                    if (now - database.getBigPopTime() >= duration) {
                        if (database.getSmallBannerShowQueue().size() >= SmallBannerDao.NUMBER_OF_SMALLBANNER && database.getSmallBannerWaitingStack().size() > 0) {
                            //pop old banner
                            int size = database.getSmallBannerShowQueue().size();
                            for (long i : new ArrayList<>(database.getSmallBannerShowQueue().keySet())) {
                                if (size <= SmallBannerDao.NUMBER_OF_SMALLBANNER - 1) {
                                    break;
                                }
                                size--;
                                new SmallBannerController().deleteSmallBanner(i);
                            }
                            //put from waiting list and remove in waiting list
                            int waitingSize = database.getSmallBannerWaitingStack().size();
                            SmallBannerEntity smallBannerEntity = new ArrayList<>(database.getSmallBannerWaitingStack().values()).get(waitingSize - 1);
                            //update
                            smallBannerEntity.setSetTime(now);
                            smallBannerEntity.setStatus(SmallBannerDao.SHOW);
                            new SmallBannerDao().update(smallBannerEntity);
                            //put and remove
                            database.getSmallBannerWaitingStack().remove(smallBannerEntity.getId());
                            database.getSmallBannerShowQueue().put(smallBannerEntity.getId(), smallBannerEntity);
                            database.setBigPopTime(now);
                        }
                    }
                }else {
                    firstTime = false;
                }
                Thread.sleep(duration);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
