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

import model.Address;
import model.Location;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.Time;

/**
 * Servlet implementation class LocationServlet
 */
@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocationServlet() {
		super();
	}

	private static String read(EntityManager em, String type, double[] boundNorthWest, double[] boundSouthEast)
					throws Exception {

		if (type == null) {
			throw new Exception("Type darf nicht null sein!");
		} else if (!(type.equals("Party") || type.equals("Kultur"))) {
			throw new Exception("Type muss entweder \"Party\" oder \"Kultur\" sein!");
		}

		// Build query with given parameters
		String selectQuery = "SELECT l FROM Location l "
						+ "WHERE l.type = '" + type + "'"
						+ " AND l.latitude BETWEEN " + boundNorthWest[0] + " AND " + boundSouthEast[0]
						+ " AND l.longitude BETWEEN " + boundNorthWest[1] + " AND " + boundSouthEast[1];

		// Select Location from database table
		Query query = em.createQuery(selectQuery);
		List<Location> result = query.getResultList();

		String JSONData;

		// check for empty resultList
		if (result.size() > 0) {
			for (Location location : result) {
				location.setTime(new Time(location.getTimeString()));
				List<String> images = new ArrayList<String>();
				// convert pictures and data to JSON
				if (location.getPictures() != null) {
					for (byte[] picture : location.getPictures()) {
						String image64 = new BASE64Encoder().encode(picture);
						images.add(image64);
					}
					location.setImages(images);
				} else {
					location.setImages(null);
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

	private static String create(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Routes that should be created
		em.getTransaction().begin();
		for (Location location : locations) {

			// find out if location already exists
			String selectQuery = "SELECT l from Location l"
							+ " WHERE l.latitude = " + location.getLatitude()
							+ " AND l.longitude = " + location.getLatitude();
			Query query = em.createQuery(selectQuery);
			List<Location> result = query.getResultList();

			if (result.size() == 0) {
				Location newLocation = new Location();
				newLocation.setName(location.getName());
				newLocation.setType(location.getType());
				newLocation.setTimeString(location.getTime().getTime());
				newLocation.setFeedback(null);
				newLocation.setAddress(location.getAddress());
				newLocation.setLatitude(location.getLatitude());
				newLocation.setLongitude(location.getLongitude());
				newLocation.setTimesReported(0);
				newLocation.setDescription(location.getDescription());

				List<byte[]> images = new ArrayList<byte[]>();
				if (location.getImages() != null) {
					for (String sBase64 : location.getImages()) {
						byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
						images.add(image);
					}
					newLocation.setPictures(images);
				} else {
					newLocation.setPictures(null);
				}

				em.persist(newLocation);
			} else {
				throw new Exception("Location \"" + location.getName() + "\" existiert bereits.");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String delete(List<Location> locations, EntityManager em) throws Exception {
		// Loop over Locations that should be deleted
		em.getTransaction().begin();
		for (Location location : locations) {
			Location result = em.find(Location.class, location.getId());
			if (result != null) {
				em.remove(result);
			} else {
				throw new Exception("Location \"" + location.getName()
								+ "\"existiert nicht und kann daher nicht gelöscht werden");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String update(List<Location> locations, EntityManager em) throws Exception {
		em.getTransaction().begin();

		// Loop over Locations that should be updated
		for (Location location : locations) {
			String selectQuery = "SELECT l from Location l WHERE l.id = " + location.getId();
			Query query = em.createQuery(selectQuery);

			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultLocation = result.get(0);
				resultLocation.setName(location.getName());
				resultLocation.setTimeString(location.getTime().getTime());
				resultLocation.setType(location.getType());
				resultLocation.setLatitude(location.getLatitude());
				resultLocation.setLongitude(location.getLongitude());
				resultLocation.setDescription(location.getDescription());

				// update Images
				List<byte[]> images = new ArrayList<byte[]>();
				for (String sBase64 : location.getImages()) {
					byte[] image = new BASE64Decoder().decodeBuffer(sBase64);
					images.add(image);
				}
				resultLocation.setPictures(images);

				// update corresponding Address
				Address address = location.getAddress();
				query = em.createQuery("SELECT a from Address a WHERE" + " a.id = " + address.getId());
				List<Address> resultAddresses = query.getResultList();

				if (resultAddresses.size() > 0) {
					Address resultAddress = resultAddresses.get(0);
					resultAddress.setAddress(address);
				} else {
					em.persist(address);
					resultLocation.setAddress(address);
				}
			} else {
				throw new Exception("Location \"" + location.getName() + "\" existiert nicht.");
			}
		}
		em.getTransaction().commit();
		return "Success";
	}

	private static String report(List<Location> locations, EntityManager em) throws Exception {

		// Loop over Locations that should be reported
		for (Location location : locations) {
			Query query = em.createQuery("SELECT l from Location l WHERE l.id = " + location.getId());
			List<Location> result = query.getResultList();

			// If location was found, it should be updated
			if (result.size() > 0) {
				Location resultLocation = result.get(0);
				int timesReported = resultLocation.getTimesReported();

				if (timesReported == 2) {
					// delete Location
					ArrayList<Location> deleteLocations = new ArrayList<Location>();
					deleteLocations.add(location);
					delete(deleteLocations, em);
				} else {
					resultLocation.setTimesReported(timesReported++);
				}

			} else {
				throw new Exception("Location \"" + location.getName() + "\" existiert nicht.");
			}
		}
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

			String[] paramBoundNorthWestString = request.getParameterValues("boundNorthWest");
			double[] paramBoundNorthWest = new double[2];
			paramBoundNorthWest[0] = Double.valueOf(paramBoundNorthWestString[0]);
			paramBoundNorthWest[1] = Double.valueOf(paramBoundNorthWestString[1]);

			String[] paramBoundSouthEastString = request.getParameterValues("boundSouthEast");
			double[] paramBoundSouthEast = new double[2];
			paramBoundSouthEast[0] = Double.valueOf(paramBoundSouthEastString[0]);
			paramBoundSouthEast[1] = Double.valueOf(paramBoundSouthEastString[1]);

			// read with parameters
			res = read(em, paramType, paramBoundNorthWest, paramBoundSouthEast);
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		HttpSession session =  request.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		Gson gson = new Gson();
		List<Location> locations = gson.fromJson(request.getParameter("data"), new TypeToken<List<Location>>() {
		}.getType());
		String res = "";

		try {
			if (!session.getAttribute("loggedin").equals("true")) {
				throw new Exception("You are not logged in.");
			}
			switch (request.getParameter("operation")) {
			case "update":
				res = update(locations, em);
				break;
			case "create":
				res = create(locations, em);
				break;
			case "delete":
				res = delete(locations, em);
				break;
			case "report":
				res = report(locations, em);
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
