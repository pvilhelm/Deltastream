/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
 
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *This class represents a part of the original stream
 * <p>
 * Each part consists transmission number, part number, timestamps and zero or more datagrams and timestamps and length for each of those datagrams. 
 * @author petter
 */
public class TransStreamData extends Part{
    
    
    TransStreamData(byte [] data, int partNr, long transmissionID){
        type = Part.PartTypes.TRANSMISSION_STREAMDATA;
        this.broadcastId = transmissionID;
        timeStamp = new Date().getTime();
        this.partNr = partNr;
        this.data = data;
                
        ProduceChunks();
    }
    
    
}
