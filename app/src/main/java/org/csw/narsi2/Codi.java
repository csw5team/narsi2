package org.csw.narsi2;

import java.util.HashMap;

public class Codi {

    private HashMap<String,Top> top ;
    private HashMap<String,Bottom> bottom ;


    public Codi() {
    }

    public void setTop(HashMap top){
        this.top = top;
    }
    public void setBottom(HashMap bottom){
        this.bottom = bottom;
    }

    public void setOuter(HashMap outerTop){
        this.top = outerTop;
    }

    public HashMap<String, Top> getTop() {
        return top;
    }

    public HashMap<String, Bottom> getBottom() {
        return bottom;
    }

}
