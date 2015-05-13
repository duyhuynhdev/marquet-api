package com.marqet.WebServer.api;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.marqet.WebServer.pojo.UserEntity;
import com.marqet.WebServer.util.Database;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GCMBroadcast extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // The SENDER_ID here is the "Browser Key" that was generated when I
    // created the API keys for my Google APIs project.
    private static final String SENDER_ID = "AIzaSyDJK0UYHQUKwUfgyYrnfDnJl8SpsSPeTdo";

    // This is a *cheat*  It is a hard-coded registration ID from an Android device
    // that registered itself with GCM using the same project id shown above.
    // This is sample android device reg key.
    // After we need to create database to save regId receive from device.
    //private static final String ANDROID_DEVICE = "APA91bGOUIQDNwvl6O_HgJpOg0d48hJT99cpPYYX89d7DefhCyBgpS1w0x_IbG8aCVKfqn9IvWYc1jms-MNyBA1TP4CoiFY94LA5CYn4OIWbsGar34CbuMLdTD9oHugIFMsszuANJuDiOmSntpE4QVShR_yTCJS70A";

    // This array will hold all the registration ids used to broadcast a message.
    // for this demo, it will only have the ANDROID_DEVICE id that was captured
    // when we ran the Android client app through Eclipse.


    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public GCMBroadcast() {

        super();

        // we'll only add the hard-coded *cheat* target device registration id 
        // for this demo.
        //androidTargets.add(ANDROID_DEVICE);

    }

    // This doPost() method is called from the form in our index.jsp file.
    // It will broadcast the passed "Message" value.
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // We'll collect the "CollapseKey" and "Message" values from our JSP page
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String collapseKey = "";
        //Mess send for each userId
        List<JSONObject> usersMessage;
        // List user you want to sent norti
        List<String> usersEmails ;
        // List Message
        List<Message> lstMessage = new ArrayList<Message>();
        String Norti = "";
        PrintWriter out = response.getWriter();
        List<List<String>> androidTargets = new ArrayList<>();
        try {
            usersMessage = (List<JSONObject>) request.getAttribute("message");
            Norti = (String) request.getAttribute("norti");
            collapseKey = (String) request.getAttribute("collapseKey");
            usersEmails = (List<String>) request.getAttribute("userEmail");

            // add all device each user into each android target list
            for (int i = 0; i < usersEmails.size(); i++) {
                UserEntity user = Database.getInstance().getUserEntityHashMap().get(usersEmails.get(i));
                List<String> target = new ArrayList<>();
                target.addAll(new ArrayList<>(Arrays.asList(user.getBroadcastId().split(","))));
                androidTargets.add(target);
                Message message = new Message.Builder()
                        // If multiple messages are sent using the same .collapseKey()
                        // the android target device, if it was offline during earlier message
                        // transmissions, will only receive the latest message for that key when
                        // it goes back on-line.
                        .collapseKey(collapseKey)
                        .timeToLive(30)
                        .delayWhileIdle(true)
                        .addData("price", URLEncoder.encode(usersMessage.get(i).toString(),"UTF-8"))
                        .build();
                lstMessage.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Instance of com.android.gcm.server.Sender, that does the
        // transmission of a Message to the Google Cloud Messaging service.


        // This Message object will hold the data that is being transmitted
        // to the Android client devices.  For this demo, it is a simple text
        // string, but could certainly be a JSON object.


        try {
            // use this for multicast messages.  The second parameter
            // of sender.send() will need to be an array of register ids.
            // for each user you have sent user's mess to user's devices
            for (int i = 0; i < usersEmails.size(); i++) {
                Sender sender = new Sender(SENDER_ID);
                if (androidTargets.get(i).isEmpty()) {
                    continue;
                }

                MulticastResult result = sender.send(lstMessage.get(i), androidTargets.get(i), 1);

                if (result.getResults() != null) {
                    int canonicalRegId = result.getCanonicalIds();
                    if (canonicalRegId != 0) {

                    }
                } else {
                    int error = result.getFailure();
                    System.out.println("Broadcast to " + usersEmails.get(i) + "  failure: " + error);
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        //Respone to sender
        if (Norti.equals("") == false) {
            out.print(Norti);
        }
        // We'll pass the CollapseKey and Message values back to gcmtest.jsp, only so
        // we can display it in our form again.


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  raw request
     * @param response raw response
     * @throws javax.servlet.ServletException if a raw-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
