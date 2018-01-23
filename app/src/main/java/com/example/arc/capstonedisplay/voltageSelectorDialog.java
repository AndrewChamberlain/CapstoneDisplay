package com.example.arc.capstonedisplay;

import android.app.DialogFragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Created by arc on 21/11/17.
 * https://www.youtube.com/watch?v=bkUHeXCX8XM
 */

public class voltageSelectorDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.voltageselectorlayout,null);
        setCancelable(false);
        Button done=(Button) view.findViewById(R.id.voltagePlotDone);
        //-----Load checked values

        //-----Set up triggers
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----Save checked values
                dismiss();
            }
        });
        //-----Return
        return view;
    }
    public void onResume() {
        //-----https://stackoverflow.com/questions/26974068/how-to-set-the-width-of-a-dialogfragment-in-percentage
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        window.setLayout((int)Math.max((width*0.75),800),(int)Math.max((height*0.75),800));
        window.setGravity(Gravity.CENTER);
    }
}
