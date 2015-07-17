package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.CategoryController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
public class GetCategoryServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{

            CategoryController controller = new CategoryController();
            request.setAttribute("categoryList", controller.getListCategory());
            request.getRequestDispatcher("category.jsp").forward(request,response);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
