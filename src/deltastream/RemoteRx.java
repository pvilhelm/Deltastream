/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petter
 */
public class RemoteRx implements Runnable {
    Transmission transmission = null;
    DatagramSocket socket;
    boolean RUN = true;
    
    RemoteRx(Transmission transmission){
        this.transmission = transmission;
    }
    
    @Override
    public void run(){
        
        try {
            socket = new DatagramSocket(transmission.getRemoteRxPort());
        } catch (SocketException ex) {
            Logger.getLogger(RemoteRx.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(RUN){
            DatagramPacket rxUDPpkt = new DatagramPacket(new byte[1600],1600);
            try {
                socket.receive(rxUDPpkt);
            } catch (IOException ex) {
                Logger.getLogger(RemoteRx.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        socket.close();  
    }
}
