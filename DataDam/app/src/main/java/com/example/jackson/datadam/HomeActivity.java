package com.example.jackson.datadam;

<<<<<<< HEAD
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
=======
>>>>>>> origin/UpdatedAppList
import android.os.Bundle;


import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.net.TrafficStats;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ActivityManager;
import android.content.Context;
import android.app.ActivityManager.RunningServiceInfo;
import android.net.ConnectivityManager;

import com.jaredrummler.android.processes.AndroidProcesses;

import java.util.List;

public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long PreviousRX = 0;
    private long PreviousTX= 0;
    private long currentRX, currentTX, rxBytes, txBytes;
    ActivityManager manager;

    ConnectivityManager connectMgn;

    List<ActivityManager.RunningServiceInfo> runningservices;
    List<Application> mAppList = new ArrayList<Application>();

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

<<<<<<< HEAD
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
=======
    public List<Application> getmAppList(){
        return mAppList;
    }

>>>>>>> origin/UpdatedAppList
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
            //If the device is offline, Data Dam records the data to the bytes the device has used offline.
            //Otherwise the system updates the flags to prepare for the next runnable cycle.
//            if(!isOnline(connectMgn)) {
            rxBytes = rxBytes + currentRX;
            txBytes= txBytes + currentTX;
            RX.setText(Long.toString(rxBytes));
            TX.setText(Long.toString(txBytes));
            for(Application application: mAppList){
                int uid = application.getUid();
                long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                application.addBytes(bytes);
            }
//                mAppAdapter.notifyDataSetChanged();
            Application highest = HighestUsingApplication();
            HighestName.setText(highest.getName());
            HighestValue.setText(Long.toString(highest.getBytes()));
//            }
            /*else{
                for(Application application: mAppList){
                    int uid = application.getUid();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    application.updatePrevious(bytes);
                }
            }*/
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    // Checks to see if the device is online with Wifi with ConnectivityManager
    // Returns true if network info exists and the phone is connected to a network
//    public boolean isOnline(ConnectivityManager connectMgn){
//
//        NetworkInfo networkInfo = connectMgn.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//
//    }
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





