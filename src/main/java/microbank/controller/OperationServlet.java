package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import microbank.model.User;
import microbank.service.OperationService;
import microbank.util.BusinessException;

import java.io.IOException;

@WebServlet("/operations/*")
public class OperationServlet extends HttpServlet {

    private final OperationService service = new OperationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("operations", service.findRecent(10));
        req.getRequestDispatcher("/WEB-INF/views/operations/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String action = req.getPathInfo();
        if (action == null) action = "";

        try {
            switch (action) {
                case "/deposit" -> {
                    service.deposit(req.getParameter("numero"), req.getParameter("montant"),
                            user.getId(), req.getParameter("description"));
                    session.setAttribute("flashSuccess", "Dépôt effectué avec succès");
                }
                case "/withdraw" -> {
                    service.withdraw(req.getParameter("numero"), req.getParameter("montant"),
                            user.getId(), req.getParameter("description"));
                    session.setAttribute("flashSuccess", "Retrait effectué avec succès");
                }
                case "/transfer" -> {
                    service.transfer(req.getParameter("source"), req.getParameter("destination"),
                            req.getParameter("montant"), user.getId(), req.getParameter("description"));
                    session.setAttribute("flashSuccess", "Virement effectué avec succès");
                }
                default -> {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
        } catch (BusinessException e) {
            session.setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/operations");
    }
}