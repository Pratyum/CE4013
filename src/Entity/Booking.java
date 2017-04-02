/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Client.BookingMonitor;
import Client.BookingMonitorSkeleton;
import Server.BookingSkeleton;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author pratyumjagannath
 */
public class Booking extends Stub implements FacilityInterface {

    private String name = "Facility";

    public Booking(DatagramSocket socket, InetAddress host, int port) {
        super(socket, host, port);
    }


    @Override
    public List<Integer> getID(String location, int space) {

        byte[] parameters = marshaller.toMessage(location);
        parameters = marshaller.appendBytes(parameters, marshaller.toMessage(space));
        //send request
        List<Integer> result = (List<Integer>) sendRequest("getID", parameters, List.class);
        if(result==null)
            result = new ArrayList();
        //return result
        return result;
    }

    @Override
    public FacilityDetails getFacilities(int id) {
        //Prepare Parameters
        byte[] parameters = marshaller.toMessage(id);

        //Send Request 
        FacilityDetails result = (FacilityDetails) sendRequest("getFacilityDetails",parameters,FacilityDetails.class);
        
        return result;    
    }


    @Override
    public int bookFacility(int ID, Date from, Date to) {
        
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        byte[] parameters = marshaller.toMessage(ID);
        parameters = marshaller.appendBytes(parameters,marshaller.toMessage(df.format(from)));
        parameters = marshaller.appendBytes(parameters,marshaller.toMessage(df.format(to)));

        int result = (Integer) sendRequest("bookFacility",parameters,Integer.class);

        return result;

    }
    
    @Override
    public String viewBooking(int id) {
        byte[] parameters = marshaller.toMessage(id);
        
        String result = (String) sendRequest("viewBooking", parameters, String.class);
        return result;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean monitorFacility(int id, long msec) {
        byte[] parameters = marshaller.toMessage(id);
        parameters = marshaller.appendBytes(parameters, marshaller.toMessage(msec));
        //send request
        Boolean result = (Boolean) sendRequest("monitorFacility", parameters, Boolean.class);
        if(result==null)
            result = false;
        //if successful
        if(result){
            //create monitor implementation and skeleton
            BookingMonitor monitor = new BookingMonitor(id);
            BookingMonitorSkeleton server = new BookingMonitorSkeleton(socket,monitor);
            try{
                server.listen(msec);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }

    @Override
    public boolean login(String user, String password) {
        byte[] parameters = marshaller.toMessage(user);
        parameters = marshaller.appendBytes(parameters, marshaller.toMessage(password));
        boolean result = (Boolean) sendRequest("login", parameters, Boolean.class);
        return result;
    }

    @Override
    public boolean changeBooking(int booking_id, int hours_to_posttone) {
        byte[] parameters = marshaller.toMessage(booking_id);
        parameters = marshaller.appendBytes(parameters, marshaller.toMessage(hours_to_posttone));
        boolean result= (Boolean) sendRequest("changeBooking", parameters, Boolean.class);
        return result;
    }

    @Override
    public boolean cancelBooking(int booking_id) {
        byte[] parameters = marshaller.toMessage(booking_id);
        boolean result = (Boolean) sendRequest("cancelBooking", parameters, Boolean.class);
        return result;
    }


    
}
