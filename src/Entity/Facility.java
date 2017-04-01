package Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pratyumjagannath
 */
public class Facility {
	private int int_id;
	private String string_name;
	private int int_space;
	private String string_location;
	private boolean is_Available;
        private Map<Date,Date> freetimes;

    //Constuctor
    public Facility(int int_id, String string_name, int int_space, String string_location, boolean is_Available) {
        this.int_id = int_id;
        this.string_name = string_name;
        this.int_space = int_space;
        this.string_location = string_location;
        this.is_Available = is_Available;
        this.freetimes = new HashMap<Date, Date>();
    }

    //Getters and Setters
    
    public int getInt_id() {
        return int_id;
    }

    public void setInt_id(int int_id) {
        this.int_id = int_id;
    }

    public String getString_name() {
        return string_name;
    }

    public void setString_name(String string_name) {
        this.string_name = string_name;
    }

    public int getInt_space() {
        return int_space;
    }

    public void setInt_space(int int_space) {
        this.int_space = int_space;
    }

    public String getString_location() {
        return string_location;
    }

    public void setString_location(String string_location) {
        this.string_location = string_location;
    }

    public boolean isIs_Available() {
        return is_Available;
    }
    public boolean isIs_Available(Date from , Date to) {
        boolean result = true;
        for(Date from_date : freetimes.keySet()){
            boolean from_dateAfterFrom = from_date.after(from);
            boolean from_dateAfterTo = from_date.after(to);
            boolean to_dateAfterFrom = freetimes.get(from_date).after(from);
            boolean to_dateAferTo = freetimes.get(from_date).after(to);
            if(!from_dateAfterFrom && from_dateAfterTo && !to_dateAferTo){
                System.out.println("Not in available");
                return false;
            }
            else if (from_dateAfterFrom && !from_dateAfterTo){
                System.out.println("Not in available");
                return false;
            }
        }
        return result;
    }
    public void setIs_Available(boolean is_Available) {
        this.is_Available = is_Available;
    }


    //returns a object of Facilities to help with Marshalling and Unmarshalling
    public FacilityDetails getFacilitiesDetails(){
        FacilityDetails details = new FacilityDetails();
        details.setString_name(this.string_name);
        details.setInt_space(this.int_space);
        details.setString_location(this.string_location);
        details.setIs_Available(this.is_Available);
        return details;
    }

    boolean setIs_Available(boolean b, Date from, Date to) {
        if(isIs_Available(from, to)){
            this.freetimes.put(from,to);
            return true;
        }else
            return false;
    }
    
}
