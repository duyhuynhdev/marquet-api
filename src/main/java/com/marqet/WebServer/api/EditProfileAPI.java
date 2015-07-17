/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.ResponseController;
import com.marqet.WebServer.controller.UserController;
import com.marqet.WebServer.util.ApiParameterChecker;
import com.marqet.WebServer.util.Database;
import com.marqet.WebServer.util.LoggerFactory;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;

@MultipartConfig
public class EditProfileAPI extends HttpServlet {
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
            // check enough parameter
            String parameters = "username,email,telephone,countryCode,cityCode,firstname,lastname,bio,website,gender,birthday";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(request.getParameterMap().keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                logger.info(LoggerFactory.REQUEST+request.getQueryString());
                Part imagePart = null;
                if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data"))
                    imagePart = request.getPart("profilePicture");
                //unicode utf8
                String[] src;
                HashMap<String, String> req = null;
                try {
                    src = URLDecoder.decode(request.getQueryString().replace("&", "#DUY#"), "UTF-8").split("#DUY#");
                    req = Database.getInstance().convertArrStringToHashMap(src, "=");
                } catch (Exception ignored) {

                }
                //get parameter
                String username = req == null ? request.getParameter("username") : req.get("username");
                String email = req == null ? request.getParameter("email") : req.get("email");
                String telephone = request.getParameter("telephone");
                String countryCode = request.getParameter("countryCode");
                String cityCode = request.getParameter("cityCode");
                long birthday = Long.parseLong(request.getParameter("birthday"));
                int gender = Integer.parseInt(request.getParameter("gender"));
                String bio = req == null ? request.getParameter("bio") : req.get("bio");
                String website = req == null ? request.getParameter("website") : req.get("website");
                String fisrtname = req == null ? request.getParameter("firstname") : req.get("firstname");
                String lastname = req == null ? request.getParameter("lastname") : req.get("lastname");
                UserController controller = new UserController();
                //edit profile
                JSONObject result = controller.editProfile(username, email, telephone,
                        imagePart, countryCode, cityCode,gender,birthday,website,bio,lastname,fisrtname);
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
