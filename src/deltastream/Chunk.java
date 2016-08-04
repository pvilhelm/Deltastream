/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
    char chunkNr;
    char totalNrOfChunks;
    byte [] chunkData; 
    char portNr; 
    long timeCreated; 
  
    Chunk(byte[] data, char chunkNr,char totalNrOfChunks, int partNr, char portNr){
        this.partNr = partNr;
        this.chunkData = data;
        this.chunkNr = chunkNr; 
        this.totalNrOfChunks = totalNrOfChunks; 
        this.portNr = portNr;
        this.timeCreated = new Date().getTime();
    }
    
    Chunk(DatagramPacket dgP){
        this.timeCreated = new Date().getTime();
        this.chunkData = dgP.getData();
        this.portNr = (char)dgP.getPort();
        
     }
    
    DatagramPacket toDatagram(InetAddress address, int port){
     
        int length = chunkData.length+Part.HeaderSizes.TRANSMISSION_STREAMDATA_DATA_HEADERSIZE;

        ByteBuffer byteBufferStream = ByteBuffer.allocate(length);
        byteBufferStream.putChar(portNr);
        byteBufferStream.putInt(partNr);
        byteBufferStream.putChar(chunkNr);
        byteBufferStream.putChar(totalNrOfChunks);
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
 
    /**
     * Get the chunks that are missing in a chunk sequence as an arraylist of Integer objects. 
     * <p>
     * Takes a linked list of chunks as argument. Returns null if the total 
     * number of chunks are inconsistent in the chunks in the list or if the 
     * part nr. differs. 
     * 
     * 
     * @param chunkList
     * @return ArrayList, null
     */
    
    public static ArrayList GetMissingChunks(LinkedList<Chunk> chunkList) {
        
        chunkList.sort(new ChunkComparator());
        
        //check if chunk numbers are unique and all numbers are there
        ArrayList<Integer> missingList = new ArrayList(0); 
        
        int firstTotalNrOfChunks = chunkList.getFirst().totalNrOfChunks;
        int firstPartNr = chunkList.getFirst().partNr;
        ArrayList<Integer> presentChunkNrs = new ArrayList(firstTotalNrOfChunks);
        
        for(Iterator<Chunk> iter = chunkList.iterator();iter.hasNext();){
            Chunk chunk = iter.next();
            int partNr = chunk.partNr;
            int totalNrOfChunks = chunk.totalNrOfChunks;
            if(firstTotalNrOfChunks!=totalNrOfChunks || firstPartNr != partNr){//Inconsistent tot nr of chunks or partNr
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
