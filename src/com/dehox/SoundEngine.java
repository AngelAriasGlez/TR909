/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import com.dehox.NeetJavaSound.NjsCallback;
import com.dehox.instruments.Instrument;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.SourceDataLine;


/**
 *
 * @author Angel
 */
public class SoundEngine extends Thread {
    
    private float mStepTimeMs = 1000f/(120f/60f) / 4f;
    private long mPreviusTime;
    
    private int mStep;
    private SourceDataLine mSoundLine;
    
    
    final Queue<float[]> mAudioQueue = new ArrayDeque();
    
    public SoundEngine(){

    }
    
    @Override
    public void run() {
        while(true){
            long ctime = System.currentTimeMillis();
            if(ctime - mPreviusTime > mStepTimeMs){
                //System.out.println("Beat");
                
                mPreviusTime = ctime;
                
                mStep++;
                if(mStep > 15){
                    mStep = 0;
                }
                for(Button b:TR909.PadButtons){
                    b.setActive(false);
                }
                TR909.PadButtons[mStep].setActive(true);
                
                ArrayList<Instrument> ins = TR909.InstrumentRack.getInStepInstruments(mStep);

                for(Instrument i : ins){

                    byte[] audio = i.getBufferedAudio();
                    try {
                       Clip clip = AudioSystem.getClip();
                       final AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);
                       clip.open(audioFormat, audio, 0, audio.length);
                       clip.start(); 
                    } catch (Exception e) {
                       System.err.println(e.getMessage());
                    }

                }
                

            }
            
            
            
        }
    }
    


  
}