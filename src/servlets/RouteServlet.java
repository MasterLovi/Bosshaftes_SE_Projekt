package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Route;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.Time;

/**
 * Servlet implementation class RouteServlet - This servlet is responsible for
 * reading route data from the database, creating new entries, deleting and
 * updating entries
 */
@WebServlet("/RouteServlet")
public class RouteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RouteServlet() {
		super();
	}

	/**
	 * Method to read route data from the database
	 * 
	 * @param em                 EntityManager manages database operations
	 * @param type               Type of Location (must be "Kultur" or "Party")
	 * @param maxTime            maximum time that the routes selected should have
	 * @param minRating          minimal rating that the routes selected should have
	 * @param maxNoStops         maximum number of stops that the routes selected
	 *                           should have
	 * @param boundNorthWestLat  Latitude of upper left corner of the map
	 * @param boundNorthWestLong Longitude of upper left corner of the map
	 * @param boundSouthEastLat  Latitude of lower right corner of the map
	 * @param boundSouthEastLong Longitude of lower right corner of the map
	 * @return JSONData with found routes; if no entries are found it returns an
	 *         empty array []
	 * @exception Exception if type is null
	 * @exception Exception if type is neither "Kultur" nor "Party"
	 */
	private static String read(EntityManager em, String type, String maxTime, double minRating, int maxNoStops,
					double boundNorthWestLat, double boundNorthWestLong, double boundSouthEastLat,
					double boundSouthEastLong) throws Exception {

		if (type == null) {
			throw new Exception("Type darf nicht null sein!");
		} else if (!(type.equals("Party") || type.equals("Kultur"))) {
			throw new Exception("Type muss entweder \"Party\" oder \"Kultur\" sein!");
		}
		// Build query with given parameters
		String selectQuery = "SELECT r FROM Route r"
						+ " WHERE r.type = '" + type + "'"
						+ " AND r.avgRating >= " + minRating
						+ " AND r.numberOfStops <= " + maxNoStops
						+ " AND r.firstLat BETWEEN " + boundSouthEastLat + " AND " + boundNorthWestLat
						+ " AND r.firstLong BETWEEN " + boundNorthWestLong + " AND " + boundSouthEastLong;

		// Select Route from database table
		Query query = em.createQuery(selectQuery);
		List<Route> result = query.getResultList();

		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			for (Route route : result) {
				route.setTime(new Time(route.getTimeString()));
				List<String> images = new ArrayList<String>();
				// convert pictures and data to JSON
				if (route.getPictures() != null) {
					for (byte[] picture : route.getPictures()) {
						String image64 = new BASE64Encoder().encode(picture);
						images.add(image64);
					}
					route.setImages(images);
				} else {
					route.setImages(null);
				}
			}
			Gson gson = new Gson();
			JSONData = gson.toJson(result);
		} else {
			JSONData = "[]";
		}
		// return results
		return JSONData;
	}

	/**
	 * Method to create new routes in the database
	 * 
	 * @param routes  List of routes that should be created
	 * @param em      EntityManager manages database operations
	 * @param session Needed to retrieve the user that creates the route
	 * @return "Success" if the routes are successfully created
	 */
	private static String create(List<Route> routes, EntityManager em, HttpSession session) throws Exception {
		// Loop over Routes that should be created
		em.getTransaction().begin();
		for (Route route : routes) {
			Route newRoute = new Route();
			newRoute.setName(route.getName());
			newRoute.setType(route.getType());
			newRoute.setTimeString(route.getTime().getTime());
			newRoute.setStops(route.getStops());
			newRoute.setNumberOfStops(route.getStops().size());
			newRoute.setFirstLong(route.getStop(0).getLongitude());
			newRoute.setFirstLat(route.getStop(0).getLatitude());
			newRoute.setFeedback(null);
			newRoute.setAvgRating(0);
			newRoute.setDescription(route.getDescription());

			// Select Route from database table
			Query query = em.createQuery(
							"SELECT u FROM Users u WHERE u.username = '" + session.getAttribute("username") + "'");
			List<Route> result = query.getResultList();
			newRoute.setOwner(route.getOwner());

			List<byte[]> images = new ArrayList<byte[]>();
			if (route.getImages() != null) {
				for (String sBase64 : route.getImages()) {
					byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
					images.add(image);
				}
				newRoute.setPictures(images);
			} else {
				newRoute.setPictures(null);
			}

			em.persist(newRoute);
		}
		em.getTransaction().commit();
		return "Success";
	}

	/**
	 * Method to delete routes from database
	 * 
	 * @param routes List of routes that should be deleted
	 * @param em     EntityManager manages database operations
	 * @return "Success" if the routes are successfully deleted
	 * @exception Exception if route doesn't exist
	 */
	private static String delete(List<Route> routes, EntityManager em) throws Exception {
		// Loop over Routes that should be deleted
		em.getTransaction().begin();
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());
			if (result != null) {
				em.remove(result);
			} else {
				throw new Exception("Route \"" + route.getName()
								+ "\"existiert nicht und kann daher nicht gelöscht werden");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	/**
	 * Method to update routes in the database
	 * 
	 * @param routes List of routes that should be updated
	 * @param em     EntityManager manages database operations
	 * @return "Success" if the routes are successfully updated
	 * @exception Exception if route doesn't exist
	 */
	private static String update(List<Route> routes, EntityManager em) throws Exception {

		em.getTransaction().begin();
		// Loop over Routes that should be updated
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());

			if (result != null) {
				result.setName(route.getName());
				result.setTimeString(route.getTime().getTime());
				result.setType(route.getType());
				result.setFeedback(route.getFeedback());

				/*
				 * List<Feedback> routeFeedback = new ArrayList<Feedback>(); routeFeedback =
				 * route.getFeedback(); double avgRating = 0;
				 * 
				 * for (Feedback feedback : routeFeedback) { avgRating = avgRating +
				 * feedback.getRating(); } avgRating = avgRating / routeFeedback.size();
				 * avgRating = Math.round(avgRating * Math.pow(10, 1)) / Math.pow(10, 1);
				 * 
				 * result.setAvgRating(avgRating);
				 */

				result.setStops(route.getStops());
				result.setNumberOfStops(route.getStops().size());
				result.setFirstLong(route.getStop(0).getLongitude());
				result.setFirstLat(route.getStop(0).getLatitude());
				result.setOwner(route.getOwner());

				List<byte[]> images = new ArrayList<byte[]>();
				for (String sBase64 : route.getImages()) {
					byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
					images.add(image);
				}
				result.setPictures(images);

			} else {
				throw new Exception("Route \"" + route.getName() + "\" existiert net.");
			}

		}
		em.getTransaction().commit();
		return "Success";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory and create EntityManager
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		// Define response
		String res;

		// read Data
		try {
			// retrieve all parameters
			String paramType = request.getParameter("type");
			String paramTime = request.getParameter("time");
			double paramRating = Double.valueOf(request.getParameter("rating"));
			int paramStops = Integer.valueOf(request.getParameter("stops"));

			double paramBoundNorthWestLat = Double.valueOf(request.getParameter("boundNorthWestLat"));
			double paramBoundNorthWestLong = Double.valueOf(request.getParameter("boundNorthWestLng"));
			double paramBoundSouthEastLat = Double.valueOf(request.getParameter("boundSouthEastLat"));
			double paramBoundSouthEastLong = Double.valueOf(request.getParameter("boundSouthEastLng"));

			res = read(em, paramType, paramTime, paramRating, paramStops, paramBoundNorthWestLat,
							paramBoundNorthWestLong, paramBoundSouthEastLat, paramBoundSouthEastLong);
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
			e.printStackTrace();
		}
		// Send Response
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @exception Exception if user is not logged in
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		HttpSession session = request.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Gson gson = new Gson();
		List<Route> routes = gson.fromJson(request.getParameter("data"), new TypeToken<List<Route>>() {
		}.getType());
		String res = "";

		try {
			if (!session.getAttribute("loggedin").equals("true")) {
				throw new Exception("You are not logged in.");
			}
			switch (request.getParameter("operation")) {
			case "update":
				res = update(routes, em);
				break;
			case "create":
				res = create(routes, em, session);
				break;
			case "delete":
				res = delete(routes, em);
				break;
			}
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			e.printStackTrace();
			res = e.getMessage();
		}
		PrintWriter writer = response.getWriter();
		writer.print(res);
		em.close();
	}
}