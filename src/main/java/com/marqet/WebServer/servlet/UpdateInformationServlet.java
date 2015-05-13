package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.InformationController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
public class UpdateInformationServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            String about = request.getParameter("about");
            String communityRule = request.getParameter("communityRule");
            String pointSystem = request.getParameter("pointSystem");
            String emailSupport = request.getParameter("emailSupport");
            InformationController controller = new InformationController();
            controller.updateInformation(about,communityRule,emailSupport,pointSystem);
            response.sendRedirect("element-information.marqet");
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
