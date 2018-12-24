package org.csw.narsi2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

    private int age;
    private int gender;
    private String name;
    private String userID;

    private Feedback feedback = new Feedback();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Preference preference = new Preference();

    public User() {
    }

    public User(Feedback feedback, Preference preference) {
        this.feedback = feedback;
        this.preference = preference;
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

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Preference getPreference() {
        if (preference != null) {
            return preference;
        } else {
            return null;
        }
    }

    public void addPreference(Preference preference) {
        this.preference = preference;
    }

    public Feedback getFeedback() {
        return feedback;


    }


}
