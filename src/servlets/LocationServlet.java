package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Location;
import util.JSON;

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 //retrieve EntityManagerFactory and create EntityManager
		 EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
	     EntityManager em = emf.createEntityManager();
	     
	     try {
	    	 
	    	 //Select Location from database table
	    	 Query query = em.createQuery("SELECT l FROM Location l");
	         List<Location> result = query.getResultList();
	         String JSONData;
	         
	         //check for empty resultList
	         if (result.size() > 0) {
	        	 //convert data to JSON
	        	 JSONData = JSON.locationToJSON(result);
	         } else {
	        	 JSONData = "[]";
	         }
	         
	         //send Response
	         response.setStatus(200);
	         response.setContentType("application/json");
	         PrintWriter writer = response.getWriter();
	         writer.append(JSONData);
	    	 
	     }
		 catch(Exception e) {
			 //send back error
			 response.setStatus(500);
			 response.setContentType("text/xml");
	         PrintWriter writer = response.getWriter();
	         writer.append(e.getMessage());
		 }
	     em.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
	}

}
