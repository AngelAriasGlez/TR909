/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox.instruments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Angel
 */
public abstract class Instrument {
     public boolean[] mPad = new boolean[16];
     
     byte[] mBufferedAudio;
     
    public void set(int pos, boolean active){
        mPad[pos] = active;
    }
    public boolean get(int pos){
        return mPad[pos];
    }
    
    
    public boolean isInStep(int step){
        if(!mPad[step]) return false;
        if(mBufferedAudio == null) return false;
        return true;

    }
    
    public byte[] getBufferedAudio(){
        return mBufferedAudio;
    }
    
    public void loadFromAssets(String path) {
        try{
            InputStream in = ClassLoader.getSystemResourceAsStream(path); 
            AudioInputStream ais = AudioSystem.getAudioInputStream(in);
            int read;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            while ((read = in.read(buff)) > 0){
                out.write(buff, 0, read);
            }
            out.flush();
            mBufferedAudio = out.toByteArray();
            ais.close();
            in.close();
        }catch(UnsupportedAudioFileException | IOException e){
            
        }

    }
    

}
