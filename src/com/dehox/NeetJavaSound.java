package com.dehox;



import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

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
     * Opens the audio stream.
     * 
     * @param sampleRate Sample rate.
     * @param channels Channels.
     * @param wantedLatency Goal latency in ms.
     * @throws NjsException if an error occurred.
     */
    public static void open(final double sampleRate, final int channels, final int wantedLatency)
    {
        if(audioThread != null)
        {
            throw new NjsException("NeetJavaAudio already open.");
        }
        final AudioFormat af = new AudioFormat((float)sampleRate, 16, channels, true, false);
        try
        {
            final SourceDataLine source = AudioSystem.getSourceDataLine(af, findMixer(af));
            source.open(af);
            
            audioThread = new AudioThread(source, channels, Math.max((int)(sampleRate * wantedLatency * 0.001 + 0.5), 512));
            
            final Thread t = new Thread(audioThread);
            t.setPriority(Thread.MAX_PRIORITY);
            t.setDaemon(true);
            t.start();
        }
        catch (LineUnavailableException e)
        {
            throw new NjsException(e);
        }
    }

    /**
     * Tries to find a suitable mixer, skipping 'Java Sound Audio Engine'. This is a
     * workaround for audio on linux (which seems to default to the crappy 'Java Sound'.
     * 
     * @param af The wanted AudioFormat.
     * @return 
     *          The Mixer.Info or <code>null</code> if no suitable mixer could be found or 
     *          the property 'javax.sound.sampled.SourceDataLine' is set.
     */
    private static Mixer.Info findMixer(final AudioFormat af)
    {
        if(System.getProperty("javax.sound.sampled.SourceDataLine") != null)
            return null;
        
        final Line.Info line = new DataLine.Info(SourceDataLine.class, af);
        final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        
        for(final Mixer.Info mi : mixers)
        {
            final Mixer m = AudioSystem.getMixer(mi);
            if(m.isLineSupported(line) 
                    && !mi.getName().toLowerCase().startsWith("java sound"))
            {
                return mi;
            }
        }
        return null;
    }
    
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
        if(audioThread != null)
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
            audioThread.line.close();
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
     * Runtime exception based NeetJavaSound exception.
     * 
     * @author René Jeschke (rene_jeschke@yahoo.de)
     */
    public static class NjsException extends RuntimeException
    {
        /** serialVersionUID */
        private static final long serialVersionUID = 6003769797804167546L;

        /** @see RuntimeException#RuntimeException(String) */
        public NjsException(String msg)
        {
            super(msg);
        }

        /** @see RuntimeException#RuntimeException(String, Throwable) */
        public NjsException(String msg, Throwable t)
        {
            super(msg, t);
        }
        
        /** @see RuntimeException#RuntimeException(Throwable) */
        public NjsException(Throwable t)
        {
            super(t);
        }
    }
    
    /**
     * The audio thread class.
     * 
     * @author René Jeschke (rene_jeschke@yahoo.de)
     */
    private static class AudioThread implements Runnable
    {
        /** Syncing semaphore. */
        private Semaphore syncer = new Semaphore(1);
        /** Are we running? .*/
        private boolean running = false;
        /** The data line. */
        final SourceDataLine line;
        /** Wanted latency in frames. */
        private final int wantedLatency;
        /** Number of channels. */
        private final int channels;
        /** De we already ran? .*/
        private boolean alreadyRan = false;
        
        /**
         * Constructor.
         * 
         * @param dataLine The data line.
         * @param channels Number of channels.
         * @param wantedLatency Wanted latency in frames.
         */
        public AudioThread(final SourceDataLine dataLine, final int channels, final int wantedLatency)
        {
            try
            {
                this.syncer.acquire();
            }
            catch (InterruptedException eaten)
            {
                // *munch*
            }
            this.line = dataLine;
            this.wantedLatency = wantedLatency;
            this.channels = channels;
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
        
        /**
         * The mighty clamp.
         * 
         * @param x x.
         * @param min min.
         * @param max max.
         * @return x &lt; min ? min : x &gt; max ? max : x;
         */
        private static int clamp(int x, int min, int max)
        {
            return x < min ? min : x > max ? max : x;
        }
        
        /** @see Runnable#run() */
        @Override
        public void run()
        {
            final int maxBuffer = this.line.getBufferSize();
            final int max = this.wantedLatency;
            final int frame = this.channels * 2;
            final int diff = max * frame;
            final int allowed = maxBuffer - diff;
            float[] output = new float[max * frame];
            byte[] buffer = new byte[output.length * 2];
            try
            {
                this.syncer.acquire();
            }
            catch (InterruptedException eaten)
            {
                // *munch*
            }
            this.line.start();
            while(this.running)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException eaten)
                {
                    // munch
                }
                final int delta = this.line.available() - allowed;
                if(delta > 0)
                {
                    final int todo = (delta + diff) / frame;
                    if(todo * this.channels >= output.length)
                    {
                        output = new float[todo * this.channels];
                        buffer = new byte[output.length * 2];
                    }
                    if(NeetJavaSound.callback != null)
                    {
                        NeetJavaSound.callback.render(output, todo);
                    }
                    for(int i = 0; i < todo * this.channels; i++)
                    {
                        final int a = clamp((int)(output[i] * 32768.0), -32768, 32767);
                        
                        buffer[i * 2 + 0] = (byte)a;
                        buffer[i * 2 + 1] = (byte)(a >> 8);
                    }
                    this.line.write(buffer, 0, todo * frame);
                }
            }
            this.line.stop();
            this.syncer.release();
        }
    }
}