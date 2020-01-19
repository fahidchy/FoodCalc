package com.example.foodcalc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btnUpdateItem;
    private Button btnAdd;
    private Button btnAddItem;
    int bC, lC, dC, oC;
    Spinner chooseMeal, chooseItem;
    Consumption con;

    private EditText tfItemName;
    private EditText tfCalories;

    private int calories = 0;
    private String itemName = "";
    public static int counter = 0;
    public static int itemId = 0;

    public static String tempItem;
    public static int tempCal;

    private DatabaseHelper myDb;
    private HashMap<String, Integer> itemCalories;

    ArrayList<String> itemNames = new ArrayList<String>();
    ArrayList<Item> items = new ArrayList<>();

    String meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myDb = new DatabaseHelper(this);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnUpdateItem = (Button) findViewById(R.id.btnUpdateItem);

        //Fore testing
        itemCalories = new HashMap<>();


        chooseMeal = (Spinner) findViewById(R.id.cboMeal);
        chooseItem = (Spinner) findViewById(R.id.cboItems);
        tfCalories = (EditText) findViewById(R.id.tfCalories);

        tfCalories.setEnabled(false);

        viewAllFromItems();

        for (int i = 0; i < items.size(); i++) {
            String itemName = items.get(i).getItemName();
            itemNames.add(itemName);
        }

        ArrayAdapter<CharSequence> adapterMeal = ArrayAdapter.createFromResource(this, R.array.meals, android.R.layout.simple_spinner_item);
        adapterMeal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseMeal.setAdapter(adapterMeal);
        chooseMeal.setOnItemSelectedListener(this);

        ArrayAdapter adapterItems = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, itemNames);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseItem.setAdapter(adapterItems);
        chooseItem.setOnItemSelectedListener(this);
        System.out.println("Before if");


        btnUpdateItem.setText("Update");

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemName.equals("")) {
                    showMessage("Add Item First", "Looks like you have not added any item, add an item to the list hen you can update later.");
                } else {
                    counter = 1;
                    openAddItem();
                }

            }
        });
        System.out.println("Set On");
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getInput()) {
                    Toast.makeText(getBaseContext(), "Your calories has been added", Toast.LENGTH_SHORT).show();
                    openHome();
                }
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openAddItem();
            }
        });
    }

    //For getting input
    public boolean getInput() {
        if (itemName.equals("") || Integer.parseInt(tfCalories.getText().toString()) == 0 || tfCalories.getText().toString().equals("")) {
            showMessage("No item selected", "If there are no items in the list, you can add a new one using the button under the list. ");
            return false;
        } else {
            calories = Integer.parseInt(tfCalories.getText().toString());
            if (meal.equals("Breakfast")) {
                Temp.breakfast += calories;
            } else if (meal.equals("Lunch")) {
                Temp.lunch += calories;
            } else if (meal.equals("Dinner")) {
                Temp.dinner += calories;
            } else {
                Temp.others += calories;
            }
            Consumption.total += calories;
            User.current += calories;
            insertData();
            return true;
        }
    }

    //For inserting data in database
    public void insertData() {
        Cursor res = myDb.getAllDataFromConsumption();
        if (res.getCount() == 0) {
            myDb.insertDataInConsumption(Temp.breakfast, Temp.lunch, Temp.dinner, Temp.others);
        } else {
            myDb.updateDataInConsumption(Temp.breakfast, Temp.lunch, Temp.dinner, Temp.others);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        Spinner chooseMeal = (Spinner) parent;
        Spinner chooseItem = (Spinner) parent;

        if (chooseMeal.getId() == R.id.cboMeal) {
            String item = parent.getItemAtPosition(position).toString();
            meal = item;
        }
        if (chooseItem.getId() == R.id.cboItems) {
            String txt = parent.getItemAtPosition(position).toString();
            itemName = txt;

            Item item;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItemName().equals(txt)) {
                    calories = items.get(i).getCalories();
                    itemId = items.get(i).getId();
                }
            }

            tempItem = itemName;
            tempCal = calories;
            tfCalories.setText(calories + "");
            //tfCalories.setText(itemCalories.get(itemName) + "");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //For viewing all saved items
    public void viewAllFromItems() {
        Cursor res = myDb.getAllDataFromItems();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            String text = res.getString(1);
            int calories = Integer.parseInt(res.getString(2));
            System.out.println("This is the item " + text);
            Item item = new Item(id, text, calories);
            items.add(item);
        }
    }

    //For getting consumption data
    public void viewAllFromConsumption() {
        Cursor res = myDb.getAllDataFromConsumption();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            int bC = Integer.parseInt(res.getString(1));
            int lC = Integer.parseInt(res.getString(2));
            int dC = Integer.parseInt(res.getString(3));
            int oC = Integer.parseInt(res.getString(4));
            if (res.getCount() == 0) {
                return;
            }
            con = new Consumption(bC, lC, dC, oC);
            Temp.breakfast = bC;
            Temp.lunch = lC;
            Temp.dinner = dC;
            Temp.others = oC;
            Consumption.total = bC + lC + dC + oC;

        }
    }

    //For showing message
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //For Navigation
    public void openAddItem() {
        Intent intent = new Intent(this, AddMeal.class);
        startActivity(intent);
        finish();
    }

    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}

