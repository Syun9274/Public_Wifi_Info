package com.zb_assignment.public_wifi_info.servlet;

import com.zb_assignment.public_wifi_info.dao.HistoryDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "FetchWifiInfoServlet", value = "/fetchWifiInfo")
public class FetchWifiInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double lat = Double.parseDouble(req.getParameter("lat"));
        double lnt = Double.parseDouble(req.getParameter("lnt"));

        HistoryDAO historyDAO = new HistoryDAO();
        try {
            historyDAO.saveHistory(lat, lnt, LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
