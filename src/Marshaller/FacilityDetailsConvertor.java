/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Marshaller;

import Entity.FacilityDetails;

/**
 *
 * @author pratyumjagannath
 */
public class FacilityDetailsConvertor implements Convertor {
    private FacilityDetails data;
    public FacilityDetailsConvertor(FacilityDetails data) {
        this.data = data;
    }
    @Override
    public int getByteCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getByteCount(FacilityDetails data) {
    	return  data.getString_name().getBytes().length+ Integer.BYTES + data.getString_location().getBytes().length + new BooleanConvertor().getByteCount() ;
    }

    @Override
    public Object fromBytes(byte[] bytes) {
    	   FacilityDetails data = new FacilityDetails();
    	int position = 0,length =0;

    	//Converting byte array into FacilityDetails object

    	//1A.Length of Name
    	byte[] attributeBytes = new byte[Integer.BYTES];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//get Length
		length = (Integer)new IntegerConvertor().fromBytes(attributeBytes);

		//1B. Name
    	attributeBytes = new byte[length];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//set attribute
		data.setString_name((String)new StringConvertor().fromBytes(attributeBytes));

		//2. Space
		attributeBytes = new byte[Integer.BYTES];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//set attribute
		data.setInt_space((Integer)new IntegerConvertor().fromBytes(attributeBytes));
		
		//3A. Length of location 
		attributeBytes = new byte[Integer.BYTES];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//get Length
		length = (Integer)new IntegerConvertor().fromBytes(attributeBytes);

		//3B. Location
		attributeBytes = new byte[length];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//set attribute
		data.setString_location((String)new StringConvertor().fromBytes(attributeBytes));
		
		//4. isAvailable
		attributeBytes = new byte[new BooleanConvertor().getByteCount()];
		System.arraycopy(bytes, position, attributeBytes, 0, attributeBytes.length);
		position += attributeBytes.length;
		//set attribute
		data.setIs_Available((Boolean)new BooleanConvertor().fromBytes(attributeBytes));
		
		//return object
		return data;



    }

    @Override
    public byte[] toBytes(Object data) {
    	return toBytes((FacilityDetails)data);
    }

    private byte[] toBytes(FacilityDetails data){
    	byte[] dataBytes = new byte[getByteCount()];
    	byte[] attributeBytes;
    	int position = 0;

    	//Conversion from attributes to byte array
        
    	//1A.Length of Name
    	attributeBytes = new IntegerConvertor().toBytes(data.getString_name().getBytes().length);
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;
    	//1B. Name
    	attributeBytes = new StringConvertor().toBytes(data.getString_name());
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;
    	//2. Space
    	attributeBytes = new IntegerConvertor().toBytes(data.getInt_space());
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;
        //3A.Length of Name
    	attributeBytes = new IntegerConvertor().toBytes(data.getString_location().getBytes().length);
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;    	
        //3B. Location
    	attributeBytes = new StringConvertor().toBytes(data.getString_location());
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;
    	//4. isAvailable
    	attributeBytes = new BooleanConvertor().toBytes(data.isIs_Available());
    	System.arraycopy(attributeBytes, 0, dataBytes, position, attributeBytes.length);
		position += attributeBytes.length;

		//return buffer
		return dataBytes;
    }


    
}
