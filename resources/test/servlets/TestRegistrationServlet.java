package test.servlets;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import servlets.RegistrationServlet;

public class TestRegistrationServlet {

	

	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	RegistrationServlet registrationServlet;
	
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		 request = mock(HttpServletRequest.class);       
	     response = mock(HttpServletResponse.class);
	     session = mock(HttpSession.class);
	     session = mock(HttpSession.class);
	     servletContext = Mockito.mock(ServletContext.class);
	     registrationServlet = new RegistrationServlet(){
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
	
	@Test
	public void testRegister() throws IOException, ServletException {
		
		
		
	}
	
	
}
