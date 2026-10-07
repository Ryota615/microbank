package microbank.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import microbank.model.*;
import microbank.util.BusinessException;
import microbank.util.JPAUtil;
import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class OperationDAO {

    public void deposit(String numero, BigDecimal montant, Long userId, String description) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Account a = lockByNumero(em, numero);
            checkActive(a);

            a.setSolde(a.getSolde().add(montant));
            em.persist(newOperation(em, a, TypeOperation.DEPOT, montant, userId, description, ref("DEP")));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void withdraw(String numero, BigDecimal montant, Long userId, String description) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Account a = lockByNumero(em, numero);
            checkActive(a);
            if (a.getSolde().compareTo(montant) < 0) {
                throw new BusinessException("Solde insuffisant (solde actuel : " + a.getSolde() + " FCFA)");
            }

            a.setSolde(a.getSolde().subtract(montant));
            em.persist(newOperation(em, a, TypeOperation.RETRAIT, montant, userId, description, ref("RET")));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void transfer(String numeroSource, String numeroDest, BigDecimal montant,
                         Long userId, String description) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            String first = numeroSource.compareTo(numeroDest) < 0 ? numeroSource : numeroDest;
            String second = first.equals(numeroSource) ? numeroDest : numeroSource;
            Account a1 = lockByNumero(em, first);
            Account a2 = lockByNumero(em, second);
            Account source = first.equals(numeroSource) ? a1 : a2;
            Account dest = (source == a1) ? a2 : a1;

            checkActive(source);
            checkActive(dest);
            if (source.getSolde().compareTo(montant) < 0) {
                throw new BusinessException("Solde insuffisant (solde actuel : " + source.getSolde() + " FCFA)");
            }

            source.setSolde(source.getSolde().subtract(montant));
            dest.setSolde(dest.getSolde().add(montant));

            String base = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String suffixe = (description == null || description.isBlank()) ? "" : " - " + description;

            em.persist(newOperation(em, source, TypeOperation.VIREMENT, montant, userId,
                    "Virement vers " + dest.getNumeroCompte() + suffixe, "VIR-" + base + "-D"));
            em.persist(newOperation(em, dest, TypeOperation.VIREMENT, montant, userId,
                    "Virement reçu de " + source.getNumeroCompte() + suffixe, "VIR-" + base + "-C"));

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Operation> findRecent(int max) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM Operation o JOIN FETCH o.compte ORDER BY o.dateOperation DESC, o.id DESC",
                            Operation.class)
                    .setMaxResults(max)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // ---------- outils ----------

    /** Charge le compte avec un verrou : personne d'autre ne peut le modifier avant la fin de la transaction. */
    private Account lockByNumero(EntityManager em, String numero) {
        List<Account> r = em.createQuery(
                        "SELECT a FROM Account a WHERE a.numeroCompte = :n", Account.class)
                .setParameter("n", numero)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
        if (r.isEmpty()) throw new BusinessException("Compte introuvable : " + numero);
        return r.get(0);
    }

    private void checkActive(Account a) {
        if (a.getStatut() != StatutCompte.ACTIF) {
            throw new BusinessException("Le compte " + a.getNumeroCompte()
                    + " n'est pas actif (statut : " + a.getStatut() + ")");
        }
    }

    private Operation newOperation(EntityManager em, Account compte, TypeOperation type,
                                   BigDecimal montant, Long userId, String description, String reference) {
        Operation op = new Operation();
        op.setReference(reference);
        op.setType(type);
        op.setMontant(montant);
        op.setDescription(description);
        op.setSoldeApres(compte.getSolde());
        op.setCompte(compte);
        op.setUser(em.getReference(User.class, userId));
        return op;
    }

    private String ref(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    private String where(String numero, TypeOperation type, LocalDateTime debut, LocalDateTime fin) {
        String w = " WHERE 1=1";
        if (numero != null) w += " AND c.numeroCompte = :n";
        if (type != null)   w += " AND o.type = :t";
        if (debut != null)  w += " AND o.dateOperation >= :d";
        if (fin != null)    w += " AND o.dateOperation < :f";
        return w;
    }

    private void params(Query q, String numero, TypeOperation type, LocalDateTime debut, LocalDateTime fin) {
        if (numero != null) q.setParameter("n", numero);
        if (type != null)   q.setParameter("t", type);
        if (debut != null)  q.setParameter("d", debut);
        if (fin != null)    q.setParameter("f", fin);
    }

    public List<Operation> search(String numero, TypeOperation type, LocalDateTime debut,
                                  LocalDateTime fin, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            var q = em.createQuery("SELECT o FROM Operation o JOIN FETCH o.compte c"
                    + where(numero, type, debut, fin) + " ORDER BY o.dateOperation DESC, o.id DESC", Operation.class);
            params(q, numero, type, debut, fin);
            return q.setFirstResult(page * size).setMaxResults(size).getResultList();
        } finally {
            em.close();
        }
    }

    public long count(String numero, TypeOperation type, LocalDateTime debut, LocalDateTime fin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            var q = em.createQuery("SELECT COUNT(o) FROM Operation o JOIN o.compte c"
                    + where(numero, type, debut, fin), Long.class);
            params(q, numero, type, debut, fin);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }
}