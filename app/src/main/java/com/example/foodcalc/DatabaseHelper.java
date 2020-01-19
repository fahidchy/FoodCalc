package com.example.foodcalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "foodcal.db";

    public static final String TABLE_NAME = "users";
    public static final String colUserName = "NAME";
    public static final String colWeight = "WEIGHT";
    public static final String colGoal = "GOAL";
    public static final String colHeight = "HEIGHT";

    public static final String alertsTable = "alerts";
    public static final String dinnerTime = "dinnerTime";
    public static final String breakfastTime = "breakfastTime";
    public static final String lunchTime = "lunchTime";

    public static final String consumptionTable = "consumption";
    public static final String dateRecord = "DateRecorded";
    public static final String breakfast = "Breakfast";
    public static final String dinner = "Dinner";
    public static final String lunch = "Lunch";
    public static final String others = "Others";


    public static final String weightTable = "weight";
    public static final String dateOfRecord = "DateRecorded";
    public static final String weight = "Weight";

    public static final String itemTable = "items";
    public static final String itemName = "ItemName";
    public static final String calories = "Calories";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createUser = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    colUserName + " TEXT NOT NULL," +
                    colWeight + " INTEGER NOT NULL," +
                    colHeight + " INTEGER NOT NULL," +
                    colGoal + " INTEGER NOT NULL);";
            System.out.println(createUser);
            db.execSQL(createUser);

            String createWeight = "CREATE TABLE " + weightTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    weight + " INTEGER NOT NULL," +
                    dateOfRecord + " TIMESTAMP DEFAULT CURRENT_DATE);";
            System.out.println(createWeight);
            db.execSQL(createWeight);

            String createAlerts = "CREATE TABLE " + alertsTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    breakfastTime + " STRING NOT NULL," +
                    lunchTime + " STRING NOT NULL," +
                    dinnerTime + " STRING NOT NULL);";
            System.out.println(createAlerts);
            db.execSQL(createAlerts);

            String createConsumption = "CREATE TABLE " + consumptionTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    breakfast + " INTEGER NOT NULL," +
                    lunch + " INTEGER NOT NULL," +
                    dinner + " INTEGER NOT NULL," +
                    others + " INTEGER NOT NULL," +
                    dateRecord + " TIMESTAMP DEFAULT CURRENT_DATE);";
            System.out.println(createConsumption);
            db.execSQL(createConsumption);

            String createItems = "CREATE TABLE " + itemTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    itemName + " TEXT NOT NULL," +
                    calories + " INTEGER NOT NULL);";
            System.out.println(createItems);
            db.execSQL(createItems);

        } catch (Exception e) {
            Log.e("Problem in creation", e.toString());
        }
