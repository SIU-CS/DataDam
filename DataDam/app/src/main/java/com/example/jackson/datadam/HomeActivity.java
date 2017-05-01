package com.example.jackson.datadam;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

        import android.content.Intent;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.net.TrafficStats;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.View;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Button;
        import android.app.ActivityManager;
        import android.net.NetworkInfo;
        import android.content.Context;
        import android.app.ActivityManager.RunningServiceInfo;
        import android.net.ConnectivityManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.io.*;
import java.util.Scanner;

import 	android.app.ListActivity;
//Custom application class
//Used to store the name, id, and data usage of an application on the device.

class Application{
    private String name;
    private int uid;
    private long previousbytes;
    private long bytes=0;
    public Application next;

    public Application(String name, int uid, long previousbytes, long bytes){
        this.name=name;
        this.uid=uid;
        this.previousbytes=previousbytes;
        this.bytes= bytes;
    }
    public Application(String name, int uid, long previousbytes){
        this.name=name;
        this.uid=uid;
        this.previousbytes=previousbytes;
        bytes= previousbytes;
    }
    //Adds bytes from current run cycle to the total number of bytes used.
    //Updates previousbytes with the parameter to create a new comparison flag.
    public void addBytes(long newbytes){
        long bytesused= newbytes-previousbytes;
        bytes= bytesused + bytes;
        previousbytes= newbytes;
    }
    //Updates previousbytes with new flag.
    //Only used when application is online
    public void updatePrevious(long bytes){
        previousbytes=bytes;
    }
    public int getUid(){
        return uid;
    }
    public long getBytes(){
        return bytes;
    }
    public String getName(){return name;}

    @Override
    public String toString(){
        return name+" "+uid+ " "+bytes;
    }
}
public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long PreviousRX = 0;
    private long PreviousTX= 0;
    private long currentRX, currentTX, rxBytes, txBytes;
    ActivityManager manager;
    ConnectivityManager connectMgn;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    String storage= "DataDamStorage";
    String Limits= "DataDamLimits";
    List<ActivityManager.RunningServiceInfo> runningservices;
    List<DataLimit> DataLimits= new ArrayList<DataLimit>();
    List<Application>applications= new ArrayList<Application>();
    private TextView RX;
    private TextView TX;
    private TextView HighestName;
    private TextView HighestValue;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        PreviousRX = TrafficStats.getTotalRxBytes();
        PreviousTX = TrafficStats.getTotalTxBytes();
        RX = (TextView) findViewById(R.id.RX);
        TX = (TextView) findViewById(R.id.TX);
        HighestName= (TextView) findViewById(R.id.HN);
        HighestValue= (TextView) findViewById(R.id.HV);
        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        connectMgn= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final Button DataLimits= (Button) findViewById(R.id.DataLimits);
        final Intent DataIntent = new Intent(HomeActivity.this,DataLimitsActivity.class);
        //Creates a sudo storage file if it doesn't already exist, otherwise does nothing
        try {
            outputStream=openFileOutput(storage, Context.MODE_APPEND);
            outputStream.close();
        }catch(Exception e){

        }
        try {
            outputStream=openFileOutput(Limits,Context.MODE_APPEND);
            outputStream.close();
        }catch(Exception e){

        }

        //Reads from storage file then populates the rx, tx, and applications with data from the scanner.
        try{
               inputStream=openFileInput(storage);
               Scanner scaninput= new Scanner(inputStream);
            //Scan once for the totalbytes value
               rxBytes= Long.parseLong(scaninput.next());
               txBytes= Long.parseLong(scaninput.next());
            //
               while(scaninput.hasNext()){
                   int uid = scaninput.nextInt();
                   String name=scaninput.next();
                    long previousbytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    long bytes= scaninput.nextLong()+previousbytes;
                     Application newapplication= new Application(name,uid,previousbytes,bytes);
                    applications.add(newapplication);
               }
            scaninput.close();
            inputStream.close();
        }catch(Exception e){

        }

        //Button listener for DataLimits, starts the DataLimits activity on click.
        DataLimits.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(DataIntent);
                //moveTaskToBack(true);
            }
        });


        Applicationupdate(manager);
// Tests to see whether the Data Dam application is compatible with TrafficStats
        if (PreviousRX== TrafficStats.UNSUPPORTED || PreviousTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }
    //Main runnable for Data Dam
    //Compares current bytes used to previous markers to assess how much data has been used within that time frame
    //The values for the total data used by the device and the data for individual applications is updated by adding the data used in the time frame to each individual total.

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            Applicationupdate(manager);
            long totalbytes= TrafficStats.getTotalRxBytes();
            currentRX = totalbytes - PreviousRX;
            PreviousRX= totalbytes;
            totalbytes = TrafficStats.getTotalTxBytes();
            currentTX= totalbytes - PreviousTX;
            PreviousTX= totalbytes;
                rxBytes = rxBytes + currentRX;
                txBytes= txBytes + currentTX;
                RX.setText(Long.toString(rxBytes));
                TX.setText(Long.toString(txBytes));
                for(Application application: applications){
                  int uid = application.getUid();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    application.addBytes(bytes);
                }
           try {
                outputStream=openFileOutput(storage, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(outputStream);
               //Change to the pw.println(totalbytesvalue)
                pw.print(rxBytes + " ");
                pw.println(txBytes);
               //
                for(Application application: applications){
                    pw.println(application.toString());
                }
                pw.close();
                outputStream.close();
            }catch(Exception e){

            }
            Application highest = HighestUsingApplication();
            HighestName.setText(highest.getName());
            HighestValue.setText(Long.toString(highest.getBytes()));
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

// Checks for any new services and adds them to a permanent list
// If list is empty, the currently running services form a new list
    private void Applicationupdate(ActivityManager manager){
        runningservices=manager.getRunningServices(Integer.MAX_VALUE);
        if(applications.isEmpty()){
            for(RunningServiceInfo runningservice : runningservices){
                int uid = runningservice.uid;
                String name=runningservice.service.getPackageName();
                long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                Application newapplication= new Application(name,uid,bytes);
                applications.add(newapplication);
            }
        }
        else{
            for(RunningServiceInfo runningservice :runningservices){

                boolean duplicate = false;
                int uid = runningservice.uid;
                for(Application application: applications){
                    if(uid== application.getUid()){
                        duplicate= true;
                        break;
                    }

                }
                if(!duplicate){
                    String name=runningservice.service.getPackageName();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    Application newapplication= new Application(name,uid,bytes);
                    applications.add(newapplication);
                }


            }
        }
    }
    //Determines the application that is using the most data.
    //This is done by comparing each applications data usage and returning the one with highest value
    private Application HighestUsingApplication(){
        Application highest= null;
        for(Application application: applications){
            if(highest==null)
                highest=application;
            else{
                if(highest.getBytes()<application.getBytes())
                    highest=application;
            }
        }
        return highest;
    }
}





