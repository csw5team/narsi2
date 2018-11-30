package org.csw.narsi2;

public class Clothes {
    private double optimumTemp;
    private int style;
    private String itemCode;

    public Clothes() {
    }

    public Clothes(double optimumTemp, int style, String itemCode) {
        this.optimumTemp = optimumTemp;
        this.style = style;
        this.itemCode = itemCode;
    }
}
