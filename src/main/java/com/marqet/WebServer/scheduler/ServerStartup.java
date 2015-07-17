package com.marqet.WebServer.scheduler;

import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.Path;
import com.marqet.WebServer.util.TempData;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;

/**
 * Created by hpduy17 on 3/20/15.
 */
public class ServerStartup implements ServletContextListener {


    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            //Build root
            Path.buildRoot();
            //Restore database
            Database database = Database.getInstance();
            database.restore();
            // get Address server
            Path.setServerAddress("http://"+InetAddress.getLocalHost().getHostAddress()+":8080");
            //create temper
            TempData.createTemper();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
