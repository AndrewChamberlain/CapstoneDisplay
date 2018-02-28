package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by arc on 22/01/18.
 *
 * Designed to store all of the data collected by the sensors.
 * This includes all data for long time scales where values are skipped
 *
 * Also included are the methods for the plotting and control of plots
 */

public class DataStorage {
    private static final int VOLTAGE=0;
    private static final int CURRENT=1;
    private static final int POWER=2;
    //-----Misc
    private static double epochStart=0;
    //-----Display plots
    public static List<SimpleXYSeries> plotsVoltage=new ArrayList<>();
    public static List<SimpleXYSeries> plotsCurrent=new ArrayList<>();
    public static List<SimpleXYSeries> plotsPower=new ArrayList<>();
    public static List<LineAndPointFormatter> formatsVoltage=new ArrayList<>();
    public static List<LineAndPointFormatter> formatsCurrent=new ArrayList<>();
    public static List<LineAndPointFormatter> formatsPower=new ArrayList<>();
    public static List<Float> maxsVoltage=new ArrayList<>();
    public static List<Float> maxsCurrent=new ArrayList<>();
    public static List<Float> maxsPower=new ArrayList<>();
    //-----Storage sizes
    public static int storageSizeImmediate=100;
    public static int storageSize1H=100;
    public static int storageSize24H=100;
    public static int storageSize72H=100;
    public static int storageSize1Week=100;
    //-----ImmediateStorage
    public static SimpleXYSeries dataImmediatePVVoltage=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediatePVCurrent=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediateBatteryVoltage=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediateBatteryCurrent=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediateSlackVoltage=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediateJunctionVoltage=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediateLoadPower=new SimpleXYSeries("");
    public static SimpleXYSeries dataImmediatePVPower=new SimpleXYSeries("");


    public DataStorage(){
        plotsVoltage.clear();
        plotsCurrent.clear();
        plotsPower.clear();
        //-----Init
        setupPlot(VOLTAGE,dataImmediatePVVoltage,"PV Voltage",0,255,0,130);
        setupPlot(CURRENT,dataImmediatePVCurrent,"PV Current",0,255,0,50);
        setupPlot(VOLTAGE,dataImmediateBatteryVoltage,"Battery Voltage",255,0,0,15);
        setupPlot(CURRENT,dataImmediateBatteryCurrent,"Battery Current",255,0,0,10);
        setupPlot(VOLTAGE,dataImmediateSlackVoltage,"Slack Voltage",255, 153, 0,150);
        setupPlot(VOLTAGE,dataImmediateJunctionVoltage,"Junction Voltage",255, 51, 153,150);
        setupPlot(POWER,dataImmediateLoadPower,"Load Power",0,255,0,300);
        setupPlot(POWER,dataImmediatePVPower,"PV Power",0,255,0,300);
        //-----Add the Battery % and 'power quality'
    }

    public static void pushData(double epoch, float vals[]){
        /*vals[]=
        * 0: PV Voltage
        * 1: PV Current
        * 2: Battery Voltage
        * 3: Battery Current
        * 4: Slack Voltage
        * 5: Junction Voltage
        * 6: Load Power
        * */
        if(vals.length==7){
            if(epochStart==0){
                epochStart=epoch;
            }
            //-----Add the data
            dataImmediatePVVoltage.addLast(epoch,vals[0]);
            dataImmediatePVCurrent.addLast(epoch,vals[1]);
            dataImmediateBatteryVoltage.addLast(epoch,vals[2]);
            dataImmediateSlackVoltage.addLast(epoch,vals[3]);
            dataImmediateJunctionVoltage.addLast(epoch,vals[4]);
            dataImmediateLoadPower.addLast(epoch,vals[5]);
            dataImmediatePVPower.addLast(epoch,vals[0]*vals[1]);
            //-----Push other data
            //-----Trim
            if(dataImmediatePVVoltage.size()>storageSizeImmediate){
                dataImmediatePVVoltage.removeFirst();
                dataImmediatePVCurrent.removeFirst();
                dataImmediateBatteryVoltage.removeFirst();
                dataImmediateSlackVoltage.removeFirst();
                dataImmediateJunctionVoltage.removeFirst();
                dataImmediateLoadPower.removeFirst();
                dataImmediatePVPower.removeFirst();
            }
            //-----Update plots
            Log.d("DataStorage","x:"+plotsVoltage.get(0).getY(0));
        }else{
            Log.e("DataStorage","'vals' is not the right size");
        }
    }

    private void setupPlot(int type, SimpleXYSeries plot, String title, int r, int g, int b,float max){
        plot.setTitle(title);
        LineAndPointFormatter f=new LineAndPointFormatter(Color.rgb(r,g,b),Color.TRANSPARENT,Color.TRANSPARENT,null);
        switch (type){
            case VOLTAGE:
                plotsVoltage.add(plot);
                formatsVoltage.add(f);
                maxsVoltage.add(max);
                break;
            case CURRENT:
                plotsCurrent.add(plot);
                formatsCurrent.add(f);
                maxsCurrent.add(max);
                break;
            case POWER:
                plotsPower.add(plot);
                formatsPower.add(f);
                maxsPower.add(max);
                break;
        }
    }
}
