package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;

import play.db.ebean.Model;

@Entity
@Table(name = "Aircraft_Manufacturers")
public class Aircraft_Manufacturer extends Model{
    private String name;
    private String country;
    private int year_founded;

    public Aircraft_Manufacturer(String name, String country, int year_founded) {
        setName(name);
        setCountry(country);
        setYearFounded(year_founded);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYearFounded() {
        return year_founded;
    }

    public void setYearFounded(int year_founded) {
        this.year_founded = year_founded;
    }
    
    public String toString() {
    	String str = "<p>Aircraft Manufacturer Name: " + this.getName() + "</p>" +
          "<p>Aricraft Manufacturer Country: " + this.getCountry() + "</p>" +
          "<p>Year Founded: " + this.getYearFounded() + "</p></br>";
		  return str;
    }
}