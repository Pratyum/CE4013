/*
 * FacilityDetails is used similarly to a data structure for easier marshaling and passing of the following fields
 * 
 */
package Entity;

/**
 *
 * @author pratyumjagannath
 */
public class FacilityDetails {

	private String string_name;
	private int int_space;
	private String string_location;
	private boolean is_Available;

	//Getters and Setters for Attributes

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

    public void setIs_Available(boolean is_Available) {
        this.is_Available = is_Available;
    }

	


    
}
