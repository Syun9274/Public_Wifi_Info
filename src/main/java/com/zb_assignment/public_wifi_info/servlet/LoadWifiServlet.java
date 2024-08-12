package com.zb_assignment.public_wifi_info.servlet;

import com.zb_assignment.public_wifi_info.service.ApiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@WebServlet(name = "LoadWifiServlet", value = "/load-wifi.jsp")
public class LoadWifiServlet extends HttpServlet {

    ApiService apiService = new ApiService();

    String apiKey = apiService.readApiKey();
    int BATCH_SIZE = apiService.getBATCH_SIZE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int startIndex = 1;
        int endIndex = BATCH_SIZE;

        StringBuilder sb = apiService.callApiData(
                Integer.toString(startIndex),
                Integer.toString(endIndex),
                apiKey);

        apiService.parseAndSaveData(sb.toString());

        String totalDataCount = apiService.parseTotalCount(sb.toString());

        startIndex = endIndex + 1;
        while(startIndex < Integer.parseInt(totalDataCount)) {
            endIndex = Math.min(startIndex + BATCH_SIZE - 1, Integer.parseInt(totalDataCount));

            sb = apiService.callApiData(
                    Integer.toString(startIndex),
                    Integer.toString(endIndex),
                    apiKey);

            apiService.parseAndSaveData(sb.toString());

            startIndex = endIndex + 1;
        }

        // 응답 콘텐츠 타입 설정
        resp.setContentType("text/html;charset=UTF-8");

        // 응답을 작성하기 위한 PrintWriter 얻기
        try (PrintWriter out = resp.getWriter()) {
            // HTML 콘텐츠 작성
            out.println("<html>");
            out.println("<head>");
            out.println("<title>와이파이 정보 구하기</title>");
            out.println("<style>");
            out.println(".spacing { text-align: center; margin-top: 20px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.printf("<h1 class=\"spacing\">%s개의 wifi 정보를 정상적으로 저장하였습니다.</h1>\n", totalDataCount);
            out.println("<div class=\"spacing\">");
            out.println("<a href=\"index.jsp\">홈으로 가기</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }

    }

}
