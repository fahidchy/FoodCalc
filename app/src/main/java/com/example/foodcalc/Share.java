package com.example.foodcalc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class Share extends AppCompatActivity {

    private Button btnShare;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        myDb = new DatabaseHelper(this);
        btnShare = (Button) findViewById(R.id.btnShare);

        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                export(v);
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
                Toast.makeText(this,"Home Selected", Toast.LENGTH_SHORT).show();
                openHome();
                return true;
            case R.id.nav_chart:
                Toast.makeText(this,"Chart Selected", Toast.LENGTH_SHORT).show();
                openProgress();
                return true;
            case R.id.nav_settings:
                Toast.makeText(this,"Settings Selected", Toast.LENGTH_SHORT).show();
                openSettings();
                return true;
            case R.id.nav_share:
                Toast.makeText(this,"Share Selected", Toast.LENGTH_SHORT).show();
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


    public void export(View view){
        //Generate necessary data from database
        Cursor res = myDb.getAllData();
        StringBuffer data = new StringBuffer();
        while (res.moveToNext()){
            data.append("Name =" + res.getString(1) + "\n");
            data.append("Weight =" + res.getString(2) + "\n");
            data.append("Height =" + res.getString(3) + "\n");
            data.append("Goal =" + res.getString(4) + "\n\n");
        }
        Cursor result=myDb.getAllConsumptions();
        while (result.moveToNext()){
            data.append("Date Of Record, Consumption\n");
            int a=Integer.parseInt(result.getString(1));
            int b=Integer.parseInt(result.getString(2));
            int c=Integer.parseInt(result.getString(3));
            int d=Integer.parseInt(result.getString(4));
            int total= a+b+c+d;
            data.append(result.getString(5)+","+total+"\n");
        }


        try{

            //Saving the file to device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //Exporting contents
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.foodcalc.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
