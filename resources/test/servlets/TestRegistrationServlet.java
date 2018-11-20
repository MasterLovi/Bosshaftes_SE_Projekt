package test.servlets;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

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
		servletContext = Mockito.mock(ServletContext.class);
		registrationServlet = new RegistrationServlet() {
			public ServletContext getServletContext() {
				return servletContext;
			}
		};
		Exception e = new Exception();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("DerbyDB");
		Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
		Mockito.doReturn(session).when(request).getSession();
		Mockito.doReturn(null).when(request).getHeader("referer");
		Mockito.doNothing().when(response).sendRedirect(null);
		Mockito.doNothing().when(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@Test
	public void testRegister() throws IOException, ServletException {

		System.out.println("\n\nRegistration Test");

		when(request.getParameter("username")).thenReturn("test");
		when(request.getParameter("email")).thenReturn("test@test.de");
		when(request.getParameter("password")).thenReturn("testpw");
		when(request.getParameter("passwordRep")).thenReturn("testpw");

		registrationServlet.doPost(request, response);
		
		assertTrue(session.getAttribute("loggedin").equals("true"));
		assertTrue(session.getAttribute("username").equals("test"));
		assertTrue(session.getAttribute("userId") != null);

	}

}
