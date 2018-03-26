package com.example.arc.capstonedisplay;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toolbar;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketOptions;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainDisplay extends AppCompatActivity {
    private RecyclerView plotsRV;
    private TextView overVoltageValue;
    private Handler mHandler;
    private SimpleXYSeries voltageSeries;
    List<PlotterCard> plotterCards=new ArrayList<>();
    final private CustomConnection con=new CustomConnection();
    private DataStorage dataStorage=null;
    int c=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        con.setup(this,(MenuItem)menu.findItem(R.id.connection_status));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connection_status:
                //-----Get state
                if(con.state==con.STATE_NOT_CONNECTED){
                        con.connect("192.168.0.100",2048);
                }else{
                        con.disconnect();
                }
                //-----Try to connect
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //-----Set up screen options
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_display);
        PixelUtils.init(this);

        //-----Get Widgets and such
        plotsRV =(RecyclerView)findViewById(R.id.PlotsRV);
        mHandler=new Handler();
        SnapHelper snap=new PagerSnapHelper();
        snap.attachToRecyclerView(plotsRV);
        //-----Create Plot
        voltageSeries=new SimpleXYSeries("Voltage");
        voltageSeries.useImplicitXVals();

        //-----Make DataStorage
        dataStorage=new DataStorage();
        //-----Make plots

        PlotterCard voltageCard=new PlotterCard("Voltages","Voltage [V]");
        plotterCards.add(voltageCard);
        PlotterCard currentCard=new PlotterCard("Currents","Current [A]");
        plotterCards.add(currentCard);
        PlotterCard powerCard=new PlotterCard("Powers", "Power [W]");
        plotterCards.add(powerCard);
        voltageCard.setData(DataStorage.plotsVoltage,DataStorage.formatsVoltage,DataStorage.maxsVoltage,DataStorage.intervalVoltage);
        currentCard.setData(DataStorage.plotsCurrent,DataStorage.formatsCurrent,DataStorage.maxsCurrent,DataStorage.intervalCurrent);
        powerCard.setData(DataStorage.plotsPower,DataStorage.formatsPower,DataStorage.maxsPower,DataStorage.intervalPower);
        //-----Create adapter
        PlotterCardAdapter adapter=new PlotterCardAdapter(plotterCards,dataStorage);
        plotsRV.setHasFixedSize(true);
        plotsRV.setAdapter(adapter);
        LinearLayoutManager layout=new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        plotsRV.setLayoutManager(layout);
        //-----Set up selection listener
        plotsRV.addOnItemTouchListener(new RecyclerItemClickListener(this, plotsRV, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int i) {
                //-----Open the plot selection dialog
                plotterCards.get(i).manager.edit(view.getContext());
            }

            @Override public void onLongItemClick(View view, int i) {
                //-----Open the time selection dialog
            }
        }){
        });
        //-----Set up displays
        StatManager.overvoltage=(StatDisplayer)findViewById(R.id.Stat_Overvoltage);
        StatManager.PVPower=(StatDisplayer)findViewById(R.id.Stat_PV_Output);
        StatManager.loadPower=(StatDisplayer)findViewById(R.id.Stat_LoadPower);
        StatManager.PVState=(StatDisplayer)findViewById(R.id.Stat_PVOn);
        StatManager.BatteryState=(StatDisplayer)findViewById(R.id.Stat_BatteryOn);
        //-----
        Logic.connection=con;
        Logic.setup(this);
        //-----
        handler.postDelayed(updateStatDisplays, 100);
    }
    private Handler handler = new Handler();
    private Runnable updateStatDisplays = new Runnable() {
        @Override
        public void run() {
            StatManager.updateAll();
            handler.postDelayed(updateStatDisplays, 100);
            Logic.updateStates();
        }
    };



}