package model;

import java.util.ArrayList;

public class Route {
	private int id;
	private String name;
	private ArrayList<Location> stops;
	private User owner;

	// getter
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Location> getStops() {
		return stops;
	}

	public Location getStop(int index) {
		return stops.get(index);
	}

	public User getOwner() {
		return owner;
	}

	// setter
	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(User owner) {
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

}
