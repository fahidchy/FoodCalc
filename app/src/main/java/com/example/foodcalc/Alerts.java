package com.example.foodcalc;

public class Alerts {
    public String breakfastTime;
    public String lunchTime;
    public String dinnerTime;

    public Alerts(String breakfastTime,String lunchTime,String dinnerTime){
        this.breakfastTime=breakfastTime;
        this.lunchTime=lunchTime;
        this.dinnerTime=dinnerTime;
    }

    public String getBreakfastTime() {
        return breakfastTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public void setBreakfastTime(String breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public void setDinnerTime(String dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    public void setLunchTime(String lunchTime) {
        this.lunchTime = lunchTime;
    }
}
