/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;
import java.net.*;
import java.nio.ByteBuffer;
/**
 *
 * @author petter
 * 
 * This class represents a datagram sized "chunk" of a part. 
 * <p>
 * Each Part is made up off atleast one chunk. Each chunk includes the part 
 * number, the chunk number in the chunk sequence 
 */
public class Chunk {
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
    
}
