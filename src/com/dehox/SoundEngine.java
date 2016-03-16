/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import com.dehox.NeetJavaSound.NjsCallback;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 *
 * @author Angel
 */
public class SoundEngine extends Thread implements NjsCallback {

    @Override
    public void render(float[] output, int nframes) {
        Arrays.fill(output, 0);
        synchronized (this) {
            TR909.Instruments.read(output, nframes);
            
            for(int i=0;i<output.length;i++){

                output[i] *= mVolume;
            }
        }
    }

    public interface Event {

        public void stepChange(int step);
    }
    Event mListener;

    public void setListener(Event listener) {
        mListener = listener;
    }

    private float mBpm = 124;

    private long mStartTime;
    private int mTotalSteps;
    private int mPreviusStep;

    final Queue<float[]> mAudioQueue = new ArrayDeque();

    public SoundEngine() {
        this.setPriority(Thread.MAX_PRIORITY);

        NeetJavaSound.setCallback(this);
        NeetJavaSound.start();

    }
    
    private float mVolume = 0.8f;
    public synchronized void setVolume(float vol){
        mVolume = vol;
    }
    private boolean mPlaying = false;
    public synchronized void play(){
        mPlaying = true;
        reset();
    }
    public synchronized void pause(){
        mPlaying = false;
    }

    public void setBpm(int bpm) {
        synchronized (this) {
            mBpm = bpm;
            reset();
        }

    }
    
    public void reset(){
        mStartTime = System.currentTimeMillis();
        mTotalSteps = 0;
        mPreviusStep = 0;
    }
    
    

    @Override
    public void run() {
        synchronized (this) {
            mStartTime = System.currentTimeMillis();
        }
        while (true) {
            synchronized (this) {
                if(mPlaying){
                    mTotalSteps = getTotalSteps();

                    if (mTotalSteps > mPreviusStep) {
                        int cStep = getCurrentStep();

                        if (mListener != null) {
                            mListener.stepChange(cStep);
                        }

                        TR909.Patterns.getActive().process(cStep);

                        mPreviusStep = mTotalSteps;

                    }
                }
            }

        }
    }

    public int getCurrentStep() {
        return mTotalSteps % 16;
    }

    public int getTotalSteps() {
        long ctime = System.currentTimeMillis();
        return (int) Math.floor((ctime - mStartTime) / getStepMs());
    }

    public float getStepMs() {
        return (60f / mBpm * 1000f) / 4f;
    }

}
