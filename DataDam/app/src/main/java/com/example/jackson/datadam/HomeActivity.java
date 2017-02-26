package com.example.jackson.datadam;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


import java.util.ArrayList;

public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;

    private TextView RX;
    private TextView TX;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();

        RX = (TextView) findViewById(R.id.RX);
        TX = (TextView) findViewById(R.id.TX);

        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
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

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            RX.setText(Long.toString(rxBytes));
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            TX.setText(Long.toString(txBytes));
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
}



