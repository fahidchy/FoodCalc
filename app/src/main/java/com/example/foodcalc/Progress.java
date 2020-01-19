package com.example.foodcalc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Progress extends AppCompatActivity {
    WebView webView;
    DatabaseHelper myDb;

    private User user;

    private Button btnLog;
    private EditText tfWeight;

    private TextView txtEmpty;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Integer> weights = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        myDb = new DatabaseHelper(this);

        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        tfWeight = (EditText) findViewById(R.id.tfNewWeight);
        btnLog = (Button) findViewById(R.id.btnLog);

        viewDate();
        BarChart barChart = (BarChart) findViewById(R.id.barchart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (!(weights.isEmpty())) {
            txtEmpty.setVisibility(View.GONE);
        }

        viewAll();
        tfWeight.setText(user.getWeight() + "");

        btnLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int weight = Integer.parseInt(tfWeight.getText().toString());
                System.out.println("This is the new weight");
                boolean isInserted = myDb.insertDataInWeight(weight);
                if (isInserted == true) {
                    Toast.makeText(getBaseContext(), "Data added in weight table", Toast.LENGTH_LONG).show();
                    user.setWeight(weight);
                    tfWeight.setText(user.getWeight() + "");
                    finish();
                    openProgress();
                } else {
                    Toast.makeText(getBaseContext(), "Something is not right", Toast.LENGTH_LONG).show();
                }
            }
        });

        for (int i = 0; i < weights.size(); i++) {
            entries.add(new BarEntry(i, weights.get(i)));
        }

        BarDataSet bardataset = new BarDataSet(entries, "Weights");
        BarData data = new BarData(bardataset);
        barChart.setData(data);

        barChart.getDescription().setText("Weight Log");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

    //For Navigation
    public void openShare() {
        Intent intent = new Intent(this, Share.class);
        startActivity(intent);
    }

    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void openProgress() {
        Intent intent = new Intent(this, Progress.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    //Getting weight record for graph
    public void viewDate() {
        Cursor res = myDb.viewWeight();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int weight = Integer.parseInt(res.getString(1));
            String dateOfrecord = res.getString(2);
            weights.add(weight);
            dates.add(dateOfrecord);
        }
    }

    //Getting all data
    public void viewAll() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Empty Data");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            String name = res.getString(1);
            int weight = Integer.parseInt(res.getString(2));
            int height = Integer.parseInt(res.getString(3));
            int goal = Integer.parseInt(res.getString(4));
            user = new User(name, weight, height, goal);
            user.setGoal(goal);

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

}
