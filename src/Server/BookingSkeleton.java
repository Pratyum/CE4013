/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Client.BookingMonitor;
import Client.BookingMonitorSkeleton;
import Entity.Booking;
import Entity.FacilityImplementation;
import Entity.Skeleton;
import Entity.SkeletonFunctionInterface;
import Marshaller.DataMarshaller;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pratyumjagannath
 */
public class BookingSkeleton extends Skeleton {
    //object(class) name
	private static String name = "Facility";
	
	//Reference of the facility Implementation object
	private FacilityImplementation ref_facility;

	//Cache of Users(IP) to last reply
	private Map<InetAddress , RequestHistory> map_facility_history;
	private Map<InetAddress , RequestHistory> map_monitor_facility_history;
	private Map<InetAddress , RequestHistory> map_cancel_facility_history;

	public BookingSkeleton(FacilityImplementation ref_facility){
		this.ref_facility = ref_facility;
		marshaller = new DataMarshaller();
		map_facility_history = new HashMap();
		map_monitor_facility_history = new HashMap();
		map_cancel_facility_history = new HashMap();

		functionMap = initializeFunctionMap();

	}

	private Map<String, SkeletonFunctionInterface> initializeFunctionMap(){
		Map<String, SkeletonFunctionInterface> functionMap = new HashMap();
                
                functionMap.put("getID", new SkeletonFunctionInterface(){
			@Override
			public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
				//unmarshal parameters from message
                                System.out.println("In GetID");
				List objects = (List) marshaller.fromMessage(data);
				//pass parameter to method implementation
				//return marshalled reply
                                System.out.println(ref_facility.getID((String)objects.get(0), (Integer)objects.get(1)));
				return marshaller.toMessage(ref_facility.getID((String)objects.get(0), (Integer)objects.get(1)));
			}
		});
                functionMap.put("getFacilityDetails", new SkeletonFunctionInterface() {
                    @Override
                    public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                        Integer id = (Integer) marshaller.fromMessage(data);
                        return marshaller.toMessage(ref_facility.getFacilities(id));
                    }
                });
                functionMap.put("bookFacility", new SkeletonFunctionInterface() {
                    @Override
                    public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                        //check if request is a repeat
				if(map_facility_history.containsKey(sourceAddress) && messageNo == map_facility_history.get(sourceAddress).getRequestNo()){
					//return cached reply
					return map_facility_history.get(sourceAddress).getReplyMessage();
				//else
				}else{
					//set user of facility implementation
					setUser(sourceAddress);
					//unmarshal parameters from message
					List objects = (List) marshaller.fromMessage(data);
					//pass parameter to method implementation
					//marshal reply
                                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                        try{
                                            byte[] reply = marshaller.toMessage(ref_facility.bookFacility((Integer)objects.get(0), df.parse((String)objects.get(1)),df.parse((String)objects.get(2))));
                                            //cache reply
                                            map_facility_history.put(sourceAddress, new RequestHistory(messageNo, reply));
                                            //return marshalled reply
                                            return reply;
                                        }catch(ParseException e){
                                            return marshaller.toMessage("Error parsing arguments");
                                        }   
                                        
				}
                    }
                });
                functionMap.put("changeBooking", new SkeletonFunctionInterface() {
                    @Override
                    public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                        //check if request is a repeat
				if(map_facility_history.containsKey(sourceAddress) && messageNo == map_facility_history.get(sourceAddress).getRequestNo()){
					//return cached reply
					return map_facility_history.get(sourceAddress).getReplyMessage();
				//else
				}else{
					//set user of facility implementation
					setUser(sourceAddress);
					//unmarshal parameters from message
					List objects = (List) marshaller.fromMessage(data);
					//pass parameter to method implementation
					//marshal reply
                                        byte[] reply = marshaller.toMessage(ref_facility.changeBooking((Integer)objects.get(0),(Integer)objects.get(1)));
                                        //cache reply
                                        map_facility_history.put(sourceAddress, new RequestHistory(messageNo, reply));
                                        //return marshalled reply
                                        return reply;
                                        
				}
                    }
                });
                functionMap.put("login", new SkeletonFunctionInterface(){
			@Override
			public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
				//unmarshal parameters from message
				List objects = (List) marshaller.fromMessage(data);
				//pass parameter to method implementation
				String user = (String)objects.get(0);
				boolean result = ref_facility.login(user, (String)objects.get(1));
				//if login successful
				if(result){
					//register user
					ref_facility.registerUser(sourceAddress, user);
                                        System.out.println("Registered");
					//clear cache for previous user using the same ip
					clearCache(sourceAddress);
                                        System.out.println("Cache Cleared");
				}
				//marshal reply
                                System.out.println("Sending Reply");
				return marshaller.toMessage(result);
			}
		});
                functionMap.put("viewBooking", new SkeletonFunctionInterface() {
                    @Override
                    public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                        //set user of flight implementation
                        setUser(sourceAddress);
                        //unmarshal parameters from message
                        int iD = (Integer)marshaller.fromMessage(data);
                        //pass parameter to method implementation
                        //marshal reply
                        return marshaller.toMessage(ref_facility.viewBooking(iD)); 
                   }
                });
                functionMap.put("cancelBooking", new SkeletonFunctionInterface() {
                    @Override
                    public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                        //set user of flight implementation
                        setUser(sourceAddress);
                        //unmarshal parameters from message
                        int iD = (Integer)marshaller.fromMessage(data);
                        //pass parameter to method implementation
                        //marshal reply
                        return marshaller.toMessage(ref_facility.cancelBooking(iD)); 
                   }
                });
                functionMap.put("monitorFacility", new SkeletonFunctionInterface(){
			@Override
			public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
				//check if request is a repeat
				if(map_facility_history.containsKey(sourceAddress) && messageNo == map_facility_history.get(sourceAddress).getRequestNo()){
					//return cached reply
					return map_facility_history.get(sourceAddress).getReplyMessage();
				}else{
					//unmarshal parameters from message
					List objects = (List) marshaller.fromMessage(data);
					//pass parameter to method implementation
					boolean result =  ref_facility.monitorFacility((Integer)objects.get(0), (Long)objects.get(1));
					try {
						//if monitor request accepted
						if(result)
                                                    //add monitor stub to flight implementation
                                                    ref_facility.addMonitor(new BookingMonitorClient(new DatagramSocket(), sourceAddress, sourcePort));
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//marshal reply
					byte[] reply = marshaller.toMessage(result);
					//cache reply
					map_facility_history.put(sourceAddress, new RequestHistory(messageNo, reply));
					//return marshalled reply
					return reply;
				}
			}
		});
		return functionMap;
	}
        
        private void setUser(InetAddress address){
            ref_facility.setUser(address);
        }
        private void clearCache(InetAddress address){
            map_facility_history.remove(address);
            map_cancel_facility_history.remove(address);
            map_monitor_facility_history.remove(address);
        }

    String getName() {
        return this.name;
    }
        

}
