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

    static final UUID BEACON_UUID = UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d");
    static final int MAJOR = 40685;
    static final int MINOR = 32341;

    BeaconManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        manager =  new BeaconManager(getApplicationContext());
        manager.connect(new BeaconManager.ServiceReadyCallback(){

            @Override
            public void onServiceReady(){
                manager.startMonitoring(new Region("beacons", BEACON_UUID, null, null));
                manager.setMonitoringListener(App.this);
            }

        });

    }


    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {

        //Enviar un mensaje en BROADCAST
        Intent intent = new Intent(BeaconReceiver.ACTION_BEACON);
        intent.putExtra(BeaconReceiver.EXTRA_MAJOR, list.get(0).getMajor());
        intent.putExtra(BeaconReceiver.EXTRA_MINOR, list.get(0).getMinor());

        sendBroadcast(intent);


        //Crear notificación

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Beacon Detectado")
                .setContentText("MINOR = "+list.get(0).getMinor())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        NotificationManager managerNotify = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        managerNotify.notify(101, notification);



    }

    @Override
    public void onExitedRegion(Region region) {

    }
}
