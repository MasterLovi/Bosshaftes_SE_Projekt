package listener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.Users;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ContextListener() {
	}

	/**
	 * Persistence Unit
	 */
	private static final String PERSISTENCE_UNIT_NAME = "DerbyDB";

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent) Release
	 *      the EntityManagerFactory:
	 */
	public void contextDestroyed(ServletContextEvent context) {
		EntityManagerFactory emf = (EntityManagerFactory) context
		        .getServletContext().getAttribute("emf");
		emf.close();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 *      Prepare the EntityManagerFactory & Enhance:
	 */
	public void contextInitialized(ServletContextEvent context) {

		// Decide on the db system directory: <userhome>/.addressbook/
		String userHomeDir = System.getProperty("user.home", ".");
		String systemDir = userHomeDir + "/.database";
		// Set the db system directory.
		System.setProperty("derby.system.home", systemDir);

		EntityManagerFactory emf = Persistence
		        .createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		context.getServletContext().setAttribute("emf", emf);

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			Users user = new Users();
			user.setUsername("test");
			user.setEmail("test@test.com");
			String password = "test";
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			String hashedPW = new String(hash);
			user.setPassword(hashedPW);
			em.persist(user);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.close();
	}

}
