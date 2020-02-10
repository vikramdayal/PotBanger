/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potbanger.play;

import java.io.Serializable;

/**
 *
 * @author vdayal
 */
public class Bar implements Serializable{
    int _seq;
    int _tempo;
    int _beatsPerBar;
    int _numFractions;
    //transient int _waitMS;
    Bar _next=null;
    Bar _prev=null;
    public int [][] _pulses;
    public static int NUM_DEVICES=10;
    
    public Bar(int seq, int tempo, int beatsPerBar, int numFractions){
        _seq = seq;
        _tempo=tempo;
        _beatsPerBar=beatsPerBar;
        _numFractions=numFractions;
      //  _waitMS =60000 / (_beatsPerBar * numFractions );
        _pulses = new int[NUM_DEVICES][numFractions*beatsPerBar];
    }
    
    public Bar(int seq, int tempo, int beatsPerBar, int numFractions, Bar bar){
        _seq = seq;
        _tempo=tempo;
        _beatsPerBar=beatsPerBar;
        _numFractions=numFractions;
      //  _waitMS = 60000 / (_beatsPerBar * numFractions );
        _pulses = new int[NUM_DEVICES][numFractions*beatsPerBar];
        for (int i=0; i < NUM_DEVICES; i++){
            System.arraycopy(bar._pulses[i], 0, _pulses[i], 0, bar.getNumCells());
        }
    }
    
    public void update(Bar bar){
        _tempo=bar.getTempo();
        _beatsPerBar=bar.getNumBeats();
        _numFractions=bar.getNumFractions();
        _pulses = new int[NUM_DEVICES][_numFractions*_beatsPerBar];
        for (int i=0; i < NUM_DEVICES; i++){
            System.arraycopy(bar._pulses[i], 0, _pulses[i], 0, bar.getNumCells());
        }
        
    }
    
    public int getWait(){
        return 60000 / (_tempo * _numFractions );
    }
    
    public void setTempo(int tempo){
        _tempo=tempo;
    }
    public int getTempo(){
        return _tempo;
    }

    public int getNumBeats(){
        return _beatsPerBar;
    }
    
    public int getNumFractions() {
        return _numFractions;
    }
   
    public int getSeq(){
        return _seq;
    }
    
    public void setSeq(int seq){
        _seq = seq;
    }
    
    public int getNumCells() {
        return _numFractions * _beatsPerBar;
    }
    
    public void setPulse (int device, int CellNo, int val){
        _pulses[device][CellNo]=val;
    }
     
     public int getPulse(int device, int CellNo){
         //System.out.println("sending --"+_pulses[device][CellNo]);
         return _pulses[device][CellNo];
     }
     
     public Bar getNext(){
         return _next;
     }
     
     public void setNext(Bar next){
         _next = next;
     }
     
     public void add(Bar next){
         _next = next;
     }
    
    
}
