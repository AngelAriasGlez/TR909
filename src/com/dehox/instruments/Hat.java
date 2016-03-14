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
public class Hat extends Instrument{
    public final static int CLOSED = 0;
    public final static int OPEN = 1;
    public final static int HI = 2; 
    
    public Hat(int type){
        if(type == CLOSED)
            loadFromAssets("assets/cl_hi_hat.wav");
        else
            loadFromAssets("assets/o_hi_hat.wav");

    }
}
