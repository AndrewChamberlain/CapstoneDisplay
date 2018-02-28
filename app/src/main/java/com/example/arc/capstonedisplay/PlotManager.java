package com.example.arc.capstonedisplay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by arc on 26/02/18.
 */
//*****REMOVE THIS CLASS*****!!!!!
public class PlotManager {
    //-----Voltages
    boolean[] mask;
    CharSequence[] names;
    PlotterCard card;

    public PlotManager(List<SimpleXYSeries> s,PlotterCard c){
        card=c;
        names=new CharSequence[s.size()];
        mask=new boolean[s.size()];
        for(int i=0;i<s.size();i++){
            names[i]=s.get(i).getTitle();
            mask[i]=true;
        }
    }
    public void edit(Context con){
        //-----https://developer.android.com/guide/topics/ui/dialogs.html
        //-----Opens a dialog to select the desired plots
        AlertDialog.Builder dialog=new AlertDialog.Builder(con);
        dialog.setTitle("Select Plots");
        dialog.setMultiChoiceItems(names, mask,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        mask[i]=isChecked;
                        card.update();
                    }
                });
        AlertDialog a=dialog.create();
        a.show();
    }
}
