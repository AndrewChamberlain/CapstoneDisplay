package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static long currentEpoch=0;
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
    public static List<Float> intervalVoltage=new ArrayList<>();
    public static List<Float> intervalCurrent=new ArrayList<>();
    public static List<Float> intervalPower=new ArrayList<>();
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
        setupPlot(VOLTAGE,dataImmediatePVVoltage,"PV Voltage",0,0,255,50,5);
        setupPlot(CURRENT,dataImmediatePVCurrent,"PV Current",0,0,255,50,5);
        setupPlot(VOLTAGE,dataImmediateBatteryVoltage,"Battery Voltage",255,0,0,15,1);
        setupPlot(VOLTAGE,dataImmediateSlackVoltage,"Slack Voltage",255, 153, 0,150,10);
        setupPlot(VOLTAGE,dataImmediateJunctionVoltage,"Junction Voltage",255, 51, 153,150,10);
        setupPlot(POWER,dataImmediateLoadPower,"Load Power",0,255,0,75,15);
        setupPlot(POWER,dataImmediatePVPower,"PV Power",0,0,255,300,25);
        //-----Add the Battery % and 'power quality'
    }

    public static void pushData(double epoch, float vals[]){
        /*vals[]=
        * 0: PV Voltage
        * 1: PV Current
        * 2: Battery Voltage
        * 3: Slack Voltage
        * 4: Junction Voltage
        * 5: Load Power
        * */
        currentEpoch=(long)(epoch*10);
        if(vals.length==6){
            if(epochStart==0){
                epochStart=currentEpoch;
            }
            //-----Add the data
            dataImmediatePVVoltage.addLast(currentEpoch,vals[0]);
            dataImmediatePVCurrent.addLast(currentEpoch,vals[1]);
            dataImmediateBatteryVoltage.addLast(currentEpoch,vals[2]);
            dataImmediateSlackVoltage.addLast(currentEpoch,vals[3]);
            dataImmediateJunctionVoltage.addLast(currentEpoch,vals[4]);
            dataImmediateLoadPower.addLast(currentEpoch,vals[5]);
            dataImmediatePVPower.addLast(currentEpoch,vals[0]*vals[1]);
            //-----Push to displays
            if(vals[4]!=0 && vals[3]!=0){
                StatManager.addOvervoltage(vals[4]/vals[3]);
            }
            StatManager.addPVPower(vals[0]*vals[1]);
            StatManager.addLoadPower(vals[5]);
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

    private void setupPlot(int type, SimpleXYSeries plot, String title, int r, int g, int b,float max,float interval){
        plot.setTitle(title);
        LineAndPointFormatter f=new LineAndPointFormatter(Color.rgb(r,g,b),Color.TRANSPARENT,Color.TRANSPARENT,null);
        switch (type){
            case VOLTAGE:
                plotsVoltage.add(plot);
                formatsVoltage.add(f);
                maxsVoltage.add(max);
                intervalVoltage.add(interval);
                break;
            case CURRENT:
                plotsCurrent.add(plot);
                formatsCurrent.add(f);
                maxsCurrent.add(max);
                intervalCurrent.add(interval);
                break;
            case POWER:
                plotsPower.add(plot);
                formatsPower.add(f);
                maxsPower.add(max);
                intervalPower.add(interval);
                break;
        }
    }
}
