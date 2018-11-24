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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import servlets.RegistrationServlet;

public class TestRegistrationServlet {

	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	RegistrationServlet registrationServlet;
	static EntityManagerFactory emf;
	
	String testName;
	Boolean testSession;
	Integer testId;
	
	@BeforeClass
	public static void connectDB() {
		emf = Persistence.createEntityManagerFactory("DerbyDB");
	}
	
	@AfterClass
	public static void closeDB() {
		emf.close();
	}
	
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
		Exception e = new Exception("TestException");
		Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
		Mockito.doReturn(session).when(request).getSession();
		Mockito.doReturn(null).when(request).getHeader("referer");
		Mockito.doNothing().when(response).sendRedirect(null);
		Mockito.doNothing().when(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		
		
		Answer<Integer> username = new Answer<Integer>() {
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	            testName = invocation.getArgument(1);
	            return null;
	        }
	    };
	    Answer<Integer> loggedin = new Answer<Integer>() {
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	        	System.out.println(invocation.getArgument(1).toString());
	            testSession = invocation.getArgument(1);
	            return null;
	        }
	    };
	    Answer<Integer> id = new Answer<Integer>() {
	        public Integer answer(InvocationOnMock invocation) throws Throwable {
	            testId = invocation.getArgument(1);
	            return null;
	        }
	    };
		
		Mockito.doAnswer(username).when(session).setAttribute(eq("username"), anyString());
		Mockito.doAnswer(loggedin).when(session).setAttribute(eq("loggedin"), anyBoolean());
		Mockito.doAnswer(id).when(session).setAttribute(eq("userid"), anyInt());
	}

	@Test
	public void testRegister() throws IOException, ServletException {

		System.out.println("\n\nRegistration Test");

		when(request.getParameter("username")).thenReturn("test");
		when(request.getParameter("email")).thenReturn("test@test.de");
		when(request.getParameter("password")).thenReturn("testpw");
		when(request.getParameter("passwordRep")).thenReturn("testpw");

		registrationServlet.doPost(request, response);
		
		System.out.println(testName + " " + testSession + " " + testId);
		assertTrue(testSession);
		assertTrue(testName.equals("test"));
		assertTrue(testId != null);
		
		registrationServlet.doPost(request, response);
		
		when(request.getParameter("username")).thenReturn("test1");
		when(request.getParameter("passwordRep")).thenReturn("testp1w");
		
		registrationServlet.doPost(request, response);
		
		when(request.getParameter("passwordRep")).thenReturn("testpw");
		registrationServlet.doPost(request, response);
	}

}
