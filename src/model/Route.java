package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROUTE")
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	private String name;
	private String type;

	@OneToMany
	@JoinColumn(name = "FEEDBACK_ID")
	private List<Feedback> feedback;

	@ManyToMany
	@JoinTable(name = "ROUTES_LOCATION", joinColumns = @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID"))
	private List<Location> stops;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private Users owner;

	// getter
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public List<Location> getStops() {
		return stops;
	}

	public Location getStop(int index) {
		return stops.get(index);
	}

	public Users getOwner() {
		return owner;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) throws Exception {
		if (type.equals("Kultur") || type.equals("Party")) {
			this.type = type;
		} else {
			throw new Exception("You can only add Routes of type \"Kultur\" or \"Party\"");
		}
	}

	public void setStops(List<Location> stops) {
		this.stops = stops;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

	// other methods
	public void addStop(Location location) {
		this.stops.add(location);
	}

	public void removeStop(Location location) {
		this.stops.remove(location);
	}

	public void removeStopAtIndex(int index) {
		this.stops.remove(index);
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

}
