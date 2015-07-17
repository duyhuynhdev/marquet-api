/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.*;
import com.marqet.WebServer.util.ApiParameterChecker;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.LoggerFactory;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class PostSubMessageAPI extends HttpServlet {
    private Logger logger = LoggerFactory.createLogger(this.getClass());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  raw request
     * @param response raw response
     * @throws ServletException if a raw-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            StringBuffer jsonData = new StringBuffer();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            JSONObject requestJSON = new JSONObject(jsonData.toString());
            logger.info(LoggerFactory.REQUEST+requestJSON);
            // check enough parameter
            String parameters = "email,type,messageId,content,tempSubMessageId";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(requestJSON.keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                //get parameter
                String senderEmail = requestJSON.getString("senderEmail");
                String receiverEmail = requestJSON.getString("receiverEmail");
                long messageId = requestJSON.getLong("messageId");
                String content = requestJSON.getString("content");
                int type = requestJSON.getInt("type");
                long tempSubMessageId = requestJSON.getLong("tempSubMessageId");
                long productId = requestJSON.getLong("productId");
                SubMessageController controller = new SubMessageController();
                JSONObject result = new JSONObject();
                System.out.println(requestJSON);
                if (messageId <= 0) {
                    if (!Database.getInstance().getDistinctMessage().containsKey(senderEmail + "#" + receiverEmail + "#" + productId)) {
                        JSONObject messResult = new MessageController().insertMessage(senderEmail, receiverEmail, productId);
                        if (!messResult.get(ResponseController.RESULT).equals(ResponseController.SUCCESS)) {
                            out.print(messResult);
                            return;
                        }
                        messageId = messResult.getJSONObject(ResponseController.CONTENT).getLong("id");
                    } else {
                        messageId = Database.getInstance().getDistinctMessage().get(senderEmail + "#" + receiverEmail + "#" + productId);
                    }
                }
                switch (type) {
                    case 1:
                        result = controller.sendSubMessage(senderEmail, messageId, content, 1, tempSubMessageId, receiverEmail);
                        break;
                    case 2:
                        result = controller.sendSubMessage(senderEmail, messageId, content, 2, tempSubMessageId, receiverEmail);
                        break;
                    case 3:
                        result = controller.requireFeedbackMessage(senderEmail, messageId, tempSubMessageId, receiverEmail);
                        break;
                    case 4:
                        double offerPrice = requestJSON.getDouble("offerPrice");
                        OfferController offerController = new OfferController();
                        result = offerController.offerProduct(senderEmail, offerPrice, messageId, tempSubMessageId, receiverEmail);
                        break;
                    case 5:
                        OfferController offerController1 = new OfferController();
                        int status = requestJSON.getInt("status");
                        result = offerController1.replyOffer(senderEmail, messageId, status, tempSubMessageId, receiverEmail);
                        break;
                    case 6:
                        OfferController offerController2= new OfferController();
                        result = offerController2.cancelOffer(senderEmail, messageId, tempSubMessageId, receiverEmail);
                        break;
                    case 7:
                        FeedbackController feedbackController = new FeedbackController();
                        String contentFeedback = requestJSON.getString("contentFeedback");
                        int statusFeedback = requestJSON.getInt("statusFeedback");
                        out.print(feedbackController.feedbackProduct(senderEmail, receiverEmail, contentFeedback, statusFeedback, productId, messageId, tempSubMessageId));
                        break;
                    case 8:
                        ProductController productController = new ProductController();
                        //mark as sold
                        out.print(productController.markAsSold(senderEmail, productId, messageId, tempSubMessageId, receiverEmail));
                        break;
                    default:
                        result = ResponseController.createFailJSON("Type is wrong");
                }
                logger.info(LoggerFactory.RESPONSE + result);
                out.print(result);
            } else {
                out.print(resultCheckerJSON);
            }
        } catch (Exception ex) {
            logger.error(ex.getStackTrace());
            out.print(ResponseController.createErrorJSON(ex.getMessage()));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  raw request
     * @param response raw response
     * @throws ServletException if a raw-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
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
     * @throws ServletException if a raw-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the raw.
     *
     * @return a String containing raw description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
