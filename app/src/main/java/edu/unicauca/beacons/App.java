package edu.unicauca.beacons;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import edu.unicauca.beacons.receivers.BeaconReceiver;

/**
 * Created by Dario Chamorro on 26/10/2016.
 */

public class App extends Application implements BeaconManager.MonitoringListener{

    //Datos de Ejemplo
    static final UUID BEACON_UUID = UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d");
    //Especificar con el valor q ustedes tienen
    static final int MAJOR = 40685;


    BeaconManager manager;

    //Boolean para determinar si se "reencontro", inicializado en onCreate como true
    // si se reencuentra seria false
    boolean firstTime;
    Region region;

    @Override
    public void onCreate() {
        super.onCreate();

        //SI deberiamos especificar el MAJOR
        region = new Region("beacons", BEACON_UUID,MAJOR, null);

        this.firstTime = true;
        manager =  new BeaconManager(getApplicationContext());
        manager.connect(new BeaconManager.ServiceReadyCallback(){

            @Override
            public void onServiceReady(){
                startBeaconMonitoring(true);
            }

        });

    }


    //Coloque estos metodos apra que podamos iniciar y detener
    //el monitoreo desde el activity

    public void startBeaconMonitoring(boolean reset){

        //Reset lo q haces esq cuando se inicia la busqueda
        // el beacon se considera nuevamente como la primera detección
        // y no el reencuentro
        if(reset)
            firstTime = true;

        manager.startMonitoring(region);
        manager.setMonitoringListener(App.this);
    }

    public void stopBeaconMonitoring(){
        manager.stopMonitoring(region);
    }


    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        if(!firstTime){
            //Aqui solo entraria si firstTime es false, en ese caso
            // es cuando el beacon se aleja y se vuelve a detectar
            // en el piloto seria cuando se encuentra a la persona
            this.firstTime = false;

            //Enviar un mensaje en BROADCAST
            Intent intent = new Intent(BeaconReceiver.ACTION_BEACON);
            intent.putExtra(BeaconReceiver.EXTRA_MAJOR, list.get(0).getMajor());
            intent.putExtra(BeaconReceiver.EXTRA_MINOR, list.get(0).getMinor());

            //Agregarmos la información de que se "encontro" en el intent
            // mirar BeconReceiver

            intent.putExtra(BeaconReceiver.EXTRA_TYPE, BeaconReceiver.FOUNDED);
            sendBroadcast(intent);

        }
        //Si no entra en el if, continua lo que seria cualquier caso
        //o primer caso q no es necesario evaluar
        firstTime = false;





    }

    //Metodo que se dispara cuando un dispositivo sale de la region
    @Override
    public void onExitedRegion(Region region) {
        //Aqui se detecta que la persona se perdio por lo que debemos
        // notificar a traves del BeaconReceiver

        Intent intent =  new Intent(BeaconReceiver.ACTION_BEACON);

        //Agregarmos la información de que se "perdio" en el intent
        // mirar BeconReceiver

        intent.putExtra(BeaconReceiver.EXTRA_TYPE, BeaconReceiver.LOST);
        sendBroadcast(intent);

    }
}
