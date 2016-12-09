package edu.unicauca.beacons;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.SystemRequirementsChecker;

import edu.unicauca.beacons.receivers.BeaconReceiver;

public class MainActivity extends AppCompatActivity implements BeaconReceiver.OnBeaconListener {

    BeaconReceiver receiver;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        //Obtenemos una instancia de Application para iniciar y detener el monitoreo desde los beacons
        app = (App) getApplication();

        receiver = new BeaconReceiver(this);
        IntentFilter filter = new IntentFilter(BeaconReceiver.ACTION_BEACON);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    //Este metodo se invoca al reencontrar el beacon
    @Override
    public void beaconFounded() {
        // Detenemos el monitoreo del beacon
        app.stopBeaconMonitoring();
        //... Logica para conectarse y comunicarse con el arduino
        //...
        // enviamos un true xq NO necesitamos que la detección del beacon se tome como un "reencuentro"
        app.startBeaconMonitoring(true);
    }

    //Este metodo se invoca al perderse el beacon
    @Override
    public void beaconLosted() {
        // Detenemos el monitoreo del beacon
        app.stopBeaconMonitoring();
        //... Logica para conectarse y comunicarse con el arduino
        //...
        // enviamos un true xq SI necesitamos que la detección del beacon se tome como un "reencuentro"
        app.startBeaconMonitoring(false);
    }
}
