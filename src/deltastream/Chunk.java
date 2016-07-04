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
 * This class is a datagram sized "chunk" of a time domain Part of the transmission. 
 * 
 * Each Part is made up off atleast one chunk. 
 */
public class Chunk {
    int partNr; 
    short chunkNr;
    short totalNrOfChunks;
    byte [] chunkData; 
    
    Chunk(byte[] data, short chunkNr,short totalNrOfChunks, int partNr){
        this.partNr = partNr;
        this.chunkData = data;
        this.chunkNr = chunkNr; 
        this.totalNrOfChunks = totalNrOfChunks; 
    }
    
    DatagramPacket toDatagram(InetAddress address, int port){
     
        int length = chunkData.length+4+2+2;

        ByteBuffer byteBufferStream = ByteBuffer.allocate(length);
        byteBufferStream.putInt(partNr);
        byteBufferStream.putShort(chunkNr);
        byteBufferStream.putShort(totalNrOfChunks);
        byteBufferStream.put(chunkData);
        byte[] datagramByteBuffer = byteBufferStream.array();
        DatagramPacket datagram = new DatagramPacket(datagramByteBuffer,datagramByteBuffer.length,address,port);
        return datagram;
        
    }
    
}
