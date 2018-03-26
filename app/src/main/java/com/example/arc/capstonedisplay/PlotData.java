package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.util.Log;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arc on 01/03/18.
 * This class is designed to handle all of the relevant storage and methods for a single plot of data
 */

public class PlotData {
    List<String> timeFrameTitles;
    List<Integer> pointCount;
    List<Integer> skipValues;

    List<List<SimpleXYSeries>> dataStored=new ArrayList<>();    //-----[TimeFrame][SeriesNumber]
    List<LineAndPointFormatter> formats=new ArrayList<>();      //-----[SeriesNumber]
    List<Integer> cyclicalCounter=new ArrayList<>();            //-----Used to time when the skips should be used [TimeFrame]
    List<Float> cyclicalSums=new ArrayList<>();                 //-----Stores the sums of all the skipped points [TimeFrame]
    List<Float> maximums=new ArrayList<>();                     //-----[SeriesNumber]
    List<Float> intervals=new ArrayList<>();                    //-----[SeriesNumber]

    public PlotData(){

    }

    public void addTimeFrame(String title, Integer count, Integer length){
        /* Adds the timeFrame to the data
        * [title]: The title of the timeframe. Used for selection
        * [count]: The maximum number of points that can be stored for this timeframe
        * [length]: Used for skipping data points. Only every 'length' points will be actually added and be an average of the missed points
        *
        * To plot every point, length=1
        */
        timeFrameTitles.add(title);
        pointCount.add(Math.max(count,1));
        skipValues.add(Math.max(length,1));
        dataStored.add(new ArrayList<SimpleXYSeries>());
    }

    public void addSeries(String title, int r, int g, int b,float max,float interval){
        //-----Adds the series to each timeframe
        for(int i=0;i<dataStored.size();i++){
            dataStored.get(i).add(new SimpleXYSeries(title));
        }
        formats.add(new LineAndPointFormatter(Color.rgb(r,g,b),Color.TRANSPARENT,Color.TRANSPARENT,null));
        maximums.add(max);
        intervals.add(interval);
    }

    public void pushData(float epoch, float[] vals){
        //-----For each timeframe
        for(int i=0;i<dataStored.size();i++){
            if (dataStored.get(i).size()==vals.length) {
                //-----Push data to each cyclicalSum and push if able
                for(int j=0;j<vals.length;j++){
                    cyclicalSums.set(i,cyclicalSums.get(i)+vals[j]);
                    cyclicalCounter.set(i,cyclicalCounter.get(i)+1);
                    int c=cyclicalCounter.get(i)%skipValues.get(i);
                    //-----Check if time to push
                    if(c==0){
                        //-----Push the average
                        dataStored.get(i).get(j).addFirst(epoch,cyclicalSums.get(i)/cyclicalCounter.get(i));
                    }
                }
            }else{
                Log.w("PlotData","Pushed values not the same size as series count");
                break;
            }
        }
    }
}
