package microbank.dao;

import jakarta.persistence.EntityManager;
import microbank.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StatsDAO {

    public long countClients() { return count("SELECT COUNT(c) FROM Client c"); }

    public long countAccounts() { return count("SELECT COUNT(a) FROM Account a"); }

    public BigDecimal totalSolde() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            BigDecimal total = em.createQuery("SELECT SUM(a.solde) FROM Account a", BigDecimal.class)
                    .getSingleResult();
            return total == null ? BigDecimal.ZERO : total;
        } finally {
            em.close();
        }
    }

    public long countOperationsToday() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(o) FROM Operation o WHERE o.dateOperation >= :debut", Long.class)
                    .setParameter("debut", LocalDate.now().atStartOfDay())
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    private long count(String jpql) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(jpql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}