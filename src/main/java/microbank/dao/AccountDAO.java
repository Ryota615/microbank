package microbank.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import microbank.model.Account;
import microbank.model.Client;
import microbank.model.Operation;
import microbank.model.StatutCompte;
import microbank.model.User;
import microbank.util.JPAUtil;

import java.util.List;

public class AccountDAO {

    private static final String WHERE =
            " WHERE LOWER(a.numeroCompte) LIKE :s OR LOWER(c.nom) LIKE :s OR LOWER(c.prenom) LIKE :s";

    private String pattern(String search) {
        if (search == null || search.isBlank()) return "%";
        return "%" + search.trim().toLowerCase() + "%";
    }

    public List<Account> search(String search, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Account a JOIN FETCH a.client c" + WHERE + " ORDER BY a.id DESC",
                            Account.class)
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
            return em.createQuery("SELECT COUNT(a) FROM Account a JOIN a.client c" + WHERE, Long.class)
                    .setParameter("s", pattern(search))
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Account findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Account> r = em.createQuery(
                            "SELECT a FROM Account a JOIN FETCH a.client WHERE a.id = :id", Account.class)
                    .setParameter("id", id)
                    .getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    public boolean existsNumero(String numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(a) FROM Account a WHERE a.numeroCompte = :n", Long.class)
                    .setParameter("n", numero)
                    .getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    /** Crée le compte et, si fourni, l'opération de dépôt initial, dans une seule transaction. */
    public void open(Account account, Long clientId, Operation depotInitial, Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            account.setClient(em.getReference(Client.class, clientId));
            em.persist(account);

            if (depotInitial != null) {
                depotInitial.setCompte(account);
                depotInitial.setUser(em.getReference(User.class, userId));
                em.persist(depotInitial);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateStatut(Long id, StatutCompte statut) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Account a = em.find(Account.class, id);
            if (a != null) a.setStatut(statut);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}