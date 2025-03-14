package com.zb_assignment.public_wifi_info.service;

import com.zb_assignment.public_wifi_info.dao.WifiDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiService {

    int BATCH_SIZE = 1000;

    public int getBATCH_SIZE() {
        return BATCH_SIZE;
    }

    public String readApiKey() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/META-INF/OpenAPI_Key.txt")))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "sample"; // sample용 API KEY
        }
    }

    public StringBuilder callApiData(String startIndex, String endIndex, String apiKey) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode(apiKey,"UTF-8") ); /*인증키*/
        urlBuilder.append("/" + URLEncoder.encode("xml","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode(startIndex,"UTF-8")); /*요청시작위치*/
        urlBuilder.append("/" + URLEncoder.encode(endIndex,"UTF-8")); /*요청종료위치*/
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

        return sb;
    }

    public String parseTotalCount(String xmlContent) {
        String listTotalCount = "0";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            Element root = doc.getDocumentElement();
            listTotalCount = root.getElementsByTagName("list_total_count").item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listTotalCount;
    }

    public void parseAndSaveData(String xmlContent) {
        WifiDAO wifiDAO = new WifiDAO();

        try {
            // XML 파서 설정
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            // 모든 <row> 엘리먼트를 가져옴
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // 데이터 추출 (Null 체크 포함)
                    String mgrNo = getTextContentOrDefault(element, "X_SWIFI_MGR_NO", "N/A");
                    String wrdofc = getTextContentOrDefault(element, "X_SWIFI_WRDOFC", "N/A");
                    String mainNm = getTextContentOrDefault(element, "X_SWIFI_MAIN_NM", "N/A");
                    String adres1 = getTextContentOrDefault(element, "X_SWIFI_ADRES1", "N/A");
                    String adres2 = getTextContentOrDefault(element, "X_SWIFI_ADRES2", "");
                    String instlFloor = getTextContentOrDefault(element, "X_SWIFI_INSTL_FLOOR", "");
                    String instlTy = getTextContentOrDefault(element, "X_SWIFI_INSTL_TY", "N/A");
                    String instlMby = getTextContentOrDefault(element, "X_SWIFI_INSTL_MBY", "N/A");
                    String svcSe = getTextContentOrDefault(element, "X_SWIFI_SVC_SE", "N/A");
                    String cmcwr = getTextContentOrDefault(element, "X_SWIFI_CMCWR", "N/A");
                    int cnstcYear = Integer.parseInt(getTextContentOrDefault(element, "X_SWIFI_CNSTC_YEAR", "0"));
                    String inoutDoor = getTextContentOrDefault(element, "X_SWIFI_INOUT_DOOR", "N/A");
                    String remars3 = getTextContentOrDefault(element, "X_SWIFI_REMARS3", null);
                    double lat = Double.parseDouble(getTextContentOrDefault(element, "LAT", "0.0"));
                    double lnt = Double.parseDouble(getTextContentOrDefault(element, "LNT", "0.0"));

                    // 날짜 형식 변환
                    String workDttmStr = getTextContentOrDefault(element, "WORK_DTTM", "2000-01-01 00:00:00.0");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S]");
                    LocalDateTime workDttm = LocalDateTime.parse(workDttmStr, formatter);

                    // 데이터베이스에 저장
                    wifiDAO.saveWifi(mgrNo, wrdofc, mainNm, adres1, adres2, instlFloor, instlTy, instlMby, svcSe, cmcwr, cnstcYear, inoutDoor, remars3, lat, lnt, workDttm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // XML 태그 값이 없을 경우 기본값을 반환하는 함수
    private String getTextContentOrDefault(Element element, String tagName, String defaultValue) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0 && nodeList.item(0) != null)
                ? nodeList.item(0).getTextContent()
                : defaultValue;
    }
}
