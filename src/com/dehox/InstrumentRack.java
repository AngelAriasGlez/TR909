/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import com.dehox.instruments.Clap;
import com.dehox.instruments.Instrument;
import com.dehox.instruments.Kick;
import com.dehox.instruments.Snare;
import java.util.ArrayList;

/**
 *
 * @author Angel
 */
public class InstrumentRack {
    public Instrument[] mRack = new Instrument[3];
    
    int mActiveInstrument = 0;
    
    InstrumentRack(){
        mRack[0] = new Kick();
        mRack[1] = new Snare();
        mRack[2] = new Clap();
    }
    
    public void set(int pos, boolean active){
        mRack[mActiveInstrument].set(pos, active);
    }
    public boolean get(int pos){
        return mRack[mActiveInstrument].get(pos);
    }
    
    public void setActiveInstrument(int ins){
        mActiveInstrument = ins;
    }
    public int getActiveInstrument(){
        return mActiveInstrument;
    }
    public int intrumentCount(){
        return mRack.length;
    }
    
    public ArrayList<Instrument> getInStepInstruments(int step){
        ArrayList<Instrument> out = new ArrayList<>();
        for(Instrument i : mRack){
            if(i.isInStep(step)){
                out.add(i);
            }
        }
        return out;
    }
}
