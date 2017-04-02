package com.example.jackson.datadam;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

import static com.example.jackson.datadam.R.layout.activity_main;
import static com.example.jackson.datadam.R.layout.home_activity;


public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;

    private TextView RX;
    private TextView TX;


    private RelativeLayout lineChart;

    private LineChart mChart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();

        RX = (TextView) findViewById(R.id.RX);
        TX = (TextView) findViewById(R.id.TX);


        lineChart = (RelativeLayout) findViewById(R.id.lineChart);
        //create line chart
        mChart = new LineChart(this);
        //add to main layout
        //activity_main.addView(mChart);
        lineChart.addView(mChart,1100,1100);


        //When the chart content is not displayed
        mChart.setContentDescription("");
        mChart.setNoDataText("No data to plot the graph at this moment");

        //enable value highlighting
        mChart.setHighlightPerDragEnabled(true);

        //enable touch gestures
        mChart.setTouchEnabled(true);


        //Scaling and Draging of the graph
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);



        //enable pinch zoom

        mChart.setPinchZoom(true);

        //alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        //Data to Plot the Graph
        LineData data = new LineData();

        data.setValueTextColor(Color.WHITE);

        //add data to linechart
        mChart.setData(data);

        //get legend object

        Legend legend = mChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);



        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaximum(100f);
        y1.setDrawGridLines(true);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);





      /*  ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxes = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        //Code For the Line Graph


        xAxes.add("Mon");
        xAxes.add("Tue");
        xAxes.add("Wed");
        xAxes.add("Thur");
        xAxes.add("Fri");

        yAxes.add(new Entry((mStartRX),0));
        yAxes.add(new Entry((mStartRX+mStartTX),1));
        yAxes.add(new Entry(40,2));
        yAxes.add(new Entry(50,3));
        yAxes.add(new Entry(60,4));

        String[] xaxes = new String[xAxes.size()];


        for(int i=0; i<xAxes.size();i++){

            xaxes[i]=xAxes.get(i).toString();

        }




        LineDataSet lineDataSet = new LineDataSet(yAxes,"values");
        lineDataSet.setDrawCircles(true);
        lineDataSet.setColor(Color.BLUE);

        lineDataSets.add(lineDataSet);

        lineChart.setData(new LineData( lineDataSet));
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);*/



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


