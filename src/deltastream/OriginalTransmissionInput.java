/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author petter
 * 
 * This class takes an arbitrary input data stream and supplies it to the part 
 * making class to supplie it to the deltastream. 
 * 
 */
public final class OriginalTransmissionInput implements Runnable {
    static boolean RUN = true; 
    static long transmissionID; 
    /**
     *
     * @return
     */
    public boolean isRUN() {
        return RUN;
    }

    /**
     *
     * @param RUN
     */
    public void setRUN(boolean RUN) {
        this.RUN = RUN;
    }
    
    DatagramSocket orgInputSocket;
    static LinkedList<byte[]> linkListIncData;
    
    OriginalTransmissionInput(int port) throws SocketException{
        orgInputSocket = new DatagramSocket(port);
        linkListIncData = new LinkedList();
        orgInputSocket.setReceiveBufferSize(3000000);
        
        transmissionID = new Random().nextLong();
    }
    
    /**
     * This blocking method returns the buffered incoming UDP Datagrams and creates a new buffer. 
     * 
     * @return 
     */   
    public static synchronized LinkedList getIncDataBuffer (){
        
        LinkedList ret = linkListIncData;
        linkListIncData = new LinkedList();
        return ret;
    }
    /**
     * This blocking method adds a datagram packet to the receive buffer. 
     * @param dgPkt 
     */
    
    public synchronized void addIncDataBuffer(DatagramPacket dgPkt){
        if(linkListIncData.size()<10000)
            linkListIncData.add(Arrays.copyOfRange(dgPkt.getData(), dgPkt.getOffset(), dgPkt.getLength()));
        return;
    }
    
    /**
     * This method waits for incoming orginal data att puts it in the buffer. 
     */
    
    @Override
    public void run(){
        
        while(RUN){
            DatagramPacket lastDpkg = new DatagramPacket(new byte[2000],2000);
            try {
                orgInputSocket.receive(lastDpkg);
                //System.out.println("Tog emot datagramm"+lastDpkg.getAddress().toString());
            } catch (IOException ex) {
                Logger.getLogger(OriginalTransmissionInput.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            this.addIncDataBuffer(lastDpkg);
        }
        
    }
    
    
}
