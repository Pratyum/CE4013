package Marshaller;
import Entity.FacilityDetails;
import java.util.ArrayList;
import java.util.List;
public class DataMarshaller {
	//byte representation for data types
	private final byte null_byte = 0;
	private final byte bool_byte = 1;
	private final byte integer_byte = 2;
	private final byte long_byte = 3;
	private final byte string_byte = 4;
	private final byte array_byte = 5;
	private final byte float_byte = 6;
	private final byte remoteObjByte = 7;
	private final byte facilityDetailsByte = 8;
	
	//combines two byte arrays together
	public byte[] appendBytes(byte[] bytes1, byte[] bytes2){
		byte[] bytes = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, bytes, 0, bytes1.length);
		System.arraycopy(bytes2, 0, bytes, bytes1.length, bytes2.length);
		return bytes;
	}
	
	//retrieves a subset of a byte array
	public byte[] subBytes(byte[] bytes, int start, int end){
		byte[] subBytes = new byte[end-start];
		System.arraycopy(bytes, start, subBytes, 0, subBytes.length);
		return subBytes;
	}
	
	//returns the appropriate convert based on data type byte
	private Convertor getConvertor(byte type){
		switch(type){
		case bool_byte:
			return new BooleanConvertor();
		case integer_byte:
			return new IntegerConvertor();
		case long_byte:
			return new LongConvertor();
		case string_byte:
			return new StringConvertor();
		case array_byte:
			return new ArrayConvertor();
		case float_byte:
			return new FloatConvertor();
		case facilityDetailsByte:
			return new FacilityDetailsConvertor(new FacilityDetails());
		}
		return null;
	}
	
	private Convertor getConvertor(byte[] data, Counter pos){
		Convertor convertor = null;
                
		switch(data[pos.getValue()]){
		case null_byte:
		case bool_byte:
		case integer_byte:
		case long_byte:
		case float_byte:
                        //if null, boolean, integer, long, float,facilityDetailsByte
 			//get convertor
 			convertor = getConvertor(data[pos.getValue()]);
 			pos.inc();
                        break;
		case string_byte:
			//if string
			//get convertor
			StringConvertor stringConvertor = (StringConvertor) getConvertor(data[pos.getValue()]);
			pos.inc();
			//initialize size parmameter of convertor
			stringConvertor.setSize(data[pos.getValue()]);
			pos.inc();
			convertor = stringConvertor;
			break;
		case array_byte:
			//if array
			//get convertor
			ArrayConvertor arrayConvertor = (ArrayConvertor) getConvertor(data[pos.getValue()]);
			pos.inc();
			//get second convertor array item type
			arrayConvertor.setInternalConvertor(getConvertor(data, pos));
			//initialize size parmameter of convertor
			arrayConvertor.setSize(data[pos.getValue()]);
			pos.inc();	
			convertor = arrayConvertor;
			break;
                case facilityDetailsByte:
                    FacilityDetailsConvertor facilityDetailsConvertor = (FacilityDetailsConvertor) getConvertor(data[pos.getValue()]);
                    pos.inc();
                    facilityDetailsConvertor.setByteCount(data[pos.getValue()]);
                    pos.inc();
                    convertor = facilityDetailsConvertor;
                    break;
                    
		case remoteObjByte:
			//remote object not implemented
			break;
		}
		return convertor;
	}
	
	//converts java data/object to byte array message
	//format of message as follows
	//null
	//(null type byte)
	//boolean, int, long, float:
	//(data type byte)(byte array of data)
	//string:
	//(string type byte)(length of string)(byte array of data)
	//array
	//(array type byte)(data type byte of array item)(length of array)(byte array of data)
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public byte[] toMessage(boolean data){
		return appendBytes(new byte[]{bool_byte}, getConvertor(bool_byte).toBytes(data));
	}
	
	public byte[] toMessage(int data){
		return appendBytes(new byte[]{integer_byte}, getConvertor(integer_byte).toBytes(data));
	}
	
	public byte[] toMessage(long data){
		return appendBytes(new byte[]{long_byte}, getConvertor(long_byte).toBytes(data));
	}
	
	public byte[] toMessage(String data){
		if(data==null)
			return nullMessage();
		return appendBytes(new byte[]{string_byte, (byte)data.length()}, getConvertor(string_byte).toBytes(data));
	}
	
	public byte[] toMessage(List<Integer> data){
		if(data==null)
			return nullMessage();
		ArrayConvertor convertor = (ArrayConvertor) getConvertor(array_byte);
		convertor.setInternalConvertor(getConvertor(integer_byte));
		convertor.setSize(data.size());
		return appendBytes(new byte[]{array_byte, integer_byte ,(byte)data.size()}, convertor.toBytes(data));
	}
	
	public byte[] toMessage(float data){
		return appendBytes(new byte[]{float_byte}, getConvertor(float_byte).toBytes(data));
	}
	
	public byte[] toMessage(FacilityDetails data){
		if(data==null)
			return nullMessage();
		return appendBytes(new byte[]{facilityDetailsByte,(byte)getConvertor(facilityDetailsByte).toBytes(data).length}, getConvertor(facilityDetailsByte).toBytes(data));
	}
	
	public byte[] nullMessage(){
		return new byte[]{null_byte};
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//unmarshalls data from byte array
	public Object fromMessage(byte[] message){
		//data object
		Object data = null;
		//data object buffer
		List dataBuffer = new ArrayList();
		//if message is too short
		if(message.length < 2){
			//return null
			return data;
		}
		//keep track of starting position of unread bytes
		Counter position = new Counter();
		//while not end of byte array
		while(position.getValue() < message.length){
			//retrieve convertor
			Convertor convertor = getConvertor(message, position);
			//if convertor is null
			if(convertor == null)
				//data is null
				data = null;
			//else
			else{
				//convert bytes to data using convertor
 				data = convertor.fromBytes(subBytes(message, position.getValue(), position.getValue()+convertor.getByteCount()));
				//updates position
				position.inc(convertor.getByteCount());
			}
			//adds data to bufffer
			dataBuffer.add(data);
		}
		//buffer has more than 1 item
		if(dataBuffer.size() > 1){
			//return buffer
			return dataBuffer;
		}
		//else return data
		return data;
	}

//    public static void main(String[] args){
//        FacilityDetails details = new FacilityDetails();
//        details.setString_name("Testing");
//        details.setInt_space(100);
//        details.setString_location("NBS");
//        details.setIs_Available(false);
//        DataMarshaller marshaller = new DataMarshaller();
//        System.out.println(marshaller.toMessage(details));
//        FacilityDetails details1 = (FacilityDetails)marshaller.fromMessage(marshaller.toMessage(details));
//        System.out.println(details1.getString_name());
//        System.out.println(details1.getString_location());
//        System.out.println(details1.getInt_space());
//        System.out.println(details1.isIs_Available());
//    }
}
