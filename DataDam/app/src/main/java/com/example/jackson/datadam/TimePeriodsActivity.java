package com.example.jackson.datadam;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackson.datadam.DataLimit;
import com.example.jackson.datadam.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TimePeriodsActivity extends Activity {
    private Handler mHandler= new Handler();
    ActivityManager manager;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String Periods= "DataDamPeriods";
    List<TimePeriod> TimePeriods= new ArrayList<TimePeriod>();
    final Context context= this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeperiod_activity);
        final Button Add = (Button) findViewById(R.id.Add);
        final Button Home = (Button) findViewById(R.id.Home);
        final TextView Dataname = (TextView) findViewById(R.id.DataName);
        try {
            inputStream = openFileInput(Periods);
            Scanner scaninput = new Scanner(inputStream);
            while (scaninput.hasNext()) {
                String name = scaninput.next();
                int timeperiod = Integer.parseInt(scaninput.next());
                int timepast= Integer.parseInt(scaninput.next());
                long bytesused = Long.parseLong(scaninput.next());
                String notification = scaninput.next();
                TimePeriod newtimeperiod = new TimePeriod(name, timeperiod, timepast, bytesused, notification);
                TimePeriods.add(newtimeperiod);
            }
            scaninput.close();
            inputStream.close();
        } catch (Exception e) {

        }

        Add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog addDialog = new Dialog(context);
                addDialog.setContentView(R.layout.timeperiod_add);
                addDialog.setTitle("Add Time Period");
                Button Cancel = (Button) addDialog.findViewById(R.id.Cancel);
                Button AddData = (Button) addDialog.findViewById(R.id.Add);
                AddData.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText name = (EditText) addDialog.findViewById(R.id.NameText);
                        EditText time = (EditText) addDialog.findViewById(R.id.Time);
                        EditText notificaiton = (EditText) addDialog.findViewById(R.id.NotificationString);
                        String sName = name.getText().toString();
                        String sTime = time.getText().toString();
                        String snotification = notificaiton.getText().toString();
                        if (sName.matches("") || sTime.matches("") || snotification.matches("")) {
                            Toast.makeText(getApplicationContext(), "You have a blank selection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TimePeriod newperiod = new TimePeriod(sName, Integer.parseInt(sTime, 10), snotification);
                        Dataname.setText(newperiod.toString());
                        TimePeriods.add(newperiod);
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
                    outputStream= openFileOutput(Periods, Context.MODE_PRIVATE);
                    PrintWriter pw = new PrintWriter(outputStream);
                    for(TimePeriod timeperiod: TimePeriods){
                        pw.println(timeperiod.toString());
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