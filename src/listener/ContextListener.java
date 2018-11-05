package listener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.Location;
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
			//create Test User
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
			
			Location location = new Location();
			location.setName("Hochschule Ludwigshafen 1");
			location.setType("party");
			location.setTimeInMinutes(5);
			location.setLatitude(49.4775206);
			location.setLongitude(8.4219807);
			em.persist(location);
			Location location2 = new Location();
			location2.setName("Hochschule Ludwigshafen 2");
			location2.setType("party");
			location2.setTimeInMinutes(5);
			location2.setLatitude(49.47303236240146);
			location2.setLongitude(8.394641872728245);
			em.persist(location2);
			Location location3 = new Location();
			location3.setName("Hochschule Ludwigshafen 3");
			location3.setType("party");
			location3.setTimeInMinutes(5);
			location3.setLatitude(49.45674385539652);
			location3.setLongitude(8.41655731201172);
			em.persist(location3);
			Location location4 = new Location();
			location4.setName("Hochschule Ludwigshafen 4");
			location4.setType("party");
			location4.setTimeInMinutes(5);
			location4.setLatitude(49.47035517151213);
			location4.setLongitude(8.44139098422602);
			em.persist(location4);
			em.getTransaction().commit();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.close();
	}

}
