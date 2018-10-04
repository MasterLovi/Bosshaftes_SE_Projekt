package listener;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     * Release the EntityManagerFactory:
     */
    public void contextDestroyed(ServletContextEvent context)  { 
    	EntityManagerFactory emf = (EntityManagerFactory) context.getServletContext().getAttribute("emf");
        emf.close();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * Prepare the EntityManagerFactory & Enhance:
     */
    public void contextInitialized(ServletContextEvent context)  { 
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        context.getServletContext().setAttribute("emf", emf);
    }
	
}
