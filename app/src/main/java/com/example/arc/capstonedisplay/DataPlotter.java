package com.example.arc.capstonedisplay;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

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

public class DataPlotter{
    //-----Plot
    XYPlot plot;
    //-----Display plots
    List<SimpleXYSeries> plotsVoltage;
    List<SimpleXYSeries> plotsCurrent;
    List<SimpleXYSeries> plotsPower;
    //-----Storage sizes
    int storageSizeImmediate=100;
    int storageSize1H=100;
    int storageSize24H=100;
    int storageSize72H=100;
    int storageSize1Week=100;
    //-----ImmediateStorage
    SimpleXYSeries dataImmediatePVVoltage;
    SimpleXYSeries dataImmediatePVCurrent;
    SimpleXYSeries dataImmediateBatteryVoltage;
    SimpleXYSeries dataImmediateSlackVoltage;
    SimpleXYSeries dataImmediateJunctionVoltage;
    SimpleXYSeries dataImmediateLoadPower;
    SimpleXYSeries dataImmediatePVPower;


    public DataPlotter(XYPlot plt){
        plot=plt;
        //-----Init
        setupPlot(plotsVoltage,dataImmediatePVVoltage,"PV Voltage");
        setupPlot(plotsCurrent,dataImmediatePVCurrent,"PV Current");
        setupPlot(plotsVoltage,dataImmediateBatteryVoltage,"Battery Voltage");
        setupPlot(plotsVoltage,dataImmediateSlackVoltage,"Slack Voltage");
        setupPlot(plotsVoltage,dataImmediateJunctionVoltage,"Junction Voltage");
        setupPlot(plotsPower,dataImmediateLoadPower,"Load Power");
        setupPlot(plotsPower,dataImmediatePVPower,"PV Power");
        //-----Add the Battery % and 'power quality'
    }

    public void pushData(float epoch, float vals[]){
        /*vals[]=
        * 0: PV Voltage
        * 1: PV Current
        * 2: Battery Voltage
        * 3: Slack Voltage
        * 4: Junction Voltage
        * 5: Load Power
        * */
        if(vals.length==6){
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

        }else{
            Log.e("DataPlotter","'vals' is not the right size");
        }
    }

    private void setupPlot(List<SimpleXYSeries> container, SimpleXYSeries plot, String title){
        plot=new SimpleXYSeries(title);
        container.add(plot);
    }

    private void setupCurrentPlot(XYPlot plot, String title){

    }

    private void setupPowerPlot(XYPlot plot, String title){

    }

    public void updatePlotListing(int voltages, int currents, int powers){

    }
}
