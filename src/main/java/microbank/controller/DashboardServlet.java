package microbank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import microbank.dao.StatsDAO;
import microbank.service.OperationService;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final StatsDAO stats = new StatsDAO();
    private final OperationService operationService = new OperationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("nbClients", stats.countClients());
        req.setAttribute("nbComptes", stats.countAccounts());
        req.setAttribute("totalSolde", stats.totalSolde());
        req.setAttribute("nbOperationsJour", stats.countOperationsToday());
        req.setAttribute("dernieres", operationService.findRecent(5));
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}