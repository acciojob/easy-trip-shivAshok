package com.driver;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class AirportService {
    @Autowired
    private AirPostRepo airrepo=new AirPostRepo();
    public void AddairPort(Airport airport) {
        airrepo.addTheAirport(airport);
    }

    public String getLargestAirportName() {
        List<Airport> airports=airrepo.getAllairPorts();
        int maxterminal=0;
        String largestAirport="";
        for(Airport airport:airports){

            if(airport.getNoOfTerminals()>maxterminal){
                maxterminal=airport.getNoOfTerminals();
                largestAirport=airport.getAirportName();
            }
            else if(airport.getNoOfTerminals()==maxterminal && airport.getAirportName().compareTo(largestAirport)<0){
                largestAirport=airport.getAirportName();
            }
        }
        return largestAirport;
    }

    public void addFlight(Flight flight) {
        airrepo.addFlight(flight);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flights=airrepo.getAllflights();
        double duration=Double.MAX_VALUE;
        for(Flight flight:flights){
            if(flight.getFromCity()==fromCity && flight.getToCity()==toCity){
                duration=Math.min(duration,flight.getDuration());
            }
        }
        if(duration==Double.MAX_VALUE){
            return -1;
        }

        return duration;
    }

    public void addPassenger(Passenger passenger) {
        airrepo.addPassenger(passenger);
    }

    public int getNoOfPeople(Date date, String airportName) {
        Optional<Airport> airportOpt=airrepo.getAirportByname(airportName);
        if(airportOpt.isEmpty()){
            return 0;
        }
        List<Flight> flights=airrepo.getAllflights();
        int numOfpeople=0;
        for(Flight flight:flights){
            if(flight.getFlightDate().equals(date) && (flight.getToCity().equals(airportOpt.get().getCity())||flight.getToCity().equals(airportOpt.get().getCity()))) {
                List<Integer> passengers=airrepo.getAllPassengerByflightId(flight.getFlightId());
                numOfpeople+=passengers.size();
            }
        }
        return numOfpeople;
    }

    public String getAirPortnamefromFlightId(Integer flightId) {
        Optional<Flight> flightOpt=airrepo.getFlightbyFlightId(flightId);
        if(flightOpt.isEmpty()){
            return null;
        }
        City city=flightOpt.get().getFromCity();
        List<Airport> airports=airrepo.getAllairPorts();
        for(Airport airport:airports){
            if(airport.getCity().equals(city)){
                return airport.getAirportName();
            }
        }
        return null;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        return airrepo.BookAteicket(flightId,passengerId);
    }

    public String cancelTicket(Integer flightId, Integer passengerId) {
        if(airrepo.getFlightbyFlightId(flightId).isEmpty()){
            return "FAILURE";
        }
        List<Integer> passenger=airrepo.getAllPassengerByflightId(flightId);
        boolean ispresent=false;
        for(int p:passenger){
            if(p==passengerId){
                ispresent=true;
            }
        }
        if(ispresent){
            airrepo.cancelTicket(flightId,passengerId);
            return "SUCCESS";
        }
        else{
            return "FAILURE";
        }
    }

    public int getCountOfbookingsdonebyPassenger(Integer passengerId) {
        List<Integer> flightId=airrepo.getAllflightIds();
        int count=0;
        for(int Id:flightId){
            List<Integer> passengers=airrepo.getAllPassengerByflightId(Id);
            for(int P:passengers){
                if(P==passengerId){
                    count++;
                }
            }
        }
        return count;
    }

    public int calculateFair(Integer flightId) {
        List<Integer> passengers=airrepo.getAllPassengerByflightId(flightId);
        int fare=3000+50*passengers.size();
        return fare;
    }

    public int calculateRevenue(Integer flightId) {
        return airrepo.getRevenue(flightId);
    }
}
