package Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

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
        private Map<Integer,Pair<Date,Date>> freetimes;

    //Constuctor
    public Facility(int int_id, String string_name, int int_space, String string_location, boolean is_Available) {
        this.int_id = int_id;
        this.string_name = string_name;
        this.int_space = int_space;
        this.string_location = string_location;
        this.is_Available = is_Available;
        this.freetimes = new HashMap<>();
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
        for (Map.Entry<Integer, Pair<Date, Date>> entry : freetimes.entrySet()) {
            Integer key = entry.getKey();
            Pair<Date, Date> value = entry.getValue();
            if(from.before(value.getValue()) && to.after(value.getKey()))
                return false;
        }
        return result;
    }
    public boolean isIs_Available(Date from , Date to, int booking_id) {
        boolean result = true;
        for (Map.Entry<Integer, Pair<Date, Date>> entry : freetimes.entrySet()) {
            Integer key = entry.getKey();
            Pair<Date, Date> value = entry.getValue();
            if(key != booking_id)
                if(from.before(value.getValue()) && to.after(value.getKey()))
                    return false;
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

    boolean setIs_Available(int booking_id, Date from, Date to) {
        if(isIs_Available(from, to)){
            this.freetimes.put(booking_id, new Pair<>(from,to));
            return true;
        }else
            return false;
    }
    
    public Pair<Date,Date> getFreeTime(int booking_id){
        if(freetimes.containsKey(booking_id)){
            return freetimes.get(booking_id);
        }
        return null;
    }
    
    public boolean removeFreeTime(int booking_id){
        if(freetimes.containsKey(booking_id)){
            freetimes.remove(booking_id);
            return true;
        }
        return false;
    }
    
}
