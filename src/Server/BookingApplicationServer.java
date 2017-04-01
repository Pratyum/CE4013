/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Entity.Facility;
import Entity.FacilityImplementation;
import java.io.IOException;

/**
 *
 * @author pratyumjagannath
 */
public class BookingApplicationServer {
    	public static void main(String[] args){
		//if no parameter provided
		if(args.length < 1){
			//initialize parameter to default
			args = new String[1];
			args[0] = "5000";
		}
		//convert string to port number
		int port = Integer.parseInt(args[0]);
		//create flight implementation
                FacilityImplementation  facilities = new FacilityImplementation();
		//add dummy data
		facilities.addfacility(new Facility(1, "LT1A", 200, "North Spine", true));
                facilities.addfacility(new Facility(2, "LT10", 100, "North Spine", true));
		facilities.addfacility(new Facility(3, "LHS-LT", 150, "South Spine", true));

		BookingSkeleton skeleton = new BookingSkeleton(facilities);
		//for testing
		//creates a thread to book/unbook a seat every 1 second
		/*
		new Thread(){
			public void run(){
				while(true){
					flights.bookFlight(1, Math.random()>0.5?1:-1);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		//*/
		try {
			//create server
			Server server = new Server(port);
			//register object on server
			server.register(skeleton, skeleton.getName());
			//listen indefinitely
                        System.out.println("Server Listening at port " + String.format("%d", port));
			while(true){
				server.listen();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
