package microbank.service;

import microbank.dao.ClientDAO;
import microbank.model.Client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ClientService {

    private final ClientDAO dao = new ClientDAO();

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public Map<String, String> validate(Client c) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (isBlank(c.getNom())) errors.put("nom", "Le nom est obligatoire");
        if (isBlank(c.getPrenom())) errors.put("prenom", "Le prénom est obligatoire");
        if (isBlank(c.getTelephone())) errors.put("telephone", "Le téléphone est obligatoire");

        if (!isBlank(c.getEmail()) && !EMAIL.matcher(c.getEmail()).matches()) {
            errors.put("email", "Adresse email invalide");
        }
        if (!isBlank(c.getNumeroPiece()) && dao.existsNumeroPiece(c.getNumeroPiece(), c.getId())) {
            errors.put("numeroPiece", "Ce numéro de pièce existe déjà");
        }
        return errors;
    }

    public List<Client> search(String search, int page, int size) {
        return dao.search(search, page, size);
    }

    public long count(String search) { return dao.count(search); }

    public Client findById(Long id) { return dao.findById(id); }

    public void save(Client c) { dao.save(c); }

    public void delete(Long id) { dao.delete(id); }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
    public List<Client> findAll() { return dao.findAll(); }
}