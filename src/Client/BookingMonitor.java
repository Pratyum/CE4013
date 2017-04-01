/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Entity.BookingMonitorInterface;

/**
 *
 * @author pratyumjagannath
 */
public class BookingMonitor implements BookingMonitorInterface {
    private int id;
    private String message; 
    
    public BookingMonitor(int id){
     this.id = id;
     this.message = "";
    }
    
    //update the Booking status of the other clients while monitoring
    @Override
    public void update(String message){
        if(!this.message.equals(message)){
            System.out.println(message);
        }
    }
    
}
