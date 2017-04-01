/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Entity.BookingMonitorInterface;
import Entity.Stub;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author pratyumjagannath
 */
public class BookingMonitorClient extends Stub implements BookingMonitorInterface {
    private final String name = "BookingMonitor";

    public BookingMonitorClient(DatagramSocket socket, InetAddress host, int port) {
        super(socket, host, port);
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void update(String message) {
        byte[] parameters = marshaller.toMessage(message);
        sendRequest("update", parameters, null);
    }
    
}
