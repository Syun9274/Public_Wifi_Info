package com.zb_assignment.public_wifi_info.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "wifi")
public class Wifi {

    // 관리번호
    @Id
    private String MGR_NO;

    // 자치구
    private String WRDOFC;

    // 와이파이 명
    private String MAIN_NM;

    // 도로명주소
    private String ADRES1;

    // 상세주소
    private String ADRES2;

    // 설치위치(층)
    private String INSTL_FLOOR;

    // 설치유형
    private String INSTL_TY;

    // 설치기관
    private String INSTL_MBY;

    // 서비스 구분
    private String SVC_SE;

    // 망 종류
    private String CMCWR;

    // 설치년도
    private int CNSTC_YEAR;

    // 실내외구분
    private String INOUT_DOOR;

    // wifi 접속 환경
    private String REMARS3;

    // Y좌표
    private double LAT;

    // X좌표
    private double LNT;

    // 작업일자
    private LocalDateTime WORK_DTTM;


    public String getMGR_NO() {
        return MGR_NO;
    }

    public void setMGR_NO(String MGR_NO) {
        this.MGR_NO = MGR_NO;
    }

    public String getWRDOFC() {
        return WRDOFC;
    }

    public void setWRDOFC(String WRDOFC) {
        this.WRDOFC = WRDOFC;
    }

    public String getMAIN_NM() {
        return MAIN_NM;
    }

    public void setMAIN_NM(String MAIN_NM) {
        this.MAIN_NM = MAIN_NM;
    }

    public String getADRES1() {
        return ADRES1;
    }

    public void setADRES1(String ADRES1) {
        this.ADRES1 = ADRES1;
    }

    public String getADRES2() {
        return ADRES2;
    }

    public void setADRES2(String ADRES2) {
        this.ADRES2 = ADRES2;
    }

    public String getINSTL_FLOOR() {
        return INSTL_FLOOR;
    }

    public void setINSTL_FLOOR(String INSTL_FLOOR) {
        this.INSTL_FLOOR = INSTL_FLOOR;
    }

    public String getINSTL_TY() {
        return INSTL_TY;
    }

    public void setINSTL_TY(String INSTL_TY) {
        this.INSTL_TY = INSTL_TY;
    }

    public String getINSTL_MBY() {
        return INSTL_MBY;
    }

    public void setINSTL_MBY(String INSTL_MBY) {
        this.INSTL_MBY = INSTL_MBY;
    }

    public String getSVC_SE() {
        return SVC_SE;
    }

    public void setSVC_SE(String SVC_SE) {
        this.SVC_SE = SVC_SE;
    }

    public String getCMCWR() {
        return CMCWR;
    }

    public void setCMCWR(String CMCWR) {
        this.CMCWR = CMCWR;
    }

    public int getCNSTC_YEAR() {
        return CNSTC_YEAR;
    }

    public void setCNSTC_YEAR(int CNSTC_YEAR) {
        this.CNSTC_YEAR = CNSTC_YEAR;
    }

    public String getINOUT_DOOR() {
        return INOUT_DOOR;
    }

    public void setINOUT_DOOR(String INOUT_DOOR) {
        this.INOUT_DOOR = INOUT_DOOR;
    }

    public String getREMARS3() {
        return REMARS3;
    }

    public void setREMARS3(String REMARS3) {
        this.REMARS3 = REMARS3;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLNT() {
        return LNT;
    }

    public void setLNT(double LNT) {
        this.LNT = LNT;
    }

    public LocalDateTime getWORK_DTTM() {
        return WORK_DTTM;
    }

    public void setWORK_DTTM(LocalDateTime WORK_DTTM) {
        this.WORK_DTTM = WORK_DTTM;
    }
}
