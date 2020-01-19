package com.example.foodcalc;

public class User {
    private String name;
    private int weight;
    private int height;
    public int goal;
    public static int goals=0;
    public static int current=0;

    public User(String name,int weight, int height,int goal){
        this.name=name;
        this.weight=weight;
        this.height=height;
        this.goal=goal;
    }

    public String getName(){
        return name;
    }
    public int getWeight(){
        return weight;
    }
    public int getHeight(){
        return height;
    }
    public int getGoal(){
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        goals=goal;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}