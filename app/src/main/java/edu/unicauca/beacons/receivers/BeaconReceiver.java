package edu.unicauca.beacons.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Dario Chamorro on 26/10/2016.
 */

public class BeaconReceiver extends BroadcastReceiver {

    public static final String ACTION_BEACON = "edu.unicauca.beacons.BEACON_ENCONTRADO";
    public static final String EXTRA_MAJOR = "major";
    public static final String EXTRA_MINOR = "minor";

    //Creamos esta etiqueta para determinar si se perdio o si regreso
    public static final String EXTRA_TYPE = "type";

    //Aqui estan los posibles valores que puede tener
    public static final int LOST = 0;
    public static final int FOUNDED = 1;


    //Aqui cambie los metodos del Listener para que sean de mas utilidad
    public interface OnBeaconListener{
        //void onBeacon(int major, int minor);
        //Llamariamos este metodo si se encuentra el beacon
        void beaconFounded();
        //Llamariamos este metodo si se pierde el beacon
        void beaconLosted();
    }

    OnBeaconListener onBeaconListener;

    public BeaconReceiver(OnBeaconListener onBeaconListener) {
        this.onBeaconListener = onBeaconListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras =  intent.getExtras();
        //int major = extras.getInt(EXTRA_MAJOR);
        //int minor = extras.getInt(EXTRA_MINOR);

        //Obtenemos el valor
        int type =  extras.getInt(EXTRA_TYPE);

        //Validamos si se perdio o no, y lo notificamos al activity
        // mirar MainActivity
        if(type == LOST){
            onBeaconListener.beaconLosted();
        }else{
            onBeaconListener.beaconFounded();
        }

    }


}
