package org.csw.narsi2;

public class Codi {

    private Clothes top, bottom;
    private Others[] others;



    public Codi(){}

    public Codi(Clothes top, Clothes bottom) {
        this.top = top;
        this.bottom = bottom;
    }


    public Codi(Clothes top, Clothes bottom, Others[] others) {
        this.top = top;
        this.bottom = bottom;
        this.others = others;
    }

    public Others[] getOthers() {

        return others;
    }

    public void setOthers(Others[] others) {
        this.others = others;
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
}
