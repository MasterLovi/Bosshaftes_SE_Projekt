package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

import model.Route;
import model.Users;

/**
 * Servlet implementation class RegistrationServlet - This servlet is
 * responsible for creating new users (= registration)
 *
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
	}

	/**
	 * Method to create a new user in the database
	 *
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @exception - Exception if password and repeated password are not identical
	 * @exception - Exception if the chosen username already exists
	 * @exception . Exception if there is already an account registered with that email
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Obtain a database connection:
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		try {

			// retrieve all parameters
			Users user = new Users();
			user.setRoutes((List<Route>) new ArrayList<Route>());
			user.setUsername(request.getParameter("username"));
			user.setEmail(request.getParameter("email"));

			// hash password
			String password = request.getParameter("password");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			String hashedPW = new String(hash);
			user.setPassword(hashedPW);

			String passwordRep = request.getParameter("passwordRep");

			// First check: passwords identical?
			if (!password.equals(passwordRep)) {
				throw new Exception("Repeated password is not the same as your password.");
			}

			// Second check: username taken?
			// Search for User
			Query query = em.createQuery("SELECT u FROM Users u WHERE u.username = '" + user.getUsername() + "'");
			List<Users> resultUser = query.getResultList();
			if (resultUser.size() > 0) {
				throw new Exception("this username already exists.");
			}

			// Third check: account with email existing?
			// Search for email
			query = em.createQuery("SELECT u FROM Users u WHERE u.email = '" + user.getEmail() + "'");
			resultUser = query.getResultList();
			if (resultUser.size() > 0) {
				throw new Exception("There is already an account attached to this email.");
			}

			// if everything's fine, save new user
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
			
			// set session attributes
			HttpSession session = request.getSession();
			session.setAttribute("userid", user.getId());
			session.setAttribute("username", user.getUsername());
			session.setAttribute("loggedin", true);
						
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter writer = response.getWriter();
			writer.append("Success");

		} catch (Exception e) {
			response.setStatus(500);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter writer = response.getWriter();
			writer.append(e.getMessage());
		}
		em.close();
	}
}
