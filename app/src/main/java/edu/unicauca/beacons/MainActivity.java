package edu.unicauca.beacons;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.SystemRequirementsChecker;

import edu.unicauca.beacons.receivers.BeaconReceiver;

public class MainActivity extends AppCompatActivity implements BeaconReceiver.OnBeaconListener {

    BeaconReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        receiver = new BeaconReceiver(this);
        IntentFilter filter = new IntentFilter(BeaconReceiver.ACTION_BEACON);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBeacon(int major, int minor) {

    }
}
