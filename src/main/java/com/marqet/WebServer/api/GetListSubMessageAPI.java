/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.FeedbackController;
import com.marqet.WebServer.controller.ResponseController;
import com.marqet.WebServer.controller.SubMessageController;
import com.marqet.WebServer.util.ApiParameterChecker;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class GetListSubMessageAPI extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  raw request
     * @param response raw response
     * @throws ServletException if a raw-specific error occurs
     * @throws IOException            if an I/O error occurs
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
            // check enough parameter
            String parameters = "email";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(requestJSON.keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                //get parameter
                String email = requestJSON.getString("email");
                int startIdx = requestJSON.getInt("startIdx");
                int numSubMessage = requestJSON.getInt("numSubMessage");
                long messageId = requestJSON.getInt("messageId");
                int type = requestJSON.getInt("type");
                switch (type) {
                    case 1:
                    case 2:
                        SubMessageController subMessageController = new SubMessageController();
                        out.print(subMessageController.getSubMessageByMessageId(email,messageId, startIdx, numSubMessage));
                        break;
                    case 3:
                        FeedbackController feedbackController = new FeedbackController();
                        out.print(feedbackController.getListFeedbackRequired(email,startIdx,numSubMessage));

                }

            } else {
                out.print(resultCheckerJSON);
            }
        }catch (Exception ex){
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
     * @throws IOException            if an I/O error occurs
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
     * @throws IOException            if an I/O error occurs
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