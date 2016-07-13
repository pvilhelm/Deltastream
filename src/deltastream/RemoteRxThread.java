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
public class RemoteRxThread implements Runnable {
    Transmission transmission = null;
    DatagramSocket socket;
    boolean RUN = true;
    short port = 6666;
    RemoteRxThread(){
        
    }
    
    @Override
    public void run(){
        
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(RemoteRxThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(RUN){
            DatagramPacket rxUDPpkt = new DatagramPacket(new byte[1600],1600);
            try {
                socket.receive(rxUDPpkt);
            } catch (IOException ex) {
                Logger.getLogger(RemoteRxThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
