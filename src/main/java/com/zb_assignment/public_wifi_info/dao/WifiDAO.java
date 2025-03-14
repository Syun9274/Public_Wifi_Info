package com.zb_assignment.public_wifi_info.dao;

import com.zb_assignment.public_wifi_info.component.DistanceCalculator;
import com.zb_assignment.public_wifi_info.entity.Wifi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class WifiDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("public_wifi_info");

    public void saveWifi(
            String MGR_NO,
            String WRDOFC,
            String MAIN_NM,
            String ADRES1,
            String ADRES2,
            String INSTL_FLOOR,
            String INSTL_TY,
            String INSTL_MBY,
            String SVC_SE,
            String CMCWR,
            int CNSTC_YEAR,
            String INOUT_DOOR,
            String REMARS3,
            double LAT,
            double LNT,
            LocalDateTime WORK_DTTM
    ) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            Wifi wifi = new Wifi();

            wifi.setMGR_NO(MGR_NO);
            wifi.setWRDOFC(WRDOFC);
            wifi.setMAIN_NM(MAIN_NM);
            wifi.setADRES1(ADRES1);
            wifi.setADRES2(ADRES2);
            wifi.setINSTL_FLOOR(INSTL_FLOOR);
            wifi.setINSTL_TY(INSTL_TY);
            wifi.setINSTL_MBY(INSTL_MBY);
            wifi.setSVC_SE(SVC_SE);
            wifi.setCMCWR(CMCWR);
            wifi.setCNSTC_YEAR(CNSTC_YEAR);
            wifi.setINOUT_DOOR(INOUT_DOOR);
            wifi.setREMARS3(REMARS3);
            wifi.setLAT(LAT);
            wifi.setLNT(LNT);
            wifi.setWORK_DTTM(WORK_DTTM);

            em.persist(wifi);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    // 전체 와이파이 불러오기
    public List<Wifi> getAllWifi(int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        List<Wifi> dataList;

        try {
            dataList = em.createQuery("SELECT w FROM Wifi w", Wifi.class)
                    .setFirstResult((page - 1) * pageSize) // 시작 인덱스 설정
                    .setMaxResults(pageSize) // 페이지당 데이터 개수 설정
                    .getResultList();
        } finally {
            em.close();
        }

        return dataList;
    }

    /**
     * 가까운 순으로 정렬하여 반환
     *
     * @param lat 위도
     * @param lnt 경도
     * @param page 페이지
     * @param pageSize 페이지 크기
     * @return List[Wi-Fi]
     */
    public List<Wifi> getNearbyWifi(double lat, double lnt, int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        List<Wifi> wifiList;

        try {
            wifiList = em.createQuery("SELECT w FROM Wifi w", Wifi.class).getResultList();
        } finally {
            em.close();
        }

        // 거리 계산 후 거리 값을 `setDistance()`로 저장
        for (Wifi wifi : wifiList) {
            double distance = DistanceCalculator.calculateDistance(lat, lnt, wifi.getLAT(), wifi.getLNT());
            wifi.setDistance(distance);
        }

        // 거리 기준으로 정렬
        wifiList.sort(Comparator.comparingDouble(w -> w.getDistance() == 0.0 ? Double.MAX_VALUE : w.getDistance()));

        // 페이지네이션 적용
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, wifiList.size());

        return wifiList.subList(fromIndex, toIndex);
    }

    // 전체 와이파이 개수
    public long getWifiCount() {
        EntityManager em = emf.createEntityManager();
        long count;

        try {
            count = em.createQuery("SELECT COUNT(w) FROM Wifi w", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }

        return count;
    }

    // 예제 데이터 삽입
    public static void main(String[] args) {
        WifiDAO wifiDAO = new WifiDAO();
        wifiDAO.saveWifi(
                "A.관리번호",
                "자치구",
                "와이파이 명",
                "도로명주소",
                "상세주소",
                "설치위치(층)",
                "설치유형",
                "설치기관",
                "서비스 구분",
                "망 종류",
                2024,
                "실내외구분",
                "wifi 접속 환경",
                37.5,
                127.0,
                LocalDateTime.now()
        );
    }

}
