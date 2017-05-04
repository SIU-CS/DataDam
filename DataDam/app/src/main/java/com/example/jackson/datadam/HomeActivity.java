package com.example.jackson.datadam;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.Calendar;

        import android.content.Intent;
        import android.app.Activity;
        import android.app.Dialog;
        import android.content.Intent;
        import android.app.AlertDialog;
        import android.net.TrafficStats;
        import android.os.Handler;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.app.ActivityManager;
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
import android.widget.Toast;
//Custom application class
//Used to store the name, id, and data usage of an application on the device.

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
    List<DataLimit> DataLimits= new ArrayList<DataLimit>();
    List<TimePeriod> TimePeriods= new ArrayList<TimePeriod>();
    List<Application> mAppList = new ArrayList<Application>();
    private TextView RX;
    private TextView TX;
    private TextView HighestName;
    private TextView HighestValue;
    double tempFloat;
    //List View

    public String[] getRunningApps() {
        int n = mAppList.size();
        String[] appsRunning = new String[n];
        String[] temp = new String[n];

        for(int i = 0; i < n; i++) {
            appsRunning[i] = mAppList.get(i).toString();
        }
//        Object [] applicationsArray = mAppList.toArray();

        return appsRunning;
    }





    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        PreviousRX = TrafficStats.getTotalRxBytes();
        PreviousTX = TrafficStats.getTotalTxBytes();
        RX = (TextView) findViewById(R.id.RX);
        TX = (TextView) findViewById(R.id.TX);
        HighestName = (TextView) findViewById(R.id.HN);
        HighestValue = (TextView) findViewById(R.id.HV);
        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        connectMgn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final Button DataLimitsButton= (Button) findViewById(R.id.DataLimits);
        final Button TimeperiodButton= (Button) findViewById(R.id.TimePeriods);
        final Intent DataIntent = new Intent(HomeActivity.this,DataLimitsActivity.class);
        final Intent PeriodIntent= new Intent(HomeActivity.this,TimePeriodsActivity.class);
        //Creates a sudo storage file if it doesn't already exist, otherwise does nothing
        try {
            outputStream = openFileOutput(storage, Context.MODE_APPEND);
            outputStream.close();
        } catch (Exception e) {

        }
        try {
            outputStream = openFileOutput(Limits, Context.MODE_APPEND);
            outputStream.close();
        } catch (Exception e) {

        }
        try {
            outputStream=openFileOutput(Periods,Context.MODE_APPEND);
            outputStream.close();
        }catch(Exception e){

        }

        //Reads from storage file then populates the rx, tx, and mAppList with data from the scanner.
        try {
            inputStream = openFileInput(storage);
            Scanner scaninput = new Scanner(inputStream);
            //Scan once for the totalbytes value
            rxBytes = Long.parseLong(scaninput.next());
            txBytes = Long.parseLong(scaninput.next());
            //
            while (scaninput.hasNext()) {
                int uid = scaninput.nextInt();
                String name = scaninput.next();
                long previousbytes = TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid);
                long bytes = scaninput.nextLong() + previousbytes;
                Application newapplication = new Application(name, uid, previousbytes, bytes);
                mAppList.add(newapplication);
            }
            scaninput.close();
            inputStream.close();
        } catch (Exception e) {

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
        DataLimitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(DataIntent);
                //moveTaskToBack(true);
            }
        });
        TimeperiodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(PeriodIntent);
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


        ListView listView = (ListView) findViewById(R.id.appNameText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,getRunningApps());
        listView.setAdapter(adapter);

    }
    //Main runnable for Data Dam
    //Compares current bytes used to previous markers to assess how much data has been used within that time frame
    //The values for the total data used by the device and the data for individual mAppList is updated by adding the data used in the time frame to each individual total.




    ////////////////////////////////////////////////////////////////////GRAPH BUTTON FUNCTIONALITY///////////////
    public void sendMessage(View view){

        Intent startNewActivity =  new Intent(this, DisplayMessageActivity.class);

        //EditText theEditText = (EditText) findViewById(R.id.edit_message);
        // String message = theEditText.get

        startActivity(startNewActivity);





    }

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
            previoustime=currenttime;
            currenttime=recorder.get(Calendar.SECOND);
            timepast=currenttime-previoustime;
                for(Application application: mAppList){
                  int uid = application.getUid();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    application.addBytes(bytes);
                }  for(DataLimit dataLimit:DataLimits){

                dataLimit.addBytes(currentRX+currentTX);
                if(dataLimit.isComplete()) {
                    Toast.makeText(getApplicationContext(), dataLimit.getNotification(), Toast.LENGTH_SHORT).show();
                    DataLimits.remove(dataLimit);
                }
            }
            for(TimePeriod timePeriod:TimePeriods){
                timePeriod.addTime(timepast,currentRX+currentTX);
                if(timePeriod.isComplete()){
                    Toast.makeText(getApplicationContext(), timePeriod.getNotification(), Toast.LENGTH_SHORT).show();
                    TimePeriods.remove(timePeriod);
                }
            }
           try {
                outputStream=openFileOutput(storage, Context.MODE_PRIVATE);
                PrintWriter pw = new PrintWriter(outputStream);
               //Change to the pw.println(totalbytesvalue)
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
            HighestValue.setText(Long.toString(highest.getBytes() / 1000) + "kB");
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


// Checks for any new services and adds them to a permanent list
// If list is empty, the currently running services form a new list
    private void Applicationupdate(ActivityManager manager){
        runningservices=manager.getRunningServices(Integer.MAX_VALUE);
        if(mAppList.isEmpty()){
            for(RunningServiceInfo runningservice : runningservices){
                int uid = runningservice.uid;
                String name=runningservice.service.getPackageName().replace(".", " ").replaceAll("com.", "").replaceAll("sec.", "")
                        .replaceAll("qualcomm", "").replaceAll("org."," ").replaceAll("dsi.","")
                        .replaceAll("incallui","").replaceAll("parser","")
                        .replaceAll("vcast mediamanager","mediamanager").replaceAll("google backuptransport","backuptransport")
                        .replaceAll("snapchat android","snapchat").replaceAll("android app bluetooth","bluetooth")
                        .replaceAll("malwarebytes antimalware","malwarebytes antimalware");
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
    //This is done by comparing each mAppList data usage and returning the one with highest value
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

    private Runnable getmRunnable(){
        return mRunnable;
    }

}





