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
    public Boolean bookFacility(int ID, Date from, Date to);

}
