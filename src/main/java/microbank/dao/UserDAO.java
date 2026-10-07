package microbank.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import microbank.model.User;
import microbank.util.JPAUtil;

import java.util.List;

public class UserDAO {

    public User findByLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<User> result = em.createQuery(
                            "SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void save(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}