package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import play.db.ebean.Model;

@Entity
@Table(name = "Airports")
public class Airport extends Model{
	
	private String Code;
	private String Name;
	private String City;
	private String Country;

	public Airport(String code, String name, String city, String country){
		setCode(code);
		setName(name);
		setCity(city);
		setCountry(country);
	}
	
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		this.Code = code;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}
	
	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		this.City = city;
	}
	
	public String getCountry() {
		return City;
	}

	public void setCountry(String country) {
		this.Country = country;
	}
	
	public String toString(){
		String str = "<p>Airport 3-Letter Code: " + this.getCode() + "</p>" +
		      "<p>Name: " + this.getName() + "</p>" +
					"<p>City: " + this.getCity() + "</p>" +
					"<p>Country: " + this.getCountry() + "</p></br>";
		return str;
	}
	
}