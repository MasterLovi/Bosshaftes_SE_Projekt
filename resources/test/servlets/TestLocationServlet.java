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

import model.Location;
import servlets.LocationServlet;
import test.util.Reader;

/**
 * 
 * Test Class, to test LocationServlet
 *
 */
public class TestLocationServlet {
	
	// Needed Objects for the Tests
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	LocationServlet locationServlet;
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
	 * Setup the Test Environment: Mock JSF specific runtime artifacts and stub needed methods
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
		
		// Initialize the LocationServlet and attach the mocked Servlet Context
		locationServlet = new LocationServlet() {
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
	 * Test CRUD - Operations for Locations
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void run() throws IOException, ServletException {
		// Test Case: Create Location
		t1Create();
		
		// Test Case: Read Location
		t2Read();
		
		// Test Case: Update Location
		t3Update();
		
		// Test Case: Report Location
		t4Report();
		
		// Test Case: Delete Location
		t5Delete();
		
		// Test Case: User not logged in
		t6LoggedOut();
	}

	/**
	 * Test Cases: Create Location
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t1Create() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Create");
		
		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/locationCreate.json", StandardCharsets.UTF_8);
		
		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("create");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Location was created successfully
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// reset result for next test
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		
		// Test Case: Location already exists
		locationServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Location \"" + "test 1" + "\" exists already."));
	}

	/**
	 * Test Cases: Read Location
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t2Read() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Read");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/locationCreate.json", StandardCharsets.UTF_8);
		
		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("rating")).thenReturn("0");
		when(request.getParameter("boundNorthWestLat")).thenReturn("180");
		when(request.getParameter("boundNorthWestLng")).thenReturn("0");
		when(request.getParameter("boundSouthEastLat")).thenReturn("0");
		when(request.getParameter("boundSouthEastLng")).thenReturn("180");

		// Test Case Read Location
		when(request.getParameter("type")).thenReturn("Party");
		locationServlet.doGet(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals(testData));

		// Test Case: Wrong Type
		when(request.getParameter("type")).thenReturn("");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Type can only be \"Party\" or \"Kultur\"."));

		// Test Case: Type is null
		when(request.getParameter("type")).thenReturn(null);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Type darf nicht null sein!"));

		// Test Case: Nothing was found in db
		when(request.getParameter("type")).thenReturn("Kultur");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doGet(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("[]"));
	}

	/**
	 * Test Cases: Update Location
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t3Update() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Update");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/locationUpdate.json", StandardCharsets.UTF_8);
		
		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("update");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Update Location
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Test Case: Location does not exist
		List<Location> locations = new ArrayList<Location>();
		Location l = new Location();
		l.setId(-1);
		l.setName("rama lama ding dong");
		locations.add(l);
		Gson gson = new Gson();
		String delete = gson.toJson(locations);
		when(request.getParameter("json")).thenReturn(delete);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Location \"rama lama ding dong\" does not exist."));
	}

	/**
	 * Test Cases: Report Location
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t4Report() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Report");
		
		// Setup writer
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String result;
		
		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/locationUpdate.json", StandardCharsets.UTF_8);
		
		// Setup parameters
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("report");
		when(response.getWriter()).thenReturn(pw);
		
		// Report same Location 3 times
		for (int i = 0; i < 3; i++) {
			
			// First one should be successful
			when(session.getAttribute("username")).thenReturn("test" + i);
			pw.flush();
			sw.getBuffer().delete(0, sw.getBuffer().length());
			locationServlet.doPost(request, response);
			result = sw.getBuffer().toString();
			System.out.println(result);
			assertTrue(result.equals("Success"));
			
			// second time same as first with other user and third time should return an error
			when(session.getAttribute("username")).thenReturn("test0");
			pw.flush();
			sw.getBuffer().delete(0, sw.getBuffer().length());
			locationServlet.doPost(request, response);
			result = sw.getBuffer().toString();

			System.out.println(result);
			String message = (i != 2 ? "You have already reported this location." : "Location \"test 3\" does not exist.");
			assertTrue(result.equals(message));

		}

		// third successful report should delete location
		List<Location> locations = new ArrayList<Location>();
		Location l = new Location();
		l.setId(-1);
		l.setName("rama lama ding dong");
		locations.add(l);
		Gson gson = new Gson();
		String delete = gson.toJson(locations);
		when(request.getParameter("json")).thenReturn(delete);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doPost(request, response);
		result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("Location \"rama lama ding dong\" does not exist."));
	}

	/**
	 * Test Cases: Delete Location 
	 *
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t5Delete() throws IOException, ServletException {
		System.out.println("\n\nLocation Test Delete");

		// Get Test Data
		String testData = Reader.readFile("resources/test/data/locationDelete.json", StandardCharsets.UTF_8);
		
		// Setup Parameters
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("operation")).thenReturn("delete");

		// Setup writer
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Delete Location successfully
		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Test Case: Location does not exist
		List<Location> locations = new ArrayList<Location>();
		Location l = new Location();
		l.setId(-1);
		l.setName("rama lama ding dong");
		locations.add(l);
		Gson gson = new Gson();
		String delete = gson.toJson(locations);
		when(request.getParameter("json")).thenReturn(delete);
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		locationServlet.doPost(request, response);
		result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(
				result.equals("Location \"rama lama ding dong\" does not exist."));

	}

	/**
	 * Test Case: User not logged in
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t6LoggedOut() throws IOException, ServletException {
		System.out.println("\n\nLocation Test 'Not Logged In'");
		when(request.getParameter("json")).thenReturn("[]");
		when(session.getAttribute("loggedin")).thenReturn(false);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		locationServlet.doPost(request, response);
		String result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("You are not logged in."));
	}

}
