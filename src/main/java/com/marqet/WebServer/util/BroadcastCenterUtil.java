package com.marqet.WebServer.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpduy17 on 6/13/15.
 */
public class BroadcastCenterUtil implements Runnable {
    private List<String> contents = new ArrayList<>();
    private List<String> emails = new ArrayList<>();
    private String collapseKey = "Marqet Server App";
    private final String urlStringPath = Path.getServerAddress()+"/marqet/GCMBroadcast";
    public BroadcastCenterUtil(List<String> contents, List<String> emails) {
        this.contents = contents;
        this.emails = emails;
    }

    @Override
    public void run() {
        try{
            //connect different server;
            URL url=new URL(urlStringPath);
            HttpURLConnection myConn = (HttpURLConnection) url .openConnection();
            myConn .setDoOutput(true); // do output or post
            PrintWriter po = new PrintWriter(new OutputStreamWriter(myConn.getOutputStream(),"UTF-8"));
            po.println(makeJsonData().toString());
            po.close();
            //read data
            StringBuffer strBuffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(myConn.getInputStream(),"UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                strBuffer.append(inputLine);
            }
            System.out.print(strBuffer.toString());
            in.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private JSONObject makeJsonData(){
        JSONObject jsonData = new JSONObject();
        JSONArray arrContent = new JSONArray();
        for(String c : contents){
            arrContent.put(c);
        }
        JSONArray arrEmail = new JSONArray();
        for(String e: emails){
            arrEmail.put(e);
        }
        jsonData.put("contents",arrContent);
        jsonData.put("emails",arrEmail);
        jsonData.put("collapseKey",collapseKey);
        return jsonData;
    }
}
