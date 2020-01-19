package com.example.foodcalc;

public class Consumption {
    public static int total=0;
    public int breakfast=0;
    public int lunch=0;
    public int dinner=0;

    public int others=0;
    public static int goal=0;

    public Consumption(int breakfast,int lunch,int dinner,int others){
        this.breakfast=breakfast;
        this.lunch=lunch;
        this.dinner=dinner;
        this.others=others;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}
