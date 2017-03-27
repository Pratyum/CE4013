/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author pratyumjagannath
 */
public class Booking extends Stub implements FacilityInterface {

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
    public Boolean bookFacility(int ID, Date from, Date to) {
        byte[] parameters = marshaller.toMessage(ID);
        parameters = marshaller.appendBytes(parameters,marshaller.toMessage(from.toString()));
        parameters = marshaller.appendBytes(parameters,marshaller.toMessage(to.toString()));

        Boolean result = (Boolean) sendRequest("bookFacility",parameters,Boolean.class);

        return result;

    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
