package com.zb_assignment.public_wifi_info.Servlet;

import com.zb_assignment.public_wifi_info.DAO.HistoryDAO;
import com.zb_assignment.public_wifi_info.Entity.History;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryServlet", value = "/history-list.jsp")
public class HistoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HistoryDAO historyDAO = new HistoryDAO();
        List<History> dataList = historyDAO.getAllHistory();

        try {
            request.setAttribute("dataList", dataList);
            request.getRequestDispatcher("history-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("An error occurred in the servlet: " + e.getMessage());
        }

    }
}
