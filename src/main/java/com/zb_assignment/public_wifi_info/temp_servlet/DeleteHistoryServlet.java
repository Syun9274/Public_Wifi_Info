package com.zb_assignment.public_wifi_info.temp_servlet;

import com.zb_assignment.public_wifi_info.temp_dao.HistoryDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteHistoryServlet", value = "/deleteHistory")
public class DeleteHistoryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        HistoryDAO historyDAO = new HistoryDAO();
        try {
            historyDAO.deleteHistoryById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
