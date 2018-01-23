package com.example.arc.capstonedisplay; /**
 * Created by arc on 10/01/18.
 * Designed to handle all communications to and from the Raspberry Pi server
 */
import java.io.ByteArrayOutputStream;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.AsyncTask;

public class CustomConnection {
    Reader reader;

    String dstAddr;
    int dstPort;
    Socket socket=null;

    public CustomConnection(){
        reader=new Reader();
    }
    public void connect(String address, int port){
        dstAddr=address;
        dstPort=port;
        //-----Connect
        new Thread(new Runnable(){
            public void run(){
                //open socket
                try {
                    Log.d("CustomConnection","Connecting to server...");
                    socket = new Socket(dstAddr, dstPort);
                    //-----Set connection icon
                    //-----Start Task
                    Log.d("CustomConnection","Starting Reader...");
                    reader.execute();
                    Log.d("CustomConnection","Connected");
                }catch(UnknownHostException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                    Log.d("CustomConnection Err:",e.toString());
                }
            }
        }).start();
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
                    SimpleDateFormat format=new SimpleDateFormat("H:m:s:S");
                    Log.d("EPOCH",format.format(new Date()));
                    //-----Get data
                    ByteBuffer PVPanelVoltageBuffer=ByteBuffer.wrap(buffer,8,8);
                    ByteBuffer PVPanelCurrentBuffer=ByteBuffer.wrap(buffer,16,8);
                    ByteBuffer BatteryVolageBuffer=ByteBuffer.wrap(buffer,24,8);
                    ByteBuffer SlackBusVoltageBuffer=ByteBuffer.wrap(buffer,32,8);
                    ByteBuffer PVJunctionVoltageBuffer=ByteBuffer.wrap(buffer,40,8);
                    //-----Print
                    Log.d("PV Voltage",String.valueOf(PVPanelVoltageBuffer.getLong()/1000.0));
                    Log.d("PV Current",String.valueOf(PVPanelCurrentBuffer.getLong()/1000.0));
                    Log.d("Battery Voltage",String.valueOf(BatteryVolageBuffer.getLong()/1000.0));
                    Log.d("Slack Voltage",String.valueOf(SlackBusVoltageBuffer.getLong()/1000.0));
                    Log.d("Junction Voltage",String.valueOf(PVJunctionVoltageBuffer.getLong()/1000.0));

                }
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
