/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
    
    int localRxPort;

    public int getLocalRxPort() {
        return localRxPort;
    }

    public void setLocalRxPort(int localRxPort) {
        this.localRxPort = localRxPort;
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
    
    BlockingQueue<DatagramPacket> remoteTxQue;
    
    RemoteRx remoteRx;
    Thread RemoteRxThread;
    RemoteTx remoteTx;
    Thread RemoteTxThread;
    
    Transmission(){
        Random rand = new Random();
        transmissionId = rand.nextLong();
        startTime = new Date();
        remoteTxQue = new LinkedBlockingQueue(ConfigData.REMOTE_TX_BLOCKING_QUEUE_CAP);
    }
    
    Transmission(long transmissionId){
        this.transmissionId = transmissionId;
        startTime = new Date();
        remoteTxQue = new LinkedBlockingQueue(ConfigData.REMOTE_TX_BLOCKING_QUEUE_CAP);
    }
    
    @Override
    public String toString(){
        return "Transmission: ID "+getTransmissionId()+" Last part nr:" + getLastPartNr()+" Started: "+getStartTime().toString()+"\n"+
                "Local Rx port: " +getLocalRxPort() + " Remote Rx port: " + getRemoteRxPort()+ " Remote tx port: " + getRemoteTxPort();
                
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
        remoteRx = new RemoteRx(this);
        RemoteRxThread = new Thread(remoteRx);
        RemoteRxThread.start();
        remoteTx = new RemoteTx(this);
        RemoteTxThread = new Thread(remoteTx);
        RemoteTxThread.start(); 
    }
}
