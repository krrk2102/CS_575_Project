package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.sql.Date;
import play.db.ebean.Model;

@Entity
@Table(name = "Aircraft_Models")
public class Aircraft_Model extends Model{
    private String tailnum;
    private String model;
    private String engine_type;
    private Date issue_date;
    private String manufacturer_name;

    @JoinColumn(name = "manufacturer_name", referencedColumnName = "name")
    private Aircraft_Manufacturer aircraft_manufacturer;    // attention

    public Aircraft_Model(String tailnum, String model, String engine_type, Date issue_date, String manufacturer_name) {
        setTailnum(tailnum);
        setModel(model);
        setEngineType(engine_type);
        setIssueDate(issue_date);
        setManufacturerName(manufacturer_name);
    }

    public String getTailnum(){
        return tailnum;
    }

    public void setTailnum(String tailnum){
        this.tailnum = tailnum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngineType() {
        return engine_type;
    }

    public void setEngineType(String engine_type) {
        this.engine_type = engine_type;
    }

    public Date getIssueDate() {
        return issue_date;
    }

    public void setIssueDate(Date issue_date) {
        this.issue_date = issue_date;
    }

    public String getManufacturerName() {
        return manufacturer_name;
    }

    public void setManufacturerName(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }
    
    public String toString(){
      String str = "<p>Aircraft Tail Number: " + this.getTailnum() + "</p>" +
          "<p>Aricraft Model Type: " + this.getModel() + "</p>" +
          "<p>Aricraft Engine Model: " + this.getEngineType() + "</p>" +
          "<p>Aircraft Produced Date: " + this.getIssueDate().toString() + "</p>" +
          "<p>Aircraft Manufacturer: " + this.getManufacturerName() + "</p></br>";
		  return str;
	  }
}