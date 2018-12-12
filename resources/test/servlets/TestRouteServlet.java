package test.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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

import com.google.gson.Gson;
import model.Route;
import servlets.RouteServlet;
import test.data.Reader;

/**
 * 
 * Test Class, to test RouteServlet
 *
 */
public class TestRouteServlet {

	// needed Test Objects
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	RouteServlet routeServlet;
	static EntityManagerFactory emf;

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
		session = mock(HttpSession.class);
		servletContext = Mockito.mock(ServletContext.class);
		
		// Initialize the RouteServlet and attach the mocked Servlet Context
		routeServlet = new RouteServlet() {
			public ServletContext getServletContext() {
				return servletContext;
			}
		};
		
		// Stub methods of JSF runtime artifacts to setup Test Environment
		Mockito.doReturn(emf).when(servletContext).getAttribute("emf");
		Mockito.doReturn(session).when(request).getSession();
		Mockito.doReturn(true).when(session).getAttribute("loggedin");
		Mockito.doReturn("test").when(session).getAttribute("username");
	}

	/**
	 * Test CRUD - Operations for Route
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void run() throws IOException, ServletException {
		
		// Test Case: Create Route
		t1Create();
		
		// Test Case: Read Route
		t2Read();
		
		// Test Case: Update Route
		t3Update();
		
		// Test Case: Delete Route
		t4Delete();
		
		// Test Case: User not logged in
		t5LoggedOut();
	}

	/*
	 * Test Cases: Create Route
	 */
	public void t1Create() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Create");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/routeCreate.json", StandardCharsets.UTF_8);
		
		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("create");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Create Route successfully
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

	}

	/**
	 * Test Cases: Read Route
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t2Read() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Read");

		// Get Test Data
		String testData = Reader.readFile("resources/test/data/routeCreate.json", StandardCharsets.UTF_8);
		
		// Setup Writer
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Setup Parameters
		when(request.getParameter("time")).thenReturn("180:00:00");
		when(request.getParameter("stops")).thenReturn("20");
		when(request.getParameter("rating")).thenReturn("0");
		when(request.getParameter("boundNorthWestLat")).thenReturn("180");
		when(request.getParameter("boundNorthWestLng")).thenReturn("0");
		when(request.getParameter("boundSouthEastLat")).thenReturn("0");
		when(request.getParameter("boundSouthEastLng")).thenReturn("180");

		// Test Case: Read Route Successfully
		when(request.getParameter("type")).thenReturn("Party");
		routeServlet.doGet(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals(testData));

		// Test Case: Read User Routes
		when(request.getParameter("owner")).thenReturn("1");
		when(request.getParameter("rating")).thenReturn(null);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals(testData));

		// Test Case: Nothing found because of Time
		when(request.getParameter("time")).thenReturn("00:00:05");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("[]"));

		// Test Case: Nothing found because of Rating
		when(request.getParameter("time")).thenReturn("180:00:00");
		when(request.getParameter("rating")).thenReturn("100");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("[]"));

		// Test Case: Wrong Type
		when(request.getParameter("type")).thenReturn("");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Type can only be \"Party\" or \"Kultur\"."));

		// Test Case: Type is null
		when(request.getParameter("type")).thenReturn(null);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Type cannot be null."));
	}

	/**
	 * Test Cases: Update Route
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t3Update() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Update");

		// Get Test Data
		String testData = Reader.readFile("resources/test/data/routeUpdate.json", StandardCharsets.UTF_8);
		
		// Setup Parameters
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("update");

		// Setup Writer
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Update Route successfully
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Test Case: Route does not exist
		List<Route> routes = new ArrayList<Route>();
		Route r = new Route();
		r.setId(-1);
		r.setName("rama lama ding dong");
		routes.add(r);
		Gson gson = new Gson();
		String delete = gson.toJson(routes);
		when(request.getParameter("json")).thenReturn(delete);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Route \"rama lama ding dong\" does not exist."));

		// Test Case: Nothing was updated
		when(request.getParameter("json")).thenReturn("[]");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));
	}

	/**
	 * Test Cases: Delete Route
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t4Delete() throws IOException, ServletException {
		System.out.println("\n\nRoute Test Delete");

		// Setup Parameters
		when(request.getParameter("json")).thenReturn("[{\"id\":1}]");
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("operation")).thenReturn("delete");

		// Setup writer
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Delete Route successfully
		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Test Case: Route does not exist.
		List<Route> routes = new ArrayList<Route>();
		Route r = new Route();
		r.setId(-1);
		r.setName("rama lama ding dong");
		routes.add(r);
		Gson gson = new Gson();
		String delete = gson.toJson(routes);
		when(request.getParameter("json")).thenReturn(delete);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		routeServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Route \"rama lama ding dong\"does not exist."));

	}

	/**
	 * Test Case: User is not logged in
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t5LoggedOut() throws IOException, ServletException {
		System.out.println("\n\nRoute Test 'Not Logged In'");

		when(request.getParameter("json")).thenReturn("[]");
		when(session.getAttribute("loggedin")).thenReturn(false);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		routeServlet.doPost(request, response);
		String result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("You are not logged in."));
	}

}
