package com.example.food4u;


import java.util.Random;

public class Circle {
    public float x;
    public float y;
    public float initialX;

    public Circle(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void updateX(float x) {
        this.x += x;
    }
    public void updateY(float y){
        this.y+=y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getInitialX() {
        return initialX;
    }


    //calculate a random x position between the start and end of image (within boundaries)
    public void setInitialX(float xPos, int radius) {
        Random random = new Random();

        //calculate a random x position between the start and end of image (within boundaries)
        int max = (Math.round(xPos) + (radius));
        int min = Math.round(xPos);
        this.initialX = random.nextInt((max - min + 1) + min);
    }


}
