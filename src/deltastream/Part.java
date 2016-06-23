/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Arrays;
import java.util.Iterator;
    /** 
    * <p>This class represents all the different kinds of transmissions made between 
    * the nodes in the deltastream.</p>
    * 
    * <p>Each part in turn consists of 1 or more chunks that fit in a datagram and makes 
    * up the part.</p>
    * 
    * @author petter
    */

public class Part {
    int size;
    long broadcastId;
    protected byte type = 0;//invalid type
    byte[] data;
    boolean complete;
    int partNr;
    LinkedList chunkList;
    long timeStamp;
    
    public int getPartNr() {
        return partNr;
    }

    public void setPartNr(int partNr) {
        this.partNr = partNr;
    }
   

    public LinkedList getChunkList() {
        return chunkList;
    }
    /**
     *
     * @return
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     *
     * @param complete
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     *
     * @return
     */
    public byte getType() {
        return type;
    }
    /**
     * Get the data stored in this part as a byte array. If the part is not complete
     * it returns null.
     * @return 
     */
    public byte[] getData() {
        return complete? data : null;
    }
    /**
     * Make an object with
     * @param data
     * 
     * This method is for the original transmittor to create new parts.
     */
    Part(byte[] data, int type){
        this.data = data;
        ProduceChunks();
        
    }   
    
    /**
     * Make an object without data.
     */
    Part(){
    }
    
    boolean ProduceChunks(){
        if(data == null)
            return false; 
        
        ArrayList<byte[]> arrList = new ArrayList();
        
        
        for(int i = 0; i<data.length;){
            int from = i;
            int to = i + Math.min(ConfigData.chunkMaxSize,(data.length-i));
            i = to;
            byte[] chunkData = Arrays.copyOfRange(data, from,to );
            arrList.add(chunkData);
        }
        int chunkNr = 0;
        chunkList = new LinkedList();
        
        for(Iterator<byte[]> arrIter = arrList.iterator();arrIter.hasNext();){
           byte[] chunkData = arrIter.next();
           Chunk tmpPtr = new Chunk(chunkData,chunkNr++,arrList.size(),partNr);
           chunkList.add(tmpPtr);
           
        }
        return true;
    }
    
    public static class PartTypes{
        /**
         * This constant represents the Part type which contains a part of the 
         * orgininal stream transmission.
         */
        public static final byte TRANSMISSION_STREAMDATA = 1;
        public static final byte PUSH_REQUEST = 2;
        public static final byte PUSH_RQ_ANSWER = 3;
        public static final byte NODELIST_REQUEST = 4;
        public static final byte NODELIST_RQ_ANSWER = 5;
        public static final byte PULL_REQUST = 6;
        public static final byte PULL_RQ_ANSWER = 7;
        public static final byte ABORT_TRANSFER = 8;
        public static final byte TRANSMISSION_METADATA = (byte) 255;
        public static final byte TRANSMISSION_PUBKEY = (byte) 254;
        public static final byte TRANSMISSION_ALTRUISTIC_NODES = (byte) 253;
        public static final byte TRANSMISSION_NODELIST = (byte) 252;
    }
}
