package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    
	private static String create(EntityManager em, Integer locationId, List<Feedback> feedbackList, String userId) throws Exception {
		
		Location location = em.find(Location.class, locationId);
		Users user = em.find(Users.class, Integer.parseInt(userId));
		
		em.getTransaction().begin();
		
		for(Feedback feedback : feedbackList) {
			//persist location
			em.persist(feedback);
			feedback.setAuthor(user);
			location.getFeedback().add(feedback);
		}
		
		em.getTransaction().commit();
		return "Success";
		
	}	
	private static String delete(Integer locationId, List<Feedback> feedbackList, EntityManager em) throws Exception {
		
		for(Feedback feedback : feedbackList) {
			Location location = em.find(Location.class, locationId);
			feedback = em.find(Feedback.class, feedback.getId());
			List<Feedback> ratings = location.getFeedback();
			for (Iterator<Feedback> iter = ratings.iterator(); iter.hasNext(); ) {
				Feedback rating = iter.next();
		    
				if(rating.getId() == feedback.getId()) {
					iter.remove();
				}
		    
			}
			em.remove(feedback);
		}
		em.getTransaction().commit();
			
		return "Success";
	}
	
	private static String update(List<Feedback> feedbackList, EntityManager em) throws Exception {
		
		em.getTransaction().begin();
		for(Feedback feedback : feedbackList) {
			Feedback resultFeedback = em.find(Feedback.class, feedback.getId());
			resultFeedback.setComment(feedback.getComment());
			resultFeedback.setRating(feedback.getRating());
		}
		em.getTransaction().commit();
		return "Success";
		
	}
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//retrieve EntityManagerFactory, create EntityManager and retrieve data
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();
		String JSONData = request.getParameter("data");
		Gson gson = new Gson();
		List<Feedback> feedbackList = gson.fromJson(request.getParameter("data"), new TypeToken<List<Feedback>>() {
		}.getType());
		String res = "";
		
		try {
			switch (request.getParameter("operation")) {
			case "update":
				res = update(feedbackList, em);
				break;
			case "create":
				res = create(em, Integer.parseInt(request.getParameter("locationId")), feedbackList, request.getSession().getAttribute("userid").toString());
				break;
			case "delete":
				res = delete(Integer.parseInt(request.getParameter("locationId")), feedbackList, em);
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
