package com.example.jackson.datadam;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

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
    ArrayList<DataLimit> DataLimitsArray = new ArrayList<>(DataLimits);
    final Context context= this;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalimit_activity);
        final Button Add =(Button) findViewById(R.id.Add);
        final Button Home = (Button) findViewById(R.id.Home);
        final ListView Dataname= (ListView) findViewById(R.id.DataLimitsList);
        //final Intent AddIntent = new Intent(DataLimitsActivity.this,AddDataLimit.class);

        Add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //startActivity(AddIntent);
                final Dialog addDialog = new Dialog(context);
                addDialog.setContentView(R.layout.datalimit_add);
                addDialog.setTitle("Add Data Limit");
                Button Cancel = (Button) addDialog.findViewById(R.id.Cancel);
                Button AddData = (Button) addDialog.findViewById(R.id.Add);
                AddData.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText name= (EditText) addDialog.findViewById(R.id.NameText);
                        EditText bytes= (EditText) addDialog.findViewById(R.id.Bytes);
                        String sName=name.getText().toString();
                        String sBytes=bytes.getText().toString();
                        if(sName.matches("")||sBytes.matches("")){
                            Toast.makeText(getApplicationContext(), "You have a blank selection",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DataLimit newlimit= new DataLimit(sName,Long.parseLong(sBytes,10));
                        DataLimits.add(newlimit);
                        addDialog.dismiss();

                    }
                });
                Cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });
                addDialog.show();
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             finish();
            }
        });
       /* try{
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

        }*/
        ListView listView = (ListView) findViewById(R.id.DataLimitsList);

        ArrayAdapter<DataLimit> adapter = new ArrayAdapter<DataLimit>(this,android.R.layout.simple_expandable_list_item_1,DataLimits);
        listView.setAdapter(adapter);
    }

  /*  @Override
    protected void onResume() {0
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
    }*/

}
