package com.example.arc.capstonedisplay; /**
 * Created by arc on 10/01/18.
 * Designed to handle all communications to and from the Raspberry Pi server
 */
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.AsyncTask;
import android.view.MenuItem;

public class CustomConnection {
    static Context context;
    static Reader reader;
    static MenuItem item;
    //-----Headers
    byte HEADER_LIGHT=(byte)0;
    byte HEADER_LOAD_STATE=(byte)1;
    byte HEADER_BATTERY_STATE=(byte)2;

    static final int STATE_NOT_CONNECTED=0;
    static final int STATE_CONNECTING=1;
    static final int STATE_CONNECTED=2;

    static int state=STATE_NOT_CONNECTED;

    static String dstAddr;
    static int dstPort;
    static Socket socket=null;


    static public void setup(Context c,MenuItem i){
        context=c;
        item=i;
        Utils.tintMenuIcon(context,item,android.R.color.darker_gray);
    }
    static public void connect(String address, int port){
        dstAddr=address;
        dstPort=port;
        //-----Connect
        new Thread(new Runnable(){
            public void run(){
                //-----Set Icon
                state=STATE_CONNECTING;
                Utils.tintMenuIcon(context,item,android.R.color.holo_orange_light);
                //-----Try connection
                try {
                    Log.d("CustomConnection","Connecting to server...");
                    socket=new Socket();
                    socket.connect(new InetSocketAddress(dstAddr, dstPort), 5000);
                    state=STATE_CONNECTED;
                    //-----Send the circuit state
                    Logic.updateServer();
                    //-----Start Task
                    Log.d("CustomConnection","Starting Reader...");
                    reader=new Reader();
                    reader.execute();
                    //-----Set icon
                    Utils.tintMenuIcon(context,item,android.R.color.holo_green_dark);
                    Log.d("CustomConnection","Connected");
                }catch(UnknownHostException e){
                    e.printStackTrace();
                    state=STATE_NOT_CONNECTED;
                }catch(IOException e){
                    e.printStackTrace();
                    Utils.tintMenuIcon(context,item,android.R.color.holo_red_dark);
                    Utils.displayConnectionError(context,e.getMessage());
                    state=STATE_NOT_CONNECTED;
                }
            }
        }).start();
    }

    static public void disconnect(){
        if(state==STATE_CONNECTED){
            try {
                Log.d("CustomConnection","Disconnecting...");
                socket.close();
                Utils.tintMenuIcon(context,item,android.R.color.darker_gray);
                state=STATE_NOT_CONNECTED;
            }catch(IOException e){
                e.printStackTrace();
            }
            Log.d("CustomConnection","Disconnected");
        }
    }
    static public void sendMsg(final byte[] msg){
        if(state==STATE_CONNECTED){
            new Thread(new Runnable(){
                public void run(){
                    //-----Try connection
                    try {
                        socket.getOutputStream().write(msg);
                    }catch(UnknownHostException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                        Utils.tintMenuIcon(context,item,android.R.color.holo_red_dark);
                        Utils.displayConnectionError(context,e.getMessage());
                    }
                }
            }).start();
        }
    }

    static private class Reader extends AsyncTask<String, Void, String>{
        //-----http://android-er.blogspot.ca/2013/12/implement-socket-on-android-to.html
        String msg;
        @Override
        public String doInBackground(String... args){
            try {
                Log.d("RUNNING:","READER");
                InputStream inputStream = socket.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    Log.d("RUNNING:",String.valueOf(bytesRead));
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    //-----DO STUFF BASED ON DATA
                    //*****SENSOR DATA
                    //-----Extract epoch
                    ByteBuffer epochBuffer=ByteBuffer.wrap(buffer,0,8);
                    long epoch=epochBuffer.getLong();
                    //-----Get data
                    ByteBuffer PVPanelVoltageBuffer=ByteBuffer.wrap(buffer,8,8);
                    ByteBuffer PVPanelCurrentBuffer=ByteBuffer.wrap(buffer,16,8);
                    ByteBuffer BatteryVolageBuffer=ByteBuffer.wrap(buffer,24,8);
                    ByteBuffer SlackBusVoltageBuffer=ByteBuffer.wrap(buffer,32,8);
                    ByteBuffer PVJunctionVoltageBuffer=ByteBuffer.wrap(buffer,40,8);
                    ByteBuffer LoadPowerBuffer=ByteBuffer.wrap(buffer,48,8);
                    //-----Convert
                    float PVPanelVoltage=(float)(PVPanelVoltageBuffer.getLong()/1000.0);
                    float PVPanelCurrent=(float)(PVPanelCurrentBuffer.getLong()/1000.0);
                    float BatteryVolage=(float)(BatteryVolageBuffer.getLong()/1000.0);
                    float SlackBusVoltage=(float)(SlackBusVoltageBuffer.getLong()/1000.0);
                    float PVJunctionVoltage=(float)(PVJunctionVoltageBuffer.getLong()/1000.0);
                    float LoadPower=(float)(LoadPowerBuffer.getLong()/1000.0);
                    //-----Push
                    DataStorage.pushData(epoch,new float[]{PVPanelVoltage,PVPanelCurrent,BatteryVolage,SlackBusVoltage,PVJunctionVoltage,LoadPower});
                    //*****CIRCUIT STATE
                    //-----Get data
                    ByteBuffer gridState=ByteBuffer.wrap(buffer,56,8);
                    ByteBuffer batteryState=ByteBuffer.wrap(buffer,64,8);
                    ByteBuffer PVState=ByteBuffer.wrap(buffer,72,8);
                    ByteBuffer light1=ByteBuffer.wrap(buffer,80,8);
                    ByteBuffer light2=ByteBuffer.wrap(buffer,88,8);
                    ByteBuffer light3=ByteBuffer.wrap(buffer,96,8);
                    ByteBuffer light4=ByteBuffer.wrap(buffer,104,8);
                    //-----Convert
                    int gridStateValue=(int)gridState.getLong();
                    int batteryStateValue=(int)batteryState.getLong();
                    int PVStateValue=(int)PVState.getLong();
                    int light1Value=(int)light1.getLong();
                    int light2Value=(int)light2.getLong();
                    int light3Value=(int)light3.getLong();
                    int light4Value=(int)light4.getLong();
                    //-----Push
                    Logic.setCircuitState(PVStateValue,batteryStateValue,light1Value,light2Value,light3Value,light4Value);

                }
                state=STATE_NOT_CONNECTED;
                socket.close();
                Utils.tintMenuIcon(context,item,android.R.color.holo_red_light);
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
