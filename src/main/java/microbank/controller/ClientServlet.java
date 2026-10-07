package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import microbank.model.Client;
import microbank.service.ClientService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/clients/*")
public class ClientServlet extends HttpServlet {

    private final ClientService service = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getPathInfo();
        if (action == null) action = "/";

        switch (action) {
            case "/new" -> showForm(req, resp, new Client());
            case "/edit" -> {
                Client c = findClient(req);
                if (c == null) redirectError(req, resp, "Client introuvable");
                else showForm(req, resp, c);
            }
            case "/details" -> {
                Client c = findClient(req);
                if (c == null) {
                    redirectError(req, resp, "Client introuvable");
                } else {
                    req.setAttribute("client", c);
                    req.getRequestDispatcher("/WEB-INF/views/clients/details.jsp").forward(req, resp);
                }
            }
            case "/delete" -> delete(req, resp);
            default -> list(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getPathInfo();

        if ("/create".equals(action)) {
            save(req, resp, new Client());
        } else if ("/update".equals(action)) {
            Client c = findClient(req);
            if (c == null) redirectError(req, resp, "Client introuvable");
            else save(req, resp, c);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ---------- liste avec recherche et pagination ----------
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

        List<Client> clients = service.search(search, page, size);

        req.setAttribute("clients", clients);
        req.setAttribute("search", search == null ? "" : search);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/WEB-INF/views/clients/list.jsp").forward(req, resp);
    }

    // ---------- formulaire ----------
    private void showForm(HttpServletRequest req, HttpServletResponse resp, Client client)
            throws ServletException, IOException {
        req.setAttribute("client", client);
        req.getRequestDispatcher("/WEB-INF/views/clients/form.jsp").forward(req, resp);
    }

    // ---------- création / modification ----------
    private void save(HttpServletRequest req, HttpServletResponse resp, Client client)
            throws ServletException, IOException {

        fill(req, client);
        Map<String, String> errors = service.validate(client);

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            showForm(req, resp, client);
            return;
        }

        boolean creation = (client.getId() == null);
        service.save(client);
        req.getSession().setAttribute("flashSuccess",
                creation ? "Client créé avec succès" : "Client modifié avec succès");
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    // ---------- suppression ----------
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Client c = findClient(req);
        HttpSession session = req.getSession();

        if (c == null) {
            session.setAttribute("flashError", "Client introuvable");
        } else {
            try {
                service.delete(c.getId());
                session.setAttribute("flashSuccess", "Client supprimé");
            } catch (RuntimeException e) {
                session.setAttribute("flashError",
                        "Suppression impossible : ce client possède des comptes");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    // ---------- outils ----------
    private void fill(HttpServletRequest req, Client c) {
        c.setNom(trim(req.getParameter("nom")));
        c.setPrenom(trim(req.getParameter("prenom")));
        c.setTelephone(trim(req.getParameter("telephone")));
        c.setEmail(emptyToNull(req.getParameter("email")));
        c.setAdresse(emptyToNull(req.getParameter("adresse")));
        c.setNumeroPiece(emptyToNull(req.getParameter("numeroPiece")));

        String dn = req.getParameter("dateNaissance");
        c.setDateNaissance(dn == null || dn.isBlank() ? null : LocalDate.parse(dn));
    }

    private Client findClient(HttpServletRequest req) {
        try {
            return service.findById(Long.parseLong(req.getParameter("id")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void redirectError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws IOException {
        req.getSession().setAttribute("flashError", msg);
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }

    private String trim(String s) { return s == null ? null : s.trim(); }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}