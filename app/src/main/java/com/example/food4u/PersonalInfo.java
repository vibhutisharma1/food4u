package com.example.food4u;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import com.parse.ParseUser;

@ParseClassName("personalInfo")
public class PersonalInfo extends ParseObject {

    //retrieve all fields from parse user
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_AGE = "age";
    public static final String KEY_USER = "user";
    public static final String KEY_HEALTH = "health";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_CALORIE = "calories";

    //getters
    public int getWeight() {
        return getInt(KEY_WEIGHT);
    }

    public String getActivity() {
        return getString(KEY_ACTIVITY);
    }

    public int getAge() {
        return getInt(KEY_AGE);
    }

    public double getHeight() {
        return getDouble(KEY_HEIGHT);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public String getHealth() { return getString(KEY_HEALTH); }

    public String getGender() {
        return getString(KEY_GENDER);
    }

    public String getCalorie() {
        return getString(KEY_CALORIE);
    }


    //setters
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setWeight(int weight) {
        put(KEY_WEIGHT, weight);
    }

    public void setActivity(String activity) {
        put(KEY_ACTIVITY, activity);
    }

    public void setAge(int age) {
        put(KEY_AGE, age);
    }

    public void setHeight(double height) {
        put(KEY_HEIGHT, height);
    }

    public void setHealth(String health){
        put(KEY_HEALTH, health);
    }

    public void setGender(String gender){
        put(KEY_GENDER, gender);
    }

    public void setCalorie(double calorie){
        put(KEY_CALORIE, calorie);
    }
}
