/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.FeedbackController;
import com.marqet.WebServer.controller.ResponseController;
import com.marqet.WebServer.util.ApiParameterChecker;
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


public class FeedbackProductAPI extends HttpServlet {
    private Logger logger = LoggerFactory.createLogger(this.getClass());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  raw request
     * @param response raw response
     * @throws javax.servlet.ServletException if a raw-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
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
            String parameters = "buyerEmail,sellerEmail,content,status,productId";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(requestJSON.keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                //get parameter
                FeedbackController controller = new FeedbackController();
                //feedback
                String buyerEmail = requestJSON.getString("buyerEmail");
                String sellerEmail = requestJSON.getString("sellerEmail");
                String content = requestJSON.getString("content");
                int status = requestJSON.getInt("status");
                long productId = requestJSON.getLong("productId");
                JSONObject result = controller.feedbackProduct(buyerEmail, sellerEmail, content, status, productId);
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
     * @throws javax.servlet.ServletException if a raw-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
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
     * @throws javax.servlet.ServletException if a raw-specific error occurs
     * @throws java.io.IOException            if an I/O error occurs
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
