package test.servlets;

import static org.mockito.Mockito.mock;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.mockito.Mockito;

import servlets.LocationServlet;

public class TestUserSessionServlet {
	

	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	LocationServlet locationServlet;
	
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		 request = mock(HttpServletRequest.class);       
	     response = mock(HttpServletResponse.class);
	     session = mock(HttpSession.class);
	     session = mock(HttpSession.class);
	     servletContext = Mockito.mock(ServletContext.class);
	     locationServlet = new LocationServlet(){
	       public ServletContext getServletContext() {
	         return servletContext;
	       }
	     };
	     
	     EntityManagerFactory emf = Persistence.createEntityManagerFactory("DerbyDB");
	     Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
	     Mockito.doReturn(session).when(request).getSession();
	     Mockito.doReturn("true").when(session).getAttribute("loggedin");
	     Mockito.doReturn("test").when(session).getAttribute("username");
	}
	

}
