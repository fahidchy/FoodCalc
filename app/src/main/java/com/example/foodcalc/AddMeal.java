package com.example.foodcalc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddMeal extends AppCompatActivity {
    private Button btnAddNewItem;
    private Button btnCancelItem;

    private EditText tfItemName;
    private EditText tfCalories;

    private TextView txtAddItem;

    private DatabaseHelper myDb;

    public String itemName;
    int calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        myDb = new DatabaseHelper(this);
        btnAddNewItem = (Button) findViewById(R.id.btnAddNewItem);
        btnCancelItem = (Button) findViewById(R.id.btnCancelItem);
        txtAddItem = (TextView) findViewById(R.id.txtAddItem);
        tfItemName = (EditText) findViewById(R.id.tfItemName);
        tfCalories = (EditText) findViewById(R.id.tfCalories);

        if (Add.counter > 0) {
            btnAddNewItem.setText("Update");
            txtAddItem.setText("Update Item");
            tfItemName.setText(Add.tempItem);
            tfCalories.setText(Add.tempCal + "");
            btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateInput()) {
                        finish();
                        openAdd();
                        Add.counter = 0;
                    }
                }
            });
        } else {
            btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getInput()) {
                        finish();
                        openAdd();
                    }
                }
            });
        }
        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdd();
            }
        });
    }

    //Validate the name text
    private boolean validateName() {
        String name = tfItemName.getText().toString().trim();

        if (name.isEmpty()) {
            return false;
        } else if (!(name.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$"))) {
            return false;
        } else {
            return true;
        }
    }

    //To get input
    public boolean getInput() {
        if (tfItemName.getText().toString().trim().equals("") || tfCalories.getText().toString().trim().equals("")) {
            showMessage("Empty Fields", "You cannot keep any field empty");
            return false;
        } else if (!(validateName())) {
            showMessage("Invalid Name", "Invalid input for item name");
            return false;
        } else if (itemExists(tfItemName.getText().toString().trim())) {
            showMessage("Item Already Exists", "The item is already in your list");
            return false;
        } else {
            itemName = tfItemName.getText().toString();
            calories = Integer.parseInt(tfCalories.getText().toString());
            boolean isInserted = myDb.insertDataInItems(itemName, calories);
            if (isInserted == true) {
                Toast.makeText(getBaseContext(), "Data added in item table", Toast.LENGTH_LONG).show();
                return true;
            } else
                Toast.makeText(getBaseContext(), "No data added in item table", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //To update data
    public boolean updateInput() {
        if (tfItemName.getText().toString().trim().equals("") || tfCalories.getText().toString().trim().equals("")) {
            showMessage("Empty Fields", "You cannot keep any field empty");
            return false;
        } else if (!(validateName())) {
            showMessage("Invalid Name", "Invalid input for item name");
            return false;
        } else if (itemExists(tfItemName.getText().toString().trim())) {
            showMessage("Item Already Exists", "The item is already in your list");
            return false;
        } else {

            itemName = tfItemName.getText().toString();
            calories = Integer.parseInt(tfCalories.getText().toString());
            boolean isUpdated = myDb.updateDataInItems(Add.itemId, itemName, calories);
            if (isUpdated == true) {
                Toast.makeText(getBaseContext(), "Data added in item table", Toast.LENGTH_LONG).show();
                return true;
            } else
                Toast.makeText(getBaseContext(), "No data added in item table", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //To check if value exists
    public boolean itemExists(String text) {
        Cursor res = myDb.existsItem(text);
        if (res.getCount() == 0) {
            return false;
        } else
            return true;
    }


    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //For Navigation
    public void openAdd() {
        Intent intent = new Intent(this, Add.class);
        startActivity(intent);
    }

}
