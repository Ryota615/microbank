package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import microbank.dao.UserDAO;
import microbank.model.User;
import microbank.util.PasswordUtil;


import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = userDAO.findByLogin(login);

        if (user == null || !user.getMotDePasse().equals(PasswordUtil.hash(password))) {
            req.setAttribute("error", "Login ou mot de passe incorrect");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } else if (!user.isActif()) {
            req.setAttribute("error", "Ce compte est désactivé");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }
}