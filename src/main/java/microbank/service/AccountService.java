package microbank.service;

import microbank.dao.AccountDAO;
import microbank.model.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class AccountService {

    private final AccountDAO dao = new AccountDAO();
    private final SecureRandom random = new SecureRandom();

    public List<Account> search(String search, int page, int size) { return dao.search(search, page, size); }

    public long count(String search) { return dao.count(search); }

    public Account findById(Long id) { return dao.findById(id); }

    public void open(Long clientId, TypeCompte type, BigDecimal depot, Long userId) {
        Account a = new Account();
        a.setNumeroCompte(generateNumero());
        a.setType(type);
        a.setSolde(depot);

        Operation op = null;
        if (depot.signum() > 0) {
            op = new Operation();
            op.setReference("OP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            op.setType(TypeOperation.DEPOT);
            op.setMontant(depot);
            op.setSoldeApres(depot);
            op.setDescription("Dépôt initial à l'ouverture du compte");
        }
        dao.open(a, clientId, op, userId);
    }

    public void changeStatut(Long id, StatutCompte statut) { dao.updateStatut(id, statut); }

    private String generateNumero() {
        String numero;
        do {
            numero = "SN" + String.format("%010d", Math.abs(random.nextLong() % 10_000_000_000L));
        } while (dao.existsNumero(numero));
        return numero;
    }
}