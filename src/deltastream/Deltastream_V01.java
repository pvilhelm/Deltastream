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
        OriginalTransmissionInput orgInputTask = null;
        try {
            // TODO code application logic here
            orgInputTask = new OriginalTransmissionInput(3333);
        } catch (SocketException ex) {
            Logger.getLogger(Deltastream_V01.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        Thread orgInThread = new Thread(orgInputTask);
        orgInThread.start();
        
        Timer chopTimer = new Timer();
        OriginalTransmissionInputChopper chopperTask = new OriginalTransmissionInputChopper();
        chopTimer.scheduleAtFixedRate(chopperTask, new Date(), 500);
        
    }
    
}
