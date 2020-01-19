package com.example.foodcalc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class SetAlerts extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private Button btnSkip;
    private Button btnSet;
    public static String breakfastTime, lunchTime, dinnerTime;
    DatabaseHelper myDb;
    Alerts alerts;
    int i = 0;
    public static int counter = 0;

    private TextView txtBreakfast;
    private TextView txtLunch;
    private TextView txtDinner;
    private NotificationHelper nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alerts);

        myDb = new DatabaseHelper(this);

        txtBreakfast = (TextView) findViewById(R.id.txtBreakfastTime);
        txtLunch = (TextView) findViewById(R.id.txtLunchTime);
        txtDinner = (TextView) findViewById(R.id.txtDinnerTime);

        Button btnSetBreakfast = (Button) findViewById(R.id.btnBreakfastTime);
        Button btnSetDinner = (Button) findViewById(R.id.btnDinnerTime);
        Button btnSetLunch = (Button) findViewById(R.id.btnLunchTime);

        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnSet = (Button) findViewById(R.id.btnSet);

        nm = new NotificationHelper(this);
        btnSetBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                i = 1;

            }
        });

        btnSetLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                i = 2;
            }
        });

        btnSetDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                i = 3;
            }
        });


        viewAll();

        if (counter > 0) {
            viewAll();

            txtBreakfast.setText(alerts.getBreakfastTime());
            txtLunch.setText(alerts.getLunchTime());
            txtDinner.setText(alerts.getDinnerTime());
            btnSkip.setText("Turn Off");

            btnSkip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cancelAlarm();
                }
            });
        } else {
            btnSkip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alerts = new Alerts("OFF", "OFF", "OFF");
                    myDb.insertDataInAlerts(alerts.getBreakfastTime(), alerts.getLunchTime(), alerts.getDinnerTime());
                    openHome();
                }
            });
        }
        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alerts = new Alerts(Temp.bTime, Temp.lTime, Temp.dTime);
                if (counter == 0) {
                    myDb.insertDataInAlerts(alerts.getBreakfastTime(), alerts.getLunchTime(), alerts.getDinnerTime());
                    openHome();
                } else {
                    myDb.updateDataInAlerts(alerts.getBreakfastTime(), alerts.getLunchTime(), alerts.getDinnerTime());
                    openSettings();
                }
                counter++;
                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        currentTime.set(Calendar.MINUTE, minute);
        currentTime.set(Calendar.SECOND, 0);


        if (i == 1) {
            TextView txtBreakfast = (TextView) findViewById(R.id.txtBreakfastTime);
            breakfastTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime.getTime());
            Temp.bTime = breakfastTime;
            txtBreakfast.setText(Temp.bTime);
            startAlarm(currentTime, 1);

        } else if (i == 2) {
            TextView txtLunch = (TextView) findViewById(R.id.txtLunchTime);
            lunchTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime.getTime());
            //lunchTime = hourOfDay + ":" + minute;
            Temp.lTime = lunchTime;
            txtLunch.setText(Temp.lTime);
            startAlarm(currentTime, 2);

        } else if (i == 3) {
            TextView txtDinner = (TextView) findViewById(R.id.txtDinnerTime);
            dinnerTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentTime.getTime());
            //dinnerTime = hourOfDay + ":" + minute;
            Temp.dTime = dinnerTime;
            txtDinner.setText(Temp.dTime);
            startAlarm(currentTime, 3);
        } else {
            Toast.makeText(view.getContext(), "Something Wroing Boi", Toast.LENGTH_SHORT).show();
        }
    }

    //Startalarm
    private void startAlarm(Calendar c, int rC) {
        AlarmManager aM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        //final int _id=(int) System.currentTimeMillis();
        PendingIntent pI = PendingIntent.getBroadcast(this, rC, intent, 0);
        if (c.before((Calendar.getInstance()))) {
            c.add(Calendar.DATE, 1);
        }
        aM.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pI);
    }

    //Cancel all alarms and update in database
    public void cancelAlarm() {
        for (i = 1; i <= 3; i++) {

            AlarmManager aM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pI = PendingIntent.getBroadcast(this, i, intent, 0);
            aM.cancel(pI);
        }

        txtBreakfast.setText("OFF");
        txtLunch.setText("OFF");
        txtDinner.setText("OFF");

        alerts = new Alerts(txtBreakfast.getText().toString().trim(),
                txtLunch.getText().toString().trim(),
                txtDinner.getText().toString().trim());

        myDb.updateDataInAlerts(alerts.getBreakfastTime(), alerts.getLunchTime(), alerts.getDinnerTime());

        Toast.makeText(getBaseContext(), "All alarms have been cancelled", Toast.LENGTH_SHORT).show();
        openSettings();
    }

    //SettingData
    public void viewAll() {
        Cursor res = myDb.getAllDataFromAlerts();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            if (id == 1) {
                alerts = new Alerts(res.getString(1), res.getString(2), res.getString(3));
            }
        }
    }


    //For displaying message
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //For Navigation
    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}

