/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Entity.Booking;
import Entity.FacilityDetails;
import Entity.FacilityImplementation;
import Entity.FacilityInterface;
import Marshaller.SimSocket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author pratyumjagannath
 */
public class BookingApplicationClient {
    
    public static int readInt(Scanner scan, String message){
        int value;
        while(true){
            System.out.println(message);
            try{
                value = Integer.parseInt(scan.nextLine());
                return value;
            }catch(NumberFormatException e){
                System.out.println("Invalid Input. Please try again!");
            }
        }
    }
    
    public static long readLong(Scanner scan, String message){
        long value;
        while(true){
            System.out.println(message);
            try{
                value = Integer.parseInt(scan.nextLine());
                return value;
            }catch(NumberFormatException e){
                System.out.println("Invalid Input. Please try again!");
            }
        }
    }

    //Login Function
    public static void login(FacilityInterface facility){
            Scanner scan = new Scanner(System.in);
            while(true){
                    //Get username
                    System.out.println("Please enter username.");
                    String user = scan.nextLine();
                    //Get password
                    System.out.println("Please enter password.");
                    String password = scan.nextLine();

                    //Send request to remote object
                    if(facility.login(user, password)){
                            //if login succeed, print success message
                            System.out.println("Login Successful");
                            break;
                    }else{
                            //else print fail message and prompt for login fields again
                            System.out.println("Login Failed. Please try again.");
                    }
            }
    }
    
    public static void main(String[] args){
            //if no parameter provided
            if(args.length < 1){
                    //initialize parameter to default
                    args = new String[4];
                    args[0] = "127.0.0.1";
                    //args[0] = "155.69.144.89";
                    args[1] = "5000";
                    args[2] = "0";
                    args[3] = "2000";
            }
            //convert string to ip address
            String[] addressBytes = args[0].split("\\.");
            byte[] addr = new byte[addressBytes.length];
            for(int i=0; i<addressBytes.length; i++){
                    addr[i] = (byte)Integer.parseInt(addressBytes[i]);
            }
            //convert string to port number
            int port = Integer.parseInt(args[1]);
            //convert string to packet loss rate
            double lossRate = Double.parseDouble(args[2]);
            //convert string to network delay
            int networkDelay = Integer.parseInt(args[3]);
            FacilityInterface facility;
            try {
                //create stimulated socket
                DatagramSocket socket = new SimSocket(lossRate, networkDelay);
                //create flight stub
                facility = new Booking(socket, InetAddress.getByAddress(addr).getByAddress(addr), port);
                //request user to login
                login(facility);
                //enter program loop
                while(true){
                        //print menu
                        System.out.println("Select service:");
                        System.out.println("1. Finds facilities given location and space");
                        System.out.println("2. Get facilitiy details.");
                        System.out.println("3. Facility booking.");
                        System.out.println("4. Monitor Facility.");
                        System.out.println("5. Check Bookings.");
                        System.out.println("6. Cancel Bookings");
                        System.out.println("7. Exit");
                        Scanner scan = new Scanner(System.in);
                        //prompt user for choice
                        int choice = readInt(scan, "");
                        switch(choice){
                        case 1:
                        {
                                //prompt user for source location
                                System.out.println("Enter location on Campus:");
                                String location = scan.nextLine();
                                //prompt user for destination location
                                int space = readInt(scan, "Enter maximum number of occupants:");
                                //send request to remote object
                                List<Integer> fIDs = facility.getID(location, space);
                                //if empty list is returned
                                if(fIDs.isEmpty()){
                                        //display no flights found
                                        System.out.println("No matching Facilities found.");
                                }else{
                                        //else, display list of flight IDs
                                        for(int fID:fIDs){
                                                System.out.println(fID);
                                        }
                                }
                        }
                                break;
                        case 2:
                        {
                                //prompt user for facility ID
                                int fID = readInt(scan, "Enter Facility ID.\n");
                                //send request to remote object
                                FacilityDetails facilities = facility.getFacilities(fID);
                                //if no facility details
                                if(facilities==null){
                                        //display facility ID not found
                                        System.out.println("Facility ID not found.");
                                }else{
                                        //else display facility details
                                        System.out.println("Name: " + String.format("%s", facilities.getString_name()));
                                        System.out.println("Location: " + String.format("%s", facilities.getString_location()));
                                        System.out.println("Space: " + String.format("%d", facilities.getInt_space()));
                                        System.out.println("Timings Booked: " + String.format("%s", facilities.isIs_Available()));
                                }
                        }
                                break;
                        case 3:
                        {
                                DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"); 
                                //prompt user for facility ID
                                int fID = readInt(scan, "Enter facility ID.\n");
                                //prompt user for number of seats
                                Date from = null,to = null;
                                do{
                                    try{
                                    System.out.println("Enter the from time (MM/dd/yyyy hh:mm:ss)");
                                    from  = df.parse(scan.nextLine());
                                    System.out.println("Enter the from time (MM/dd/yyyy hh:mm:ss)");
                                    to = df.parse(scan.nextLine());
                                    break;
                                    }catch( ParseException e){
                                        System.out.println("Please enter in the right format");
                                    }
                                }while(from ==null || to==null);
                                //else, send request to remote object
                                int result = facility.bookFacility(fID, from, to);
                                if(result >= 0){
                                    System.out.println(String.format("Booking Successful: Booking ID : %d",result));
                                } else{
                                    System.out.println("Booking UnSuccessful");
                                }
                        }
                                break;
                        case 4:
                        {
                                //prompt user for Facility ID
                                int fID = readInt(scan, "Enter facility ID.\n");
                                long duration = readLong(scan, "Enter duration in ms.\n");
                                //send request to remote object
                                //if request fail
                                if(!facility.monitorFacility(fID, duration)){
                                        //inform user flight ID not found
                                        System.out.println("Flight ID not found.");
                                }
                        }
                                break;
                        case 5:
                        {
                                //prompt user for booking ID
                                int fID = readInt(scan, "Enter booking ID.\n");
                                //send request to remote object
                                //display number of tickets
                                System.out.println(facility.viewBooking(fID));
                        }
                                break;
                        case 6:
                        {
//                                //prompt user for flight ID
//                                int fID = readInt(scan, "Enter flight ID.\n");
//                                int tickets = readInt(scan, "Enter number of tickets.\n");
//                                //send request to remote object
//                                //display cancellation success result
//                                if(facility.cancelTickets(fID, tickets))
//                                        System.out.println("Cancellation sucessful.");
//                                else
//                                        System.out.println("Cancellation failed.");
                        }
                                break;
                        case 7:
                                //terminate system
                                System.out.println("System terminating...");
                                System.exit(0);
                                break;
                        }
                }
        } catch (UnknownHostException | SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        //*/
    }
}
