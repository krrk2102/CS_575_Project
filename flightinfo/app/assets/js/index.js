function httpGet(theUrl)
{
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function listFlight() {
    var num = $("input[name='flight_num']").val();
    if (isNaN(num) || num < 0 || num > 9999) {
      alert("Invalid flight number");
      return;
    }
    var selected_index = document.getElementById('carrier_selection_list0').selectedIndex;
    var carrier = document.getElementById('carrier_selection_list0').options[selected_index].value;
    if (carrier=='-1') {
      alert("Please select a carrier.");
      return;
    }
    var flight = carrier + num.toString();
    document.getElementById('flight_info').innerHTML = httpGet('/getFlight/' + flight);
}

function listRoutes() {
    var src_selected_index = document.getElementById('airport_selection_list1').selectedIndex;
    var src = document.getElementById('airport_selection_list1').options[src_selected_index].value;
    if (src=='-1') {
      alert("Please select departure airport.");
      return;
    }
    var dest_selected_index = document.getElementById('airport_selection_list2').selectedIndex;
    var dest = document.getElementById('airport_selection_list2').options[dest_selected_index].value;
    if (dest=='-1') {
      alert("Please select destination.");
      return;
    }
    var date = $("input[name='route_date']").val();
    var newDate = date.slice(0,4)+date.slice(5,7)+date.slice(8);
    var RouteStr = src+'_'+dest+'_'+newDate;
    document.getElementById('route_info').innerHTML = httpGet('/getRoute/' + RouteStr);
}

function listAirports() {
    var selected_index = document.getElementById('airport_selection_list0').selectedIndex;
    var airport = document.getElementById('airport_selection_list0').options[selected_index].value;
    if (airport=='-1') {
      alert("Please select an airport.");
      return;
    }
    document.getElementById('reachable_airports').innerHTML = httpGet('/getDes/' + airport);
}

function listCarrierFlights() {
    var selected_index = document.getElementById('carrier_selection_list1').selectedIndex;
    var carrier = document.getElementById('carrier_selection_list1').options[selected_index].value;
    if (carrier=='-1') {
      alert("Please select a carrier.");
      return;
    }
    document.getElementById('carrier_flights').innerHTML = httpGet('/getFlightByCarrier/' + carrier);
}

function listBoughtTickets() {
    var first_name = $("input[name='record_first_name']").val().toUpperCase();
    var last_name = $("input[name='record_last_name']").val().toUpperCase();
    var dob = $("input[name='record_dob']").val();
    var new_dob = dob.slice(0,4)+dob.slice(5,7)+dob.slice(8);
    var passenger = first_name+'_'+last_name+'_'+new_dob;
    document.getElementById('bought_tickets').innerHTML = httpGet('/getBoughtTickets/' + passenger);
}

function listTakingPassengers() {
    var selected_index = document.getElementById('carrier_selection_list2').selectedIndex;
    var carrier = document.getElementById('carrier_selection_list2').options[selected_index].value;
    if (carrier=='-1') {
      alert("Please select a carrier.");
      return;
    }
    var num = $("input[name='take_flight_num']").val();
    var flight_num = carrier + num.toString();
    var flight_date = $("input[name='take_flight_date']").val();
    var new_flight_date = flight_date.slice(0,4)+flight_date.slice(5,7)+flight_date.slice(8);
    var flight_info = flight_num+'_'+new_flight_date;
    document.getElementById('taking_passengers').innerHTML = httpGet('/getTakingPassengers/' + flight_info);
}

function listCountryStats() {
    var selected_index = document.getElementById('CountryStats').selectedIndex;
    var country = document.getElementById('CountryStats').options[selected_index].value;
    if (country=='-1') {
      alert("Please select a country.");
      return;
    }
    document.getElementById('stats_by_countries').innerHTML = httpGet('/getStatsByCountry/' + country);
}

function listCarrierStats() {
    var selected_index = document.getElementById('carrier_selection_list3').selectedIndex;
    var carrier = document.getElementById('carrier_selection_list3').options[selected_index].value;
    if (carrier=='-1') {
      alert("Please select a carrier.");
      return;
    }
    document.getElementById('stats_by_carrier').innerHTML = httpGet('/getCarrierStats/' + carrier);
}
