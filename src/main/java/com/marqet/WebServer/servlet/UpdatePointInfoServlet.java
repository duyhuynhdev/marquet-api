package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.ElementController;
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
public class UpdatePointInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            long level1 = Long.parseLong(request.getParameter("level1"));
            long level2 = Long.parseLong(request.getParameter("level2"));
            long level3 = Long.parseLong(request.getParameter("level3"));
            long level4 = Long.parseLong(request.getParameter("level4"));
            ElementController controller = new ElementController();
            JSONObject responseJSON = controller.changePointLevel(level1, level2, level3, level4);
            if (responseJSON.get(ResponseController.RESULT).equals(ResponseController.SUCCESS))
                response.sendRedirect("element-information.marqet");
            else{
                request.setAttribute("isError", true);
                request.setAttribute("errorTitle", "Change point level info fail");
                request.setAttribute("errorMessage",responseJSON.get(ResponseController.CONTENT));
                request.getRequestDispatcher("element-information.marqet").forward(request,response);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("isError", true);
            request.setAttribute("errorTitle", "Change point level exception");
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