//        db.execSQL("CREATE TABLE "+TABLE_NAME+"(NAME TIME,WEIGHT TIME, HEIGHT TIME)");
//        db.execSQL("CREATE TABLE "+TABLE_NAME+"(NAME TEXT,WEIGHT DOUBLE, HEIGHT DOUBLE, GOAL DOUBLE)");
//        db.execSQL("CREATE TABLE "+TABLE_NAME+"(NAME TEXT,WEIGHT DOUBLE, HEIGHT DOUBLE, GOAL DOUBLE)");
//        db.execSQL("CREATE TABLE "+TABLE_NAME+"(NAME TEXT,WEIGHT DOUBLE, HEIGHT DOUBLE, GOAL DOUBLE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + weightTable);
        db.execSQL("DROP TABLE IF EXISTS " + alertsTable);
        db.execSQL("DROP TABLE IF EXISTS " + consumptionTable);
        db.execSQL("DROP TABLE IF EXISTS " + itemTable);
        onCreate(db);
    }

    public boolean insertData(String name, int weight, int height, int goal) {
//        name="Fahid";
//        weight=80;
//        height=174;
//        goal=4290;

        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        String text = name + " " + height + " " + weight + " " + goal + " ";
        System.out.println(text);
        ContentValues cV = new ContentValues();
        cV.put(colUserName, name);
        cV.put(colWeight, weight);
        cV.put(colHeight, height);
        cV.put(colGoal, goal);
        //long result = db.insert(TABLE_NAME, null, cV);
        try {
            result = db.insert(TABLE_NAME, null, cV);
        } catch (Exception e) {
            Log.e("Problem in insertion", e.toString());
        }
        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public boolean insertDataInWeight(int weight) {

        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(colWeight, weight);
        //long result = db.insert(TABLE_NAME, null, cV);
        try {
            result = db.insert(weightTable, null, cV);
        } catch (Exception e) {
            Log.e("Problem in insertion", e.toString());
        }

        if (result == -1)
            return false;
        else
            updateDatainWeight(weight);
            return true;

    }

    public boolean updateDatainWeight(int weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colWeight, weight);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ "1"});
        return true;
    }

    public boolean insertDataInItems(String newItem,int cal) {
        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(itemName,newItem);
        cV.put(calories, cal);

        //long result = db.insert(TABLE_NAME, null, cV);
        try {
            result = db.insert(itemTable, null, cV);
        } catch (Exception e) {
            Log.e("Problem in insertion", e.toString());
        }

        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean insertDataInConsumption(int b,int l,int d,int o) {

        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(breakfast,b);
        cV.put(lunch, l);
        cV.put(dinner, d);
        cV.put(others, o);
        //long result = db.insert(TABLE_NAME, null, cV);
        try {
            result = db.insert(consumptionTable, null, cV);
        } catch (Exception e) {
            Log.e("Problem in insertion", e.toString());
        }
        if (result == -1)
            return false;
        else
            return true;

    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public boolean updateData(String name, int weight, int height, int goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colWeight, weight);
        contentValues.put(colGoal, goal);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ "1"});
        return true;
    }

    public boolean updateDataInConsumption(int b,int l,int d,int o) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery="UPDATE "+ consumptionTable+ " SET "+
                breakfast+"="+b+","+
                lunch+"="+l+","+
                dinner+"="+d+","+
                others+"="+o+
                " WHERE DateRecorded = CURRENT_DATE ";
//        ContentValues cV = new ContentValues();
//        cV.put(breakfast,b);
//        cV.put(lunch, l);
//        cV.put(dinner, d);
//        cV.put(others, o);
//        db.update(consumptionTable, cV, "DateRecorded = ?", new String[]{"current_date"});
        db.execSQL(updateQuery);
        return true;
    }

    public boolean insertDataInAlerts(String time1,String time2,String time3) {

        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(breakfastTime, time1);
        cV.put(lunchTime, time2);
        cV.put(dinnerTime, time3);
        //long result = db.insert(TABLE_NAME, null, cV);
        try {
            result = db.insert(alertsTable, null, cV);
        } catch (Exception e) {
            Log.e("Problem in insertion", e.toString());
        }
        if (result == -1)
            return false;
        else
            return true;

    }

    public boolean updateDataInAlerts(String time1,String time2,String time3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(breakfastTime, time1);
        contentValues.put(lunchTime, time2);
        contentValues.put(dinnerTime, time3);
        db.update(alertsTable, contentValues, "ID = ?", new String[]{"1"});
        return true;
    }

    public boolean updateDataInItems(int id,String txt,int cal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(itemName, txt);
        cV.put(calories, cal);
        db.update(itemTable, cV ,"ID = ?", new String[] { id+"" });
        return true;
    }

    public Cursor getAllDataFromAlerts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + alertsTable;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor getAllDataFromItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + itemTable;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor getAllDataFromConsumption() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + consumptionTable +" WHERE DateRecorded=current_date";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor getAllConsumptions() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + consumptionTable;
        //+" WHERE DateRecorded=current_date";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor existsItem(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + itemTable+" WHERE "+itemName+" = '"+item+"';";
        //+" WHERE DateRecorded=current_date";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor viewWeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + weightTable ;
                //+" WHERE DateRecorded=current_date";
        Cursor res = db.rawQuery(query, null);
        return res;
    }


}
