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


import java.util.ArrayList;

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

    LineChart lineChart;

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



        ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxes = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        //Code For the Line Graph
        lineChart = (LineChart) findViewById(R.id.lineChart);

        xAxes.add("Mon");
        xAxes.add("Tue");
        xAxes.add("Wed");
        xAxes.add("Thur");
        xAxes.add("Fri");

        yAxes.add(new Entry(10,0));
        yAxes.add(new Entry(50,1));
        yAxes.add(new Entry(40,2));
        yAxes.add(new Entry(60,3));
        yAxes.add(new Entry(20,4));

        String[] xaxes = new String[xAxes.size()];


        for(int i=0; i<xAxes.size();i++){

            xaxes[i]=xAxes.get(i).toString();

        }


        LineDataSet lineDataSet = new LineDataSet(yAxes,"values");
        lineDataSet.setDrawCircles(true);
        lineDataSet.setColor(Color.BLUE);

        lineDataSets.add(lineDataSet);

        lineChart.setData(new LineData(lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);

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


