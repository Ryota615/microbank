package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import microbank.model.*;
import microbank.service.AccountService;
import microbank.service.ClientService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/accounts/*")
public class AccountServlet extends HttpServlet {

    private final AccountService service = new AccountService();
    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getPathInfo();
        if (action == null) action = "/";

        switch (action) {
            case "/new" -> showForm(req, resp);
            case "/details" -> {
                Account a = findAccount(req);
                if (a == null) {
                    flashError(req, resp, "Compte introuvable");
                } else {
                    req.setAttribute("account", a);
                    req.getRequestDispatcher("/WEB-INF/views/accounts/details.jsp").forward(req, resp);
                }
            }
            default -> list(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getPathInfo();

        if ("/create".equals(action)) create(req, resp);
        else if ("/status".equals(action)) changeStatus(req, resp);
        else resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        int size = parseInt(req.getParameter("size"), 10);
        if (size < 1 || size > 100) size = 10;
        int page = parseInt(req.getParameter("page"), 0);

        long total = service.count(search);
        int totalPages = (int) Math.max(1, Math.ceil((double) total / size));
        if (page < 0) page = 0;
        if (page > totalPages - 1) page = totalPages - 1;

        req.setAttribute("accounts", service.search(search, page, size));
        req.setAttribute("search", search == null ? "" : search);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/WEB-INF/views/accounts/list.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Client> clients = clientService.findAll();
        req.setAttribute("clients", clients);
        req.setAttribute("types", TypeCompte.values());
        req.getRequestDispatcher("/WEB-INF/views/accounts/form.jsp").forward(req, resp);
    }

    private void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, String> errors = new LinkedHashMap<>();

        Long clientId = null;
        try { clientId = Long.parseLong(req.getParameter("clientId")); }
        catch (NumberFormatException e) { errors.put("clientId", "Choisissez un client"); }

        TypeCompte type = null;
        try { type = TypeCompte.valueOf(req.getParameter("type")); }
        catch (IllegalArgumentException | NullPointerException e) { errors.put("type", "Type de compte invalide"); }

        BigDecimal depot = BigDecimal.ZERO;
        String d = req.getParameter("depot");
        if (d != null && !d.isBlank()) {
            try {
                depot = new BigDecimal(d.trim().replace(',', '.'));
                if (depot.signum() < 0) errors.put("depot", "Le dépôt ne peut pas être négatif");
            } catch (NumberFormatException e) {
                errors.put("depot", "Montant invalide");
            }
        }

        if (clientId != null && clientService.findById(clientId) == null) {
            errors.put("clientId", "Client introuvable");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("selectedClient", clientId);
            req.setAttribute("selectedType", type == null ? "" : type.name());
            req.setAttribute("depot", d);
            showForm(req, resp);
            return;
        }

        User user = (User) req.getSession().getAttribute("user");
        service.open(clientId, type, depot, user.getId());

        req.getSession().setAttribute("flashSuccess", "Compte ouvert avec succès");
        resp.sendRedirect(req.getContextPath() + "/accounts");
    }

    private void changeStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account a = findAccount(req);
        if (a == null) {
            flashError(req, resp, "Compte introuvable");
            return;
        }
        try {
            StatutCompte statut = StatutCompte.valueOf(req.getParameter("statut"));
            service.changeStatut(a.getId(), statut);
            req.getSession().setAttribute("flashSuccess", "Statut du compte mis à jour");
        } catch (IllegalArgumentException | NullPointerException e) {
            req.getSession().setAttribute("flashError", "Statut invalide");
        }
        resp.sendRedirect(req.getContextPath() + "/accounts/details?id=" + a.getId());
    }

    private Account findAccount(HttpServletRequest req) {
        try {
            return service.findById(Long.parseLong(req.getParameter("id")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void flashError(HttpServletRequest req, HttpServletResponse resp, String msg) throws IOException {
        req.getSession().setAttribute("flashError", msg);
        resp.sendRedirect(req.getContextPath() + "/accounts");
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}