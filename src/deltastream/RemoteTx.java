/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

/**
 *
 * @author petter
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petter
 */
public class RemoteTx implements Runnable {
    Transmission transmission = null;
    DatagramSocket socket;
    boolean RUN = true;

    public boolean isRUN() {
        return RUN;
    }

    public void setRUN(boolean RUN) {
        this.RUN = RUN;
    }
    
     
    
    RemoteTx(Transmission transmission){
        this.transmission = transmission;
        
    }
    
    @Override
    public void run(){
        
        try {
            socket = new DatagramSocket(transmission.getRemoteTxPort());
        } catch (SocketException ex) {
            Logger.getLogger(RemoteRx.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(RUN){
            DatagramPacket txUDPpkt;
            try {
                txUDPpkt = transmission.remoteTxQue.take().dgPkt;
            } catch (InterruptedException ex) {               
                Logger.getLogger(RemoteTx.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            try {
                socket.send(txUDPpkt);
            } catch (IOException ex) {
                Logger.getLogger(RemoteRx.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        socket.close();
    }
}
