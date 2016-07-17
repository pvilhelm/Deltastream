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
import java.util.Date;
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
    Part(byte[] data){
        this.data = data;
        ProduceChunks();
        
    }   
    
    /**
     * Make an object without data.
     */
    Part(){
    }
    
    /**
     * Produces chunks when a part has it's data.
     * @return 
     */
    
    
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
        short chunkNr = 0;
        chunkList = new LinkedList();
        
        for(Iterator<byte[]> arrIter = arrList.iterator();arrIter.hasNext();){
           byte[] chunkData = arrIter.next();
           Chunk tmpPtr = new Chunk(chunkData,chunkNr++,(short)arrList.size(),partNr, ConfigData.remotePortRx);
           chunkList.add(tmpPtr);
           
        }
        return true;
    }
    
    public static class PartTypes{
        /**
         * This constant represents the Part type which contains a part of the 
         * orgininal stream transmission.
         */
        public static final byte MULTI_PART_PART = 0;
        public static final byte TRANSMISSION_STREAMDATA = 1;
        public static final byte PUSH_REQUEST = 2;
        public static final byte PUSH_RQ_ANSWER = 3;
        public static final byte NODELIST_REQUEST = 4;
        public static final byte NODELIST_RQ_ANSWER = 5;
        public static final byte PULL_REQUST = 6;
        public static final byte PULL_RQ_ANSWER = 7;
        public static final byte ABORT_TRANSFER = 8;
        public static final byte PARTLIST_REQUEST = 9;
        public static final byte PARTLIST_RQ_ANSWER = 10;
        public static final byte RETRANSMIT_REQUEST = 11;
        public static final byte RETRANSMIT_RQ_ANSWER = 12;
        public static final byte CHANGE_PORT = 13;
        public static final byte PING = 14;
        public static final byte PUBLIC_KEY_EXCHANGE = 15;
        public static final byte IMPLEMITATION_DEFINED = 127;
        public static final byte TRANSMISSION_METADATA = (byte) 255;
        public static final byte TRANSMISSION_PUBKEY = (byte) 254;
        public static final byte TRANSMISSION_ALTRUISTIC_NODES = (byte) 253;
        public static final byte TRANSMISSION_NODELIST = (byte) 252;
    }
    
    public static class HeaderSizes{
        public static final int BYTE = 1;
        public static final int SHORT = 2;
        public static final int INT = 4;
        public static final int FLOAT = 4;
        public static final int LONG = 8;
        public static final int DOUBLE = 8;
        public static final int TRANSMISSION_STREAMDATA_HEADERSIZE = LONG+SHORT;
        public static final int STREAMDATA_CHUNK_HEADERSIZE =SHORT+INT+SHORT*2;
        public static final int PING_HEADERSIZE = BYTE*2;
    }
}
