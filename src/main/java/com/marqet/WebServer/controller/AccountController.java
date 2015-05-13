package com.marqet.WebServer.controller;

import com.marqet.WebServer.dao.AccountDao;
import com.marqet.WebServer.pojo.AccountEntity;
import com.marqet.WebServer.util.Database;

/**
 * Created by hpduy17 on 5/7/15.
 */
public class AccountController {
    private AccountDao dao = new AccountDao();
    private Database database = Database.getInstance();

    public boolean login(String email, String password){
        AccountEntity admin = database.getAccountEntityHashMap().get(email);
        if (admin != null && admin.getPassword().equals(password))
            return true;
        return false;
    }
    public boolean register(String email, String password, String userName){
        AccountEntity admin = database.getAccountEntityHashMap().get(email);
        if(admin!=null)
            return false;
        admin = new AccountEntity();
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setRole(1);
        admin.setUserName(userName);
        database.getAccountEntityHashMap().put(email,admin);
        if(dao.insert(admin)){
            return true;
        }
        return false;
    }
}
