/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.SocketException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petter
 */
public class Transmission {
    boolean localTransmission = false;

    public boolean isLocalTransmission() {
        return localTransmission;
    }
    public void setLocalTransmission(boolean localTransmission) {
        this.localTransmission = localTransmission;
    }
    
    long transmissionId; 
    public long getTransmissionId() {
        return transmissionId;
    }
    
    int lastPartNr = 0;
    public int getLastPartNr() {
        return lastPartNr;
    }
    
    Date startTime;
    public Date getStartTime() {
        return startTime;
    }
    
    OriginalTransmissionInput orgInputTask  = null;
    Thread orgInThread; 
    OriginalTransmissionInputChopper chopperTask;
    
    Transmission(boolean isLocal){
        Random rand = new Random();
        transmissionId = rand.nextLong();
        startTime = new Date();
        
         
        try {
            // TODO code application logic here
            orgInputTask = new OriginalTransmissionInput(3333);
        } catch (SocketException ex) {
            Logger.getLogger(Deltastream_V01.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        orgInThread = new Thread(orgInputTask);
        orgInThread.start();
        
        Timer chopTimer = new Timer();
        chopperTask = new OriginalTransmissionInputChopper();
        chopTimer.scheduleAtFixedRate(chopperTask, new Date(), 500);
    }
    
    
    
}
