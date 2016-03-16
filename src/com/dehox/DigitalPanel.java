/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Angel
 */
public class DigitalPanel extends JLabel{
    
    public DigitalPanel(){
        setVerticalAlignment(JLabel.TOP);
        setHorizontalAlignment(JLabel.RIGHT);
        setBorder(new EmptyBorder(3,5,3,5));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
            
            setFont(new Font("Digital-7 MonoItalic", Font.BOLD, (int)((float)getHeight()*1.2)));
            
            setOpaque(true);
            setBackground(new Color(101, 0, 6));
            String oldText = getText();
            setForeground(new Color(133, 0, 0));
            setText("888");
            super.paintComponent(g);
            
            setOpaque(false);
            setForeground(new Color(247, 5, 0));
            setText(oldText);
            super.paintComponent(g);

            
            //g.setFont(new Font("DS-Digital Italic", Font.BOLD, 60));
            //g.setColor(Color.red);
            //g.drawString(mText, 60, 60);

    }

}
