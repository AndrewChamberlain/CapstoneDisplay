package com.example.arc.capstonedisplay;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

/**
 * Created by arc on 23/01/18.
 */

public class PlotterCard {
    SimpleXYSeries series[];
    XYPlot plot;
    String title="BLANK TITLE";

    public PlotterCard(String name){
        //plot=new XYPlot();
        title=name;
    }

    public void updateData(SimpleXYSeries data[]){
        series=data;
    }
}
