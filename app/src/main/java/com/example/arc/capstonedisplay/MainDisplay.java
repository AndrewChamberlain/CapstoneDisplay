package com.example.arc.capstonedisplay;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidplot.xy.SimpleXYSeries;

import java.util.ArrayList;
import java.util.List;

public class MainDisplay extends AppCompatActivity {
    private RecyclerView plotsRV;
    private TextView overVoltageValue;
    private Handler mHandler;
    private SimpleXYSeries voltageSeries;
    private CustomConnection con;
    int c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);
        //-----Get Widgets and such
        plotsRV =(RecyclerView)findViewById(R.id.PlotsRV);
        overVoltageValue=(TextView) findViewById(R.id.overvoltageValue);
        mHandler=new Handler();
        //-----Create Plot
        /*voltageSeries=new SimpleXYSeries("Voltage");
        voltageSeries.useImplicitXVals();
        plot.addSeries(voltageSeries,new LineAndPointFormatter(Color.rgb(200,100,100),Color.TRANSPARENT,Color.TRANSPARENT,null));
        plot.setDomainBoundaries(0,100,BoundaryMode.FIXED);
        plot.setRangeBoundaries(0,150,BoundaryMode.FIXED);
        */
        List<PlotterCard> plotterCards=new ArrayList<>();
        plotterCards.add(new PlotterCard("Voltages"));
        //-----Setup plot selections
        plotsRV.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            FragmentManager manager=getFragmentManager();
            voltageSelectorDialog dialog=new voltageSelectorDialog();
            dialog.show(manager,"voltagePlotSelector");
        }
        });

        //-----Connect to server
        con=new CustomConnection();
        con.connect("192.168.0.100",2048);
        //-----Start Polling
        //startRepeatingTask();
    }
    /*void startRepeatingTask() {
        updatePoll.run();
    }
    Runnable updatePoll = new Runnable() {
        @Override
        public void run() {
            updateData(100+50*Math.sin(0.1*c));
            c++;
            //Log.d("STATE","tick:"+c);
            plot.redraw();
            mHandler.postDelayed(updatePoll, 100);
        }
    };*/
    void updateData(double val){
        float fac=100;
        overVoltageValue.setText(Math.round((val-120)*fac)/fac+"%");
        if(voltageSeries.size()>=100){
            voltageSeries.removeFirst();
        }
        voltageSeries.addLast(null,val);
    }
    public void openCircuitConfig(View view){
        Intent intent = new Intent(getApplicationContext(),CircuitConfig.class);
        startActivity(intent);
    }
    public void openVoltageConfig(View view){
    }
}
