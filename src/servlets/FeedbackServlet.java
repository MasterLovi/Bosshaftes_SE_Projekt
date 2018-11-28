package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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

import model.Feedback;
import model.Location;
import model.Route;
import model.Users;

/**
 * Servlet implementation class FeedbackServlet
 */
@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FeedbackServlet() {
		super();
	}

	/**
	 * Method to create new feedback for a route
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of route where feedback should be added
	 * @param feedbackList List of feedback that should be created
	 * @param session      Needed to retrieve the user that creates the feedback
	 * @return "Success" if route feedback has been created successfully
	 * @exception Exception if corresponding route is not found
	 * @exception Exception if user has already given feedback for this route
	 */
	private static String createRouteFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding route, check if it exists
		Route route = em.find(Route.class, id);
		if (route == null) {
			throw new Exception("Zugehörige Route existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();

		if (result.size() > 0) {
			Users user = result.get(0);
			// check if user has already given feedback for this route
			query = em.createQuery("SELECT f from Feedback f WHERE f.author.username = '" + username + "'");
			result = query.getResultList();
			if (result.size() > 0) {
				throw new Exception("Dieser User hat schon ein Feedback für diese Route abgegeben.");
			} else {

				for (Feedback feedback : feedbackList) {
					// persist feedback
					em.persist(feedback);
					feedback.setAuthor(user);
					route.getFeedback().add(feedback);
				}
			}
			route.setAvgRating(updateAvgRouteRating(route));
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	/**
	 * Method to create feedback for a location
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of location where feedback should be added
	 * @param feedbackList List of feedback that should be created
	 * @param session      Needed to retrieve the user that creates the feedback
	 * @return "Success" if location feedback has been created successfully
	 * @exception Exception if corresponding location is not found
	 * @exception Exception if user has already given feedback for this location
	 */
	private static String createLocationFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding location, check if it exists
		Location location = em.find(Location.class, id);
		if (location == null) {
			throw new Exception("Zugehörige Location existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();

		if (result.size() > 0) {
			Users user = result.get(0);

			// check if user has already given feedback for this location
			query = em.createQuery("SELECT f from Feedback f WHERE f.author.username = '" + username + "'");
			result = query.getResultList();
			if (result.size() > 0) {
				throw new Exception("Dieser User hat schon ein Feedback für diese Location abgegeben.");
			} else {

				for (Feedback feedback : feedbackList) {
					// persist location
					em.persist(feedback);
					feedback.setAuthor(user);
					location.getFeedback().add(feedback);
				}
			}
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	/**
	 * Method to delete feedback of a route
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of route where feedback should be deleted
	 * @param feedbackList List of feedback that should be deleted
	 * @param session      Needed to retrieve the user that deletes the feedback
	 * @return "Success" if route feedback has been deleted successfully
	 * @exception Exception if corresponding route doesn't exist
	 * @exception Exception if logged in user != author of feedback
	 */
	private static String deleteRouteFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding route, check if it exists
		Route route = em.find(Route.class, id);
		if (route == null) {
			throw new Exception("Zugehörige Route existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();
		if (result.size() > 0) {
			Users user = result.get(0);

			for (Feedback feedback : feedbackList) {

				// get feedback
				feedback = em.find(Feedback.class, feedback.getId());

				// check if the feedback belongs to the user
				if (feedback.getAuthor().getUsername().equals(user.getUsername())) {

					List<Feedback> ratings = route.getFeedback();
					for (Iterator<Feedback> iter = ratings.iterator(); iter.hasNext();) {
						Feedback rating = iter.next();
						if (rating.getId() == feedback.getId()) {
							iter.remove();
						}
					}
					// if everything went fine, delete feedback
					em.remove(feedback);
				} else {
					throw new Exception("Du kannst nur dein eigenes Feedback löschen.");
				}
			}
			route.setAvgRating(updateAvgRouteRating(route));
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	/**
	 * Method to delete feedback of a location
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of location where feedback should be deleted
	 * @param feedbackList List of feedback that should be deleted
	 * @param session      Needed to retrieve the user that deletes the feedback
	 * @return "Success" if location feedback has been deleted successfully
	 * @exception Exception if corresponding location doesn't exist
	 * @exception Exception if logged in user != author of feedback
	 */
	private static String deleteLocationFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding location, check if it exists
		Location location = em.find(Location.class, id);
		if (location == null) {
			throw new Exception("Zugehörige Location existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();
		if (result.size() > 0) {
			Users user = result.get(0);

			for (Feedback feedback : feedbackList) {
				// get feedback
				feedback = em.find(Feedback.class, feedback.getId());

				// check if the feedback belongs to the user
				if (feedback.getAuthor().getUsername().equals(user.getUsername())) {
					List<Feedback> ratings = location.getFeedback();
					for (Iterator<Feedback> iter = ratings.iterator(); iter.hasNext();) {
						Feedback rating = iter.next();
						if (rating.getId() == feedback.getId()) {
							iter.remove();
						}
					}
					// if everything went fine, delete feedback
					em.remove(feedback);
				} else {
					throw new Exception("Du kannst nur dein eigenes Feedback löschen.");
				}
			}
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	/**
	 * Method to update feedback of a route
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of route where feedback should be updated
	 * @param feedbackList List of feedback that should be updated
	 * @param session      Needed to retrieve the user that updates the feedback
	 * @return "Success" if route feedback has been updated successfully
	 * @exception Exception if corresponding route doesn't exist
	 * @exception Exception if logged in user != author of feedback
	 */
	private static String updateRouteFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding route, check if it exists
		Route route = em.find(Route.class, id);
		if (route == null) {
			throw new Exception("Zugehörige Route existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();
		if (result.size() > 0) {
			Users user = result.get(0);

			for (Feedback feedback : feedbackList) {
				// check if the feedback belongs to the user
				if (feedback.getAuthor().getUsername().equals(user.getUsername())) {
					// if yes, update feedback
					Feedback resultFeedback = em.find(Feedback.class, feedback.getId());
					resultFeedback.setComment(feedback.getComment());
					resultFeedback.setRating(feedback.getRating());
				} else {
					throw new Exception("Du kannst nur dein eigenes Feedback bearbeiten.");
				}
			}
			route.setAvgRating(updateAvgRouteRating(route));
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	/**
	 * Method to update feedback of a location
	 * 
	 * @param em           EntityManager manages database operations
	 * @param id           Id of location where feedback should be updated
	 * @param feedbackList List of feedback that should be updated
	 * @param session      Needed to retrieve the user that updates the feedback
	 * @return "Success" if location feedback has been updated successfully
	 * @exception Exception if corresponding location doesn't exist
	 * @exception Exception if logged in user != author of feedback
	 */
	private static String updateLocationFeedback(EntityManager em, int id, List<Feedback> feedbackList,
					HttpSession session) throws Exception {

		// get corresponding location, check if it exists
		Location location = em.find(Location.class, id);
		if (location == null) {
			throw new Exception("Zugehörige Location existiert nicht!");
		}

		// get corresponding user
		String username = (String) session.getAttribute("username");
		Query query = em.createQuery("SELECT u from Users u WHERE u.username = '" + username + "'");
		List<Users> result = query.getResultList();
		if (result.size() > 0) {

			Users user = result.get(0);

			for (Feedback feedback : feedbackList) {
				// check if the feedback belongs to the user
				if (feedback.getAuthor().getUsername().equals(user.getUsername())) {
					// if yes, update feedback
					Feedback resultFeedback = em.find(Feedback.class, feedback.getId());
					resultFeedback.setComment(feedback.getComment());
					resultFeedback.setRating(feedback.getRating());
				} else {
					throw new Exception("Du kannst nur dein eigenes Feedback bearbeiten.");
				}
			}
		} else {
			throw new Exception("User \"" + username + "\" existiert nicht.");
		}
		return "Success";
	}

	private static double updateAvgRouteRating(Route route) throws Exception {
		// get current feedback from route
		List<Feedback> routeFeedback = route.getFeedback();
		double avgRating = 0;

		// update avg rating
		if (routeFeedback != null && routeFeedback.size() > 0) {
			for (Feedback feedback : routeFeedback) {
				avgRating = avgRating + feedback.getRating();
			}
			avgRating = avgRating / routeFeedback.size();
			avgRating = Math.round(avgRating * Math.pow(10, 1)) / Math.pow(10, 1);
		} else {
			avgRating = 5;
		}
		return avgRating;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @exception Exception if user is not logged in
	 * @exception Exception if type is neither "Route" nor "Location"
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// retrieve EntityManagerFactory, create EntityManager and retrieve data
		HttpSession session = request.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		String JSONData = request.getParameter("json");
		Gson gson = new Gson();
		List<Feedback> feedbackList = gson.fromJson(JSONData, new TypeToken<List<Feedback>>() {
		}.getType());
		String res = "";

		String type = request.getParameter("type");
		int id = Integer.parseInt(request.getParameter("id"));

		try {
			em.getTransaction().begin();
			if ((boolean) session.getAttribute("loggedin") != true) {
				throw new Exception("You are not logged in.");
			}
			if (type.equals("Route")) {
				switch (request.getParameter("operation")) {
				case "update":
					res = updateRouteFeedback(em, id, feedbackList, session);
					break;
				case "create":
					res = createRouteFeedback(em, id, feedbackList, session);
					break;
				case "delete":
					res = deleteRouteFeedback(em, id, feedbackList, session);
					break;
				}
			} else if (type.equals("Location")) {
				switch (request.getParameter("operation")) {
				case "update":
					res = updateLocationFeedback(em, id, feedbackList, session);
					break;
				case "create":
					res = createLocationFeedback(em, id, feedbackList, session);
					break;
				case "delete":
					res = deleteLocationFeedback(em, id, feedbackList, session);
					break;
				}
			} else {
				throw new Exception("Type muss entweder \"Route\" oder \"Location\" sein!");
			}
			em.getTransaction().commit();
			response.setStatus(200);
		} catch (Exception e) {
			// send back error
			response.setStatus(500);
			res = e.getMessage();
			em.getTransaction().rollback();
		}
		PrintWriter writer = response.getWriter();
		writer.append(res);
		em.close();
	}

}
