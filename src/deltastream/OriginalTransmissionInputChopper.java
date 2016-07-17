/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

/**
 *This class implements a runnable which chops 
 * @author petter
 */
public class OriginalTransmissionInputChopper extends TimerTask{
    int partNr = 0;
    @Override
    public void run(){
        
        partNr++;//first number is one
        
        LinkedList datagramBuffer = OriginalTransmissionInput.getIncDataBuffer();
        
        
        //count size of buffer
        int n = datagramBuffer.size();
        if(n>65535){
            System.out.println(new Date().toString()+": Incoming original transmission buffer too big");
            return;
        }
        int sum=0;
        for(Iterator<DatagramWrapper> iter =  datagramBuffer.iterator();iter.hasNext();){
            sum+=iter.next().datagramData.length;
        }
        
        ByteBuffer dgrStream = ByteBuffer.allocate(sum+Part.HeaderSizes.TRANSMISSION_STREAMDATA_HEADERSIZE+n*(Part.HeaderSizes.STREAMDATA_CHUNK_HEADERSIZE));//TODO do header size nice
        dgrStream.putLong(OriginalTransmissionInput.transmissionID);
        dgrStream.put(Part.PartTypes.TRANSMISSION_STREAMDATA);
        dgrStream.putLong(new Date().getTime());
        
        for(Iterator<DatagramWrapper> iter = datagramBuffer.iterator();iter.hasNext();){
            DatagramWrapper tmp = iter.next();
            dgrStream.putShort((short)tmp.datagramData.length);
            if((short)tmp.datagramData.length>0)
                dgrStream.put(tmp.datagramData);
        }
        TransStreamData newPart = new TransStreamData(dgrStream.array(),partNr,OriginalTransmissionInput.transmissionID);
        System.out.println(new Date().toString()+": Gjorde del med storlek "+dgrStream.array().length/1024.0 + " kb");
    }
}
 