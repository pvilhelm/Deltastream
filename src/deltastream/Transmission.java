/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petter
 */
public class Transmission {
    HashMap<InetAddress,Node> hashMapNodes;
    Timer chopTimer;
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
    
    int remoteTxPort;

    public int getRemoteTxPort() {
        return remoteTxPort;
    }

    public void setRemoteTxPort(int remoteTxPort) {
        this.remoteTxPort = remoteTxPort;
    }

    int remoteRxPort;
    
    public int getRemoteRxPort() {
        return remoteRxPort;
    }

    public void setRemoteRxPort(int remoteRxPort) {
        this.remoteRxPort = remoteRxPort;
    }
    
    OriginalTransmissionInput orgInputTask  = null;
    Thread orgInThread; 
    OriginalTransmissionInputChopper chopperTask;
    
    RemoteRx remoteRx;
    Thread RemoteRxThread;
    RemoteTx remoteTx;
    Thread RemoteTxThread;
    
    Transmission(){
        Random rand = new Random();
        transmissionId = rand.nextLong();
        startTime = new Date();
    }
    
    Transmission(long transmissionId){
        this.transmissionId = transmissionId;
        startTime = new Date();
    }
    
    void StartLocalTransmissionRx(){
        setLocalTransmission(true);
        try {
            // TODO code application logic here
            orgInputTask = new OriginalTransmissionInput(3333);
        } catch (SocketException ex) {
            Logger.getLogger(Deltastream_V01.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        orgInThread = new Thread(orgInputTask);
        orgInThread.start();
        chopTimer = new Timer();
        chopperTask = new OriginalTransmissionInputChopper();
        chopTimer.scheduleAtFixedRate(chopperTask, new Date(), 500);
    }
    
    void StopLocalTransmissionRx(){
        if(chopTimer != null)
            chopTimer.cancel();
        try{
            orgInputTask.orgInputSocket.close();
        }
        catch(Exception ee){
            ;
        }
    }
    /**
     * Starts the receiving and transmitting of parts with the deltastream. 
     */
    void StartRemoteRxTx(){
        
    }
}
