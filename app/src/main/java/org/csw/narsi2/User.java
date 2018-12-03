package org.csw.narsi2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

    private int age;
    private int gender;
    private String userID;

    private Feedback feedback;

    public User() {
        feedback = new Feedback();

    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Feedback getFeedback() {
        return feedback;

    }

}
