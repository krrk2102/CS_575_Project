package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import java.sql.Date;
import play.db.ebean.Model;

@Entity
@Table(name = "Flights")
public class Flight extends Model{
	
	private String Num; // Include carrier 2-letter code first.
	private String CarrierCode;
	private Date DepartureDate;
	private int DepartureTime; // Use pattern: e.g., "1300", stands for 1:00 pm.
	private int Duration; // Use pattern: e.g., "200", stands for 2 hours 0 minutes.
	private String TailNum;
	private String Ori;
	private String Des;
	
	@JoinColumn(name = "TailNum", referencedColumnName = "tailnum")
  private Aircraft_Model aircraft_model;
  
  @JoinColumn(name = "CarrierCode", referencedColumnName = "code") // Operated by.
  private Carrier carrier;
  
  @JoinColumn(name = "Ori", referencedColumnName = "Code")
	private Airport oriAirport;
	@JoinColumn(name = "Des", referencedColumnName = "Code")
	private Airport desAirport;

	public Flight(String num, String carrierCode, Date date, int time, int duration, String tailnum, String ori, String des) {
		setNum(num);
		setCarrierCode(carrierCode);
		setDepartureDate(date);
		setDepartureTime(time);
		setDuration(duration);
		setTailNum(tailnum);
		setOri(ori);
		setDes(des);
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		this.Num = num;
	}
	
	public String getCarrierCode() {
		return CarrierCode;
	}
	
	public void setCarrierCode(String carrierCode) {
		this.CarrierCode = carrierCode;
	}

	public Date getDepartureDate() {
		return DepartureDate;
	}

	public void setDepartureDate(Date date) {
		this.DepartureDate = date;
	}

	public int getDepartureTime() {
		return DepartureTime;
	}

	public void setDepartureTime(int time) {
		this.DepartureTime = time;
	}
	
	public int getDuration() {
		return Duration;
	}

	public void setDuration(int duration) {
		this.Duration = duration;
	}
	
	public String getTailNum() {
		return TailNum;
	}

	public void setTailNum(String tailnum) {
		this.TailNum = tailnum;
	}
	
	public String getOri() {
		return Ori;
	}
	
	public void setOri(String ori) {
		this.Ori = ori;
	}
	
	public String getDes() {
		return Des;
	}
	
	public void setDes(String des) {
		this.Des = des;
	}
	
	public String toString(){
		String str = "<p>Flight Number: " + this.getNum() + "</p>" +
					"<p>Departure Date: " + this.getDepartureDate().toString() + "</p>" +
					"<p>Departure Time: " + (this.getDepartureTime()/60) + ":" + (this.getDepartureTime()%60) + "</p>" +
					"<p>Duration: " + (this.getDuration()/60) + "h" + (this.getDuration()%60) + "m" + "</p>" +
					"<p>Aircraft Number: " + this.getTailNum() + "</p>" +
					"<p>Departure From: " + this.getOri() + "</p>" +
					"<p>Destination: " + this.getDes() + "</p></br>";
		return str;
	}
	
}