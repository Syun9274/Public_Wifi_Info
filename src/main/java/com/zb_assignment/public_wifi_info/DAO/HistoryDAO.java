package com.zb_assignment.public_wifi_info.DAO;

import com.zb_assignment.public_wifi_info.Entity.History;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("public_wifi_info");

    public void saveHistory(double xPos, double yPos, LocalDateTime date) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            History history = new History();

            history.setxPos(xPos);
            history.setyPos(yPos);
            history.setDate(date);

            em.persist(history);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public List<History> getAllHistory() {
        EntityManager em = emf.createEntityManager();
        List<History> dataList;

        try {
            dataList = em.createQuery("select h from History h", History.class).getResultList();
        } finally {
            em.close();
        }

        return dataList;
    }

    // 예제 데이터 삽입
    public static void main(String[] args) {
        HistoryDAO historyDAO = new HistoryDAO();
        historyDAO.saveHistory(10.5, 20.5, LocalDateTime.now());
    }
}