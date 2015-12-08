package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.Aircraft_Manufacturer;
import models.Aircraft_Model;
import models.Airport;
import models.Carrier;
import models.Flight;
import models.Passenger;
import models.Ticket;
import play.*;
import play.db.DB;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;
import utils.DBUtils;
import views.html.*;
import static play.libs.Json.toJson;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render());
	}

	public static Result addFlight() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String Num = requestData.get("Num").toUpperCase();
		String CarrierCode = requestData.get("Num").substring(0,2).toUpperCase();
		int DepartureTimeH = Integer.parseInt(requestData.get("DepartureTimeH"));
		int DepartureTimeM = Integer.parseInt(requestData.get("DepartureTimeM"));
		int DepartureTime = 0;
		if ((DepartureTimeH>=0&&DepartureTimeH<24)&&(DepartureTimeM>=0&&DepartureTimeM<60)) {
			DepartureTime = DepartureTimeH*100 + DepartureTimeM;
		}
		int DurationH = Integer.parseInt(requestData.get("DurationH"));
		int DurationM = Integer.parseInt(requestData.get("DurationM"));
		int Duration = 0;
		if ((DurationH>=0&&DurationH<24)&&(DurationM>=0&&DurationM<60)) {
			Duration = DurationH*100 + DurationM;
		}
		String TailNum = requestData.get("TailNum").toUpperCase();
		String Ori = requestData.get("Ori").toUpperCase();
		String Des = requestData.get("Des").toUpperCase();
		String DepartureDate = requestData.get("DepartureDate");
		Flight flight = new Flight(Num, CarrierCode, new java.sql.Date(System.currentTimeMillis()), DepartureTime, Duration, TailNum, Ori, Des);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(DepartureDate);
			flight.setDepartureDate(new java.sql.Date(date.getTime()));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		if (flight.getNum().length()>6||flight.getNum().length()<3) {
			return ok("Your input flight number " + flight.getNum() + " is invalid.");
		}
		
		if (flight.getDepartureTime()<0||(flight.getDepartureTime()%100)>59||(flight.getDepartureTime()/100)>23) {
			return ok("Your input departure time is invalid.");
		}
		
		Connection conn1 = DB.getConnection();
		String sql = "Select * from Flights where num = \'" + flight.getNum() 
		             + "\' and Departure_Date = \'" + flight.getDepartureDate() + "\'";
		try {
			Statement st = conn1.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn1);
				return ok("This flight number is already used on desired date. Please go back and use a different number.");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn1);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn2 = DB.getConnection();
		sql = "Select * from Airports where code = \'" + flight.getOri() + "\'";
		try {
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn2);
				return ok("Your departure airport doesn't exist in database, please go back to check your input or add this airport first.");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn2);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn3 = DB.getConnection();
		sql = "Select * from Airports where code = \'" + flight.getDes() + "\'";
		try {
			Statement st = conn3.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn3);
				return ok("Your destination airport doesn't exist in database, please go back to check your input or add this airport first.");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn3);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn4 = DB.getConnection();
		sql = "Select * from Aircraft_Models where tailnum = \'" + flight.getTailNum() + "\'";
		try {
			Statement st = conn4.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn4);
				return ok("Your desired aircraft doesn't exist, please go back and add your model.");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn4);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn5 = DB.getConnection();
		sql = "Select * from Carriers where code = \'" + flight.getCarrierCode() + "\'";
		try {
			Statement st = conn5.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn5);
				return ok("Input carrier code is incorrect.");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn5);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		flight.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addAircraft_Manufacturer() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String name = requestData.get("name");
		String country = requestData.get("country");
		int year = Integer.parseInt(requestData.get("year_founded"));
		Aircraft_Manufacturer aircraft_Manufacturer = new Aircraft_Manufacturer(name, country, year);
		
		if (aircraft_Manufacturer.getYearFounded()<1900||aircraft_Manufacturer.getYearFounded()>2100) {
			return ok("Input founded year of manufacturer is invalid.");
		}
		
		Connection conn = DB.getConnection();
		String sql = "Select name from Aircraft_Manufacturers where name = \'" + aircraft_Manufacturer.getName() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Aircraft manufacturer name \'" + aircraft_Manufacturer.getName() +"\' already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		aircraft_Manufacturer.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addAircraft_Model() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String tailnum = requestData.get("plane_tailnum").toUpperCase();
		String model = requestData.get("plane_model").toUpperCase();
		String engine_type = requestData.get("plane_engine_type");
		String manufacturer_name = requestData.get("plane_manufacturer");
		Aircraft_Model aircraft_Model = new Aircraft_Model(tailnum, model, engine_type, new java.sql.Date(System.currentTimeMillis()), manufacturer_name);
		String issue_date = requestData.get("plane_issue_date");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(issue_date);
			aircraft_Model.setIssueDate(new java.sql.Date(date.getTime()));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		Connection conn = DB.getConnection();
		String sql = "Select tailnum from Aircraft_Models where tailnum = \'" + aircraft_Model.getTailnum() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Aircraft tailnum \'" + aircraft_Model.getTailnum() +"\' already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		aircraft_Model.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addAirport() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String Code = requestData.get("Code").toUpperCase();
		String Name = requestData.get("Name");
		String City = requestData.get("City");
		String Country = requestData.get("Country");
		Airport airport = new Airport(Code, Name, City, Country);
		
		Connection conn = DB.getConnection();
		String sql = "Select Code from Airports where Code = \'" + airport.getCode() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Airport (3-letter code \'" + airport.getCode() +"\') already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		airport.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addCarrier() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String code = requestData.get("carrier_code").toUpperCase();
		String name = requestData.get("carrier_name");
		String country = requestData.get("country2");
		Carrier carrier = new Carrier(code, name, country);
		
		Connection conn = DB.getConnection();
		String sql = "Select code from Carriers where Code = \'" + carrier.getCode() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Carrier: " + carrier.getName() + "(2-letter code \'" + carrier.getCode() +"\') already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		carrier.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addPassenger() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String FirstName = requestData.get("FirstName").toUpperCase();
		String LastName = requestData.get("LastName").toUpperCase();
		String dob = requestData.get("Dob");
		String Gender = requestData.get("Gender");
		String Nationality = requestData.get("Nationality");
		Passenger passenger = new Passenger(LastName, FirstName, new java.sql.Date(System.currentTimeMillis()), Gender, Nationality);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(dob);
			passenger.setDob(new java.sql.Date(date.getTime()));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		Connection conn = DB.getConnection();
		String sql = "Select Name,Dob from Passengers where First_Name = \'" + passenger.getFirstName() 
		             + "\' and Last_Name = \'" + passenger.getLastName() +"\' and Dob = \'" 
		             + passenger.getDob().toString() + "\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Passenger " + passenger.getFirstName() + " " + passenger.getLastName() + ", Dob: " + passenger.getDob().toString() +" already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		passenger.save();
		return redirect(routes.Application.index());
	}
	
	public static Result addTicket() {
		DynamicForm requestData = Form.form().bindFromRequest();
		long num = Long.parseLong(requestData.get("ticket_number"));
		String Seat = requestData.get("ticket_seat").toUpperCase();
		int Price = Integer.parseInt(requestData.get("ticket_price"));
		String last_name = requestData.get("ticket_last_name").toUpperCase();
		String first_name = requestData.get("ticket_first_name").toUpperCase();
		String FlightNum = requestData.get("ticket_flight_number").toUpperCase();
		Ticket ticket = new Ticket(num, Seat, Price, last_name, first_name, new java.sql.Date(System.currentTimeMillis()), FlightNum, new java.sql.Date(System.currentTimeMillis()));
		String dob = requestData.get("ticket_dob");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(dob);
			ticket.setDob(new java.sql.Date(date.getTime()));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		String flightDate = requestData.get("ticket_flight_date");
		try {
			java.util.Date date = formatter.parse(flightDate);
			ticket.setFlightDate(new java.sql.Date(date.getTime()));
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		if (ticket.getNum()<0) {
			return ok("Input ticket number is invalid.");
		}
		
		if (ticket.getPrice()<0) {
			return ok("Input price is invalid.");
		}
		
		Connection conn = DB.getConnection();
		String sql = "select Num from Tickets where Flight_Num = \'" + ticket.getNum() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn);
				return ok("Ticket number: " + ticket.getNum() + " already exists. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn1 = DB.getConnection();
		sql = "select * from Passengers where first_name = \'" + ticket.getFirstName() + "\'"
		             + "and last_name = \'" + ticket.getLastName() + "\'"
		             + "and dob = \'" + ticket.getDob() + "\'";
		try {
			Statement st = conn1.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn1);
				return ok("Input passenger doesn't exist, please go back and add passenger first. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn1);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn2 = DB.getConnection();
		sql = "select * from Flights where num = \'" + ticket.getFlightNum() + "\'"
		      + "and departure_date = \'" + ticket.getFlightDate() + "\'";
		try {
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (!rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn2);
				return ok("Input flight doesn't exist, please go back. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn1);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		Connection conn3 = DB.getConnection();
		sql = "select seat from Tickets where seat = \'" + ticket.getSeat() +"\'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				rs.close();
			  st.close();
			  DBUtils.closeDBConnection(conn3);
				return ok("Seat number: " + ticket.getSeat() + " is occupied. ");
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn3);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		ticket.save();
		return redirect(routes.Application.index());
	}
	
	// This method uses group by and left outer join.
	public static Result getFlight(String flightNum) {
    String sql = "select distinct F.Departure_Date, "
               + "C.name as cname, C.country as ccountry, F.Ori as fori, F.Des as fdes, "
               + "F.Departure_Time as ftime, F.Duration as fduration, F.Tail_num as ftail, "
               + "A1.city as a1city , A2.city as a2city, A1.country as a1country, A2.country as a2country, "
               + "A1.name as a1name, A2.name as a2name, "
               + "A.model as amodel, A.manufacturer_name as amanu, A.engine_type as aengine, A.issue_date as adate, "
               + "AM.Country as amcountry, "
               + "count(T.num) as tnum "
               + "from Flights F left outer join Tickets T on (f.departure_date=t.flight_date and f.num=t.flight_num), "
               + "Aircraft_Models A, Airports A1, Airports A2, Carriers C, Aircraft_Manufacturers AM "
               + "where F.num = \'" + flightNum + "\' "
               + "and A1.Code = F.Ori and A2.Code = F.Des and F.Carrier_Code = C.Code and A.tailnum = F.tail_num "
               + "and A.manufacturer_name = AM.name "
               + "group by F.Departure_Date, C.name, C.country, F.Ori, F.Des, F.Departure_Time, F.Duration, F.Tail_num, "
               + "A1.city, A2.city, A1.country, A2.country, A1.name, A2.name, A.model, A.manufacturer_name, "
               + "A.engine_type, A.issue_date, AM.Country";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\">\n<caption>Flight Details</caption>\n<thread>";
    result += "<tr><th>Airline Information</th><th>Departure Date & Time</th><th>Duration</th>"
            + "<th>Departure</th><th>Destination</th><th>Aircraft Information</th><th>Aircraft Manufacturer</th>"
            + "<th>Tickets Sold</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            result += "<tr><td>"+rs.getString("cname")+" ("+rs.getString("ccountry")+")</td><td>"
                    +rs.getDate("Departure_Date").toString()+" ";
            if (rs.getInt("ftime")/100<10) result += "0"+rs.getInt("ftime")/100+":";
            else result += rs.getInt("ftime")/100+":";
            if (rs.getInt("ftime")%100<10) result += "0"+rs.getInt("ftime")%100+"</td><td>";
            else result += rs.getInt("ftime")%100+"</td><td>";
            result +=rs.getInt("fduration")/100+"h "+rs.getInt("fduration")%100+"m</td><td>("
                    +rs.getString("fori")+") "+rs.getString("a1city")+", "+rs.getString("a1name")
                    +", "+rs.getString("a1country")+"</td><td>("
                    +rs.getString("fdes")+") "+rs.getString("a2city")+", "+rs.getString("a2name")
                    +", "+rs.getString("a2country")+"</td><td>"
                    +rs.getString("ftail")+"("+rs.getDate("adate").toString()+") "+rs.getString("amodel")+", "
                    +rs.getString("aengine")+" engine</td><td>"
                    +rs.getString("amanu")+" ("+rs.getString("amcountry")+")</td><td>"
                    +rs.getInt("tnum")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  public static Result getRoute(String RouteStr) {
  	String[] route_combo = RouteStr.split("_");
  	String Ori = route_combo[0];
  	String Des = route_combo[1];
  	String DepartureDate = route_combo[2];
    String sql = "select * from Flights where Ori = \'" + Ori + "\' and Des = \'" + Des 
                 + "\' and Departure_Date = \'" + DepartureDate + "\'";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Route Details</caption><thread>";
    result += "<tr><th>Flight Number</th><th>Departure Date</th><th>Departure Time</th>"
            + "<th>Duration</th><th>Tail Number</th><th>Origin</th><th>Destination</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            result += "<tr><td>"+rs.getString("Num")+"</td><td>"
                    +rs.getDate("Departure_Date").toString()+"</td><td>";
            if (rs.getInt("Departure_Time")/100<10) result += "0"+rs.getInt("Departure_Time")/100+":";
            else result += rs.getInt("Departure_Time")/100+":";
            if (rs.getInt("Departure_Time")%100<10) result += "0"+rs.getInt("Departure_Time")%100+"</td><td>";
            else result += rs.getInt("Departure_Time")%100+"</td><td>";
            result += rs.getInt("Duration")/100+"h "+rs.getInt("Duration")%100+"m</td><td>"
                    +rs.getString("Tail_Num")+"</td><td>"
                    +rs.getString("Ori")+"</td><td>"
                    +rs.getString("Des")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  // This method uses left outer join. 
  public static Result getDes(String Ori) {
    String sql = "select distinct D.Code, D.Country, D.City, D.Name from Flights F left outer join Airports D on F.Des = D.Code "+
                 "where F.Ori = \'" + Ori + "\' order by D.code";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Destinations</caption><thread>";
    result += "<tr><th>Airport 3-Letter Code</th><th>Airport Name</th>"
            + "<th>City</th><th>Country</th></tr><thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            result += "<tr><td>"+rs.getString("Code")+"</td><td>"
                    +rs.getString("Name")+"</td><td>"
                    +rs.getString("City")+"</td><td>"
                    +rs.getString("Country")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  // This method uses left outer join. 
  public static Result getFlightByCarrier(String Code) {
    String sql = "select distinct (F.Num, F.Departure_Date), "
               + "C.name as cname, F.Num as fnum, F.Ori as fori, F.Des as fdes, F.Departure_Date as fdate, "
               + "F.Departure_Time as ftime, F.Duration as fduration, A.model as amodel, A1.city as a1city , A2.city as a2city "
               + "from Flights F left outer join Aircraft_models A on F.tail_num = A.tailnum, Airports A1, Airports A2, Carriers C where F.Carrier_Code = \'" + Code + "\' "
               + "and A1.Code = F.Ori and A2.Code = F.Des and F.Carrier_Code = C.Code order by F.Num";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Flight List</caption><thread>";
    result += "<tr><th>Airline Name</th><th>Flight Number</th><th>Departure</th><th>Destination</th>"
            + "<th>Date</th><th>Time</th><th>Duration</th><th>Aircraft Model</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            result += "<tr><td>"+rs.getString("cname")+"</td><td>"
                    +rs.getString("fnum")+"</td><td>"
                    +rs.getString("a1city")+" ("+rs.getString("fori")+")</td><td>"
                    +rs.getString("a2city")+" ("+rs.getString("fdes")+")</td><td>"
                    +rs.getString("fdate")+"</td><td>";
            if (rs.getInt("ftime")/100<10) result += "0"+rs.getInt("ftime")/100+":";
            else result += rs.getInt("ftime")/100+":";
            if (rs.getInt("ftime")%100<10) result += "0"+rs.getInt("ftime")%100+"</td><td>";
            else result += rs.getInt("ftime")%100+"</td><td>";
            result += rs.getInt("fduration")/100+"h "+rs.getInt("fduration")%100+"m</td><td>"
                    +rs.getString("amodel")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  // This method uses right outer join.
  public static Result getBoughtTickets(String Passenger) {
    String[] Passenger_combo = Passenger.split("_");
    String FirstName = Passenger_combo[0];
    String LastName = Passenger_combo[1];
    String Dob = Passenger_combo[2];
    String sql = "select distinct T.num, T.price, T.seat, T.flight_num, T.flight_date "
                +"from Passengers P right outer join Tickets T on P.last_name = T.last_name and P.first_name = T.first_name and P.dob = T.dob "
                +"where P.first_name = \'"+FirstName+"\' and P.last_name=\'"+LastName+"\' and P.dob=\'"+Dob+"\' "
                +"order by T.flight_date desc";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Tickets History</caption><thread>";
    result += "<tr><th>Ticket Number</th><th>Ticket Price (USD)</th><th>Seat</th>"
            + "<th>Flight Number</th><th>Flight Date</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (!rs.next()) {
        	return ok("This passenger doesn't exist.");
        }
        
        while (rs.next()) {
            result += "<tr><td>"+rs.getString("num")+"</td><td>"
                    +rs.getString("price")+"</td><td>"
                    +rs.getString("seat")+"</td><td>"
                    +rs.getString("flight_num")+"</td><td>"
                    +rs.getString("flight_date")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }

  // This method uses left outer join.
  public static Result getTakingPassengers(String Flightinfo) {
    String[] flight_info_combo = Flightinfo.split("_");
    String FlightNum = flight_info_combo[0];
    String FlightDate = flight_info_combo[1];
    String sql = "select distinct T.num, P.first_name, P.last_name, P.dob, P.gender, P.nationality "
                +"from flights F, tickets T left outer join passengers P on p.last_name=t.last_name and p.first_name=t.first_name and p.dob=t.dob "
                +"where F.departure_date=T.flight_date "
                +"and T.flight_num=f.num "
                +"and T.flight_date=f.departure_date "
                +"and F.num=\'"+FlightNum+"\' "
                +"and F.departure_date=\'"+FlightDate+"\' order by T.num";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Passenger List</caption><thread>";
    result += "<tr><th>Ticket Number</th><th>First Name</th><th>Last Name</th>"
            + "<th>Date of Birth</th><th>Gender</th><th>Nationality</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (!rs.next()) {
        	return ok("No passenger on board.");
        }
        
        String tmp;
        while (rs.next()) {
            // rename gender
            if(rs.getString("gender").equals("M")) tmp = "Male";
            else if(rs.getString("gender").equals("F")) tmp = "Female";
            else tmp = "";
            result += "<tr><td>"+rs.getString("num")+"</td><td>"
                    +rs.getString("first_name")+"</td><td>"
                    +rs.getString("last_name")+"</td><td>"
                    +rs.getDate("dob").toString()+"</td><td>"
                    +tmp+"</td><td>"
                    +rs.getString("nationality")+"</td></tr>";
        }
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  public static Result getCarrierStats(String Carrier) {
    String csql = "select * from carriers where code =\'" + Carrier + "\'";
    String asql = "select count(a.code) as anum from airports a, flights f where f.carrier_code =\'" + Carrier + "\' "
                  +"and f.ori = a.code";
    String msql = "select count(distinct m.tailnum) as mnum from Aircraft_Models m, flights f  "
                  +"where f.carrier_code = \'" + Carrier + "\' and m.tailnum = f.tail_num ";
    String amsql = "select count(distinct am.name) as amnum from Aircraft_Manufacturers am, flights f, Aircraft_Models a where f.carrier_code = \'" + Carrier + "\' "
                   +"and a.tailnum = f.tail_num and a.manufacturer_name = am.name";
    String psql = "select count(distinct (p.first_name,p.last_name,p.dob)) as pnum from Passengers p, flights f, tickets t where f.carrier_code = \'" + Carrier + "\' "
                  +"and t.flight_date = f.Departure_Date and t.Flight_Num = f.num "
                  +"and T.last_name = P.last_name and T.first_name = P.first_name and T.Dob = P.dob ";
    String tsql = "select count(T.num) as tnum, sum(T.price) as revenue from Tickets T, flights f where f.carrier_code = \'" + Carrier + "\' "
                  +"and T.flight_num = f.num ";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Carrier Details</caption><thread>";
    result += "<tr><th>Carrier Name</th><th>Country</th><th>Number of Destinations</th><th>Number of Aircrafts</th>"
            + "<th>Number of Aircraft Brands</th><th>Number of Passengers</th>"
            + "<th>Tickets Sold</th><th>Tickets Revenue</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(csql);
        if (rs.next()) {
        	result += "<tr><td>"+rs.getString("name")+"("+rs.getString("code")+")</td><td>"
        	          +rs.getString("country")+"</td><td>";
        }
        else result += "<tr><td></td><td></td><td>";
        rs = st.executeQuery(asql);
        if (rs.next()) result += rs.getInt("anum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(msql);
        if (rs.next()) result += rs.getInt("mnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(amsql);
        if (rs.next()) result += rs.getInt("amnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(psql);
        if (rs.next()) result += rs.getInt("pnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(tsql);
        if (rs.next()) {
        	result += rs.getInt("tnum")+"</td><td>"
        	         +rs.getInt("revenue")+"</td></tr>";
        }
        else result += "</td><td></td></tr>";
        rs.close();
        st.close();


        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  public static Result getStatsByCountry(String Country) {
    String csql = "select count(code) as cnum from carriers where country =\'" + Country + "\'";
    String asql = "select count(code) as anum from airports where country =\'" + Country + "\'";
    String amsql = "select count(name) as amnum from Aircraft_Manufacturers where country =\'" + Country + "\'";
    String psql = "select count(distinct (p.first_name,p.last_name,p.dob)) as pnum from Passengers p where nationality =\'" + Country + "\'";
    String tsql = "select count(T.num) as tnum from Tickets T, Passengers P where p.nationality =\'" + Country + "\' "
                  +"and T.last_name = P.last_name and T.first_name = P.first_name and T.Dob = P.dob ";
    Connection conn = DB.getConnection();
    String result = "<table class=\"table table-hover\"><caption>Country Details</caption><thread>";
    result += "<tr><th>Country</th><th>Number of Carriers</th><th>Number of Airports</th>"
            + "<th>Number of Aircraft Manufacturers</th><th>Number of Passengers</th><th>Tickets Sold</th></tr></thread>";
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(csql);
        result += "<tr><td>"+Country+"</td><td>";
        if (rs.next()) result += rs.getInt("cnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(asql);
        if (rs.next()) result += rs.getInt("anum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(amsql);
        if (rs.next()) result += rs.getInt("amnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(psql);
        if (rs.next()) result += rs.getInt("pnum")+"</td><td>";
        else result += "</td><td>";
        rs = st.executeQuery(tsql);
        if (rs.next()) result += rs.getInt("tnum")+"</td></tr>";
        else result += "</td><td>";
                    
        rs.close();
        st.close();

        DBUtils.closeDBConnection(conn);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    result += "</table>";
    return ok(result);
  }
  
  public static Result getCarrierList(int i) {
    String sql = "select code, name from Carriers order by name";
    Connection conn = DB.getConnection();
		String result = "<select class=\"form-control\" id='carrier_selection_list"+i+"'>";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			result += "<option value='-1'>Select a Carrier</option>";
			while (rs.next()) {
				result += "<option value='"+rs.getString("Code")+"'>"+
				          rs.getString("Name") + " (" + rs.getString("Code")+")</option>";
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		result += "</select>";
		return ok(result);
  }

	public static Result getAirportCodeList(int i) {
    String sql = "select Code, City from Airports order by city";
    Connection conn = DB.getConnection();
		String result = "<select class=\"form-control\" id='airport_selection_list"+i+"'>";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			result += "<option value='-1'>Select an Airport</option>";
			while (rs.next()) {
				result += "<option value='"+rs.getString("Code")+"'>"+
				          rs.getString("City")+" (" + rs.getString("Code") + ")</option>";
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		result += "</select>";
		return ok(result);
  }
  
  public static Result getManufacturerList(int i) {
    String sql = "select Name from Aircraft_Manufacturers order by name";
    Connection conn = DB.getConnection();
		String result = "<select class=\"form-control\" id='plane_manufacturer"+i+"' name=\"plane_manufacturer\">";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			result += "<option value='-1'>Select a Manufacturer</option>";
			while (rs.next()) {
				result += "<option value='"+rs.getString("Name")+"'>"+
				          rs.getString("Name")+"</option>";
			}
			rs.close();
			st.close();

			DBUtils.closeDBConnection(conn);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		result += "</select>";
		return ok(result);
  }
}
