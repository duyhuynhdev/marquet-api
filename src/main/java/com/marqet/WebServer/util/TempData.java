package com.marqet.WebServer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 4/24/15.
 */
public class TempData {
    public static List<String> tempFollow(String email){
        List<String> result = new ArrayList<>(Database.getInstance().getUserEntityHashMap().keySet());
        result.remove(email);
        return result ;
    }
}
