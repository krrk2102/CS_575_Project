drop table if exists Tickets;
drop table if exists Passengers;
drop table if exists Flights;
drop table if exists Carriers;
drop table if exists Airports;
drop table if exists Aircraft_Models;
drop table if exists Aircraft_Manufacturers;


create table Carriers(
    code varchar(8) primary key,
    name varchar(64) not null,
    country varchar(32) not null
);

create table Airports(
    Code varchar(8) primary key,
    Name varchar(128) not null,
    City varchar(32) not null,
    Country varchar(32) not null
);

create table Aircraft_Manufacturers(
    name varchar(32) primary key,
    country varchar(32),
    year_founded integer
);

create table Aircraft_Models(
    tailnum varchar(16) primary key,
    model varchar(32) not null,
    engine_type varchar(32) not null,
    issue_date date,
    manufacturer_name varchar(32) not null,
    foreign key (manufacturer_name) references Aircraft_Manufacturers(name)
);

create table Flights (
    Num varchar(16) not null,
    Carrier_Code varchar(8) not null,
    Departure_Date date not null,
    Departure_Time int not null,
    Duration integer not null,
    Tail_Num varchar(32) not null,
    Ori varchar(8) not null,
    Des varchar(8) not null,
    primary key (Num, Departure_Date),
    foreign key (Carrier_Code) references Carriers(code),
    foreign key (Tail_Num) references Aircraft_Models(tailnum),
    foreign key (Ori) references Airports(Code),
    foreign key (Des) references Airports(Code)
);

create table Passengers (
    last_name varchar(32) not null,
    first_name varchar(32) not null,
    Dob date not null,
    Gender varchar(1),
    Nationality varchar(32),
    primary key (last_name, first_name, Dob)
);

create table Tickets (
    Num bigint primary key,
    Price integer not null,
    Seat varchar(8),
    Flight_Num varchar(16) not null,
    Flight_Date date,
    last_name varchar(32),
    first_name varchar(32),
    Dob date,
    backup integer,
    foreign key (Flight_Num, Flight_Date) references Flights(Num, Departure_Date),
    foreign key (last_name, first_name, Dob) references Passengers(last_name, first_name, Dob)
);

\copy Passengers (first_name, last_name, Dob, Gender, Nationality) from 'csv/passengers.csv' delimiters ',' csv;
\copy Aircraft_Manufacturers (name, country, year_founded) from 'csv/aircraft_manu.csv' delimiters ',' csv;
\copy Aircraft_Models(tailnum, model, engine_type, issue_date, manufacturer_name) from 'csv/plane.csv' delimiters ',' csv;
\copy Airports (Code, Name, City, Country) from 'csv/airports.csv' delimiters ',' csv;
\copy Carriers(code, name, country) from 'csv/carriers.csv' delimiters ',' csv;
\copy Flights (Num, Carrier_Code, Departure_Date, Departure_Time, Duration, Tail_Num, Ori, Des) from 'csv/flights.csv' delimiters ',' csv;
\copy Tickets (backup, Num, Price, Seat, Flight_Num, Flight_Date, first_name, last_name, Dob) from 'csv/tickets.csv' delimiters ',' csv;

ALTER TABLE Tickets DROP COLUMN backup;