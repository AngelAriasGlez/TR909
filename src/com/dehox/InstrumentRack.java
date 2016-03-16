/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import com.dehox.instruments.Clap;
import com.dehox.instruments.Crash;
import com.dehox.instruments.Tom;
import com.dehox.instruments.Hat;
import com.dehox.instruments.Instrument;
import com.dehox.instruments.Kick;
import com.dehox.instruments.Ride;
import com.dehox.instruments.Rim;
import com.dehox.instruments.Snare;
import java.util.ArrayList;

/**
 *
 * @author Angel
 */
public class InstrumentRack {
    public Instrument[] mRack = new Instrument[11];
    
    int mActiveInstrument = 0;
    
    InstrumentRack(){
        mRack[0] = new Kick();
        mRack[1] = new Snare();
        mRack[2] = new Tom(Tom.LOW);
        mRack[3] = new Tom(Tom.MID);
        mRack[4] = new Tom(Tom.HI);
        mRack[5] = new Rim();
        mRack[6] = new Clap();
        mRack[7] = new Hat(Hat.CLOSED);
        mRack[8] = new Hat(Hat.OPEN);
        mRack[9] = new Crash();
        mRack[10] = new Ride();
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
    
    public Instrument getInstrument(int ins){
        return mRack[ins];
    }

    
    public void play(int ins){
        mRack[ins].play();
    }
    
    public void read(float[] output, int frames){
        for(Instrument i : mRack){
            i.read(output, frames);
        }
    }
}
