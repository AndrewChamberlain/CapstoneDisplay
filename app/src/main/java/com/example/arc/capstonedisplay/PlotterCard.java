package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.util.Log;
import android.util.Size;

import com.androidplot.ui.SizeMode;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;
import java.util.List;

/**
 * Created by arc on 23/01/18.
 */

public class PlotterCard {
    //List<SimpleXYSeries> series=new ArrayList<SimpleXYSeries>();
    XYPlot plot;
    List<SimpleXYSeries> series;
    PlotManager manager;
    List<LineAndPointFormatter> formats;
    boolean active=true;
    List<Float>maximums;
    float max=0;

    String title="BLANK TITLE";
    String range="BLANK RANGE";

    public PlotterCard(String name, String rangeLabel){
        title=name;
        range=rangeLabel;
    }

    public void setData(List<SimpleXYSeries> data, List<LineAndPointFormatter> fs, List<Float>maxs){
        series=data;
        formats=fs;
        maximums=maxs;
        manager=new PlotManager(data,this);
    }

    public void refresh(){
        if(plot!=null) {
            //Log.w("PlotterCard","Redrawing:"+title+":"+series.get(0).size());
            plot.redraw();
        }else{
            Log.w("PlotterCard","Cannot Refresh: '"+title+"'. Plot is null");
        }
    }

    public void update(){
        //-----Updates the data being displayed
        plot.clear();
        max=0;
        for(int i=0;i<series.size();i++){
            if(manager.mask[i]){
                //-----add plot
                max=Math.max(max,maximums.get(i));
                plot.addSeries(series.get(i),formats.get(i));
            }
        }
        plot.setRangeBoundaries(0,max,BoundaryMode.FIXED);
        plot.setRangeStepMode(StepMode.INCREMENT_BY_VAL);
        plot.setRangeLabel(range);
        plot.setRangeStepMode(StepMode.INCREMENT_BY_VAL);
        plot.getGraph().setLinesPerRangeLabel(2);
        Log.d("AAA","DONE");
    }
}
