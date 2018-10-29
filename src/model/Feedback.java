package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FEEDBACK")
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	private String comment;
	private int rating;
	
	@OneToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Users author;

	// getter
	public int getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public int getRating() {
		return rating;
	}

	public Users getAuthor() {
		return author;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

}
