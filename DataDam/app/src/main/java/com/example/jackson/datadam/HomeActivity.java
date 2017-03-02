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


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.net.TrafficStats;
        import android.os.Bundle;
        import android.os.Handler;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.app.ActivityManager;
        import android.net.NetworkInfo;
        import android.content.Context;
        import android.app.ActivityManager.RunningServiceInfo;
        import android.net.ConnectivityManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import 	android.app.ListActivity;
class Application{
    private String name;
    private int uid;
    private long previousbytes;
    private long bytes=0;
    public Application next;

    public Application(String name, int uid, long previousbytes){
        this.name=name;
        this.uid=uid;
        this.previousbytes=previousbytes;
    }
    public void addBytes(long newbytes){
        long bytesused= newbytes-previousbytes;
        bytes= bytesused + bytes;
        previousbytes= newbytes;
    }
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
}
public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long PreviousRX = 0;
    private long PreviousTX= 0;
    private long currentRX, currentTX, rxBytes, txBytes;
    ActivityManager manager;

    ConnectivityManager connectMgn;
    List<ActivityManager.RunningServiceInfo> runningservices;
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
        Applicationupdate(manager);

        if (PreviousRX== TrafficStats.UNSUPPORTED || PreviousTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }
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
            //If the device is offline, Data Dam records the data to the bytes the device has used offline.
            //Otherwise the system updates the flags to prepare for the next runnable cycle.
//            if(!isOnline(connectMgn)) {
                rxBytes = rxBytes + currentRX;
                txBytes= txBytes + currentTX;
                RX.setText(Long.toString(rxBytes));
                TX.setText(Long.toString(txBytes));
                for(Application application: applications){
                  int uid = application.getUid();
                    long bytes= TrafficStats.getUidRxBytes(uid)+TrafficStats.getUidTxBytes(uid);
                    application.addBytes(bytes);
                }
            Application highest = HighestUsingApplication();
            HighestName.setText(highest.getName());
            HighestValue.setText(Long.toString(highest.getBytes()));
//            }
            /*else{
                for(Application application: applications){
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





