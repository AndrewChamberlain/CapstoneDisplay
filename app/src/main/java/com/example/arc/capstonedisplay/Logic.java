package com.example.arc.capstonedisplay;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by arc on 14/03/18.
 */

public class Logic {
    static int mode=0;
    static int batteryMode=0;
    static int PVMode=0;

    static int PVCurrentState=0;
    static int batteryCurrentState=0;
    //
    static public CustomConnection connection;
    //
    public static final int LIGHT_ON=1;
    public static final int LIGHT_AUTO=2;
    //-----Light state
    static boolean light1State=false;
    static boolean light2State=false;
    static boolean light3State=false;
    static boolean light4State=false;
    //-----Light mode
    static boolean light1Auto=true;
    static boolean light2Auto=true;
    static boolean light3Auto=true;
    static boolean light4Auto=true;
    //-----Spinners buttons
    static Spinner modeSpinner;
    static Spinner PVSpinner;
    static Spinner batterySpinner;
    //-----Light buttons
    static ImageButton light1;
    static ImageButton light2;
    static ImageButton light3;
    static ImageButton light4;
    //-----Auto boxes
    static CheckBox light1Mode;
    static CheckBox light2Mode;
    static CheckBox light3Mode;
    static CheckBox light4Mode;
    //-----Light assets
    static Integer lightImages[]=new Integer[]{R.drawable.ic_lightoff,R.drawable.ic_lighton,R.drawable.ic_lightautooff,R.drawable.ic_lightautoon};

    static Dialog dialog;

