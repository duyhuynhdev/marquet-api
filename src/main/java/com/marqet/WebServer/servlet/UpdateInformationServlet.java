package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.InformationController;
import com.marqet.WebServer.controller.ResponseController;
import org.json.JSONObject;

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
            String communityGuidelines = request.getParameter("communityGuidelines");
            String helpFAQ = request.getParameter("helpFAQ");
            String emailSupport = request.getParameter("emailSupport");
            InformationController controller = new InformationController();
            JSONObject responseJSON = controller.updateInformation(about, communityGuidelines, emailSupport, helpFAQ);
            if (responseJSON.get(ResponseController.RESULT).equals(ResponseController.SUCCESS))
                response.sendRedirect("element-information.marqet");
            else{
                request.setAttribute("isError", true);
                request.setAttribute("errorTitle", "Change information detail fail");
                request.setAttribute("errorMessage",responseJSON.get(ResponseController.CONTENT));
                request.getRequestDispatcher("element-information.marqet").forward(request,response);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("isError", true);
            request.setAttribute("errorTitle", "Change information detail exception");
            request.setAttribute("errorMessage",ex.getMessage());
            request.getRequestDispatcher("element-information.marqet").forward(request, response);
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
