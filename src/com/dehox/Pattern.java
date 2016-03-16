/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Angel
 */
public class Pattern implements Serializable{
    private InstrumentRack mInstrumentRack;
    private boolean[][] mPad;
    private Map<Integer, boolean[]> mTimes = new HashMap();
    
    public Pattern(InstrumentRack rack){
        mInstrumentRack = rack;
        mPad = new boolean[mInstrumentRack.intrumentCount()][16];
    }
    
    public void set(int instrument, int time){
        //mTimes[instrument][0];
    }
    
    public void setPad(int instrument, int pos, boolean active){
        if(instrument < 0) return;
        mPad[instrument][pos] = active;
    }
    public boolean getPad(int instrument, int pos){
        if(instrument < 0) return false;
        return mPad[instrument][pos];
    }
    
    public void process(int step){
        for(int i=0;i<mPad.length;i++){
            if(mPad[i][step]){
                //System.out.println("play " + step);
                mInstrumentRack.play(i);
            }
        }
    }
}
