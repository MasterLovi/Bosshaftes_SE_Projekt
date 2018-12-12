package test.servlets;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

import servlets.UserSessionServlet;

/**
 * 
 * Test Class, to test UserSessionServlet
 *
 */
public class TestUserSessionServlet {

	// Needed Test Objects
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	UserSessionServlet userSessionServlet;
	static EntityManagerFactory emf;

	String testName;
	Boolean testSession;
	Integer testId;

	/*
	 * Connect to DB before Tests are Started
	 */
	@BeforeClass
	public static void connectDB() {
		emf = Persistence.createEntityManagerFactory("TEST");
	}

	/**
	 * After Tests are finished, disconnect from DB
	 */
	@AfterClass
	public static void closeDB() {
		emf.close();
	}

	/**
	 * Setup the Test Environment: Mock JSF specific runtime artifacts and stub
	 * needed methods.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {

		// Mock the needed Servlet Objects (request, response, session, servletContext)
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		servletContext = Mockito.mock(ServletContext.class);

		// Initialize the RouteServlet and attach the mocked Servlet Context
		userSessionServlet = new UserSessionServlet() {
			public ServletContext getServletContext() {
				return servletContext;
			}
		};

		// Stub methods of JSF runtime artifacts to setup Test Environment
		Exception e = new Exception("TestException");
		Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
		Mockito.doReturn(session).when(request).getSession();
		Mockito.doReturn(null).when(request).getHeader("referer");
		Mockito.doNothing().when(response).sendRedirect(null);
		Mockito.doNothing().when(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

		// Create Answer objects for stubbed session methods
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

		// stub session methods
		Mockito.doAnswer(username).when(session).setAttribute(eq("username"), anyString());
		Mockito.doAnswer(loggedin).when(session).setAttribute(eq("loggedin"), anyBoolean());
		Mockito.doAnswer(id).when(session).setAttribute(eq("userid"), anyInt());
	}

	/**
	 * Test Login
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void TestLogin() throws ServletException, IOException {
		System.out.println("\n\nLogin Test");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Setup Parameter (username and password)
		when(request.getParameter("username")).thenReturn("test");
		when(request.getParameter("password")).thenReturn("testpw");

		// Test Case: User Logged in successfully
		userSessionServlet.doPost(request, response);
		assertTrue(testSession);
		assertTrue(testName.equals("test"));
		assertTrue(testId != null);

		// Test Case: Wrong Password
		when(request.getParameter("password")).thenReturn("testpw1");
		userSessionServlet.doPost(request, response);
		assertTrue(!testSession);

		// Test Case: Username already taken
		when(request.getParameter("username")).thenReturn("test1");
		userSessionServlet.doPost(request, response);
		assertTrue(!testSession);
	}

	/**
	 * Test Case: Logout
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void TestLogout() throws ServletException, IOException {
		System.out.println("\n\nLogout Test");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: User not logged out
		when(request.getParameter("logout")).thenReturn("false");
		userSessionServlet.doGet(request, response);
		assertNull(testSession);
		assertNull(testId);
		assertNull(testName);

		// Test Case: User not logged in
		when(request.getParameter("logout")).thenReturn("true");
		when(session.getAttribute("loggedin")).thenReturn(false);
		userSessionServlet.doGet(request, response);
		assertNull(testSession);
		assertNull(testId);
		assertNull(testName);

		// Test Case: successful logout
		when(request.getParameter("logout")).thenReturn("true");
		when(session.getAttribute("loggedin")).thenReturn(true);
		userSessionServlet.doGet(request, response);
		assertNull(testName);
		assertNull(testId);
		assertTrue(!testSession);

	}

}
