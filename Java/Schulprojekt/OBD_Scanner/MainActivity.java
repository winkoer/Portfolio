package com.example.obd2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorStateListDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.media.ToneGenerator;

/**
 * Mainaktivität mit runuithread
 * <p> lädt Drehzahlanzeige ( Bild)
 * <p>ruft die Klassen auf und zeichnet die Tachonadel
 * <p>Auswahl per ELM Emulator oder Bluetooth
 *
 * */
public class MainActivity extends AppCompatActivity {
    /**Gesetzter Wert für Tachonadel (FEST)(Im weiteren sind sie Variabel)*/
    public static int rpm=1000;
    /**Auswahl des Emulator startes*/
    public static boolean emulator_start=false;
    /**Auswahl des Bluetooth startes*/
    public static boolean bluetooth_start=true;
    /**Bluetooth object */
    public static OBD_Bluetooth obd_bluetooth=null;
    /**Mainactivity Context*/
    public static Context cc =null;
    /**Wassertemperatur -40 bis 215 Grad*/
    public static int watertemp=0;
    /**Geschwindigkeit von 0 bis 255 KMH*/
    public static int speed=0;
    /**Lufttemparatur von -40 bis 215 Grad*/
    public static int airtemp=0;
    /**Motorbelastung von 0 bis 100%*/
    public static int engload = 0;
    /**Benzinanzeige von 0 bis 100% */
    public static int Fuel = 0;
    public static ColorStateList ctt=null;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        cc = this;


        if (bluetooth_start) {
           obd_bluetooth = new OBD_Bluetooth();


            int ret = obd_bluetooth.init(  cc);
            //() -> OBD_Bluetooth.HandlerThread


        }

        Timer timerObj = new Timer(true);
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {




                        if (emulator_start) {
                            OBD_Befehle obd_befehle = new OBD_Befehle();
                            String ip= "192.168.xxx.xx";   //Ip von Gerät auf dem der Emulator läuft
                            obd_befehle.Socket1(ip,"010C");
                            rpm = obd_befehle.Drehzahl1;

                        }


                        if (bluetooth_start) {


                            rpm = obd_bluetooth.mm_rpm;
                            speed = obd_bluetooth.mm_speed;
                            watertemp = obd_bluetooth.mm_water;
                            airtemp = obd_bluetooth.mm_airtmp;
                            engload = obd_bluetooth.mm_engload;
                            Fuel = obd_bluetooth.mm_Fuel;


                        }

                        double drehzahl = rpm / 61.11; // 11.000 rpm = 180grad


                        ImageView va = findViewById(R.id.imageView3);
                        TextView t = findViewById(R.id.id_hello);
                        TextView t1 = findViewById(R.id.id_hello2);
                        TextView t2 = findViewById(R.id.id_hello3);
                        TextView t3 = findViewById(R.id.id_hello4);
                        TextView t4 = findViewById(R.id.id_hello5);

                        if(ctt==null)ctt = t3.getTextColors(); //init


                        if (watertemp > 70){t1.setTextColor(Color.GREEN);}
                        if (watertemp > 110){
                            t1.setTextColor(Color.RED);
                        }else{t1.setTextColor(ctt);}
                        if (airtemp > 50) {
                            t2.setTextColor(Color.RED);
                        }else{t2.setTextColor(ctt);}
                        if (engload > 90) {
                            t3.setTextColor(Color.RED);
                        }else{t3.setTextColor(ctt);}

                        t.setTextSize(30);
                        t1.setTextSize(30);
                        t2.setTextSize(30);
                        t3.setTextSize(30);
                        t4.setTextSize(30);

                        t.setText("Geschwindigkeit : "+ speed +" KM/h ");
                        t1.setText("Wasser : "+ watertemp +" Grad Celsius ");
                        t2.setText("Luft : "+ airtemp +" Grad Celsius ");
                        t3.setText("Motorlast : "+ engload +" % ");
                        t4.setText("Tankfüllung : "+ Fuel +" % ");

                        Log.d("DEBUG", "" + va.getMeasuredWidth() + ", " + va.getMeasuredHeight());

                        int wi = va.getMeasuredWidth();
                        int he = va.getMeasuredHeight();
                        double radi = he;
                        int dx = (int) (radi * Math.cos(Math.toRadians(drehzahl)));
                        int dy = (int) (radi * Math.sin(Math.toRadians(drehzahl)));

                        if (wi > 0) {



                            Bitmap b = Bitmap.createBitmap(va.getMeasuredWidth(), va.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                            Canvas c = new Canvas(b);
                            //c.drawColor(Color.WHITE);
                            Paint paint = new Paint();
                            paint.setColor(Color.BLACK);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(8);
                            paint.setAntiAlias(true);
                            //c.drawLine(200, 200, 500, 500, paint);
                            c.drawLine(wi/2 - dx, he - dy, wi/2, he, paint);

                            va.setImageBitmap(b);
                            va.postInvalidate();
                        }
                    }//run
                  });
                //perform your action here
            };
        };
        timerObj.schedule(timerTaskObj, 0, 100);



        Button button = (Button) findViewById(R.id.B_Start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                if (bluetooth_start) {
                    int ret = obd_bluetooth.init(cc);
                }


                Log.d("Info", "This is my message");


                if (emulator_start) {
                    OBD_Befehle obd_befehle = new OBD_Befehle();
                    String ip= "192.168.xxx.xx";  //Ip des Computers auf dem der Emulator läuft
                    obd_befehle.Socket1(ip,"010C");
                    rpm = obd_befehle.Drehzahl1;
                }


            }
        });


    }//oncreate


}
