/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.JLabel;

/**
 *
 * @author Angel
 */
public class Knob extends JLabel implements MouseMotionListener, MouseListener{


    private int mProgress;
    
    
    public interface Event{
        public void progress(int progress);
        public void progressOnRelease(int progress);
    }
    Event mListener;
    public void setListener(Event listener){
        mListener = listener;
    }
    
    
    
    
    public Knob() throws IOException {
        //mId = id;

        //ClassLoader cldr = this.getClass().getClassLoader();
        //if(Button.mIdleImage == null) Button.mIdleImage = ImageIO.read(cldr.getResource("com/dehox/idle_button.png"));
        //setIcon(new ImageIcon(mIdleImage));
        addMouseMotionListener(this);
        addMouseListener(this);
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D )g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //g.setColor(new Color(34, 34, 34));
            g.setColor(Color.WHITE);
            
            Rectangle circle = new Rectangle(5, 5, getWidth()-10, getHeight()-10);
            
            g.fillOval(circle.x, circle.y, circle.width, circle.height);
            
            Rectangle marker = new Rectangle(-2, 0, 4, (int)(getWidth()*0.2));
            
  
            g2.translate(circle.getCenterX(), circle.y);
            
            g2.rotate(Math.toRadians(-145 + (290.0f / 100.0f * (float)mProgress)), 0, circle.getCenterY()-marker.getCenterY());
           
            g2.setColor(new Color(255, 102, 0));
            g2.fillRect(marker.x, marker.y, marker.width, marker.height);
            
            
            

    }
    
    public void setProgress(int progress){
        mProgress = progress;
        mProgress = Math.max(mProgress, 0);
        mProgress = Math.min(mProgress, 100);
        
        if(mListener != null){
            mListener.progress(mProgress);
        }
        
        repaint();
    }

    
    private Point mousePreviusDrag;
    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = new Point();
        p.x = (int)(mousePreviusDrag.getX() - e.getX());
        p.y = (int)(mousePreviusDrag.getY() - e.getY());
        
        setProgress(mProgress + p.x + p.y);
        
        mousePreviusDrag = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePreviusDrag = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(mListener != null){
            mListener.progressOnRelease(mProgress);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    
}
