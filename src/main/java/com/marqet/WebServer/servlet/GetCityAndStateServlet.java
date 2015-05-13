package com.marqet.WebServer.servlet;

import com.marqet.WebServer.controller.CityController;
import com.marqet.WebServer.controller.StateController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hpduy17 on 1/20/15.
 */
public class GetCityAndStateServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try{
            String countryCode = request.getParameter("countryCode");
            CityController cityController = new CityController();
            StateController stateController = new StateController();
            request.setAttribute("cityList",cityController.getListCity(countryCode));
            request.setAttribute("stateList",stateController.getListState(countryCode));
            request.setAttribute("countryCode",countryCode);
            request.getRequestDispatcher("city-state.jsp").forward(request,response);

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
