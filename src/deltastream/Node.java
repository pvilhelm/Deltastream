/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.*;

/**
 *
 * @author petter
 * 
 * A node is a remote computer that's part of the transmission. This class handles the connection methods to the node as well as stores data about properties of the node. 
 */
public class Node {
    InetAddress inetAddress;
    byte rating;
    float debtTo;
    long uploadedToB;
    long downloadFromB;
    int ping = Integer.MAX_VALUE;
    Transmission transmission = null;
        
    Node(InetAddress inetAddress){
        this.inetAddress = inetAddress;
        
    }
    
    void SetTransmission(Transmission transmission){
        this.transmission = transmission;
    }
    Transmission GetTransmission(){
        return this.transmission;
    }
    
    
    
    
    /**
     * Ping the node
     */
    
    void Ping(){
        
    }  
}
