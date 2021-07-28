package com.example.food4u;


public class Ball {
    public static final double MAX_SPEED = 50;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private float x;
    private float y;
    private double speed;
    private double angle;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        angle = 180;
        speed = 4;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


}
