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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Angel
 */
public class TR909 extends JPanel {
    private final BufferedImage bg;
    
    public static Button[] PadButtons = new Button[16];
    public static InstrumentRack InstrumentRack = new InstrumentRack();
    
    private Clip mClip;

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
            PadButtons[i].setListener(new Button.Event(){
                @Override
                public void pressed(Button btn, boolean active) {
                    TR909.InstrumentRack.set(Integer.parseInt(btn.getName()), active);
                }

            });

        }
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
        
        
        try {
            mClip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(TR909.class.getName()).log(Level.SEVERE, null, ex);
        }

        SoundEngine se = new SoundEngine();
        se.setListener(new SoundEngine.Event(){
            @Override
            public void stepChange(int step) {
                for(Button b:TR909.PadButtons){
                    b.setActive(false);
                }
                TR909.PadButtons[step].setActive(true);
                
                
                if(step == 0 && mClip != null && !mClip.isRunning()){
                    try {
                      AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Main.class.getResourceAsStream("/assets/VEDH_Mel2_126.wav"));
                      mClip.open(inputStream);
                      mClip.loop(Clip.LOOP_CONTINUOUSLY);
                      mClip.start(); 
                    } catch (Exception e) {
                      System.err.println(e.getMessage());
                    }        
                }
            }
        });
        se.start();
        
        
        createInstrumentLabels();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.drawImage(bg, 0, 0, null);
            //repaint();
            
    }
    public void updatePad(){
        for(int k=0;k<PadButtons.length;k++){
            PadButtons[k].setSetted(TR909.InstrumentRack.get(k));
        }
    }
    
    
    
    public void setInstrument(int instrument){
        if(InstrumentRack.getActiveInstrument() == instrument) instrument = -1;
        InstrumentRack.setActiveInstrument(instrument);
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
        updateInstrumentLabels(-1);
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
