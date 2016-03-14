/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import com.dehox.instruments.Instrument;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;


/**
 *
 * @author Angel
 */
public class SoundEngine extends Thread {
    
    public interface Event{
        public void stepChange(int step);
    }
    Event mListener;
    public void setListener(Event listener){
        mListener = listener;
    }
    
    
    private float mStepTimeMs = (60f / 126f * 1000f) / 4f;
    
    private long mStartTime;
    private int mTotalSteps;
    private int mPreviusStep;    
    
    final Queue<float[]> mAudioQueue = new ArrayDeque();
    
    public SoundEngine(){
        this.setPriority(Thread.MAX_PRIORITY);
    }
    
    @Override
    public void run() {
        
        mStartTime = System.currentTimeMillis();
        while(true){
            long ctime = System.currentTimeMillis();
            mTotalSteps = (int)Math.floor((ctime - mStartTime) / mStepTimeMs);

            if(mTotalSteps > mPreviusStep){
                int cStep = mTotalSteps % 16;
                
                if(mListener != null){
                    mListener.stepChange(cStep);
                }
                
                                
                ArrayList<Instrument> ins = TR909.InstrumentRack.getInStepInstruments(cStep);

                for(Instrument i : ins){

                    byte[] audio = i.getBufferedAudio();
                    try {
                       Clip clip = AudioSystem.getClip();
                       final AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);
                       clip.addLineListener(new LineListener() {
                            public void update(LineEvent myLineEvent) {
                                if (myLineEvent.getType() == LineEvent.Type.STOP)
                                    clip.close();
                                }
                        });
                       clip.open(audioFormat, audio, 0, audio.length);
                       clip.start(); 
                    } catch (Exception e) {
                       System.err.println(e.getMessage());
                    }

                }
                
                mPreviusStep = mTotalSteps;

            }
            
            
            
        }
    }
    


  
}