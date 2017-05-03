package com.example.jackson.datadam;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.app.ActivityManager;
import android.widget.*;
import android.content.Context;
import java.io.*;


public class AddDataLimit extends Activity{
    FileOutputStream outputStream;
    String Limits= "DataDamLimits";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalimit_add);
        final Button Cancel = (Button) findViewById(R.id.Cancel);
        final Button Add = (Button) findViewById(R.id.Add);

        Add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText name= (EditText) findViewById(R.id.NameText);
                EditText bytes= (EditText) findViewById(R.id.Bytes);
                String sName=name.getText().toString();
                String sBytes=bytes.getText().toString();
                if(sName.matches("")||sBytes.matches("")){
                    Toast.makeText(getApplicationContext(), "You have a blank selection",Toast.LENGTH_SHORT).show();
                    return;
                }
               try{
                   outputStream=openFileOutput(Limits,Context.MODE_APPEND);
                   PrintWriter pw= new PrintWriter(outputStream);

               }catch(Exception e){

               }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
