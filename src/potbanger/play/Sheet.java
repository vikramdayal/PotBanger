/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potbanger.play;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import potbanger.comm.TwoWaySerialComm;

/**
 *
 * @author vdayal
 */
public class Sheet implements Serializable {

    String _name;
    Bar First = null;
    int _size = 0;
    transient static TwoWaySerialComm _com = null;

    public Sheet(String name) {
        _name = name;
    }
    
    public static void init(){
        if (_com == null) {
            _com = new TwoWaySerialComm();
            try {
                _com.connect("/dev/cu.usbmodemFD121");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }        
    }

    public Bar getBarAt(int num) {
        Bar ret = First;
        while (ret != null && ret.getSeq() != num) {
            ret = ret.getNext();
        }
        return ret;
    }
    
    public synchronized void play(Bar b) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bar ret = b;
                while (ret != null) {
                    int wait = ret.getWait();
                    for (int i = 0; i < ret.getNumCells(); i++) {
                        for (int j = 0; j < Bar.NUM_DEVICES; j++) {
                            String cmd = "+" + j + " " + ret._pulses[j][i] + "\n";
                            //System.out.println(cmd);
                            _com.send(cmd);                        
                        }
                        try {
                            //System.out.println("sleep--"+wait);
                            java.lang.Thread.sleep(wait);
                            //System.out.println("done sleep--"+wait);
                        } catch (Exception e) {
                        }
                    }
                    ret = ret.getNext();
                }
                System.out.println("*** done");
            }
        });
        thread.start();
    }

    public synchronized void play() {
        play(First);
        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bar ret = First;
                while (ret != null) {
                    int wait = ret.getWait();
                    for (int i = 0; i < ret.getNumCells(); i++) {
                        for (int j = 0; j < Bar.NUM_DEVICES; j++) {
                            String cmd = "+" + j + " " + ret._pulses[j][i] + "\n";
                            System.out.println(cmd);
                            _com.send(cmd);                        
                        }
                        try {
                            System.out.println("sleep--"+wait);
                            java.lang.Thread.sleep(wait);
                            System.out.println("done sleep--"+wait);
                        } catch (Exception e) {
                        }
                    }
                    ret = ret.getNext();
                }
                System.out.println("*** done");
            }
        });
        thread.start();
*/
    }

    public String getName() {
        return _name;
    }
    
    public void reSeq(){
        int seq =0;
        Bar curr = First;
        while (curr != null){
            curr.setSeq(seq);
            seq++;
            curr = curr.getNext();
        }
    }

    public void deleteBar(Bar b){
        if (b == First){
            First = First.getNext();
            First.setSeq(0);
            return;
        }
        Bar curr=First; 
        Bar prev=curr;
        while (curr != b){
            prev = curr;
            curr = curr.getNext();
            if (curr == null){
                prev.setNext(null);
                return;
            }
        }
        curr = curr.getNext();
        prev.setNext(curr);
        reSeq();
    }
    
    public void addBar(int tempo, int beatsPerBar, int numFractions) {
        _size++;
        if (First == null) {
            First = new Bar(0, tempo, beatsPerBar, numFractions);
            return;
        }
        Bar curr = First;
        Bar prev = curr;
        int i = 0;
        while (curr != null) {
            prev = curr;
            curr = curr.getNext();
            i++;
        }
        prev.add(new Bar(i, tempo, beatsPerBar, numFractions, prev));
    }

    public int getSize() {
        return _size;
    }

    public void save() {
        try {
            FileOutputStream fo = new FileOutputStream(_name + ".vickyd");
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(this);
            so.flush();
            so.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Sheet load(String name) {
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(name + ".vickyd");
            ObjectInputStream si = new ObjectInputStream(fi);
            Sheet s = (Sheet) si.readObject();
            si.close();
            return s;
        } catch (FileNotFoundException ex) {
            // Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Sheet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
};
