package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
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
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import model.Route;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
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

		// Select Route from database table
		Query query = em.createQuery("SELECT r FROM Route r WHERE r.type = '" + type + "'");
		List<Route> result = query.getResultList();
		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			for (Route route : result) {
				List<String> images = new ArrayList<String>();
				// convert pictures and data to JSON
				for (byte[] picture : route.getPictures()) {
					String image64 = new BASE64Encoder().encode(picture);
					images.add(image64);
				}
				route.setImages(images);
			}
			JSONData = JSON.routeToJSON(result);
		} else {
			JSONData = "[]";
		}
		// return
		return JSONData;
	}

	private static String create(List<Route> routes, EntityManager em) throws Exception {

		// Loop over Routes that should be created
		em.getTransaction().begin();
		for (Route route : routes) {
			Route newRoute = new Route();
			newRoute.setName(route.getName());
			newRoute.setType(route.getType());
			newRoute.setTime(route.getTime());
			newRoute.setStops(route.getStops());
			newRoute.setFeedback(null);
			newRoute.setDescription(route.getDescription());
			newRoute.setOwner(route.getOwner());

			List<byte[]> images = new ArrayList<byte[]>();
			for (String sBase64 : route.getImages()) {
				byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
				images.add(image);
			}
			newRoute.setPictures(images);

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
			em.remove(result);
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String update(List<Route> routes, EntityManager em) throws Exception {

		em.getTransaction().begin();
		// Loop over Routes that should be updated
		for (Route route : routes) {
			Route result = em.find(Route.class, route.getId());
			result.setName(route.getName());
			result.setTime(route.getTime());
			result.setType(route.getType());
			result.setFeedback(route.getFeedback());
			result.setStops(route.getStops());
			result.setOwner(route.getOwner());

			List<byte[]> images = new ArrayList<byte[]>();
			for (String sBase64 : route.getImages()) {
				byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
				images.add(image);
			}
			result.setPictures(images);

		}
		em.getTransaction().commit();
		return "Success";
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
		List<Route> routes = gson.fromJson(request.getParameter("data"), new TypeToken<List<Route>>() {
		}.getType());
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