/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Angel
 */
public class TR909 extends JPanel {
    private final BufferedImage bg;
    
    public static Button[] PadButtons = new Button[16];
    public static InstrumentRack InstrumentRack = new InstrumentRack();

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
        Button b = new Button("20");
        b.setBounds(100, 100, 50, 50);
        add(b);
        b.setListener(new Button.Event(){
            @Override
            public void pressed(Button btn, boolean active) {
                int ai = InstrumentRack.getActiveInstrument();
                ai++;
                if(ai > InstrumentRack.intrumentCount()) ai = 0;
                        
                InstrumentRack.setActiveInstrument(ai);
                
                for(int k=0;k<PadButtons.length;k++){
                    PadButtons[k].setSetted(TR909.InstrumentRack.get(k));
                }
            }
        
        });
                
        
       
        SoundEngine se = new SoundEngine();
        se.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.drawImage(bg, 0, 0, null);
            //repaint();

    }
    
    

}
