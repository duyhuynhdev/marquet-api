/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.ProductController;
import com.marqet.WebServer.controller.ResponseController;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@MultipartConfig
public class AddProductAPI extends HttpServlet {
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
            // check enough parameter
            String parameters = "email,title,cityCode,price,subCategoryId,description,numImg";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(request.getParameterMap().keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                logger.info(LoggerFactory.REQUEST+request.getQueryString());
                int numberImage = Integer.parseInt(request.getParameter("numImg"));
                List<Part> productImages = null;
                List<Part> videoAndThumbnail = null;
                if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
                    productImages = new ArrayList<>();
                    for (int i = 1; i <= numberImage; i++) {
                        if (request.getPart("productImage" + i) != null) {
                            productImages.add(request.getPart("productImage" + i));
                        }
                    }
                    if(request.getPart("productVideo") != null){
                        videoAndThumbnail = new ArrayList<>();
                        videoAndThumbnail.add(request.getPart("productVideo"));
                        videoAndThumbnail.add(request.getPart("productThumbnailVideo"));
                    }

                }
                //encoding utf8
                String[] src;
                HashMap<String, String> req = null;
                try {
                    src = URLDecoder.decode(request.getQueryString().replace("&", "#DUY#"), "UTF-8").split("#DUY#");
                    req = Database.getInstance().convertArrStringToHashMap(src, "=");
                } catch (Exception ignored) {

                }
                //get parameter
                String title = req == null ? request.getParameter("title") : req.get("title");
                String description = req == null ? request.getParameter("description") : req.get("description");
                String email = req == null ? request.getParameter("email") : req.get("email");
                long price = Long.parseLong(request.getParameter("price"));
                long subCategoryId = Long.parseLong(request.getParameter("subCategoryId"));
                String cityCode = request.getParameter("cityCode");
                ProductController controller = new ProductController();
                //add product
                JSONObject result = controller.addProduct(email, title, cityCode, price, subCategoryId, description, productImages,videoAndThumbnail);
                logger.info(LoggerFactory.RESPONSE+result);
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
