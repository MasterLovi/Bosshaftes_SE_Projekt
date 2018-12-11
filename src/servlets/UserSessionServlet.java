package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

import model.Users;

/**
 * Servlet implementation class UserSessionServlet - This servlet is responsible
 * for logging a user in and out
 */
@WebServlet("/UserSessionServlet")
public class UserSessionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserSessionServlet() {
		super();
	}

	/**
	 * Method to log user out
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get URI Parameter for logout and retrieve session
		String sLogout = request.getParameter("logout");
		HttpSession session = request.getSession();

		// check if user needs to be logged out
		if (sLogout.equals("true") && (boolean) session.getAttribute("loggedin")) {
			session.setAttribute("userid", null);
			session.setAttribute("username", null);
			session.setAttribute("loggedin", false);
			session.invalidate();
		}

		// forward back to request page
		response.sendRedirect(request.getHeader("referer"));
	}

	/**
	 * Method to log user in
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * @exception Exception if username or password is incorrect
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Obtain a database connection:
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		try {
			// Search for User
			Query query = em.createQuery("SELECT u FROM Users u WHERE u.username = '" + username + "'");
			List<Users> resultUser = query.getResultList();

			// retrieve session object
			HttpSession session = request.getSession();

			// hash user´s password input
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			String hashedPW = new String(hash);

			// compare user input with user password in db
			// set session if user input is correct
			if (resultUser.size() > 0 && resultUser.get(0).getPassword().equals(hashedPW)) {
				Users resUser = resultUser.get(0);
				session.setAttribute("userid", resUser.getId());
				session.setAttribute("username", resUser.getUsername());
				session.setAttribute("loggedin", true);
			}
			// otherwise set session false
			else {
				session.setAttribute("loggedin", false);
				session.invalidate();
				throw new Exception("Password or username is incorrect.");
			}

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
