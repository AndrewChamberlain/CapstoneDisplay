package com.example.arc.capstonedisplay;

/**
 * Created by arc on 12/03/18.
 */

public class StatManager {
    //-----PV Power
    public static StatDisplayer PVPower;
    static boolean initPVPower=false;
    static float alphaPVPower=0.3f;
    static float valuePVPower=0f;
    //-----Load Power
    public static StatDisplayer loadPower;
    static boolean initLoadPower=false;
    static float alphaLoadPower=0.3f;
    static float valueLoadPower=0f;
    //-----Overvoltages
    public static StatDisplayer overvoltage;
    static boolean initOvervoltage=false;
    static float alphaOvervoltage=0.3f;
    static float valueOvervoltage=0f;
    //-----States
    public static StatDisplayer PVState;
    public static StatDisplayer BatteryState;
    public static StatDisplayer LoadState;

    public static void addPVPower(float val){
        if(initPVPower){
            valuePVPower=(1-alphaPVPower)*valuePVPower+alphaPVPower*val;
        }else{
            valuePVPower=val;
            initPVPower=true;
        }
        //PVPower.setText(String.format("%4.1f",valuePVPower)+" W");
    }

    public static void addLoadPower(float val){
        if(initLoadPower){
            valueLoadPower=(1-alphaLoadPower)*valueLoadPower+alphaLoadPower*val;
        }else{
            valueLoadPower=val;
            initLoadPower=true;
        }
        //loadPower.setText(String.format("%4.1f",valueLoadPower)+" W");
    }

    public static void addOvervoltage(float val){
        if(initOvervoltage){
            valueOvervoltage=(1-alphaOvervoltage)*valueOvervoltage+alphaOvervoltage*val;
        }else{
            valueOvervoltage=val;
            initOvervoltage=true;
        }
        //overvoltage.setText(String.format("%4.1f",valueOvervoltage*100)+" %");
    }
    public static void updateAll(){
        loadPower.setText(String.format("%.1fW",valueLoadPower));
        PVPower.setText(String.format("%.1fW",valuePVPower));
        overvoltage.setText(String.format("%.1f%%",valueOvervoltage*100));
        //-----PV
        switch (Logic.PVCurrentState){
            case 1:
                PVState.setText("ON");
                break;
            case 2:
                PVState.setText("OFF");
                break;
            default:
                PVState.setText("UNKNOWN");

        }//-----Battery
        switch (Logic.batteryCurrentState){
            case 1:
                BatteryState.setText("Charging");
                break;
            case 2:
                BatteryState.setText("Discharging");
                break;
            default:
                BatteryState.setText("UNKNOWN");

        }
    }
}
