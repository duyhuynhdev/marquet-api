package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.ElementController;
import com.marqet.WebServer.controller.InformationController;
import com.marqet.WebServer.util.Database;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
public class GetElementAndInformationServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            Database database = Database.getInstance();
            request.setAttribute("element",new ElementController().getElement());
            request.setAttribute("information",new InformationController().getInformation(-1));
            request.getRequestDispatcher("element-information.jsp").forward(request,response);
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
