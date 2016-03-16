/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.JLabel;

/**
 *
 * @author Angel
 */
public class Button extends JLabel implements MouseListener{


    //private String mId;
    public boolean mSetted = false;
    public boolean mActive = false;
    public boolean mPressed = false;  
    
    
    
    public Button(String id) throws IOException {
        //mId = id;
        setName(id);
        //ClassLoader cldr = this.getClass().getClassLoader();
        //if(Button.mIdleImage == null) Button.mIdleImage = ImageIO.read(cldr.getResource("com/dehox/idle_button.png"));
        //setIcon(new ImageIcon(mIdleImage));
        addMouseListener(this);
        
        
    }
    
    public void setActive(boolean active){
        mActive = active;
        repaint();
    }
    public void setSetted(boolean set){
        mSetted = set;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.white);
            g.fillRect(5, 5, getWidth()-10, getHeight()-10);
            
            
            if(mActive){
                g.setColor(Color.CYAN);
                g.fillRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
            }else if(mSetted  || mPressed){       
                g.setColor(Color.MAGENTA);
                g.fillRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
                
            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mPressed = true;
        repaint();    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mPressed = false;
        repaint(); 
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}
