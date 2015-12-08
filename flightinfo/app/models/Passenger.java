package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import play.db.ebean.Model;

@Entity
@Table(name = "Passengers")
public class Passenger extends Model {
	private String last_name;
	private String first_name;
	private Date Dob;
	private String Gender;
	private String Nationality;

	public Passenger(String lname, String fname, Date dob, String gender, String nationality) {
		setLastName(lname);
		setFirstName(fname);
		setDob(dob);
		setGender(gender);
		setNationality(nationality);
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String lname) {
		this.last_name = lname;
	}
	
	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String fname) {
		this.first_name = fname;
	}

	public Date getDob() {
		return Dob;
	}

	public void setDob(Date dob) {
		this.Dob = dob;
	}
	
	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		this.Gender = gender;
	}

	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		this.Nationality = nationality;
	}
	
	public String toString(){
		String str = "<p>Passenger Name: " + this.getFirstName() + " " + this.getLastName()+ "</p>" +
					"<p>DOB: " + this.getDob().toString() + "</p>" +
					"<p>Gender: " + this.getGender() + "</p>" +
					"<p>Nationality: " + this.getNationality() + "</p></br>";
		return str;
	}
	
}