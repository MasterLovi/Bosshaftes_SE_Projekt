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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Route;
import util.JSON;

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

	private static String read(EntityManager em, String type) throws Exception {

		try {
			// Select Route from database table
			Query query = em.createQuery("SELECT r FROM Route r WHERE r.type = '" + type + "'");
			List<Route> result = query.getResultList();
			String JSONData;

			// check for empty resultList
			if (result.size() > 0) {
				// convert data to JSON
				JSONData = JSON.routeToJSON(result);
			} else {
				JSONData = "[]";
			}
			// return
			return JSONData;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String create(List<Route> routes, EntityManager em) {

		// Loop over Routes that should be created
		try {
			for (Route route : routes) {
				
				Route newRoute = new Route();
				
				newRoute.setName(route.getName());
				newRoute.setType(route.getType());
				newRoute.setTime(route.getTime());
				newRoute.setStops(route.getStops());
				newRoute.setFeedback(null);
				newRoute.setOwner(route.getOwner());
				
				em.getTransaction().begin();
				em.persist(newRoute);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			return "Failed: " + e.getMessage();
		}
		return "Success";
	}

	private static String delete(List<Route> routes, EntityManager em) throws Exception {
		// Loop over Routes that should be deleted
		try {
			for (Route route : routes) {
				Route result = em.find(Route.class, route.getId());
				em.getTransaction().begin();
				em.remove(result);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			return "Failed: " + e.getMessage();
		}
		return "Success";
	}

	private static String update(List<Route> routes, EntityManager em) throws Exception {

		try {
			Integer errorCount = 0;
			ArrayList<String> log = new ArrayList<String>();

			// Loop over Routes that should be updated
			for (Route route : routes) {
				Route result = em.find(Route.class, route.getId());

				// If route was found, it should be updated
				if (result.size() > 0) {
					Route resultRoute = new Route();
					resultRoute.setName(route.getName());
					resultRoute.setTime(route.getTime());
					resultRoute.setType(route.getType());
					resultRoute.setFeedback(route.getFeedback());
					resultRoute.setStops(route.getStops());
					resultRoute.setOwner(route.getOwner());

					em.getTransaction().begin();
					em.persist(resultRoute);
					em.getTransaction().commit();

				} else {
					errorCount++;
					log.add(route.getName());
				}
			}
			return errorCount == 0 ? "Success" : "{ errors: " + errorCount + ", routes: " + log.toString() + "}";
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory and create EntityManager
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		// Define response
		String res;

		// read Data
		try {
			String paramType = request.getParameter("type");
			res = read(em, paramType);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Gson gson = new Gson();
		List<Route> routes = gson.fromJson(request.getParameter("data"), new TypeToken<List<Route>>(){}.getType());
		String res = "";

		try {
			switch (request.getParameter("operation")) {
			case "update":
				res = update(routes, em);
				break;
			case "create":
				res = create(routes, em);
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
		writer.append(res);
		em.close();
	}
}