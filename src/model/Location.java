package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOCATION")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	private String name;
	private long longitude;
	private long latitude;
	
	@OneToOne
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;
	
	@OneToMany
	@JoinColumn(name = "FEEDBACK_ID")
	private List<Feedback> feedback;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getLongitude() {
		return longitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public Address getAddress() {
		return address;
	}

	// setter
	public void setName(String name) {
		this.name = name;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public void setAddress(Address address) {
		address.setAddress(address);
		;
	}
	
	public List<Feedback> getFeedback() {
		return feedback;
	}
	
	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

}
