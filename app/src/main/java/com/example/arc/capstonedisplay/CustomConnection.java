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
    CustomConnection myThis=this;
    Context context;
    Reader reader;
    MenuItem item;

    static final int STATE_NOT_CONNECTED=0;
    static final int STATE_CONNECTING=1;
    static final int STATE_CONNECTED=2;

    int state=STATE_NOT_CONNECTED;

    String dstAddr;
    int dstPort;
    Socket socket=null;

    public CustomConnection(){

    }
    public void setup(Context c,MenuItem i){
        context=c;
        item=i;
        Utils.tintMenuIcon(context,item,android.R.color.darker_gray);
    }
    public void connect(String address, int port){
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
                    //-----Set connection icon
                    //-----Start Task
                    Log.d("CustomConnection","Starting Reader...");
                    reader=new Reader();
                    reader.execute();
                    //-----Set icon
                    Utils.tintMenuIcon(context,item,android.R.color.holo_green_dark);
                    Log.d("CustomConnection","Connected");
                }catch(UnknownHostException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                    Utils.tintMenuIcon(context,item,android.R.color.holo_red_dark);
                    Utils.displayConnectionError(context,myThis,e.getMessage());
                }
            }
        }).start();
    }

    public void disconnect(){
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

    private class Reader extends AsyncTask<String, Void, String>{
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
                    //Log.d("RUNNING:",String.valueOf(bytesRead));
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    //-----DO STUFF BASED ON DATA
                    //-----Extract epoch
                    ByteBuffer epochBuffer=ByteBuffer.wrap(buffer,0,8);
                    long epoch=epochBuffer.getLong();
                    //SimpleDateFormat format=new SimpleDateFormat("H:m:s:S");
                    //Log.d("EPOCH",format.format(new Date()));
                    //-----Get data
                    ByteBuffer PVPanelVoltageBuffer=ByteBuffer.wrap(buffer,8,8);
                    ByteBuffer PVPanelCurrentBuffer=ByteBuffer.wrap(buffer,16,8);
                    ByteBuffer BatteryVolageBuffer=ByteBuffer.wrap(buffer,24,8);
                    ByteBuffer SlackBusVoltageBuffer=ByteBuffer.wrap(buffer,32,8);
                    ByteBuffer PVJunctionVoltageBuffer=ByteBuffer.wrap(buffer,40,8);
                    //-----Print
                    float PVPanelVoltage=(float)(PVPanelVoltageBuffer.getLong()/1000.0);
                    float PVPanelCurrent=(float)(PVPanelCurrentBuffer.getLong()/1000.0);
                    float BatteryVolage=(float)(BatteryVolageBuffer.getLong()/1000.0);
                    float SlackBusVoltage=(float)(SlackBusVoltageBuffer.getLong()/1000.0);
                    float PVJunctionVoltage=(float)(PVJunctionVoltageBuffer.getLong()/1000.0);
                    DataStorage.pushData(epoch,new float[]{PVPanelVoltage,PVPanelCurrent,BatteryVolage,0,SlackBusVoltage,PVJunctionVoltage,0});
                    Log.d("PVPanelVoltage",Float.toString(PVPanelVoltage));

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
