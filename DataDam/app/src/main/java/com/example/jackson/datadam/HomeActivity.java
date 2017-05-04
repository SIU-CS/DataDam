package com.example.jackson.datadam;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.net.TrafficStats;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.app.ActivityManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.net.ConnectivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.io.*;
import java.util.Scanner;

import 	android.app.ListActivity;
import android.widget.Toast;

public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long PreviousRX = 0;
    private long PreviousTX= 0;
    private Calendar recorder= Calendar.getInstance();
    private int previoustime=0;
    private int currenttime= recorder.get(Calendar.SECOND);
    private int timepast=0;
    private long currentRX, currentTX, rxBytes, txBytes;
    ActivityManager manager;

    ConnectivityManager connectMgn;

    FileInputStream inputStream;
    FileOutputStream outputStream;
    String storage= "DataDamStorage";
    String Limits= "DataDamLimits";
    String Periods="DataDamPeriods";

    List<ActivityManager.RunningServiceInfo> runningservices;
    List<Application> mAppList = new ArrayList<Application>();
    List<DataLimit> DataLimits= new ArrayList<DataLimit>();
    List<TimePeriod> TimePeriods= new ArrayList<TimePeriod>();


//    public List<ActivityManager.RunningAppProcessInfo> processes = AndroidProcesses.getRunningAppProcessInfo(getApplication());

//    private RecyclerView recyclerView;
//    private ApplicationAdapter mAppAdapter;


    public String[] getRunningApps() {
        int n = mAppList.size();

        String[] appsRunning = new String[n];

        for(int i = 0; i < n; i++) {
            appsRunning[i] = mAppList.get(i).getName();
        }

//        Object [] applicationsArray = mAppList.toArray();

        return appsRunning;
    }




    private TextView RX;
    private TextView TX;
    private TextView HighestName;
    private TextView HighestValue;

    public List<Application> getmAppList(){
        return mAppList;
    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // adapter = new ArrayAdapter (this,R.layout.application_view,mAppList);

        PreviousRX = TrafficStats.getTotalRxBytes();
        PreviousTX = TrafficStats.getTotalTxBytes();
        RX = (TextView) findViewById(R.id.RX);
        TX = (TextView) findViewById(R.id.TX);
        HighestName= (TextView) findViewById(R.id.HN);
        HighestValue= (TextView) findViewById(R.id.HV);
        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        connectMgn= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final Button DataLimitsButton= (Button) findViewById(R.id.DataLimits);
        final Button TimeperiodButton= (Button) findViewById(R.id.TimePeriods);
        final Button GraphButton= (Button) findViewById(R.id.Graph);
        final Intent DataIntent = new Intent(HomeActivity.this,DataLimitsActivity.class);
        final Intent PeriodIntent= new Intent(HomeActivity.this,TimePeriodsActivity.class);
        final Intent GraphIntent = new Intent(HomeActivity.this,GraphActivity.class);
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
        try {
            outputStream=openFileOutput(Periods,Context.MODE_APPEND);
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
                mAppList.add(newapplication);
            }
            scaninput.close();
            inputStream.close();
        }catch(Exception e){

        }
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


        }

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

        //Button listener for DataLimits, starts the DataLimits activity on click.
        DataLimitsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(DataIntent);
            }
        });

        GraphButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(GraphIntent);
            }
        });

        TimeperiodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(PeriodIntent);
            }
        });
        Applicationupdate(manager);

        if (PreviousRX== TrafficStats.UNSUPPORTED || PreviousTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // COMPLETED (41) Set the layoutManager on mRecyclerView
        ListView listView = (ListView) findViewById(R.id.list_item);
//        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
//        mAppAdapter = new ApplicationAdapter(mAppList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAppAdapter);

        //array adapter for list view

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getRunningApps());
        listView.setAdapter(adapter);

    }

    private final Runnable mRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
            recorder= Calendar.getInstance();
            previoustime=currenttime;
            currenttime=recorder.get(Calendar.SECOND);
            timepast=currenttime-previoustime;
            for(Application application: mAppList){
                int uid = application.getUid();
                long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                application.addBytes(bytes);
            }
            for(DataLimit dataLimit:DataLimits){

                    dataLimit.addBytes(rxBytes + txBytes);
                if(dataLimit.isComplete()) {
                    Toast.makeText(getApplicationContext(), dataLimit.getNotification(), Toast.LENGTH_SHORT).show();
                    DataLimits.remove(dataLimit);
                }
            }
           for(TimePeriod timePeriod:TimePeriods){
             timePeriod.addTime(timepast,rxBytes+txBytes);
                if(timePeriod.isComplete()){
                    Toast.makeText(getApplicationContext(), timePeriod.getNotification(), Toast.LENGTH_SHORT).show();
                    TimePeriods.remove(timePeriod);
                }
            }

            try {
                outputStream=openFileOutput(storage, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(outputStream);
                pw.print(rxBytes + " ");
                pw.println(txBytes);
                //
                for(Application application: mAppList){
                    pw.println(application.toString());
                }
                pw.close();
                outputStream.close();
            }catch(Exception e){

            }
            try {
                outputStream= openFileOutput(Limits, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(outputStream);
                for(DataLimit dataLimit: DataLimits){
                    if(!dataLimit.isComplete()) {
                        pw.println(dataLimit.toString());
                    }
                }
                pw.close();
                outputStream.close();
            } catch (Exception e) {

            }
            try {
                outputStream= openFileOutput(Periods, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(outputStream);
                for(TimePeriod timeperiod: TimePeriods){
                    if(!timeperiod.isComplete()) {
                        pw.println(timeperiod.toString());
                    }
                }
                pw.close();
                outputStream.close();
            } catch (Exception e) {

            }

            Application highest = HighestUsingApplication();
            HighestName.setText(highest.getName());
            HighestValue.setText(Long.toString(highest.getBytes()));

            mHandler.postDelayed(mRunnable, 1000);
        }
    };

// Checks for any new services and adds them to a permanent list
// If list is empty, the currently running services form a new list
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private void Applicationupdate(ActivityManager manager){
        runningservices=manager.getRunningServices(Integer.MAX_VALUE);
        if(mAppList.isEmpty()){
            for(RunningServiceInfo runningservice : runningservices){
                int uid = runningservice.uid;
                String name=runningservice.service.getPackageName();
                long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                Application newapplication= new Application(name,uid,bytes);
                mAppList.add(newapplication);
            }
        }
        else{
            for(RunningServiceInfo runningservice :runningservices){

                boolean duplicate = false;
                int uid = runningservice.uid;
                for(Application application: mAppList){
                    if(uid== application.getUid()){
                        duplicate= true;
                        break;
                    }

                }
                if(!duplicate){
                    String name=runningservice.service.getPackageName();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    Application newapplication= new Application(name,uid,bytes);
                    mAppList.add(newapplication);
                }


            }
        }
    }
    //Determines the application that is using the most data.
    private Application HighestUsingApplication(){
        Application highest= null;
        for(Application application: mAppList){
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





