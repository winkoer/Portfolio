package com.example.obd2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import android.widget.Toast;

/**
 * Bluetooth Klasse mit Thread
 * <br> speichert die Anezigewerte in lokale Variablen , die dann vom
 * <br> Main UI Thread ausgelesen werden : runOnUiThread
 * */
public class OBD_Bluetooth {
    /**interner Adapter*/
    public static BluetoothAdapter mBluetoothAdapter = null;
    /**Adapter erkennung verbindung*/
    public static BluetoothDevice device = null;
    /**Absoluter erkennungscode*/
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-xxxxxxxxxx");    //Nicht für euch
    /**Verbinden zum bluetooth Socket*/
    public static BluetoothSocket mmSocket = null;
    /**lese-stream zu ELM Adapter*/
    public InputStream mmInStream = null;
    /**write-stream zu ELM Adapter*/
    public OutputStream mmOutStream = null;
    /**interne Drehzahl*/
    public static int mm_rpm = 0;
    /**interne Geschwindigkeit*/
    public static int mm_speed = 0;
    /**interne Wassertemperatur*/
    public static int mm_water = 0;
    /**interne Lufttemparatur*/
    public static int mm_airtmp = 0;
    /**interne Motorbelastung*/
    public static int mm_engload = 0;
    /**interne Benzinanzeige */
    public static int mm_Fuel = 0;

    /**
    * Init Funktion
     * <p> - sucht Default Adapter
     * <p> - sucht verbundede Devices
     * <p> - started den Bluetooth Thread
     * @param cc Kontext für Anzeige UI Thread
     * @return 0: failed , 1: OK
     * */
    public int init(Context cc) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(cc, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return 0;
        }


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Toast.makeText(cc, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return 0;

        }


        Set<BluetoothDevice> all_devices = mBluetoothAdapter.getBondedDevices();
        if (all_devices.size() > 0) {
            for (BluetoothDevice currentDevice : all_devices) {
                Log.d("DEBUG", "" + currentDevice.getName());

                if (currentDevice.getName().toLowerCase().endsWith("obdii")) {
                    BluetoothSocket tmp = null;

                    String mac = currentDevice.getAddress();
                    Toast.makeText(   cc, "obd found" + mac, Toast.LENGTH_LONG).show();
                    device = mBluetoothAdapter.getRemoteDevice(mac);
                    try {
                        tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                    } catch (IOException e) {
                        Log.e("ERR", "Socket Type: create() failed", e);
                    }
                    mmSocket = tmp;
                    if (tmp == null)
                        Toast.makeText(cc, "obd create nok" + mac, Toast.LENGTH_LONG).show();
                }
            }
        }

        if (mmSocket != null) {
            // Stellt verbindung zum Bluetooth Socket
            try {
                mmSocket.connect();
            } catch (IOException e) {

                try {
                    mmSocket.close();
                    Log.d("ERR", " close() socket during connection failure", e);
                } catch (IOException e2) {
                    Log.d("ERR", "unable to close() socket during connection failure", e2);
                }
            ;
                return 0;
            }
            Toast.makeText(cc, "obd connect ok", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(cc, "obd connect not ok", Toast.LENGTH_LONG).show();

            return 0;

        }

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        if (mmSocket != null) {

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.d("ERR", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            if (mmInStream != null && mmOutStream != null)
                Toast.makeText(cc, "obd io ok", Toast.LENGTH_LONG).show();
        }

        Thread b = new BThread();
        b.start();
        return 1;
    }//init

    /**
     * Btread Klasse
     * <br> - schickt und liest messages alle 100ms...
     *
     * */
    public class BThread extends Thread {

        public void run() {

            while(true) {
                mm_rpm = readBsocket("010C");
                mm_speed = readBsocket("010D");
                mm_water = readBsocket("0105");
                mm_airtmp = readBsocket("010F");
                mm_engload = readBsocket("0104");
                mm_Fuel = readBsocket("012F");
                //mm_rpm = 1000; Testwert
                Log.d("ERR", "This is my msg----------------------------" );
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * readBsocket Funktion
     * <p> - sucht Default Adapter
     * <p> - sucht verbundede Devices
     * @param cmd OBD Kommando Beispiel 010C für Drehzahl
     * @return 1: ok , 0: failed
     * */
    public int readBsocket(String cmd) {

        if(mmOutStream==null){return 0;}

        try {
            mmOutStream.write(cmd.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            //e.printStackTrace();
            Log.d("ERR", "This is my err"+e);
        }

        String s,msg="";
        while (true) {
            try {
                byte[] buffer = new byte[1];
                int bytes = mmInStream.read(buffer, 0, buffer.length);
                s = new String(buffer);
                for (int i = 0; i < s.length(); i++) {
                    char x = s.charAt(i);
                    msg = msg + x;
                    if (x == 0x3e) {
                        //mHandler.obtainMessage(OBDActivity.MESSAGE_READ, buffer.length, -1, msg).sendToTarget();
                        //msg=""; Debugging Nachrichten
                        Log.d("ERR", "This is my msg" + msg);

                        //rpm 010C41 0C 0F 9C  <
                        // oil 010541 05 6A
                        if (msg.length() > 14 && msg.contains("010C") && !msg.contains("SEARCHING")) { //rpm
                            msg = msg.substring(10, 15).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg) / 4;
                        }
                        if (msg.length() > 11 && msg.contains("010D")) {  //speed
                            msg = msg.substring(10, 12).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg);
                        }
                        if (msg.length() > 11 && msg.contains("0105")) {  //watertemperatur
                            msg = msg.substring(10, 12).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg) - 40;
                        }

                        if (msg.length() > 11 && msg.contains("010F")) {  //air temperatur
                            msg = msg.substring(10, 12).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg) - 40;
                        }

                        if (msg.length() > 11 && msg.contains("0104")) {  //engineload
                            msg = msg.substring(10, 12).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg) * 100 / 255;
                        }

                        if (msg.length() > 11 && msg.contains("012F")) {   //Fuel
                            msg = msg.substring(10, 12).trim().replace(" ", "");
                            //Toast.makeText(this, "obd:"+msg, Toast.LENGTH_LONG).show();
                            return Integer.decode("0x" + msg)*100/255;
                        }
                        return 1;
                    }
                }

            } catch(IOException e){
                mmOutStream = null;
                Log.d("ERR", "This is my msg err" + e);
                break;
            }
        }//while
        return 0;
    }

}
