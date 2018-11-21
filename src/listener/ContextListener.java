package listener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import util.Time;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.Location;
import model.Route;
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
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent) Release the
	 *      EntityManagerFactory:
	 */
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		EntityManagerFactory emf = (EntityManagerFactory) context
						.getServletContext().getAttribute("emf");
		emf.close();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent) Prepare
	 *      the EntityManagerFactory & Enhance:
	 */
	@Override
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
			// create Test User
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

			Time time = new Time("03:30:00");

			Location location = new Location();
			location.setName("Hochschule Ludwigshafen 1");
			location.setType("Party");
			location.setTimeString("03:30:00");
			location.setTime(time);
			location.setLatitude(49.4775206);
			location.setLongitude(8.4219807);
			location.setPictures((List<byte[]>) new ArrayList<byte[]>());
			em.persist(location);
			Location location2 = new Location();
			location2.setName("Hochschule Ludwigshafen 2");
			location2.setType("Party");
			location.setTimeString("03:30:00");
			location2.setTime(time);
			location2.setLatitude(49.47303236240146);
			location2.setLongitude(8.394641872728245);
			location.setPictures((List<byte[]>) new ArrayList<byte[]>());
			em.persist(location2);
			Location location3 = new Location();
			location3.setName("Hochschule Ludwigshafen 3");
			location3.setType("Party");
			location.setTimeString("03:30:00");
			location3.setTime(time);
			location3.setLatitude(49.45674385539652);
			location3.setLongitude(8.41655731201172);
			location.setPictures((List<byte[]>) new ArrayList<byte[]>());
			em.persist(location3);
			Location location4 = new Location();
			location4.setName("Hochschule Ludwigshafen 4");
			location4.setType("Party");
			location.setTimeString("03:30:00");
			location4.setTime(time);
			location4.setLatitude(49.47035517151213);
			location4.setLongitude(8.44139098422602);
			location.setPictures((List<byte[]>) new ArrayList<byte[]>());
			em.persist(location4);
			
			Route route = new Route();
			route.setName("Tims Duftpfad");
			route.setDescription("Route über alle Punkte an denen Tim stinkt/gestunken hat/stinken wird.");
			route.setAvgRating(0);
			route.setFirstLat(location.getLatitude());
			route.setFirstLong(location.getLongitude());
			route.setNumberOfStops(4);
			route.setOwner(user);
			route.setTimeString("03:30:00");
			route.setTime(time);
			route.setPictures((List<byte[]>) new ArrayList<byte[]>());
			route.setType("Party");
			
			List<Location> l = new ArrayList<Location>();
			l.add(location);
			l.add(location2);
			l.add(location3);
			l.add(location4);
			route.setStops(l);
			
			em.persist(route);
			em.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		em.close();
	}

}
