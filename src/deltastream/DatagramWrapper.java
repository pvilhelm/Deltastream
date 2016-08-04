/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author petter
 */
public class DatagramWrapper{
        public long timestamp = 0;
        public byte[] datagramData = null;
        public InetAddress inetAddress = null;
        public int port;
        public DatagramPacket dgPkt;
        DatagramWrapper(DatagramPacket dgPkt){
            this.dgPkt = dgPkt;
            port = dgPkt.getPort();
            timestamp = new Date().getTime();
            this.datagramData = Arrays.copyOfRange(dgPkt.getData(), dgPkt.getOffset(), dgPkt.getLength()); 
            inetAddress = dgPkt.getAddress();
        }
    }