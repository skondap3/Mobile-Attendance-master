package mobileattendancecom.mobileattendance;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import java.util.UUID;


/**
 * Created by riju on 7/1/16.
 */
public class startBluetooth extends Thread{

    public static BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket bluetoothServerSocket;
    Boolean enBluetooth;
    private static final int DISCOVERABLE_DURATION = 0;
    private final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    BluetoothSocket bluetoothSocket;
    static int devicecount=0;
    Context app;

    startBluetooth(Context applicationContext)
    {
        app=applicationContext;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enBluetooth = setBluetooth(true);
        makeDiscoverable();
        start();
    }

    @Override
    public void run() {
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("MY_APP", uuid);
            while (true) {
                bluetoothSocket = bluetoothServerSocket.accept();
                devicecount = devicecount + 1;
                if (devicecount <= 50) {
                    listenDevices contDevices = new listenDevices(app,bluetoothSocket);
                }
            }
        }catch(Exception e){
        }
    }

    protected void makeDiscoverable() {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(discoverableIntent);
    }

    public static boolean setBluetooth(boolean enable) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }
}
