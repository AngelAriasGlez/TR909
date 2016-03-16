package com.dehox;



import com.portaudio.BlockingStream;
import com.portaudio.DeviceInfo;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Semaphore;


/**
 * Low-latency javax.sound.sampled implementation with callback API. Implemented as 
 * singleton to match my JPA API.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class NeetJavaSound
{
 
    
    /** Constructor. */
    private NeetJavaSound()
    {
        // prevent instantiation.
    }
    
    /** Our audio thread worker. */
    private static AudioThread audioThread;
    /** The callback. */
    static NjsCallback callback = null; 
    


    /**
     * Sets the audio callback.
     * 
     * @param callback The callback.
     */
    public static void setCallback(NjsCallback callback)
    {
        NeetJavaSound.callback = callback;
    }
    
    /**
     * Starts the audio stream. (Can't be restarted).
     */
    public static void start()
    {
        if(audioThread == null){
            audioThread = new AudioThread();
            final Thread t = new Thread(audioThread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.setDaemon(true);
            t.start();
        
        }
        audioThread.start();

        
    }
    
    /**
     * Stops the audio stream.
     */
    public static void stop()
    {
        if(audioThread != null)
            audioThread.stop();
    }
    
    /**
     * Closes the audio system.
     */
    public static void close()
    {
        if(audioThread != null)
        {
            stop();
            audioThread = null;
        }
    }
    
    /**
     * The rendering callback interface.
     * 
     * @author René Jeschke (rene_jeschke@yahoo.de)
     */
    public static interface NjsCallback
    {
        /**
         * Called when sound hast to be rendered.
         * 
         * @param output Interleaved output. 
         * @param nframes Number of frames to render.
         */
        public void render(float[] output, int nframes);
    }


    
    /**
     * The audio thread class.
     * 
     * @author René Jeschke (rene_jeschke@yahoo.de)
     */
    private static class AudioThread implements Runnable
    {
        private Semaphore syncer = new Semaphore(1);
        private boolean running = false;
        private boolean alreadyRan = false;
        

        public AudioThread()
        {
            try
            {
                this.syncer.acquire();
            }
            catch (InterruptedException eaten)
            {
                // *munch*
            }
        }
        
        /**
         * Starts audio processing. 
         */
        public void start()
        {
            if(this.running || this.alreadyRan)
                return;
            this.running = true;
            this.alreadyRan = true;
            this.syncer.release();
        }
        
        /**
         * Stops audio processing. 
         */
        public void stop()
        {
            if(!this.running)
                return;
            this.running = false;
            try
            {
                this.syncer.acquire();
            }
            catch (InterruptedException eaten)
            {
                // *munch*
            }
        }

        /** @see Runnable#run() */
        @Override
        public void run()
        {

            try
            {
                this.syncer.acquire();
            }
            catch (InterruptedException eaten)
            {
                // *munch*
            }
            
            
            		PortAudio.initialize();

		// Get the default device and setup the stream parameters.
		int deviceId = PortAudio.getDefaultOutputDevice();
		DeviceInfo deviceInfo = PortAudio.getDeviceInfo( deviceId );
		double sampleRate = deviceInfo.defaultSampleRate;
		System.out.println( "  deviceId    = " + deviceId );
		System.out.println( "  sampleRate  = " + sampleRate );
		System.out.println( "  device name = " + deviceInfo.name );

		StreamParameters streamParameters = new StreamParameters();
		streamParameters.channelCount = 1;
		streamParameters.device = deviceId;
                streamParameters.sampleFormat = PortAudio.FORMAT_FLOAT_32;
		streamParameters.suggestedLatency = deviceInfo.defaultLowOutputLatency;


		System.out.println( "  suggestedLatency = "
				+ streamParameters.suggestedLatency );

		int framesPerBuffer = 32;
		int flags = 0;
		
                BlockingStream stream = PortAudio.openStream( null, streamParameters,
				(int) sampleRate, framesPerBuffer, flags );

            stream.start();
            
            float[] buffer = new float[framesPerBuffer];

            while(this.running)
            {

                if(NeetJavaSound.callback != null)
                {
                    NeetJavaSound.callback.render(buffer, buffer.length);
                }
                

                /*short[] sbuf = new short[framesPerBuffer/2];
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < sbuf.length; i++) {
                    sbuf[i] = bb.getShort();
                }*/

                
                stream.write(buffer, buffer.length);
                    
            }
                
                
		stream.stop();
		stream.close();

		PortAudio.terminate();
		System.out.println( "JPortAudio test complete." );
                
                
                
            this.syncer.release();
        }
    }
    
    



}