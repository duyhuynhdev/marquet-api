/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marqet.WebServer.api;

import com.marqet.WebServer.controller.ProductController;
import com.marqet.WebServer.controller.ResponseController;
import com.marqet.WebServer.util.ApiParameterChecker;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class EditProductAPI extends HttpServlet {
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
            String parameters = "productId,title,cityCode,price,subCategoryId,description,oldProductImagesIdx";
            JSONObject resultCheckerJSON = ApiParameterChecker.check(request.getParameterMap().keySet(), parameters);
            if (ResponseController.isSuccess(resultCheckerJSON)) {
                int numberImage = Integer.parseInt(request.getParameter("numImg"));
                List<Part> productImages = null;
                if (request.getContentType().startsWith("multipart/form-data")) {
                    productImages = new ArrayList<>();
                    for (int i = 1; i <= numberImage; i++) {
                        productImages.add(request.getPart("productImage" + i));

                    }
                }
                //get parameter
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                JSONArray oldProductImagesIdx = new JSONArray(request.getParameter("oldProductImagesIdx"));
                long productId = Long.parseLong(request.getParameter("productId"));
                long price = Long.parseLong(request.getParameter("price"));
                long subCategoryId = Long.parseLong(request.getParameter("subCategoryId"));
                String cityCode = request.getParameter("cityCode");
                ProductController controller = new ProductController();
                //edit products
                out.print(controller.editProduct(productId, title, cityCode, price, subCategoryId, description, productImages,oldProductImagesIdx));
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
