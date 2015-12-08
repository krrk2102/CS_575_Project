package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import java.sql.Date;
import play.db.ebean.Model;

@Entity
@Table(name = "Tickets")
public class Ticket extends Model{
	private long Num;
	private String Seat;
	private int Price;
	private String last_name;
	private String first_name;
	private Date Dob;
	private String FlightNum;
	private Date FlightDate;
	
	@JoinColumns({
		@JoinColumn(name = "last_name", referencedColumnName = "last_name"),
		@JoinColumn(name = "first_name", referencedColumnName = "first_name"),
		@JoinColumn(name = "Dob", referencedColumnName = "Dob") })
	private Passenger passenger;
	
	@JoinColumns({
		@JoinColumn(name = "FlightNum", referencedColumnName = "Num"),
		@JoinColumn(name = "FlightDate", referencedColumnName = "DepartureDate") })
	private Flight flight;
	
	public Ticket(long num, String seat, int price, String lname, String fname, Date dob, String flightnum, Date flightdate) {
		setNum(num);
		setSeat(seat);
		setPrice(price);
		setLastName(lname);
		setFirstName(fname);
		setDob(dob);
		setFlightNum(flightnum);
		setFlightDate(flightdate);
	}

	public long getNum() {
		return Num;
	}

	public void setNum(long num) {
		this.Num = num;
	}

	public String getSeat() {
		return Seat;
	}

	public void setSeat(String seat) {
		this.Seat = seat;
	}

	public int getPrice() {
		return Price;
	}

	public void setPrice(int price) {
		this.Price = price;
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
	
	public String getFlightNum() {
		return FlightNum;
	}

	public void setFlightNum(String flightnum) {
		this.FlightNum = flightnum;
	}
	
	public Date getFlightDate() {
		return FlightDate;
	}

	public void setFlightDate(Date flightdate) {
		this.FlightDate = flightdate;
	}
	
	public String toString(){
		String str = "<p>Ticket Number: " + this.getNum() + "</p>" +
					"<p>Flight Number: " + this.getFlightNum() + "</p>" +
					"<p>Flight Date: " + this.getFlightDate() + "</p>" +
					"<p>Seat Number: " + this.getSeat() + "</p>" +
					"<p>Price: " + this.getPrice() + "</p>" +
					"<p>Passenger Name: " + this.getFirstName() + " " + this.getLastName() + "</p>" +
					"<p>Passenger Dob: " + this.getDob().toString() + "</p></br>";
		return str;
	}
	
}
