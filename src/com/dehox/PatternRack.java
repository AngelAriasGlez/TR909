/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dehox;

import java.io.Serializable;



/**
 *
 * @author Angel
 */
public class PatternRack {
    private Pattern[] mPatterns = new Pattern[4];
    private int mActivePattern = 0;
    public PatternRack(InstrumentRack ir) {
        mPatterns[0] = new Pattern(ir);
        mPatterns[1] = new Pattern(ir);
        mPatterns[2] = new Pattern(ir);
        mPatterns[3] = new Pattern(ir);
    }
    public Pattern getActive(){
        return mPatterns[mActivePattern];
    }
    public int getActiveIndex(){
        return mActivePattern;
    }
    public void setActive(int npat){
        mActivePattern = npat;
    }
    
}
