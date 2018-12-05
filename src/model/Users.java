package model;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * This class represents the Users database table as mapped Java object
 */
@Entity
@Table(name = "USERS")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@Expose
	private int id;

	@Column(unique = true)
	@Expose
	private String username;
	
	@Expose
	private String email;
	private String password;

	@OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Route> routes;

	@ManyToMany
	@JoinTable(name = "USERS_ROUTES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ID"))
	private List<Route> visitedRoutes;

	// getter
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public List<Route> getVisitedRoutes() {
		return visitedRoutes;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public void setVisitedRoutes(List<Route> visitedRoutes) {
		this.visitedRoutes = visitedRoutes;
	}

	// other methods
	public void addRoute(Route route) {
		this.routes.add(route);
	}
	
	public void removeRoute(Route route) {
		for (Iterator<Route> iter = this.routes.iterator(); iter.hasNext();) {
			Route r = iter.next();
			if(r.getId() == route.getId()) {
				iter.remove();
			}
		}
	}

	public List<Route> getRoutes() {
		return routes;
	}

	// other methods
	@Override
	public String toString() {
		String userString = "USER= "
						+ "Id: " + this.id + ", "
						+ "Username: " + this.username + ", "
						+ "Email: " + this.email + ", "
						+ "Password" + this.password;

		return userString;
	}

}
