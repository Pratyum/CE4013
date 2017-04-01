/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Client.BookingMonitor;
import Client.BookingMonitorSkeleton;
import Server.BookingMonitorClient;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pratyumjagannath
 */
public class FacilityImplementation implements FacilityInterface {

    //Facility Lists
    private Map<Integer,Facility> map_facility;
    private Integer monitor_facility_id;
    private long monitor_period;
    private Map<Integer, Set<BookingMonitorInterface>> monitors;
    private Map<BookingMonitorInterface , Long> monitor_end_times;
    int booking_id = 0;
    //====================================for additional features=========================================
    //maps users to passwords
    private Map<String, String> user_accounts;
    //maps ip addresses to users
    private Map<InetAddress, String> user_IPs;
    //user of current request message
    private String current_user;
    //maps user to their booking record
    private Map<String, Map<Integer, String>> user_bookings;
    
    
    
    public FacilityImplementation() {
        map_facility = new HashMap<>();
        monitor_facility_id = null;
        monitors = new HashMap<>();
        monitor_end_times = new HashMap<>();
        loadUsers();
    }

    public FacilityImplementation(Map<Integer, Facility> map_facility) {
        this.map_facility = map_facility;
    }

    @Override
    public List<Integer> getID(String location, int space) {
    	
    	List<Integer> result = new ArrayList<Integer>();
        for(Facility facility:map_facility.values()){
            if(facility.getString_location().equalsIgnoreCase(location) && facility.getInt_space() >= space){
                System.out.println(String.format("%d", facility.getInt_id())+ "is one of the choices");
                result.add(facility.getInt_id());
            }
        }
        
        return result;
    }

    @Override
    public FacilityDetails getFacilities(int id) {
        if(!map_facility.containsKey(id)){
            return null;
        }
        Facility result = map_facility.get(id);
        return result.getFacilitiesDetails();
    }

    @Override
    public int bookFacility(int id, Date from, Date to) {
        //if flight not found
		if(!map_facility.containsKey(id)){
			//return -1
			return -1;
		}
		//if flight has sufficient seats
		Facility result = map_facility.get(id);
		if(result.isIs_Available(from , to)){
			//for addition feature
                        changeBooking(result, from, to);
			if(!recordBooking(result.getString_name(),booking_id , from ,to,id)){
				return -1;
                        }
			//return 1
			return booking_id++;
		}
		//else
		//return 0
		return -1;
    }
    @Override
    public String viewBooking(int id) {
        if(user_bookings.containsKey(current_user)){
                    if(user_bookings.get(current_user).containsKey(id)){
                            //if exist, return record
                            return user_bookings.get(current_user).get(id);
                    }
            }
        return "No Record found";
    }

    //store booking record in user booking record
    private boolean recordBooking(String name,int booking_id ,Date from , Date to , int id){
            if(user_accounts.containsKey(current_user)){
                    String ticketsBooked = viewBooking(booking_id);
                    if(ticketsBooked == "No Record found"){
                            if(!user_bookings.containsKey(current_user))
                                    user_bookings.put(current_user, new HashMap());
                    }
                    user_bookings.get(current_user).put(booking_id, String.format("%d : %s is booked from %s to %s",id,name,from.toString(),to.toString()));
                    return true;
            }
            return false;
    }
    
    private void changeBooking(Facility facility, Date from , Date to){
        facility.setIs_Available(false,from,to);
        informMonitor(facility.getInt_id(),from , to);
    }
    

    @Override
    public boolean monitorFacility(int id, long msec) {
        if(!map_facility.containsKey(id)){
                //return false
                return false;
        }
        //else
        //initialized to monitor flight ID
        monitor_facility_id = id;
        //set monitor period;
        monitor_period = msec;
        //return true
        return true;
    }
    //dummy data for user login
    private void loadUsers(){
            user_accounts = new HashMap();
            user_accounts.put("user1", "123");
            user_accounts.put("user2", "abc");
            //initialize additional feature attributes
            user_IPs = new HashMap();
            current_user = "";
            user_bookings = new HashMap();
    }
	
    
    //sets current user based on ip address
    public void setUser(InetAddress address){
            //if ip address maps to a user 
            if(user_IPs.containsKey(address)){
                    //set current user to that user
                    current_user = user_IPs.get(address);
            }else{
                    //else, clear current user
                    current_user = "";
            }
    }

    //registers an ip address to the user
    public void registerUser(InetAddress address, String user){
            //confirms the current user is login
            if(current_user.equals(user)){
                    //map ip address to user
                    user_IPs.put(address, user);
            }
    }

    @Override
    public boolean login(String user, String password) {
		// TODO Auto-generated method stub
		//if user exist and password matches user
		if(user_accounts.containsKey(user) && user_accounts.get(user).equals(password)){
			//set current user to user
			current_user = user;
			//return true
			return true;
		}
		return false;
    }
    
    

    private void informMonitor(int int_id, Date from, Date to) {
        //if no monitor for specified flight ID
        if(!monitors.containsKey(int_id)){
                //do nothing
                return;
        }
        //else
        List<BookingMonitorClient> tempList = new ArrayList(monitors.get(int_id));
        //loop through monitors for specified flight ID
        for(BookingMonitorClient monitor:tempList){
                //if monitor has not expire
                if(monitor_end_times.get(monitor) > System.currentTimeMillis()){
                        //update monitor
                        
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        monitor.update("A booking was done from "+ df.format(from) + " to " + df.format(to));
                //else
                }else{
                        //destroy monitor
                        monitors.get(int_id).remove(monitor);
                        monitor_end_times.remove(monitor);
                }
        }
    }
    
    
    public void addMonitor(BookingMonitorInterface monitor) {
                //if no flight to be monitored
		if(monitor_facility_id == null)
			//return
			return;
		//else
		//retrieve monitor parameters
		int iD = monitor_facility_id;
		long msec = monitor_period;
		//clear flight to be monitored
		monitor_facility_id = null;
		//create empty monitor set for flight if set not initialized
		if(!monitors.containsKey(iD)){
			monitors.put(iD, new HashSet<>());
		}
		//add monitor to monitor set
		monitors.get(iD).add(monitor);
		//set end time for monitor
		monitor_end_times.put(monitor, System.currentTimeMillis()+msec);
    }

    public void addfacility(Facility facility) {
        if(map_facility.containsKey(facility.getInt_id())){
            System.out.println("Error in creating Facility " + facility.getString_name());
        }else{
        map_facility.put(facility.getInt_id(), facility);
        }
        
    }







    
}
