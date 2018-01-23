package com.example.arc.capstonedisplay;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.util.List;

/**
 * Created by arc on 23/01/18.
 */

public class PlotterCardAdapter extends RecyclerView.Adapter<PlotterCardAdapter.PlotterViewHolder>{
    List<PlotterCard> plots;

    public static class PlotterViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        XYPlot plot;
        TextView title;

        PlotterViewHolder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.plotterCard);
            plot=(XYPlot)itemView.findViewById(R.id.plot);
            title=(TextView)itemView.findViewById(R.id.plotTitle);
        }
    }
    PlotterCardAdapter(List<PlotterCard> plts){
        plots=plts;
    }
    @Override
    public int getItemCount() {
        return plots.size();
    }
    @Override
    public PlotterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chart_card, viewGroup, false);
        PlotterViewHolder pvh = new PlotterViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(PlotterViewHolder personViewHolder, int i) {
        personViewHolder.title.setText(plots.get(i).title);
        SimpleXYSeries tmpPlots[]=plots.get(i).series;
        for(int x=0;x<tmpPlots.length;x++) {
            personViewHolder.plot.addSeries(tmpPlots[x], new LineAndPointFormatter(Color.rgb(200, 100, 100), Color.TRANSPARENT, Color.TRANSPARENT, null));
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
