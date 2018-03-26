package com.example.arc.capstonedisplay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by arc on 12/03/18.
 * https://code.tutsplus.com/tutorials/creating-compound-views-on-android--cms-22889
 * https://stackoverflow.com/questions/4495511/how-to-pass-custom-component-parameters-in-java-and-xml
 */

public class StatDisplayer extends LinearLayout{
    //-----Components
    public TextView titleComp;
    public TextView statComp;

    public StatDisplayer(Context context, AttributeSet st){
        super(context, st);
        initControl(context);
        titleComp.setText(context.obtainStyledAttributes(st, R.styleable.StatDisplayer,0,0).getString(R.styleable.StatDisplayer_title));

    }
    /**
     * Load component XML layout
     */
    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.layout, this);

        // layout is inflated, assign local variables to components
        titleComp = (TextView)findViewById(R.id.StatTitle);
        statComp = (TextView)findViewById(R.id.StatValue);
    }

    public void setText(String text){
        statComp.setText(text);
    }
}
