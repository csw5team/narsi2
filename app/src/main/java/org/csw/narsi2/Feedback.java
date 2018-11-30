package org.csw.narsi2;

public class Feedback {
    private double tempFeedback;
    private int styleFeedback;

    public Feedback() {
    }

    public Feedback(double tempFeedback, int styleFeedback) {
        this.styleFeedback = styleFeedback;
        this.tempFeedback = tempFeedback;
    }

    public double getTempFeedback() {
        return tempFeedback;
    }

    public int getStyleFeedback() {
        return styleFeedback;
    }

    public void setTempFeedback(double tempFeedback) {
        this.tempFeedback = tempFeedback;
    }

    public void setStyleFeedback(int styleFeedback) {
        this.styleFeedback = styleFeedback;
    }
    public void setStatus(int styleFeedback, double tempFeedback){
        this.tempFeedback = tempFeedback;
        this.styleFeedback = styleFeedback;
    }


}
