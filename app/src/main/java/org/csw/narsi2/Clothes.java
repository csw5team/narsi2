package org.csw.narsi2;


import java.util.List;

public class Clothes {

    private List<String> temp;

    private String casual,formal,sporty,sex;

    public Clothes() {
    }

    public List<String> getTemp() {
        return temp;
    }

    public void setTemp(List<String> temp) {
        this.temp = temp;
    }

    public String getCasual() {
        return casual;
    }

    public void setCasual(String casual) {
        this.casual = casual;
    }

    public String getFormal() {
        return formal;
    }

    public void setFormal(String formal) {
        this.formal = formal;
    }

    public String getSporty() {
        return sporty;
    }

    public void setSporty(String sporty) {
        this.sporty = sporty;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
