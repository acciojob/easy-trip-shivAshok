package com.driver;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class AirPostRepo {
    Map<String,Airport> Airports=new HashMap<>();
    Map<Integer,Flight> flights=new HashMap<>();
    Map<Integer,Passenger> passengers=new HashMap<>();
    Map<Integer,List<Integer>> FlightwithPassenger=new HashMap<>();
    Map<Integer,Integer> flightWithRevenue=new HashMap<>();
    public void addTheAirport(Airport airport) {
        Airports.put(airport.getAirportName(),airport);
    }

    public List<Airport> getAllairPorts() {
        return new ArrayList<>(Airports.values());
    }

    public void addFlight(Flight flight) {
        flights.put(flight.getFlightId(),flight);
    }

    public List<Flight> getAllflights() {
        return new ArrayList<>(flights.values());
    }

    public void addPassenger(Passenger passenger) {
        passengers.put(passenger.getPassengerId(),passenger);
    }

    public Optional<Airport> getAirportByname(String airportName) {
        if(Airports.containsKey(airportName)){
            return Optional.of(Airports.get(airportName));
        }
        return Optional.empty();
    }

    public Optional<Flight> getFlightbyFlightId(Integer flightId) {
        if(flights.containsKey(flightId)){
            return Optional.of(flights.get(flightId));
        }
        return Optional.empty();
    }

    public String BookAteicket(Integer flightId, Integer passengerId) {
        if(FlightwithPassenger.containsKey(flightId)){
            List<Integer> passengers=FlightwithPassenger.get(flightId);
            if(passengers.size()==this.getFlightbyFlightId(flightId).get().getMaxCapacity()){
                return "FAILURE";
            }
            for(int passenger:passengers ){
                if(passenger==passengerId){
                    return "FAILURE";
                }
            }
            int revenue=flightWithRevenue.get(flightId)+3000*passengers.size();
            flightWithRevenue.put(flightId,revenue);
            passengers.add(passengerId);
            FlightwithPassenger.put(flightId,passengers);

        }
        else{
            List<Integer> passengers=new ArrayList<>();
            passengers.add(passengerId);
            FlightwithPassenger.put(flightId,passengers);
            flightWithRevenue.put(flightId,3000);
        }
        return "SUCCESS";
    }


    public List<Integer> getAllPassengerByflightId(Integer flightId) {
        return FlightwithPassenger.getOrDefault(flightId,new ArrayList<>());
    }

    public void cancelTicket(Integer flightId, Integer passengerId) {
        List<Integer> passengers=FlightwithPassenger.get(flightId);
        passengers.remove(passengerId);
        int revenue=flightWithRevenue.get(flightId)-(passengers.size()-1)*50;
        flightWithRevenue.put(flightId,revenue);
    }

    public List<Integer> getAllflightIds() {
        return new ArrayList<>(flights.keySet());
    }

    public int getRevenue(Integer flightId) {
        return flightWithRevenue.getOrDefault(flightId,0);
    }
}
