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
import com.github.mikephil.charting.utils.ColorTemplate;


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
    private static long mStartRX = 0;
    private static long mStartTX = 0;

    private static TextView RX;
    private static TextView TX;


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
        lineChart.addView(mChart,1000,1000);


        //When the chart content is not displayed
       // mChart.setContentDescription("");
       // mChart.setNoDataText("No data to plot the graph at this moment");

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

        data.setValueTextColor(Color.BLACK);

        //add data to linechart
        mChart.setData(data);

        //get legend object

        Legend legend = mChart.getLegend();

        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);



        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setAxisMinimum(0f);
        y1.setAxisMaximum(1000f);
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

    private void addEntry(String data_Value) {

        LineData data = mChart.getData();
        float data_val=Float.parseFloat(data_Value);
        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (data_val/1024)), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(6);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }


    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Data in KBPS");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {

            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            RX.setText(Long.toString(rxBytes));
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            TX.setText(Long.toString(txBytes));

            addEntry(Long.toString(txBytes+rxBytes));
            mHandler.postDelayed(mRunnable, 1000);

        }
    };

}


