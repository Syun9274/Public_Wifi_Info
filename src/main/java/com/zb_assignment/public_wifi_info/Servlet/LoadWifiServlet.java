package com.zb_assignment.public_wifi_info.Servlet;

import com.zb_assignment.public_wifi_info.DAO.WifiDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "LoadWifiServlet", value = "/load-wifi.jsp")
public class LoadWifiServlet extends HttpServlet {

    String apiKey = readApiKey();

    private String readApiKey() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/META-INF/OpenAPI_Key.txt")))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode(apiKey,"UTF-8") ); /*인증키*/
        urlBuilder.append("/" + URLEncoder.encode("xml","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("1","UTF-8")); /*요청시작위치*/
        urlBuilder.append("/" + URLEncoder.encode("140","UTF-8")); /*요청종료위치*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String listTotalCount = parseAndSaveData(sb.toString());

        request.setAttribute("listTotalCount", listTotalCount);

        // 캐시 무효화 설정
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        request.getRequestDispatcher("/load-wifi.jsp").forward(request,response);
    }

    public String parseAndSaveData(String xmlContent) {
        WifiDAO wifiDAO = new WifiDAO();
        String listTotalCount = "0";

        try {
            // XML 파서 설정
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            Element root = doc.getDocumentElement();
            listTotalCount = root.getElementsByTagName("list_total_count").item(0).getTextContent();

            // 모든 <row> 엘리먼트를 가져옴
            NodeList nodeList = doc.getElementsByTagName("row");

            // 각 <row> 엘리먼트에 대해 필요한 데이터 추출 및 저장
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // 필요한 데이터 추출
                    String mgrNo = element.getElementsByTagName("X_SWIFI_MGR_NO").item(0).getTextContent();
                    String wrdofc = element.getElementsByTagName("X_SWIFI_WRDOFC").item(0).getTextContent();
                    String mainNm = element.getElementsByTagName("X_SWIFI_MAIN_NM").item(0).getTextContent();
                    String adres1 = element.getElementsByTagName("X_SWIFI_ADRES1").item(0).getTextContent();
                    String adres2 = element.getElementsByTagName("X_SWIFI_ADRES2").item(0).getTextContent();
                    String instlFloor = element.getElementsByTagName("X_SWIFI_INSTL_FLOOR").item(0).getTextContent();
                    String instlTy = element.getElementsByTagName("X_SWIFI_INSTL_TY").item(0).getTextContent();
                    String instlMby = element.getElementsByTagName("X_SWIFI_INSTL_MBY").item(0).getTextContent();
                    String svcSe = element.getElementsByTagName("X_SWIFI_SVC_SE").item(0).getTextContent();
                    String cmcwr = element.getElementsByTagName("X_SWIFI_CMCWR").item(0).getTextContent();
                    int cnstcYear = Integer.parseInt(element.getElementsByTagName("X_SWIFI_CNSTC_YEAR").item(0).getTextContent());
                    String inoutDoor = element.getElementsByTagName("X_SWIFI_INOUT_DOOR").item(0).getTextContent();
                    String remars3 = element.getElementsByTagName("X_SWIFI_REMARS3").item(0).getTextContent();
                    double lat = Double.parseDouble(element.getElementsByTagName("LAT").item(0).getTextContent());
                    double lnt = Double.parseDouble(element.getElementsByTagName("LNT").item(0).getTextContent());

                    // 날짜 형식 맞추기
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                    LocalDateTime workDttm = LocalDateTime.parse(element.getElementsByTagName("WORK_DTTM").item(0).getTextContent(), formatter);

                    // 데이터베이스에 저장
                    wifiDAO.saveWifi(mgrNo, wrdofc, mainNm, adres1, adres2, instlFloor, instlTy, instlMby, svcSe, cmcwr, cnstcYear, inoutDoor, remars3, lat, lnt, workDttm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listTotalCount;
    }
}
