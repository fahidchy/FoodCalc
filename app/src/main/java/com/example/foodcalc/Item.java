package com.example.foodcalc;

public class Item {
    public int id;
    public String itemName;
    public int calories;

    public Item(int id, String itemName,int calories){
        this.id=id;
        this.itemName=itemName;
        this.calories=calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
