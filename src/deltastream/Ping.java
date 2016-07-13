/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author petter
 */
public class Ping extends Part{
    byte seqNr;
    public byte getSeqNr() {
        return seqNr;
    }
    public void setSeqNr(byte seqNr) {
        this.seqNr = seqNr;
    }

    byte totSeqs;
    public byte getTotSeqs() {
        return totSeqs;
    }
    public void setTotSeqs(byte totSeqs) {
        this.totSeqs = totSeqs;
    }
    

    @Override
    public String toString() {
        return "Ping{" + "seqNr=" + seqNr + ", totSeqs=" + totSeqs + '}';
    }
    
    float CalculatePing(List<Ping> listOfPings){
        if(listOfPings == null | listOfPings.isEmpty())
            return Float.NaN;
        
        Collections.sort(listOfPings,new PingComparator());
        float sum = 0;
        long time = 0;
        long diff = 0;
        for(Iterator<Ping> iter = listOfPings.iterator();iter.hasNext();){
            Ping tmp = iter.next();
            if(time == 0){
                time = tmp.timeStamp;
                diff = 0;
            }
            else{
                diff = tmp.timeStamp - time;
                sum+=diff;
                time = tmp.timeStamp;
            }
        }
        return sum/(listOfPings.size()-1);
    }
    
    Ping(byte [] data, int partNr, long transmissionID){
        type = Part.PartTypes.PING;
        this.broadcastId = transmissionID;
        timeStamp = new Date().getTime();
        this.partNr = partNr;
        this.data = data;
                
        ProduceChunks();
    }
    
    Ping(int partNr, long transmissionID, byte seqNr, byte totSeqs){
        timeStamp = new Date().getTime();
        type = Part.PartTypes.PING;
        this.broadcastId = transmissionID;
        this.partNr = partNr;
        this.seqNr = seqNr;
        this.totSeqs = totSeqs;
        ByteBuffer dataBuffer = ByteBuffer.allocate(Part.HeaderSizes.PING_HEADERSIZE);
        dataBuffer.put(seqNr);
        dataBuffer.put(totSeqs);
        this.data = dataBuffer.array();
        ProduceChunks();
        
    }
    
    class PingComparator implements Comparator<Ping> {

        @Override
        public int compare(Ping o1, Ping o2) {
            if(o1.timeStamp > o2.timeStamp) {
                return 1;
            }
            if(o1.timeStamp < o2.timeStamp) {
                return -1;
            }
            return 0;
        }

    }
}
