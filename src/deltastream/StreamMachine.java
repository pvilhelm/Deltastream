/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deltastream;

/**
 *This class implements a machine to handle the deltastream logic. 
 * <p>It's gets 
 * incoming parts from other nodes, pocesses them, tracks exchanges 
 * of parts etc.
 * * @author petter
 */
public class StreamMachine {
    Transmission transmission;
    
    
    StreamMachine(Transmission transmission){
        this.transmission = transmission;
    }
}