    /*static public void openCircuitConfig(Context con){
        //-----https://developer.android.com/guide/topics/ui/dialogs.html
        //-----Opens a dialog to select the desired plots
        dialog = new Dialog(con);
        dialog.setContentView(R.layout.circuit_config);
        //-----Get the objects
        loadSpinner=dialog.findViewById(R.id.LoadSpinner);
        batterySpinner=dialog.findViewById(R.id.BatterySpinner);
        light1=dialog.findViewById(R.id.Light1);
        light2=dialog.findViewById(R.id.Light2);
        light3=dialog.findViewById(R.id.Light3);
        light4=dialog.findViewById(R.id.Light4);
        //-----Add stuff
        loadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadValue=position;
                updateServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                loadValue=0;
                parentView.setSelection(0);
                updateServer();
            }
        });
        batterySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                batteryValue=position;
                updateServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                batteryValue=0;
                parentView.setSelection(0);
                updateServer();
            }
        });
        //-----Light1
        light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light1State=!light1State;
                light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
                updateServer();
            }
        });
        light1Mode=dialog.findViewById(R.id.AutoLight1);
        light1Mode.setChecked(light1Auto);
        light1Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light1Auto=v;
                light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
                updateServer();
            }
        });
        //-----Light2
        light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
        light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light2State=!light2State;
                light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
                updateServer();
            }
        });
        light2Mode=dialog.findViewById(R.id.AutoLight2);
        light2Mode.setChecked(light2Auto);
        light2Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light2Auto=v;
                light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
                updateServer();
            }
        });
        //-----Light3
        light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
        light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light3State=!light3State;
                light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
                updateServer();
            }
        });
        light3Mode=dialog.findViewById(R.id.AutoLight3);
        light3Mode.setChecked(light3Auto);
        light3Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light3Auto=v;
                light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
                updateServer();
            }
        });
        //-----Light4
        light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
        light4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light4State=!light4State;
                light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
                updateServer();
            }
        });
        light4Mode=dialog.findViewById(R.id.AutoLight4);
        light4Mode.setChecked(light4Auto);
        light4Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light4Auto=v;
                light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
                updateServer();
            }
        });
        dialog.setTitle("Circuit Configuration");
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog2, int i, KeyEvent key){
                light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
                return true;
            }
        });
        dialog.show();
    }*/
    static public void setup(AppCompatActivity con){
        //-----https://developer.android.com/guide/topics/ui/dialogs.html
        //-----Opens a dialog to select the desired plots
        //-----Get the objects
        modeSpinner=con.findViewById(R.id.QuickSettingsSpinner);
        PVSpinner=con.findViewById(R.id.PVModeSpinner);
        batterySpinner=con.findViewById(R.id.BatteryModeSpinner);
        light1=con.findViewById(R.id.Light1Button);
        light2=con.findViewById(R.id.Light2Button);
        light3=con.findViewById(R.id.Light3Button);
        light4=con.findViewById(R.id.Light4Button);
        light1Mode=con.findViewById(R.id.Light1Auto);
        light2Mode=con.findViewById(R.id.Light2Auto);
        light3Mode=con.findViewById(R.id.Light3Auto);
        light4Mode=con.findViewById(R.id.Light4Auto);
        //-----Add stuff
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mode=position;
                updateServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                mode=0;
                parentView.setSelection(0);
                updateServer();
            }
        });
        PVSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PVMode=position;
                updateServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                PVMode=0;
                parentView.setSelection(0);
                updateServer();
            }
        });
        batterySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                batteryMode=position;
                updateServer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                batteryMode=0;
                parentView.setSelection(0);
                updateServer();
            }
        });
        //-----Light1
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light1State=!light1State;
                light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
                updateServer();
            }
        });
        light1Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light1Auto=v;
                light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
                updateServer();
            }
        });
        //-----Light2
        light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light2State=!light2State;
                light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
                updateServer();
            }
        });
        light2Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light2Auto=v;
                light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
                updateServer();
            }
        });
        //-----Light3
        light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light3State=!light3State;
                light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
                updateServer();
            }
        });
        light3Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light3Auto=v;
                light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
                updateServer();
            }
        });
        //-----Light4
        light4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parentView) {
                light4State=!light4State;
                light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
                updateServer();
            }
        });
        light4Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton b, boolean v){
                light4Auto=v;
                light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
                updateServer();
            }
        });
        updateStates();
    }

    static private int getStateInt(boolean on, boolean auto){
        int ret=0;
        if(on){
            ret+=LIGHT_ON;
        }
        if(auto){
            ret+=LIGHT_AUTO;
        }
        return ret;
    }

    static public void updateServer(){
        connection.sendMsg(
                new byte[]{
                        (byte)((light1State?1:0)+(light1Auto?2:0)),
                        (byte)((light2State?1:0)+(light2Auto?2:0)),
                        (byte)((light3State?1:0)+(light3Auto?2:0)),
                        (byte)((light4State?1:0)+(light4Auto?2:0)),
                        (byte)(mode),(byte)(PVMode),(byte)(batteryMode)
                });
    }
    static public void setCircuitState(int PVState, int batteryState, int light1S, int light2S, int light3S, int light4S){
        PVCurrentState=PVState;
        batteryCurrentState=batteryState;
        light1State=(light1S&LIGHT_ON)!=0?true:false;
        light1Auto=(light1S&LIGHT_AUTO)!=0?true:false;
        light2State=(light2S&LIGHT_ON)!=0?true:false;
        light2Auto=(light2S&LIGHT_AUTO)!=0?true:false;
        light3State=(light3S&LIGHT_ON)!=0?true:false;
        light3Auto=(light3S&LIGHT_AUTO)!=0?true:false;
        light4State=(light4S&LIGHT_ON)!=0?true:false;
        light4Auto=(light4S&LIGHT_AUTO)!=0?true:false;
        Log.d("LIGHT STATE",Integer.toString(light1S)+":"+
                Integer.toString(light2S)+":"+
                Integer.toString(light3S)+":"+
                Integer.toString(light4S));
        //updateStates();
    }
    static public void updateStates(){
        light1.setImageResource(lightImages[getStateInt(light1State,light1Auto)]);
        light2.setImageResource(lightImages[getStateInt(light2State,light2Auto)]);
        light3.setImageResource(lightImages[getStateInt(light3State,light3Auto)]);
        light4.setImageResource(lightImages[getStateInt(light4State,light4Auto)]);
        light1Mode.setChecked(light1Auto);
        light2Mode.setChecked(light2Auto);
        light3Mode.setChecked(light3Auto);
        light4Mode.setChecked(light4Auto);
    }
}
