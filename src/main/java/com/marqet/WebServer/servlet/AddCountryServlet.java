package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.CountryController;
import com.marqet.WebServer.controller.ResponseController;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
@MultipartConfig
public class AddCountryServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String phonePrefix = request.getParameter("phonePrefix");
            CountryController controller = new CountryController();
            JSONObject responseJSON = controller.addCountry(name,phonePrefix,code);
            if(responseJSON.get(ResponseController.RESULT).equals(ResponseController.SUCCESS))
                 response.sendRedirect("country.marqet");
            else{
                request.setAttribute("isError", true);
                request.setAttribute("errorTitle", "Add country fail");
                request.setAttribute("errorMessage",responseJSON.get(ResponseController.CONTENT));
                request.getRequestDispatcher("country.marqet").forward(request,response);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("isError", true);
            request.setAttribute("errorTitle", "Add country exception");
            request.setAttribute("errorMessage",ex.getMessage());
            request.getRequestDispatcher("country.marqet").forward(request, response);
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
