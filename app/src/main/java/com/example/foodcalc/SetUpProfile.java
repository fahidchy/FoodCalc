package com.example.foodcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SetUpProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btnSetUp;
    private TextInputLayout textInputUsername;
    private EditText tfName, tfWeight, tfHeight;
    DatabaseHelper myDb;
    String name, strGoal;
    Spinner chooseGoal;
    int height, weight, goal;
    public static User user;
    private TextView txtSetUpProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        myDb = new DatabaseHelper(this);

        chooseGoal = findViewById(R.id.cboGoal);
        ArrayAdapter<CharSequence> adapterGoal = ArrayAdapter.createFromResource(this, R.array.goal, android.R.layout.simple_spinner_item);
        adapterGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseGoal.setAdapter(adapterGoal);
        chooseGoal.setOnItemSelectedListener(this);

        tfName = (EditText) findViewById(R.id.tfName);
        tfWeight = (EditText) findViewById(R.id.tfWeight);
        tfHeight = (EditText) findViewById(R.id.tfHeight);
        btnSetUp = (Button) findViewById(R.id.btnSetUp);
        txtSetUpProfile = (TextView) findViewById(R.id.txtSetUpProfile);

        btnSetUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (getInput()) {
                    finish();
                    openSetAlerts();
                }
            }

        });

        if (getData()) {
            viewAll();
            txtSetUpProfile.setText("Update Profile");
            btnSetUp.setText("Update");
            btnSetUp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (updateProfile())
                        openSettings();
                }
            });
        }

    }

    //Check if name is valid
    private boolean validateUsername() {
        String usernameInput = tfName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            return false;
        } else if (!(usernameInput.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$"))) {
            return false;
        } else {
            return true;
        }
    }

    //Get all user data from database
    public void viewAll() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Empty Data");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            name = res.getString(1);
            weight = Integer.parseInt(res.getString(2));
            height = Integer.parseInt(res.getString(3));
            goal = Integer.parseInt(res.getString(4));
            user = new User(name, weight, height, goal);
            tfName.setText(user.getName());
            tfWeight.setText(user.getWeight() + "");
            tfHeight.setText(user.getHeight() + "");
            chooseGoal.setPrompt(user.getGoal() + "");
        }
    }

    //Showing message
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //On inserting for the first time
    public boolean getInput() {
        if (validateUsername()) {
            try {
                name = tfName.getText().toString();
                height = Integer.parseInt(tfHeight.getText().toString());
                weight = Integer.parseInt(tfWeight.getText().toString());
                goal = Integer.parseInt(strGoal);

                String text = name + " " + height + " " + weight + " " + goal + " ";
                System.out.println(text);
                boolean isInserted = myDb.insertData(name, weight, height, goal);
                myDb.insertDataInWeight(weight);
                if (isInserted) {
                    user = new User(name, weight, height, goal);
                    tfName.setText(user.getName());
                    tfWeight.setText(user.getWeight() + "");
                    tfHeight.setText(user.getHeight() + "");
                    user.setGoal(goal);


                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();

                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                showMessage("Error", "Please do not leave fields empty");
                return false;
            }
        } else {
            showMessage("Error", "Please enter a valid name");
            return false;
        }
    }

    //On Updating
    public boolean updateProfile() {
        if (validateUsername()) {
            try {
                name = tfName.getText().toString();
                height = Integer.parseInt(tfHeight.getText().toString());
                weight = Integer.parseInt(tfWeight.getText().toString());
                goal = Integer.parseInt(strGoal);

                String text = name + " " + height + " " + weight + " " + goal + " ";
                System.out.println(text);
                boolean isUpdate = myDb.updateData(
                        name,
                        weight,
                        height,
                        goal);
                if (isUpdate) {
                    user = new User(name, weight, height, goal);
                    tfName.setText(user.getName());
                    tfWeight.setText(user.getWeight() + "");
                    tfHeight.setText(user.getHeight() + "");
                    user.setGoal(goal);
                    Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(this, "Data Not Updated", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception ex) {
                showMessage("Error", "Please do not leave fields empty");
                return false;
            }
        } else {
            showMessage("Error", "Please enter correct name");
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if (chooseGoal.getId() == R.id.cboGoal) {
            String item = parent.getItemAtPosition(position).toString();
            strGoal = item;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Check if data already there
    public boolean getData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return false;
        } else
            return true;
    }

    //For Navigation
    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void openSetAlerts() {
        Intent intent = new Intent(this, SetAlerts.class);
        startActivity(intent);
    }
}
