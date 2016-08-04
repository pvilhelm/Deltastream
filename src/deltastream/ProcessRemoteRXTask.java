/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *This class implements a machine to handle the deltastream logic. 
 * <p>It's gets 
 * incoming parts from other nodes, pocesses them, tracks exchanges 
 * of parts etc.
 * * @author petter
 */
public class ProcessRemoteRXTask implements Runnable {
    Transmission transmission;
    boolean RUN = true; 

    public boolean isRUN() {
        return RUN;
    }

    public void setRUN(boolean RUN) {
        this.RUN = RUN;
    }
    
    ProcessRemoteRXTask(Transmission transmission){
        this.transmission = transmission;
    }
    
    @Override
    public void run(){
        while(RUN){
            BlockingQueue<DatagramWrapper> remoteRxQue = transmission.remoteRxQue;
            
            DatagramWrapper dgW;
            try {
                dgW = remoteRxQue.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessRemoteRXTask.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            
            byte[] key = Node.GetKey(dgW.dgPkt);
            if(transmission.nodeHashMap.containsKey(key)){
                //TODO do processing of existing node    
            }
            else{
                //TODO do processing of new node
                Node newNode = new Node(dgW.dgPkt);
                transmission.nodeHashMap.put(key, newNode);
            }
            transmission.nodeHashMap.get(key).ProcessDatagram(dgW);
        }
    }
}
