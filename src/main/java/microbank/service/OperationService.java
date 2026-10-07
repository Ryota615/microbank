package microbank.service;

import microbank.dao.OperationDAO;
import microbank.model.Operation;
import microbank.util.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OperationService {

    private final OperationDAO dao = new OperationDAO();

    public void deposit(String numero, String montant, Long userId, String description) {
        dao.deposit(cleanNumero(numero), parseMontant(montant), userId, defaut(description, "Dépôt"));
    }

    public void withdraw(String numero, String montant, Long userId, String description) {
        dao.withdraw(cleanNumero(numero), parseMontant(montant), userId, defaut(description, "Retrait"));
    }

    public void transfer(String source, String dest, String montant, Long userId, String description) {
        String s = cleanNumero(source);
        String d = cleanNumero(dest);
        if (s.equals(d)) {
            throw new BusinessException("Le compte source et le compte destinataire doivent être différents");
        }
        dao.transfer(s, d, parseMontant(montant), userId, description);
    }

    public List<Operation> findRecent(int max) {
        return dao.findRecent(max);
    }

    private String cleanNumero(String numero) {
        if (numero == null || numero.isBlank()) throw new BusinessException("Le numéro de compte est obligatoire");
        return numero.trim().toUpperCase();
    }

    private BigDecimal parseMontant(String s) {
        BigDecimal m;
        try {
            m = new BigDecimal(s.trim().replace(',', '.')).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException | NullPointerException e) {
            throw new BusinessException("Montant invalide");
        }
        if (m.signum() <= 0) throw new BusinessException("Le montant doit être supérieur à zéro");
        if (m.compareTo(new BigDecimal("9999999999999")) > 0) throw new BusinessException("Montant trop élevé");
        return m;
    }

    private String defaut(String description, String valeur) {
        return (description == null || description.isBlank()) ? valeur : description.trim();
    }
}