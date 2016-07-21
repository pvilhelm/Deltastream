/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author petter
 * 
 * This class represents a datagram sized "chunk" of a part. 
 * <p>
 * Each Part is made up off atleast one chunk. Each chunk includes the part 
 * number, the chunk number in the chunk sequence 
 */
public class Chunk{
    int partNr; 
    short chunkNr;
    short totalNrOfChunks;
    byte [] chunkData; 
    short portNr; 
    
    Chunk(byte[] data, short chunkNr,short totalNrOfChunks, int partNr, short portNr){
        this.partNr = partNr;
        this.chunkData = data;
        this.chunkNr = chunkNr; 
        this.totalNrOfChunks = totalNrOfChunks; 
        this.portNr = portNr;
    }
    
    DatagramPacket toDatagram(InetAddress address, int port){
     
        int length = chunkData.length+Part.HeaderSizes.STREAMDATA_CHUNK_HEADERSIZE;

        ByteBuffer byteBufferStream = ByteBuffer.allocate(length);
        byteBufferStream.putShort(portNr);
        byteBufferStream.putInt(partNr);
        byteBufferStream.putShort(chunkNr);
        byteBufferStream.putShort(totalNrOfChunks);
        byteBufferStream.put(chunkData);
        byte[] datagramByteBuffer = byteBufferStream.array();
        DatagramPacket datagram = new DatagramPacket(datagramByteBuffer,datagramByteBuffer.length,address,port);
        return datagram;
        
    }
    /**
    *Sorts the chunks according to their chunk number. 
    */
    public static class ChunkComparator implements Comparator<Chunk>{
        @Override
        public int compare(Chunk a, Chunk b){
            return a.chunkNr < b.chunkNr ? -1 : a.chunkNr == b.chunkNr ? 0 : 1;
        }
    }
    
    public static ArrayList GetMissingChunks(LinkedList<Chunk> chunkList) {
        
        chunkList.sort(new ChunkComparator());
        
        //check if chunk numbers are unique and all numbers are there
        ArrayList<Integer> missingList = new ArrayList(0); 
        
        int firstTotalNrOfChunks = chunkList.getFirst().totalNrOfChunks;
        ArrayList<Integer> presentChunkNrs = new ArrayList(firstTotalNrOfChunks);
        
        for(Iterator<Chunk> iter = chunkList.iterator();iter.hasNext();){
            Chunk chunk = iter.next();
            
            int totalNrOfChunks = chunk.totalNrOfChunks;
            if(firstTotalNrOfChunks!=totalNrOfChunks){//Inconsistent tot nr of chunks
                return null; 
            }  
            presentChunkNrs.add((int)chunk.chunkNr);
        }
        
        //check which chunks are acctually there
        for(int i = 0;i<firstTotalNrOfChunks;i++){
            if(!presentChunkNrs.contains(i)){
                missingList.add(i);
            }            
        }
        
        return missingList; 
    }
}
