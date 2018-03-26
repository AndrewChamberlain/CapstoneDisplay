package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.util.List;

/**
 * Created by arc on 23/01/18.
 */

public class PlotterCardAdapter extends RecyclerView.Adapter<PlotterCardAdapter.PlotterViewHolder>{
    List<PlotterCard> plots;
    DataStorage dataStorage;
    private Handler handler = new Handler();
    private Runnable updatePlots = new Runnable() {
        @Override
        public void run() {
            if(plots!=null){
                for(int i=0;i<plots.size();i++){
                    plots.get(i).refresh();
                }
            }
            handler.postDelayed(updatePlots, 100);
        }
    };

    public static class PlotterViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        XYPlot plot;
        //TextView title;
        int i=-1;

        PlotterViewHolder(View itemView){
            super(itemView);
            //Log.d("PlotterViewHolder","GETTING STUFF");
            cv=(CardView)itemView.findViewById(R.id.plotterCard);
            plot=(XYPlot)itemView.findViewById(R.id.plot);
            //plot.setDomainBoundaries(0,100,BoundaryMode.GROW);
            //title=(TextView)itemView.findViewById(R.id.plotTitle);
        }
    }
    PlotterCardAdapter(List<PlotterCard> plts, DataStorage data){
        Log.d("PlotterCardAdapter","Creating:"+plts.size());
        plots=plts;
        dataStorage=data;
        handler.postDelayed(updatePlots, 100);
    }
    @Override
    public int getItemCount() {
        return plots.size();
    }
    @Override
    public PlotterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d("PlotterCardAdapter","CreatingViewHolder");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chart_card, viewGroup, false);
        PlotterViewHolder pvh = new PlotterViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PlotterViewHolder plotterViewHolder, int i) {
        plots.get(i).plot=plotterViewHolder.plot;
        plotterViewHolder.i=i;
        plotterViewHolder.plot.setTitle(plots.get(i).title);
        plots.get(i).update();
        //plotterViewHolder.title.setText(plots.get(i).title);
        //plots.get(i).plot=plotterViewHolder.plot;
    }
    @Override
    public void onViewAttachedToWindow(PlotterViewHolder plotterViewHolder) {
        Log.d("PlotterCardAdapter","Attach");
        plots.get(plotterViewHolder.i).active=true;
    }
    @Override
    public void onViewDetachedFromWindow(PlotterViewHolder plotterViewHolder) {
        Log.d("PlotterCardAdapter","Detach");
        plots.get(plotterViewHolder.i).active=false;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
