package microbank.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import microbank.model.Client;
import microbank.util.JPAUtil;

import java.util.List;

public class ClientDAO {
    public List<Client> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Client c ORDER BY c.nom, c.prenom", Client.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    private static final String WHERE =
            " WHERE LOWER(c.nom) LIKE :s OR LOWER(c.prenom) LIKE :s"
                    + " OR c.telephone LIKE :s OR LOWER(c.numeroPiece) LIKE :s";

    private String pattern(String search) {
        if (search == null || search.isBlank()) return "%";
        return "%" + search.trim().toLowerCase() + "%";
    }

    public List<Client> search(String search, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Client c" + WHERE + " ORDER BY c.id DESC", Client.class)
                    .setParameter("s", pattern(search))
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long count(String search) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Client c" + WHERE, Long.class)
                    .setParameter("s", pattern(search))
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Client findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public boolean existsNumeroPiece(String numeroPiece, Long excludeId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Client> found = em.createQuery(
                            "SELECT c FROM Client c WHERE c.numeroPiece = :np", Client.class)
                    .setParameter("np", numeroPiece)
                    .getResultList();
            for (Client c : found) {
                if (excludeId == null || !c.getId().equals(excludeId)) return true;
            }
            return false;
        } finally {
            em.close();
        }
    }

    public void save(Client client) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (client.getId() == null) em.persist(client);
            else em.merge(client);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Client c = em.find(Client.class, id);
            if (c != null) em.remove(c);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}