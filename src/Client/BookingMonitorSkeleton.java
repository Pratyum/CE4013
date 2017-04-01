/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Entity.Skeleton;
import Entity.SkeletonFunctionInterface;
import Marshaller.DataMarshaller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 *
 * @author pratyumjagannath
 */
public class BookingMonitorSkeleton extends Skeleton {
    //Socket to send/recieve messages
    private DatagramSocket socket;
    
    private static String name = "BookingMonitor";
    
    private BookingMonitor bookingMonitor;
    
    public BookingMonitorSkeleton(DatagramSocket socket , BookingMonitor bookingMonitor){
        this.socket = socket;
        this.bookingMonitor = bookingMonitor;
        marshaller = new DataMarshaller();
        //
        
        functionMap = new HashMap<>();
        
        functionMap.put("update", new SkeletonFunctionInterface() {
            @Override
            public byte[] resolve(int messageNo, InetAddress sourceAddress, int sourcePort, byte[] data) {
                    //unmarshall parameter from message
                    String message = (String)marshaller.fromMessage(data);
                    //pass parameters to method implementation
                    bookingMonitor.update(message);
                    //return null message
                    return marshaller.nullMessage();
            }
        });
    }
    
    public BookingMonitorSkeleton(int port, BookingMonitor bookingMonitor) throws SocketException{
        this(new DatagramSocket(port),bookingMonitor);
    }
    	
	//listen until monitor period has ended
	public void listen(long msec) throws IOException{
		//records end time
		long endTime = System.currentTimeMillis() + msec;
		//set time out to end time
		socket.setSoTimeout((int) msec);
		//keep listen for messages
		while(true){
			try{
				//wait for incoming message
				listen();
				//if monitor period is not over
				if(System.currentTimeMillis() < endTime){
					//reset time out to remaining monitor time
					socket.setSoTimeout((int)(endTime-System.currentTimeMillis()));
				}else{
					break;
				}
			}catch(SocketTimeoutException e){
				//if time out, stop listening
				break;
			}
		}
	}
	
	//waits for incoming message and forwards them to the respective object
	public void listen() throws IOException{
		//buffer to receive message
		byte[] buffer = new byte[65536];
		//create receive packet
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        //wait for incoming packet
        socket.receive(incoming);
        //forwards message to processing method
        byte[] result = processMessage(incoming);
        //if there is reply message
        if(result!=null){
        	//send reply message back to client
        	DatagramPacket packet = new DatagramPacket(result , result.length , incoming.getAddress() , incoming.getPort());
        	socket.send(packet);
        }
	}
	
	public String getName(){
		return name;
	}
    
}
