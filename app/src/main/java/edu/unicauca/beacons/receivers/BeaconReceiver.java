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

    public interface OnBeaconListener{
        void onBeacon(int major, int minor);
    }

    OnBeaconListener onBeaconListener;

    public BeaconReceiver(OnBeaconListener onBeaconListener) {
        this.onBeaconListener = onBeaconListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras =  intent.getExtras();
        int major = extras.getInt(EXTRA_MAJOR);
        int minor = extras.getInt(EXTRA_MINOR);

        onBeaconListener.onBeacon(major, minor);

    }


}
