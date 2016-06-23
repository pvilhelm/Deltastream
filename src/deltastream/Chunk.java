/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

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
    int chunkNr;
    int totalNrOfChunks;
    byte [] chunkData;
    
    Chunk(byte[] data, int chunkNr,int totalNrOfChunks, int partNr){
        this.partNr = partNr;
        this.chunkData = data;
        this.chunkNr = chunkNr; 
        this.totalNrOfChunks = totalNrOfChunks; 
    }
    
    
}
