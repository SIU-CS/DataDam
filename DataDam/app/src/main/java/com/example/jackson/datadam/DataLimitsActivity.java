package com.example.jackson.datadam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.app.ActivityManager;
import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLimitsActivity extends Activity {
    private Handler mHandler= new Handler();
    ActivityManager manager;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String Limits= "DataDamLimits";
    List<DataLimit> DataLimits= new ArrayList<DataLimit>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalimit_activity);
        final Button Add =(Button) findViewById(R.id.Add);
        final Button Home = (Button) findViewById(R.id.Home);
        final Intent AddIntent = new Intent(DataLimitsActivity.this,AddDataLimit.class);

        Add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(AddIntent);
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             finish();
            }
        });
        try{
            inputStream=openFileInput(Limits);
            Scanner scaninput= new Scanner(inputStream);
            while(scaninput.hasNext()){
             String name=scaninput.next();
             long bytes= Long.parseLong(scaninput.next());
             DataLimit newdatalimit= new DataLimit(name,bytes);
             DataLimits.add(newdatalimit);
            }
            scaninput.close();
            inputStream.close();
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            inputStream=openFileInput(Limits);
            Scanner scaninput= new Scanner(inputStream);
            while(scaninput.hasNext()){
                String name=scaninput.next();
                long bytes= Long.parseLong(scaninput.next());
                DataLimit newdatalimit= new DataLimit(name,bytes);
                DataLimits.add(newdatalimit);
            }
            scaninput.close();
            inputStream.close();
        }catch (Exception e){

        }
    }

}
