package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import play.db.ebean.Model;

@Entity
@Table(name = "Carriers")
public class Carrier extends Model{
    private String code;
    private String name;
    private String country;

    public Carrier(String code, String name, String country) {
        setCode(code);
        setName(name);
        setCountry(country);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    
    public String toString(){
      String str = "<p>Carrier 2-Letter Code: " + this.getCode() + "</p>" +
          "<p>Carrier Name: " + this.getName() + "</p>" +
          "<p>Country: " + this.getCountry() + "</p></br>";
		  return str;
	  }
}