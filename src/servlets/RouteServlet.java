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
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Route;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.Time;

/**
 * Servlet implementation class RouteServlet
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

	private static String read(EntityManager em, String type, String maxTime, double minRating, int maxNoStops,
					double[] boundNorthWest, double[] boundSouthEast) throws Exception {

		if (type == null) {
			throw new Exception("Type darf nicht null sein!");
		} else if (!(type.equals("Party")  || type.equals("Kultur"))) {
			throw new Exception("Type muss entweder \"Party\" oder \"Kultur\" sein!");
		}

		// Build query with given parameters
		String selectQuery = "SELECT r FROM Route r"
						+ " WHERE r.type = '" + type + "'"
						+ " AND r.timeString <= \"" + maxTime + "\""
						+ " AND r.avgRating >= " + minRating
						+ " AND r.numberOfStops <= " + maxNoStops
						+ " AND r.firstLat BETWEEN " + boundNorthWest[0] + " AND " + boundSouthEast[0]
						+ " AND r.firstLong BETWEEN " + boundNorthWest[1] + " AND " + boundSouthEast[1];

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

	private static String update(List<Route> routes, EntityManager em) throws Exception {

		em.getTransaction().begin();
		// Loop over Routes that should be updated
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());
			
			if(result != null) {
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

			String[] paramBoundNorthWestString = request.getParameterValues("boundNorthWest");
			double[] paramBoundNorthWest = new double[2];
			paramBoundNorthWest[0] = Double.valueOf(paramBoundNorthWestString[0]);
			paramBoundNorthWest[1] = Double.valueOf(paramBoundNorthWestString[1]);

			String[] paramBoundSouthEastString = request.getParameterValues("boundSouthEast");
			double[] paramBoundSouthEast = new double[2];
			paramBoundSouthEast[0] = Double.valueOf(paramBoundSouthEastString[0]);
			paramBoundSouthEast[1] = Double.valueOf(paramBoundSouthEastString[1]);

			res = read(em, paramType, paramTime, paramRating, paramStops, paramBoundNorthWest, paramBoundSouthEast);
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
		}
		// Send Response
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		HttpSession session =  request.getSession();
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
			res = e.getMessage();
		}
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.print(res);
		em.close();
	}
}