package com.example.jackson.datadam;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
    private final String TAG = "::DATALIMITSACTIVITY::";
    ActivityManager manager;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String Limits= "DataDamLimits";
    List<DataLimit> DataLimits= new ArrayList<DataLimit>();
    final Context context= this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalimit_activity);
        final Button Add = (Button) findViewById(R.id.Add);
        final Button Home = (Button) findViewById(R.id.Home);
        final TextView Dataname = (TextView) findViewById(R.id.DataName);
        try {
            inputStream = openFileInput(Limits);
            Scanner scaninput = new Scanner(inputStream);
            while (scaninput.hasNext()) {
                String name = scaninput.next();
                long bytes = Long.parseLong(scaninput.next());
                long bytesused = Long.parseLong(scaninput.next());
                String notification = scaninput.next();
                DataLimit newdatalimit = new DataLimit(name, bytes, bytesused, notification);
                DataLimits.add(newdatalimit);
            }
            scaninput.close();
            inputStream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exeception Thrown"+e.getMessage());
        }

        Add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Dialog addDialog = new Dialog(context);
                addDialog.setContentView(R.layout.datalimit_add);
                addDialog.setTitle("Add Data Limit");
                Button Cancel = (Button) addDialog.findViewById(R.id.Cancel);
                Button AddData = (Button) addDialog.findViewById(R.id.Add);
                AddData.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText name = (EditText) addDialog.findViewById(R.id.NameText);
                        EditText bytes = (EditText) addDialog.findViewById(R.id.Bytes);
                        EditText notificaiton = (EditText) addDialog.findViewById(R.id.NotificationString);
                        String sName = name.getText().toString();
                        String sBytes = bytes.getText().toString();
                        String snotification = notificaiton.getText().toString();
                        if (sName.matches("") || sBytes.matches("") || snotification.matches("")) {
                            Toast.makeText(getApplicationContext(), "You have a blank selection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DataLimit newlimit = new DataLimit(sName, Long.parseLong(sBytes, 10), snotification);
                        Dataname.setText(newlimit.toString());
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
                try {
                    outputStream= openFileOutput(Limits, Context.MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter(outputStream);
                   for(DataLimit dataLimit: DataLimits){
                       pw.println(dataLimit.toString());
                   }
                    pw.close();
                    outputStream.close();
                } catch (Exception e) {

                }
                finish();
            }
        });




    }

}
