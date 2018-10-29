package servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Users;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password	= request.getParameter("password");
		
		// Obtain a database connection:
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try {
        	//Search for User
        	Query query = em.createQuery("SELECT u FROM Users u WHERE u.username = '" + username + "'");
        	Users resultUser = (Users) query.getSingleResult();
        	
        	HttpSession session = request.getSession();
        	
        	MessageDigest digest = MessageDigest.getInstance("SHA-256");
        	byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        	String hashedPW = new String(hash);
        	
        	if (resultUser != null && resultUser.getPassword().equals(hashedPW)) {
        				session.setAttribute("userid", resultUser.getId());
        				session.setAttribute("username", resultUser.getUsername());
        				session.setAttribute("loggedin", true);
        	} 
        	else {
        		session.setAttribute("loggedin", false);
        	}
        	em.close();
        	request.getRequestDispatcher("/index.jsp")
            .forward(request, response);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	em.close();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
	}

}
