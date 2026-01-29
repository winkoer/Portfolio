package com.example.obd2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**Socket Test mit Elm Emulator auf port 35000*/
public class OBD_Befehle {
    /** interne Drehzahl*/
    public static int Drehzahl1=0 ;
    /** Mittelwert*/
    public static int[] intArray = new int[100];

//emulator.answer['RPM'] = '<exec>ECU_R_ADDR_E + " 04 41 0C %.4X" % int(4 * 500)</exec><writeln />'
//https://github.com/Ircama/ELM327-emulator
//c:\elm\elm :  elm -n 35000

    /**
     * Socket funktion für den python Emulator auf port 35000
     * <p>bildet mittelwert aus 10 werten und speichert es in Drehzahl1
     * @param ip: IP Adresse des Emulators
     * @param cmd : OBD Komanndo , Beispiel 010C für Drehzahl
     * @return 1: ok , 0: failed
     * */
    public int Socket1(String ip,String cmd) {
        String text1 = "";

        try {

            Log.i("Info", "start obd:"+cmd);


            Socket s = new Socket(ip, 35000);
            if(!s.isConnected()){
                Log.i("Info", "start obd out:"+cmd);
                return 0;
            }
            PrintWriter out =   new PrintWriter(s.getOutputStream(), true);
            InputStreamReader bs = new InputStreamReader(s.getInputStream());
            out.println(cmd);
            BufferedReader reader = new BufferedReader(bs);

            String line;
            while ((line = reader.readLine()).length() != 0) {
                Log.i("Info", "start obd out:"+cmd);
                text1 += line;

                //für tests mit powershell reader
                if(line.length()==21) {break;}

            }
            Log.i("Info", "stop obd"+text1);
            reader.close();
            s.close();

        }
        catch (UnknownHostException ex) {

            Log.i("Info", "This is my message1 "+ ex.getMessage());
        }
        catch (IOException ex) {

            Log.i("Info", "This is my message2 "+ ex.getMessage());

            if (Drehzahl1 > 0) {Drehzahl1 = 0;	      return 1;}
        }

        if(text1.length()>17 && text1.contains("0142")) {
            System.out.print(text1);
        }
            if(text1.length()>17 && text1.contains("010C")) {
            text1 = text1.substring(17);


            int Drehzahl2 = Integer.decode("0x" + text1) / 4;

                //010C7E8 04 41 0D 3E80
                //0100441 0D 3E80
                //010C41 0C 14 5F



            // mittelwert aus 10 werten.
            if (Drehzahl2 != Drehzahl1) {

                int sum = 0;
                //mittelwert...
                for (int i = 0; i < 9; i++) {
                    intArray[i] = intArray[i + 1];
                    intArray[9] = Drehzahl2;
                }
                for (int i = 0; i < 10; i++) {
                    sum += intArray[i];
                }
                Drehzahl1 = sum / 10;

                Log.i("Info", text1 + ":" + Drehzahl2 + "\n");

                return 1;
            }
        }
        return 0;

    }

}
