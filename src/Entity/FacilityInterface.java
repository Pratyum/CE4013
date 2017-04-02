/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.Date;
import java.util.List;

/**
 *
 * @author pratyumjagannath
 */
public interface FacilityInterface {
    
	//returns a list of the Facilities mactching the location and a maximum of space
    public List<Integer> getID(String location,int space);

    //returns the details of the facilities for a given ID
    public FacilityDetails getFacilities(int id);

    //returns the status of the booking for a given ID for a given date period
    public int bookFacility(int id, Date from, Date to);

    //returns the status of monitoring the facility change
    public boolean monitorFacility(int id, long msec);
    
    // Returns the Booking Object
    public String viewBooking(int id);
    
    //change Booking
    public boolean changeBooking(int booking_id, int hours_to_posttone);
    
    // Additional Functions 
    public boolean login(String username, String password);
    
    public boolean cancelBooking(int booking_id);
    
}
