package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import microbank.dao.OperationDAO;
import microbank.model.TypeOperation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {

    private final OperationDAO dao = new OperationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String numero = clean(req.getParameter("numero"));
        String typeParam = clean(req.getParameter("type"));
        String debutParam = clean(req.getParameter("debut"));
        String finParam = clean(req.getParameter("fin"));

        if (numero != null) numero = numero.toUpperCase();
        TypeOperation type = null;
        LocalDateTime debut = null, fin = null;
        try {
            if (typeParam != null) type = TypeOperation.valueOf(typeParam);
            if (debutParam != null) debut = LocalDate.parse(debutParam).atStartOfDay();
            if (finParam != null) fin = LocalDate.parse(finParam).plusDays(1).atStartOfDay();
        } catch (RuntimeException e) {
            // filtre invalide : on l'ignore
        }

        int size = 10;
        long total = dao.count(numero, type, debut, fin);
        int totalPages = (int) Math.max(1, Math.ceil((double) total / size));
        int page = 0;
        try { page = Integer.parseInt(req.getParameter("page")); } catch (NumberFormatException e) { }
        page = Math.max(0, Math.min(page, totalPages - 1));

        req.setAttribute("operations", dao.search(numero, type, debut, fin, page, size));
        req.setAttribute("numero", numero == null ? "" : numero);
        req.setAttribute("type", typeParam == null ? "" : typeParam);
        req.setAttribute("debut", debutParam == null ? "" : debutParam);
        req.setAttribute("fin", finParam == null ? "" : finParam);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);
        req.getRequestDispatcher("/WEB-INF/views/operations/history.jsp").forward(req, resp);
    }

    private String clean(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}