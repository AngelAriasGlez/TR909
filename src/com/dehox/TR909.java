/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Angel
 */
public class TR909 extends JPanel {
    private final BufferedImage bg;

    public static Button[] PatternButtons = new Button[4];
    public static Button[] PadButtons = new Button[16];
    public static InstrumentRack Instruments = new InstrumentRack();
    public static PatternRack Patterns = new PatternRack(Instruments);

    TR909() throws IOException{
        
        setLayout(null);
        
        ClassLoader cldr = this.getClass().getClassLoader();
        bg = ImageIO.read(cldr.getResource("com/dehox/bg909.png"));
        
        setSize(bg.getWidth(), bg.getHeight());
        
        
        

        
        
        
        
        
        int buttonsInitOffsetX = 210;
        int buttonsInitOffsetY = 638;
        int buttonsStepX = 68;  
        
        for(int i=0;i<PadButtons.length;i++){
            PadButtons[i] = new Button(""+i);
            add(PadButtons[i]);
            PadButtons[i].setBounds(buttonsInitOffsetX + (buttonsStepX * i), buttonsInitOffsetY, 50, 50);
            final int k = i;
            PadButtons[i].addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {}
                @Override
                public void mousePressed(MouseEvent e) {
                    padPressedEvent(k);
                }
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });

        }
        

        SoundEngine se = new SoundEngine();
        se.start();
        se.setListener(new SoundEngine.Event(){
            @Override
            public void stepChange(int step) {
                for(Button b:TR909.PadButtons){
                    b.setActive(false);
                }
                TR909.PadButtons[step].setActive(true);
                
                
                /*if(step == 0 && mClip[0] != null && !mClip[0].isRunning()){
                    try {
                      AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Main.class.getResourceAsStream("/assets/VEDH_Mel1_124.wav"));
                      mClip[0].open(inputStream);
                      mClip[0].loop(Clip.LOOP_CONTINUOUSLY);
                      mClip[0].start(); 
                    } catch (Exception e) {
                      System.err.println(e.getMessage());
                    }        
                }

                if(step == 0 && mClip[1] != null && !mClip[1].isRunning()){
                    try {
                      AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Main.class.getResourceAsStream("/assets/SHAL_Vocal1_124.wav"));
                      mClip[1].open(inputStream);
                      mClip[1].loop(Clip.LOOP_CONTINUOUSLY);
                      mClip[1].start(); 
                    } catch (Exception e) {
                      System.err.println(e.getMessage());
                    }        
                }              */  
                
                
            }
        });

        
        
                /*Button b = new Button("20");
        b.setBounds(100, 100, 50, 50);
        add(b);
        b.setListener(new Button.Event(){
            @Override
            public void pressed(Button btn, boolean active) {
                int ai = InstrumentRack.getActiveInstrument();
                ai++;
                if(ai >= InstrumentRack.intrumentCount()) ai = 0;
                        
                InstrumentRack.setActiveInstrument(ai);
                updateInstrumentLabels(ai);
                
            }
        
        });*/
                
                
        DigitalPanel bpmPanel = new DigitalPanel();
        bpmPanel.setBounds(282, 445, 113, 50);
        add(bpmPanel);
                
        Knob tempo = new Knob();
        tempo.setBounds(434, 432, 80, 80);
        tempo.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                int bpm = 60 + (int)(80.0f / 100.0f * (float)progress);
                bpmPanel.setText(String.valueOf(bpm));
                se.setBpm(bpm);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        tempo.setProgress(80);
        add(tempo);
        

        Knob bassDrumVol = new Knob();
        bassDrumVol.setBounds(286, 273, 42, 42);
        bassDrumVol.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                Instruments.getInstrument(0).setVolume(1.0f / 100.0f * (float)progress);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        bassDrumVol.setProgress(80);
        add(bassDrumVol);
        
        
        Knob bassDrumDecay = new Knob();
        bassDrumDecay.setBounds(286, 333, 42, 42);
        bassDrumDecay.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                Instruments.getInstrument(0).setDecay(1.0f / 100.0f * (float)progress);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        bassDrumDecay.setProgress(100);
        add(bassDrumDecay);
        
        Knob bassDrumAttack = new Knob();
        bassDrumAttack.setBounds(223, 333, 42, 42);
        bassDrumAttack.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                Instruments.getInstrument(0).setAttack(1.0f / 100.0f * (float)progress);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        bassDrumAttack.setProgress(100);
        add(bassDrumAttack);
        
        
        Knob bassDrumTone = new Knob();
        bassDrumTone.setBounds(223, 273, 42, 42);
        bassDrumTone.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {

            }

            @Override
            public void progressOnRelease(int progress) {
                 Instruments.getInstrument(0).setTone(1.0f / 100.0f * (float)progress);            
            }
        });
        bassDrumTone.setProgress(100);
        add(bassDrumTone);

        
        
        
        Knob snareDrumVol = new Knob();
        snareDrumVol.setBounds(423, 273, 42, 42);
        snareDrumVol.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                Instruments.getInstrument(1).setVolume(1.0f / 100.0f * (float)progress);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        snareDrumVol.setProgress(80);
        add(snareDrumVol);
        
        
        
                
        Knob snareDrumTone = new Knob();
        snareDrumTone.setBounds(360, 273, 42, 42);
        snareDrumTone.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {

            }

            @Override
            public void progressOnRelease(int progress) {
                 Instruments.getInstrument(1).setTone(1.0f / 100.0f * (float)progress);            
            }
        });
        snareDrumTone.setProgress(100);
        add(snareDrumTone);


        
        
        Button play = new Button("play");
        play.setBounds(120, 447, 50, 50);
        play.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                se.play();
                play.setSetted(true);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(play);

        Button stop = new Button("stop");
        stop.setBounds(194, 447, 50, 50);
        stop.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                se.pause();
                play.setSetted(false);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(stop);
        
        
        
        
        
        Knob volume = new Knob();
        volume.setBounds(1213, 432, 80, 80);
        volume.setListener(new Knob.Event(){
            @Override
            public void progress(int progress) {
                se.setVolume(1.0f / 100.0f * (float)progress);
            }

            @Override
            public void progressOnRelease(int progress) {
             
            }
        });
        volume.setProgress(80);
        add(volume);
        
        
        
        
        
        PatternButtons[0] = new Button("p0");
        PatternButtons[0].setBounds(718, 440, 30, 30);
        PatternButtons[0].addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                setPattern(0);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(PatternButtons[0]);
        
        PatternButtons[1] = new Button("p1");
        PatternButtons[1].setBounds(754, 440, 30, 30);
        PatternButtons[1].addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                setPattern(1);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(PatternButtons[1]);   
        
        PatternButtons[2] = new Button("p2");
        PatternButtons[2].setBounds(790, 440, 30, 30);
        PatternButtons[2].addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                setPattern(2);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(PatternButtons[2]);  
        
        PatternButtons[3] = new Button("p2");
        PatternButtons[3].setBounds(825, 440, 30, 30);
        PatternButtons[3].addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                setPattern(3);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        add(PatternButtons[3]);     
        
        
        
       
        
        
        
        
        createInstrumentLabels();
        setInstrument(-1);
        setPattern(0);
    }
    
    public void setPattern(int pat){
            Patterns.setActive(pat);
            updatePatternButtons();
            updatePad();
    }
    public void updatePatternButtons(){
        for(int k=0;k<PatternButtons.length;k++){
            PatternButtons[k].setSetted(TR909.Patterns.getActiveIndex() == k);
        }
    }
    
 
   
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.drawImage(bg, 0, 0, null);
            //repaint();
            
    }
    public void padPressedEvent(int n){
        int activeIns = Instruments.getActiveInstrument();
        if(activeIns == -1){
            TR909.Instruments.play(n);
        }else{
            TR909.Patterns.getActive().setPad(activeIns, n, !TR909.Patterns.getActive().getPad(activeIns, n));
        }
        updatePad();
    }

    public void updatePad(){
        for(int k=0;k<PadButtons.length;k++){
            PadButtons[k].setSetted(TR909.Patterns.getActive().getPad(Instruments.getActiveInstrument(), k));
        }
    }
    
    
    
    public void setInstrument(int instrument){
        if(Instruments.getActiveInstrument() == instrument) instrument = -1;
        Instruments.setActiveInstrument(instrument);
        updateInstrumentLabels(instrument);
        updatePad();
    }
    
    
    
    private JLabel[] mInstrumentLabels = new JLabel[11];
    private void createInstrumentLabels(){
        
        mInstrumentLabels[0] = new JLabel("BASS DRUM", SwingConstants.CENTER);
        mInstrumentLabels[0].setBounds(206, 720, 124, 20);
        
        mInstrumentLabels[1] = new JLabel("SNARE DRUM", SwingConstants.CENTER);
        mInstrumentLabels[1].setBounds(206 + 136, 720, 124, 20);

        mInstrumentLabels[2] = new JLabel("LOW TOM", SwingConstants.CENTER);
        mInstrumentLabels[2].setBounds(206 + (136*2)+1, 720, 124, 20);
        
        mInstrumentLabels[3] = new JLabel("MID TOM", SwingConstants.CENTER);
        mInstrumentLabels[3].setBounds(206 + (136*3)+1, 720, 124, 20);     
        
        mInstrumentLabels[4] = new JLabel("HI TOM", SwingConstants.CENTER);
        mInstrumentLabels[4].setBounds(206 + (136*4)+2, 720, 124, 20);        
       
        
        mInstrumentLabels[5] = new JLabel("RIM SHOT", SwingConstants.CENTER);
        mInstrumentLabels[5].setBounds(206 + (136*5)+2, 720, 62, 20);    
        
        mInstrumentLabels[6] = new JLabel("HAND CLAP", SwingConstants.CENTER);
        mInstrumentLabels[6].setBounds(206 + (136*5)+2 + 62, 720, 62, 20); 

        
        
        mInstrumentLabels[7] = new JLabel("CLOSED", SwingConstants.CENTER);
        mInstrumentLabels[7].setBounds(206 + (136*6)+3, 720, 62, 20);    
        
        mInstrumentLabels[8] = new JLabel("OPEN", SwingConstants.CENTER);
        mInstrumentLabels[8].setBounds(206 + (136*6)+3 + 62, 720, 62, 20); 
        
        
        
        mInstrumentLabels[9] = new JLabel("CRASH", SwingConstants.CENTER);
        mInstrumentLabels[9].setBounds(206 + (136*7)+3, 720, 62, 20);    
        
        mInstrumentLabels[10] = new JLabel("RIDE", SwingConstants.CENTER);
        mInstrumentLabels[10].setBounds(206 + (136*7)+3 + 62, 720, 62, 20);
        
        
        for(int i=0;i<mInstrumentLabels.length;i++){
            JLabel l = mInstrumentLabels[i];
            l.setOpaque(true);
            l.setFont(new Font("Arial", Font.BOLD, 10));
            final int k = i;
            l.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {}
                @Override
                public void mousePressed(MouseEvent e) {
                    setInstrument(k);
                }
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            
            add(l);
        }
    }
    
    public void updateInstrumentLabels(int selectedInstrument){
         for(int i=0;i<mInstrumentLabels.length;i++){
             if(i == selectedInstrument){
                mInstrumentLabels[i].setBackground(Color.MAGENTA);
             }else{
                mInstrumentLabels[i].setBackground(Color.WHITE);
             }
            
        }       
    }
            
       

    
    
    
    

}
