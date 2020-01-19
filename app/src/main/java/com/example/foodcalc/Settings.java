package com.example.foodcalc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity{
//        implements AdapterView.OnItemSelectedListener{
//    private Button btnSettingGoal;
private Button btnUpdateProfile;
    private Button btnSetAlerts;
    int goal;
    String strGoal;
    Spinner chooseGoal;
    private DatabaseHelper myDb;

    private TextView txtBreakfastTime;
    private TextView txtLunchTime;
    private TextView txtDinnerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myDb = new DatabaseHelper(this);

        txtBreakfastTime=(TextView)findViewById(R.id.txtBreakfastTime);
        txtLunchTime=(TextView)findViewById(R.id.txtLunchTime);
        txtDinnerTime=(TextView)findViewById(R.id.txtDinnerTime);
        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnSetAlerts = (Button) findViewById(R.id.btnSetAlert);

        viewAll();

        txtBreakfastTime.setText(Temp.bTime);
        txtLunchTime.setText(Temp.lTime);
        txtDinnerTime.setText(Temp.dTime);



        btnSetAlerts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SetAlerts.counter++;
                openAlerts();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSetUp();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                openHome();
                return true;
            case R.id.nav_chart:
                openProgress();
                return true;
            case R.id.nav_settings:
                openSettings();
                return true;
            case R.id.nav_share:
                openShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //For navigation
    public void openShare(){
        Intent intent = new Intent(this,Share.class);
        startActivity(intent);
    }
    public void openSetUp(){
        Intent intent = new Intent(this,SetUpProfile.class);
        startActivity(intent);
    }
    public void openHome(){
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
    public void openProgress(){
        Intent intent = new Intent(this,Progress.class);
        startActivity(intent);
    }
    public void openSettings(){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }
    public void openAlerts() {
        Intent intent = new Intent(this, SetAlerts.class);
        startActivity(intent);
    }

    //For getting all data
    public void viewAll() {
        Cursor res = myDb.getAllDataFromAlerts();
        if (res.getCount() == 0) {
            Temp.bTime="OFF";
            Temp.lTime="OFF";
            Temp.dTime="OFF";
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {

            int id=Integer.parseInt(res.getString(0));
            Temp.bTime=res.getString(1);
            Temp.lTime=res.getString(2);
            Temp.dTime=res.getString(3);
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

}
