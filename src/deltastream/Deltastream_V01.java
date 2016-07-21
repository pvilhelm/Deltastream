/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.SocketException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petter
 */
public class Deltastream_V01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Starting transmission ...");
        Transmission localTransmission = new Transmission();
        localTransmission.setLocalRxPort(3333);
        localTransmission.setRemoteRxPort(4444);
        localTransmission.setRemoteTxPort(5555);
        
        localTransmission.StartLocalTransmissionRx();
        localTransmission.StartRemoteRxTx();
        System.out.println(localTransmission);
        
    }
    
}
