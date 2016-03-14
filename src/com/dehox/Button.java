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
public class Button extends JLabel implements MouseListener,MouseMotionListener{

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("drag");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("move");
    }
    //private static BufferedImage mIdleImage;
    public interface Event{
        void pressed(Button btn, boolean active);
    }
    private Event mListener;
    public void setListener(Event listener){
        mListener = listener;
    }
    
    //private String mId;
    public boolean mSetted = false;
    public boolean mActive = false;  
    
    
    
    public Button(String id) throws IOException {
        //mId = id;
        setName(id);
        //ClassLoader cldr = this.getClass().getClassLoader();
        //if(Button.mIdleImage == null) Button.mIdleImage = ImageIO.read(cldr.getResource("com/dehox/idle_button.png"));
        //setIcon(new ImageIcon(mIdleImage));
        addMouseListener(this);
        this.addMouseMotionListener(this);
        
        
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
                g.fillRect(10, 10, 30, 30);
            }else if(mSetted){       
                g.setColor(Color.MAGENTA);
                g.fillRect(10, 10, 30, 30);
                
            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(getName());
        mSetted = !mSetted;
        
        if(mListener != null){
            mListener.pressed(this, mSetted);
        }
        
       
        repaint();    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}
