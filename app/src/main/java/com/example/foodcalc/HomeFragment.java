package com.example.foodcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button btnAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home1,container,false);
        btnAdd = v.findViewById(R.id.btnAdd1);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openAdd();
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public void openAdd() {
        Intent intent = new Intent(this.getActivity(), Add.class);
        startActivity(intent);
    }

    private Button btnLog;
    private EditText tfWeight;
    private Button btnAdd;

    private TextView txtGoal;
    private TextView txtCurrent;

    private TextView txtInfo;
    String name;
    //int bC,lC,dC,oC;
    Consumption con;
    int id,weight,height,goal;
    public static User user;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            openSetup();
        }

        myDb = new DatabaseHelper(this);
        if(!(getData())){
            openSetup();
            showMessage("Details Needed","Please enter your details first");
        }
        if(getData()) {
            viewAllFromConsumption();
            btnAdd = (Button) findViewById(R.id.btnAddConsumption);
            btnAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    openAdd();
                }
            });

            txtGoal = (TextView) findViewById(R.id.txtGoal);

            viewAll();
            //tfWeight = (EditText) findViewById(R.id.tfNewWeight);

            // tfWeight.setText(user.getWeight()+"");
            txtGoal.setText(user.getGoal() + "");



            txtCurrent = (TextView) findViewById(R.id.txtCurrent);
            txtCurrent.setText(Consumption.total + "");




            txtInfo = (TextView) findViewById(R.id.txtInfo);
            if(Consumption.total==0){
                txtInfo.setText("Looks like you haven't eaten the whole day\nStart by adding something using the button below");
            }
            else if(Consumption.total>Integer.parseInt(txtGoal.getText().toString())){
                txtInfo.setText("Wow! Looks like you have eaten a lot today\nTry and reduce to keep up with your goal");
            }
            else if(Consumption.total<Integer.parseInt(txtGoal.getText().toString())){
                txtInfo.setText("Keep it up\nTry and keep the calories intake low");
            }
            else if(Consumption.total<Integer.parseInt(txtGoal.getText().toString())){
                txtInfo.setText("DONT EAT ANYMORE\nOR ELSE YOU MIGHT EXCEED YOUR GOAL");
            }
            Consumption.goal=Integer.parseInt(txtGoal.getText().toString());
            //myDb.insertData("",0,0,0);
        }
        else{
            openSetup();
        }

        List<PieEntry> pieEntries=new ArrayList<>();
        pieEntries.add(new PieEntry(Consumption.total,""));
        int difference;
        if(Consumption.goal-Consumption.total<=0){
            difference=0;
        }else{
            difference=Consumption.goal-Consumption.total;
        }
        pieEntries.add(new PieEntry(difference,""));

        PieDataSet dataSet=new PieDataSet(pieEntries,"");
        dataSet.setColors(new int[] {Color.rgb(0,43,85),Color.rgb(100,177,255)});
        PieData data=new PieData(dataSet);


        PieChart chart= (PieChart) findViewById(R.id.piechart);
        chart.getDescription().setText("");
        chart.setData(data);
        chart.animateY(500);
        chart.invalidate();
    }




    public void viewAll() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Empty Data");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            id=Integer.parseInt(res.getString(0));
            name=res.getString(1);
            weight=Integer.parseInt(res.getString(2));
            height=Integer.parseInt(res.getString(3));
            goal=Integer.parseInt(res.getString(4));
            res.getString(2);
            res.getString(3);
            res.getString(4);
            user = new User(name,weight,height,goal);
            user.setGoal(goal);
            //txtGoal.setText(user.getGoal()+"");
            //viewDate();
        }
        //showMessage("Data", buffer.toString());
    }

    public boolean getData(){
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "Empty Data");
            return false;
        }else
            return true;
    }




    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void openSetup() {
        Intent intent = new Intent(this, SetUpProfile.class);
        startActivity(intent);
    }

    public void openAdd() {
        Intent intent = new Intent(this, Add.class);
        startActivity(intent);
    }


    public void getInput() {

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

    public void viewAllFromConsumption() {
        Cursor res = myDb.getAllDataFromConsumption();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            int id=Integer.parseInt(res.getString(0));
            int bC=Integer.parseInt(res.getString(1));
            int lC=Integer.parseInt(res.getString(2));
            int dC=Integer.parseInt(res.getString(3));
            int oC=Integer.parseInt(res.getString(4));
            if (res.getCount() == 0) {
                return;
            }
            con=new Consumption(bC,lC,dC,oC);
            Temp.breakfast=bC;
            Temp.lunch=lC;
            Temp.dinner=dC;
            Temp.others=oC;
            Consumption.total=bC+lC+dC+oC;
            //itemNames.add(item);
//            Toast.makeText(getBaseContext(), itemCalories.get("Biryani"), Toast.LENGTH_SHORT).show();
        }
    }
}
