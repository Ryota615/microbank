package microbank.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import microbank.dao.UserDAO;
import microbank.model.Role;
import microbank.model.User;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserDAO userDAO = new UserDAO();

        // si la table users est vide, on crée un compte administrateur de test
        if (userDAO.count() == 0) {
            User admin = new User();
            admin.setNom("Administrateur");
            admin.setPrenom("Compte");
            admin.setLogin("admin");
            admin.setMotDePasse(PasswordUtil.hash("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            userDAO.save(admin);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}