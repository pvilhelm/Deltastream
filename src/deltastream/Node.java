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
 * A node is a remote computer that's part of the transmission. This class handles the connection methods to the node as well as stores data about properties of the node. 
 */
public class Node {
    InetAddress inetAddress;
    int remotePort;
    byte rating;
    float debtTo;
    long uploadedToB;
    long downloadFromB;
    int ping = Integer.MAX_VALUE;
    Transmission transmission = null;
    
        
    Node(InetAddress inetAddress, int remotePort){
        this.inetAddress = inetAddress;
        this.remotePort = remotePort;
    }
    
    Node(DatagramPacket dgP){
        this.inetAddress = dgP.getAddress();
        ByteBuffer dgDataBuffer = ByteBuffer.wrap(dgP.getData(),dgP.getOffset(),dgP.getLength());
        if(dgP.getLength()>=Part.HeaderSizes.CHUNK_HEADERSIZE)
            remotePort = dgDataBuffer.getChar();
        else
            remotePort=0;
    }
    
    byte[] GetKey(){
        byte[] key = new byte[18];
        ByteBuffer keyBuffer = ByteBuffer.wrap(key);
        keyBuffer.put(inetAddress.getAddress());
        keyBuffer.putChar((char)remotePort);
        return keyBuffer.array();
    }
    
    static byte[]  GetKey(DatagramPacket dgP){
        byte[] key = new byte[18];
         
                
        ByteBuffer keyBuffer = ByteBuffer.wrap(key);
        keyBuffer.put(dgP.getAddress().getAddress());
        keyBuffer.putChar((char)dgP.getPort());
        return keyBuffer.array();
    }
    
    
    void SetTransmission(Transmission transmission){
        this.transmission = transmission;
    }
    Transmission GetTransmission(){
        return this.transmission;
    }
    
    void ProcessDatagram(DatagramWrapper dgW){
        
    }
    
    
    /**
     * Ping the node
     */
    
    void Ping(){
        
    }  
}
