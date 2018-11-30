package org.csw.narsi2;

public class Codi {

    private Clothes top,bottom,outer = new Clothes(){};

    public Codi() {
    }

    public Clothes getTop() {
        return top;
    }

    public void setTop(Clothes top) {
        this.top = top;
    }

    public Clothes getBottom() {
        return bottom;
    }

    public void setBottom(Clothes bottom) {
        this.bottom = bottom;
    }

    public Clothes getOuter() {
        return outer;
    }

    public void setOuter(Clothes outer) {
        this.outer = outer;
    }
}
