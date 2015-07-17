package com.marqet.WebServer.util;

import com.marqet.WebServer.controller.BigBannerController;
import com.marqet.WebServer.dao.BigBannerDao;
import com.marqet.WebServer.dao.ElementDao;
import com.marqet.WebServer.pojo.BigBannerEntity;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hpduy17 on 6/13/15.
 */
public class BigBannerTimer implements Runnable {
    private Database database = Database.getInstance();
    private boolean firstTime = true;

    @Override
    public void run() {
        try {
            while (true) {
                long now = new DateTimeUtil().getNow();
                long duration = new JSONObject(database.getElementEntity().getBigBannerInfo()).getLong(ElementDao.DURATION);
                if(!firstTime) {
                    if (now - database.getBigPopTime() >= duration) {
                        if (database.getBigBannerShowQueue().size() >= BigBannerDao.NUMBER_OF_BIGBANNER && database.getBigBannerWaitingStack().size() > 0) {
                            //pop old banner
                            int size = database.getBigBannerShowQueue().size();
                            for (long i : new ArrayList<>(database.getBigBannerShowQueue().keySet())) {
                                if (size <= BigBannerDao.NUMBER_OF_BIGBANNER - 1) {
                                    break;
                                }
                                size--;
                                new BigBannerController().deleteBigBanner(i);

                            }
                            // put from waiting list and remove in waiting list
                            int waitingSize = database.getBigBannerWaitingStack().size();
                            BigBannerEntity bigBannerEntity = new ArrayList<>(database.getBigBannerWaitingStack().values()).get(waitingSize - 1);
                            //update
                            bigBannerEntity.setSetTime(now);
                            bigBannerEntity.setStatus(BigBannerDao.SHOW);
                            new BigBannerDao().update(bigBannerEntity);
                            //put and remove
                            database.getBigBannerWaitingStack().remove(bigBannerEntity.getId());
                            database.getBigBannerShowQueue().put(bigBannerEntity.getId(), bigBannerEntity);
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
