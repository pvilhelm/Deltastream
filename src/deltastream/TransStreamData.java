/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
 
import java.util.Date;

/**
 *This class represents a part of the original stream
 * @author petter
 */
public class TransStreamData extends Part{
    
    
    TransStreamData(byte [] data, int partNr, long transmissionID){
        type = Part.PartTypes.TRANSMISSION_STREAMDATA;
        this.broadcastId = transmissionID;
        this.data = data;
        timeStamp = new Date().getTime();
        ProduceChunks();
    }
}
