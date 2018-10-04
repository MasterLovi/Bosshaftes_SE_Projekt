package servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.JPAClass1;

/**
 * Servlet implementation class FirstServlet
 * 
 * The EntityManagerFactory is retrieved from the application scope attribute,
 * and then an EntityManager (representing a database connection) is
 * constructed in order to perform different Actions.
 * 
 * Finally, the database connection (including the transaction if still active) is closed.
 */
@WebServlet("/FirstServlet")
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FirstServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {

		// Obtain a database connection:
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext()
		        .getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		try {
			//request.setAttribute("attributeName", Object);
			//request.getRequestDispatcher("/JSP1.jsp").forward(request,
			//        response);

		} finally {
			// Close the database connection:
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			em.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
