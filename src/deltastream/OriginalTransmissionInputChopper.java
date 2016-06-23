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
        int sum=0;
        for(Iterator<byte[]> iter =  datagramBuffer.iterator();iter.hasNext();){
            sum+=iter.next().length;
        }
        
        ByteBuffer dgrStream = ByteBuffer.allocate(sum+8+4+1+n*4);//TODO do header size nice
        dgrStream.putLong(OriginalTransmissionInput.transmissionID);
        dgrStream.putInt(partNr);
        dgrStream.put(Part.PartTypes.TRANSMISSION_STREAMDATA);
        
        for(Iterator<byte[]> iter = datagramBuffer.iterator();iter.hasNext();){
            byte[] tmp = iter.next();
            dgrStream.putInt(tmp.length);
            if(tmp.length>0)
                dgrStream.put(tmp);
        }
        TransStreamData newPart = new TransStreamData(dgrStream.array(),partNr,OriginalTransmissionInput.transmissionID);
        System.out.println(new Date().toString()+": Gjorde del med storlek "+dgrStream.array().length/1024.0 + " kb");
    }
}
