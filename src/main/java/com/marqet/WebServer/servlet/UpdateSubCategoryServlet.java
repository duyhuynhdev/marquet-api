package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.ResponseController;
import com.marqet.WebServer.controller.SubCategoryController;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
@MultipartConfig
public class UpdateSubCategoryServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            long id = Long.parseLong(request.getParameter("id"));
            long categoryId = Long.parseLong(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            Part part = null;
            if (request.getContentType().startsWith("multipart/form-data")) {
                part = request.getPart("coverImage");
            }
            SubCategoryController controller = new SubCategoryController();
            JSONObject responseJSON = controller.editSubCategory(id, name, part, categoryId);
            if (responseJSON.get(ResponseController.RESULT).equals(ResponseController.SUCCESS))
                response.sendRedirect("subcategory.marqet?categoryId=" + categoryId);
            else{
                request.setAttribute("isError", true);
                request.setAttribute("errorTitle", "Update sub-category fail");
                request.setAttribute("errorMessage",responseJSON.get(ResponseController.CONTENT));
                request.getRequestDispatcher("subcategory.marqet?categoryId=" + categoryId).forward(request,response);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("isError", true);
            request.setAttribute("errorTitle", "Update sub-category exception");
            request.setAttribute("errorMessage",ex.getMessage());
            request.getRequestDispatcher("subcategory.marqet?categoryId=" + request.getParameter("categoryId")).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
