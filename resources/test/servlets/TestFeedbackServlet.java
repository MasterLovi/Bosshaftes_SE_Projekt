package test.servlets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.persistence.EntityManager;
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
import com.google.gson.GsonBuilder;

import model.Location;
import model.Route;
import model.Users;
import servlets.FeedbackServlet;
import test.data.Reader;

/**
 * 
 * Test Class, to test Feedback Servlet
 *
 */
public class TestFeedbackServlet {

	// Needed Objects for the Tests
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	FeedbackServlet feedbackServlet;
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
		
		// Initialize the Feedback Servlet and attach the mocked Servlet Context
		feedbackServlet = new FeedbackServlet() {
			@Override
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
	 * Test CUD - Operations for Location Feedback (Read is tested in LocationServlet Test Classes)
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void runLocation() throws IOException, ServletException {
		
		// Test Case: Create Location Feedback
		t1LCreate();
		
		// Test Case: Update Location Feedback
		t2LUpdate();
		
		// Test Case: Delete Location Feedback
		t3LDelete();
	}
	
	/**
	 * Test CUD - Operations for Route Feedback (Read is tested in RouteServlet Test Classes)
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void runRoute() throws IOException, ServletException {
		
		// Test Case: Create Route Feedback
		t1RCreate();
		
		// Test Case: Update Route Feedback
		t2RUpdate();
		
		// Test Case: Delete Route Feedback
		t3RDelete();
	}
	
	/**
	 * Test Case: Wrong Location-/Route- Type Parameter
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void tTypeIncorrect() throws IOException, ServletException {
		System.out.println("\n\nFeedback Wrong Type Test");

		when(request.getParameter("json")).thenReturn("[]");
		when(request.getParameter("type")).thenReturn("test");
		when(request.getParameter("id")).thenReturn("5");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("Type can only be \"Route\" or \"Location\"."));
	}
	
	/**
	 * Test Case: User not logged in
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void tLoggedOut() throws IOException, ServletException {
		System.out.println("\n\nFeedback Test 'Not Logged In'");

		when(request.getParameter("json")).thenReturn("[]");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("loggedin")).thenReturn(false);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("You are not logged in."));
	}
	
	/**
	 * Test Cases: Create Location Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t1LCreate() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Create");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackCreate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationCreateTest.json",
						StandardCharsets.UTF_8);

		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("create");
		when(request.getParameter("id")).thenReturn("5");
		
		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Feedback was created successfully
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		assertTrue(result.equals("Success"));
		
		// Assert that there is a location is persistent in DB and get it from there
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertNotNull(location);
		
		// Convert to JSON and test if Feedback has been saved
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(location);
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		// reset result for next test
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		
		// Test Case: User can only create one Feedback
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("This user has already given feedback for this route."));
		
		// Test Case: User does not exist
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));

		// Test Case: Location does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("The corresponding location does not exist."));

	}
	
	/**
	 * Test Cases: Update Location Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t2LUpdate() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Update");
		
		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackUpdate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationUpdateTest.json",
						StandardCharsets.UTF_8);
		
		// Setup parameters for first Test Case: Update Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("update");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("username")).thenReturn("test");
		
		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Update Feedback successfully
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));
		
		// Assert that there is a location is persistent in DB and get it from there
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertNotNull(location);
		
		// Convert to JSON and test if Feedback has been saved
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(location);
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));
		
		// Test Case: Modify another User's Feedback
		when(request.getParameter("json")).thenReturn("[{\"author\":{username:\"testFail\"}}]");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("You can only modify your own feedback."));
		
		// Test Case: User does not exist
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));
		
		// Test Case: Location does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("The corresponding location does not exist."));
	}
	
	/**
	 * Test Cases: Delete Location Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t3LDelete() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Delete");
		
		// Setup Delete Test
		Users testUser = new Users();
		testUser.setUsername("testFail");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(testUser);
		em.getTransaction().commit();

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackUpdate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackLocationUpdateTest.json",
						StandardCharsets.UTF_8);
		
		// Setup parameters for Test Cases: Delete Feedback
		when(request.getParameter("type")).thenReturn("Location");
		when(request.getParameter("operation")).thenReturn("delete");
		when(request.getParameter("id")).thenReturn("5");
		when(session.getAttribute("username")).thenReturn("testFail");
		
		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Delete another User's Feedback
		when(request.getParameter("json")).thenReturn("[{\"id\":1}]");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("You can only delete your own feedback."));
		
		// Test Case: User does not exist
		em.getTransaction().begin();
		em.remove(testUser);
		em.getTransaction().commit();
		em.close();
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));
		
		// Test Case: Location does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("The corresponding location does not exist."));

		
		// Test Case: Delete Feedback successfully
		when(request.getParameter("id")).thenReturn("5");
		when(request.getParameter("json")).thenReturn(testData);
		when(session.getAttribute("username")).thenReturn("test");
		
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		
		System.out.println(result);
		Location location = emf.createEntityManager().find(Location.class, 5);
		assertTrue(result.equals("Success"));
		assertNotNull(location);
		assertTrue(location.getFeedback().size() == 0);
	}

	/**
	 * Test Cases: Create Route Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t1RCreate() throws IOException, ServletException {
		System.out.println("\n\n Route Feedback Test Create");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackCreate.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteCreateTest.json",
						StandardCharsets.UTF_8);

		// Setup parameters for first Test Case: Create Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("create");
		when(request.getParameter("id")).thenReturn("2");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Create Feedback successfully
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Assert that there is a Route is persistent in DB and get it from there
		Route route = emf.createEntityManager().find(Route.class, 2);
		assertNotNull(route);

		// Convert to JSON and test if Feedback has been saved
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(route);
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));

		// Test Case: User creates another Feedback
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("This user has already given feedback for this route."));

		// Test Case: User does not exist
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));

		// Test Case: Route does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("The corresponding route does not exist."));

	}

	/**
	 * Test Cases: Update Route Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t2RUpdate() throws IOException, ServletException {
		System.out.println("\n\nRoute Feedback Test Update");

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackUpdate2.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteUpdateTest.json",
						StandardCharsets.UTF_8);

		// Setup parameters for first Test Case: Update Feedback successfully
		when(request.getParameter("json")).thenReturn(testData);
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("update");
		when(request.getParameter("id")).thenReturn("2");
		when(session.getAttribute("username")).thenReturn("test");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Update Feedback successfully
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("Success"));

		// Assert that there is a Route is persistent in DB and get it from there
		Route route = emf.createEntityManager().find(Route.class, 2);
		assertNotNull(route);

		// Convert to JSON and test if Feedback has been saved
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();
		String JSONData = gson.toJson(route);
		System.out.println(JSONData);
		assertTrue(JSONData.equals(testFeedback));

		// Test Case: Modify another User's Feedback
		when(request.getParameter("json")).thenReturn("[{\"author\":{username:\"testFail\"}}]");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("You can only modify your own feedback."));

		// Test Case: User does not exist
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));

		// Test Case: Route does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("The corresponding route does not exist."));
	}

	/**
	 * Test Cases: Delete Route Feedback
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void t3RDelete() throws IOException, ServletException {
		System.out.println("\n\nLocation Feedback Test Delete");

		Users testUser = new Users();
		testUser.setUsername("testFail");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(testUser);
		em.getTransaction().commit();

		// Get Test Data from test Files
		String testData = Reader.readFile("resources/test/data/feedbackUpdate2.json", StandardCharsets.UTF_8);
		String testFeedback = Reader.readFile("resources/test/data/feedbackRouteUpdateTest.json",
						StandardCharsets.UTF_8);

		// Setup parameters for first Test Cases: Delete Feedback
		when(request.getParameter("type")).thenReturn("Route");
		when(request.getParameter("operation")).thenReturn("delete");
		when(request.getParameter("id")).thenReturn("2");
		when(session.getAttribute("username")).thenReturn("testFail");

		// Setup Writer to test if returned message is same as expected
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		// Test Case: Delete another User's Feedback
		when(request.getParameter("json")).thenReturn("[{\"id\":2}]");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		String result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("You can only delete your own feedback."));

		// Test Case: User does not exist
		em.getTransaction().begin();
		em.remove(testUser);
		em.getTransaction().commit();
		em.close();
		when(session.getAttribute("username")).thenReturn("testFail");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		System.out.println(result);
		assertTrue(result.equals("User \"testFail\" does not exist."));

		// Test Case: Location does not exist
		when(request.getParameter("id")).thenReturn("-1");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();

		System.out.println(result);
		assertTrue(result.equals("The corresponding route does not exist."));

		
		// Test Case: Delete Feedback successfully
		when(request.getParameter("id")).thenReturn("2");
		when(request.getParameter("json")).thenReturn(testData);
		when(session.getAttribute("username")).thenReturn("test");
		pw.flush();
		sw.getBuffer().delete(0, sw.getBuffer().length());
		feedbackServlet.doPost(request, response);
		result = sw.getBuffer().toString();
		Route route = emf.createEntityManager().find(Route.class, 2);
		System.out.println(result);
		assertTrue(result.equals("Success"));
		assertNotNull(route);
		assertTrue(route.getFeedback().size() == 0);
	}

}
