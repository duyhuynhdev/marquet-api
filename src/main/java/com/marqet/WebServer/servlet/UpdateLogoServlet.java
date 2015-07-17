package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.ElementController;
import com.marqet.WebServer.controller.ResponseController;
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
public class UpdateLogoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            Part part = null;
            if (request.getContentType().startsWith("multipart/form-data")) {
                part =request.getPart("logo");
            }
            ElementController controller = new ElementController();
            JSONObject responseJSON = controller.changeLogo(part);
            if (responseJSON.get(ResponseController.RESULT).equals(ResponseController.SUCCESS))
                response.sendRedirect("element-information.marqet");
            else{
                request.setAttribute("isError", true);
                request.setAttribute("errorTitle", "Change logo fail");
                request.setAttribute("errorMessage",responseJSON.get(ResponseController.CONTENT));
                request.getRequestDispatcher("element-information.marqet").forward(request,response);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("isError", true);
            request.setAttribute("errorTitle", "Change logo exception");
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
