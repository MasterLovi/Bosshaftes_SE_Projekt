package servlets;

import java.io.IOException;
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Obtain a database connection:
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		try {

			Users user = new Users();
			user.setUsername(request.getParameter("username"));
			user.setEmail(request.getParameter("email"));

			String password = request.getParameter("password");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			String hashedPW = new String(hash);
			user.setPassword(hashedPW);

			String passwordRep = request.getParameter("passwordRep");

			// First check: passwords identical?
			if (!password.equals(passwordRep)) {
				throw new Exception("Die Passwörter stimmen nicht überein!");
			}

			// Second check: username taken?
			// Search for User
			Query query = em.createQuery("SELECT u FROM Users u WHERE u.username = '" + user.getUsername() + "'");
			List<Users> resultUser = query.getResultList();
			if (resultUser.size() > 0) {
				throw new Exception("Username schon vergeben!");
			}

			// Third check: account with email existing?
			// Search for email
			query = em.createQuery("SELECT u FROM Users u WHERE u.email = '" + user.getEmail() + "'");
			resultUser = query.getResultList();
			if (resultUser.size() > 0) {
				throw new Exception("Es existiert schon ein Account mit dieser E-Mail!");
			}

			// if everything's fine, save new user
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();

			HttpSession session = request.getSession();
			session.setAttribute("userid", user.getId());
			session.setAttribute("username", user.getUsername());
			session.setAttribute("loggedin", true);
			
			response.sendRedirect(request.getHeader("referer"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
		em.close();
	}
}
