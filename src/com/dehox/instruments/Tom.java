/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox.instruments;

/**
 *
 * @author Angel
 */
public class Tom extends Instrument{
    public final static int LOW = 0;
    public final static int MID = 1;
    public final static int HI = 2; 
    
    public Tom(int type){
        if(type == LOW)
            loadFromAssets("assets/low_tom.wav");
        else if(type == MID)
             loadFromAssets("assets/mid_tom.wav");
        else
            loadFromAssets("assets/hi_tom.wav");

    }

}
