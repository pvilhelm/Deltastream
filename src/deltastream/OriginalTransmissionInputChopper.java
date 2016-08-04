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
            System.err.println(new Date().toString()+": Incoming original transmission buffer too big. Discarding all ...");
            return;
        }
        int sum=0;
        for(Iterator<DatagramWrapper> iter =  datagramBuffer.iterator();iter.hasNext();){
            DatagramWrapper dgW = iter.next();
            int l = dgW.datagramData.length;
            if(l>ConfigData.INCOMING_ORIGINAL_UDP_MAX_SIZE){
               System.err.println(new Date().toString()+": Incoming original transmission UDP package too big. Deleting it ...");
               datagramBuffer.remove(dgW);
            }
            else
                sum+=l;
        }
        
        ByteBuffer dgrStream = ByteBuffer.allocate(sum+Part.HeaderSizes.TRANSMISSION_STREAMDATA_HEADERSIZE+n*(Part.HeaderSizes.TRANSMISSION_STREAMDATA_DATA_HEADERSIZE));//TODO do header size nice
        dgrStream.putLong(OriginalTransmissionInput.transmissionID);
        dgrStream.put(Part.PartTypes.TRANSMISSION_STREAMDATA);
        dgrStream.putLong(new Date().getTime());
        
        for(Iterator<DatagramWrapper> iter = datagramBuffer.iterator();iter.hasNext();){
            DatagramWrapper tmp = iter.next();
            int msSinceHour = (int)(tmp.timestamp%(60*60*1000));
            dgrStream.putInt(msSinceHour);
            dgrStream.putChar((char)tmp.port);
            dgrStream.putChar((char)tmp.datagramData.length);
            if((char)tmp.datagramData.length>0)
                dgrStream.put(tmp.datagramData);
        }
        TransStreamData newPart = new TransStreamData(dgrStream.array(),partNr,OriginalTransmissionInput.transmissionID);
        System.out.println(new Date().toString()+": Gjorde del med storlek "+ dgrStream.array().length/1024.0 + " kb");
    }
}
 