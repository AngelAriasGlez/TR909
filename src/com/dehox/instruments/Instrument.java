/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox.instruments;


import com.dehox.PitchShifter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Angel
 */
public abstract class Instrument {
     public boolean[] mPad = new boolean[16];
     
    float[] mOriginalAudio;
    float[] mBufferedAudio;
     
    //LinkedBlockingQueue<Byte> mOutputAudio = new  LinkedBlockingQueue();
     

    private float mVolume = 1.0f;
    private float mDecay = 1.0f;
    private float mAttack = 1.0f;
    private float mTone = 1.0f;  
    
    public void setVolume(float vol){
        mVolume = vol;
    }
    public void setDecay(float dec){
        mDecay = dec;
    }  
    public void setAttack(float at){
        mAttack = at;
    }
    public void setTone(float tn){
        mTone = tn;
        changeTone();
    } 
    
    public void changeTone(){
        Arrays.fill(mBufferedAudio, 0);
        
        int sbuff = 2048;
        int overlap = 2048-64;
        PitchShifter pitchShifter =  new PitchShifter(mTone+0.5, 44100, sbuff, overlap);
        float[] buffer = new float[sbuff];
        for(int i=0;i<mOriginalAudio.length;i+=(sbuff-overlap)){
            int len = Math.min(buffer.length, mOriginalAudio.length - i);
            System.arraycopy(mOriginalAudio, i, buffer, 0, len);
            float[] ps = pitchShifter.process(buffer);

            System.arraycopy(buffer, 0, mBufferedAudio, i, len);
        }

    }
    
    
    public float[] getBufferedAudio(){
        return mBufferedAudio;
    }
    
    public void loadFromAssets(String path) {
        try{
            InputStream in = ClassLoader.getSystemResourceAsStream(path); 

            AudioInputStream ais = AudioSystem.getAudioInputStream(ClassLoader.getSystemResourceAsStream(path));
            int read;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            while ((read = in.read(buff)) > 0){
                out.write(buff, 0, read);
            }
            out.flush();
            //mBufferedAudio = floatMe(out.toByteArray());
            
            byte[] oaudio = out.toByteArray();
            mBufferedAudio = new float[oaudio.length/2];
            for (int i = 0; i < oaudio.length-1; i+=2) {
                mBufferedAudio[i/2] = (float)((short)((oaudio[i+1] & 0xFF) << 8 ) | (short)(oaudio[i] & 0xFF)) / (float)Short.MAX_VALUE;
            }
            mOriginalAudio = new float[mBufferedAudio.length];
            System.arraycopy(mBufferedAudio, 0, mOriginalAudio, 0, mBufferedAudio.length);
                   
            ais.close();
            in.close();

        }catch(UnsupportedAudioFileException | IOException e){
            
        }


    }
    public void play(){
        //mOutputAudio.clear();
        
        //for(byte b:mBufferedAudio){
            //mOutputAudio.add(b);
        //}
        
        play=true;
        playPos=0;
    }
    
    public boolean play = false;
    public int playPos = 0;
    
    public void read(float[] output, int frames){
        if(!play) return;
        for(int i=0;i<output.length;i++,playPos++){
            if(playPos+1>=mBufferedAudio.length){
                play=false;
                break;
            }
            
            //Para byte 8bits
            //short ou =  (short)((short)((output[i+1] & 0xFF) << 8 ) | (short)(output[i] & 0xFF));
            //short in =  (short)((short)((mBufferedAudio[playPos+1] & 0xFF) << 8 ) | (short)(mBufferedAudio[playPos] & 0xFF));
            
            float in = mBufferedAudio[playPos];
            in *= mVolume;
            if(playPos > (mBufferedAudio.length / 6))
            in *= (1.0f - (1.0f / (mBufferedAudio.length-(mBufferedAudio.length / 6)) *  (playPos-(mBufferedAudio.length / 6)))) * mDecay;
            
            if(playPos < (mBufferedAudio.length / 6))
            in *= (1.0f / (mBufferedAudio.length-(mBufferedAudio.length / 6)) *  (playPos + ((mBufferedAudio.length-(mBufferedAudio.length / 6))* mAttack))) * mAttack;      
            
            output[i] = MixSamples(output[i], in);
            
            
            //output[i] = (byte) mx;
            //output[i+1] = (byte) (mx >> 8);
        }
      
        //float[] buffer = floatMe(output);
        

        //output = byteMe(buffer);

     }
    
    public static short[] shortMe(byte[] bytes) {
        short[] out = new short[bytes.length / 2]; // will drop last byte if odd number
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < out.length; i++) {
            out[i] = bb.getShort();
        }
        return out;
    }
    public static float[] floatMe(byte[] bytes) {
        float[] out = new float[bytes.length / 2]; // will drop last byte if odd number
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        for (int i = 0; i < out.length; i++) {
            out[i] = (float)bb.getShort() / (float)Short.MAX_VALUE;
        }
        return out;
    }
    
    public byte[] byteMe(float[] pcms) {
        ByteBuffer bb = ByteBuffer.allocate(pcms.length*4);
        for (int i = 0; i < pcms.length; i++) {
            bb.putFloat(pcms[i]);
        }
        return bb.array();
    }
  
    
    private float MixSamples(float a, float b) {
    return  
            // If both samples are negative, mixed signal must have an amplitude between the lesser of A and B, and the minimum permissible negative amplitude
            a < 0 && b < 0 ?
                ((a + b) - ((a * b)/-1.0f)) :
 
            // If both samples are positive, mixed signal must have an amplitude between the greater of A and B, and the maximum permissible positive amplitude
            ( a > 0 && b > 0 ?
                (a + b) - ((a * b)/1.0f)
 
            // If samples are on opposite sides of the 0-crossing, mixed signal should reflect that samples cancel each other out somewhat
            :
                a + b);
    }
    
    
    

    

}
