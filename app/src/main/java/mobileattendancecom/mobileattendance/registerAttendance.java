package mobileattendancecom.mobileattendance;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by riju on 7/1/16.
 */
public class registerAttendance extends AppCompatActivity{

    ListView listViewDetected;
    ListView listViewPaired;
    ArrayList<String> arrayListpaired;
    ArrayAdapter<String> adapter,detectedAdapter;
    BluetoothDevice bdDevice;
    BluetoothDevice device;
    BluetoothClass bdClass;
    ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    ListItemClicked listItemClicked;
    ListItemClickedonPaired listItemClickedonPaired;
    dataStorage regCourses;
    ArrayList<String> ccurDate;
    ArrayList<String> ccurTime;
    String crTerm;
    courseDetails fcourses=new courseDetails();
    String selectedCourse;
    int curTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_attendance);
        regCourses=dataStorage.getInstance(this);
        listViewDetected = (ListView) findViewById(R.id.listViewDetected);
        listViewPaired = (ListView) findViewById(R.id.listViewPaired);
        arrayListpaired = new ArrayList<String>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
        listItemClickedonPaired = new ListItemClickedonPaired();
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        adapter= new ArrayAdapter<String>(registerAttendance.this, android.R.layout.simple_list_item_1, arrayListpaired);
        detectedAdapter = new ArrayAdapter<String>(registerAttendance.this, android.R.layout.simple_list_item_single_choice);
        listViewDetected.setAdapter(detectedAdapter);
        listItemClicked = new ListItemClicked();
        detectedAdapter.notifyDataSetChanged();
        listViewPaired.setAdapter(adapter);
        onBluetooth();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String now = df.format(new Date());
        ccurDate = new ArrayList<String>();
        for (String retval: now.split("/")) {
            ccurDate.add(retval);
        }
        String mdscat = ccurDate.get(0)+ccurDate.get(1);
        int mdTerm = Integer.parseInt(mdscat);
        crTerm = fcourses.getTermvalue(mdTerm);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dstr = sdf.format(new Date());
        ccurTime = new ArrayList<String>();
        for (String retval: dstr.split(":")) {
            ccurTime.add(retval);
        }
        String tscat = ccurTime.get(0)+ccurTime.get(1);
        curTime = Integer.parseInt(tscat);
        arrayListBluetoothDevices.clear();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerAttendance.this.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onStart() {
        super.onStart();
       // IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
       // registerAttendance.this.registerReceiver(myReceiver, intentFilter);
       // bluetoothAdapter.startDiscovery();
        getPairedDevices();
        listViewDetected.setOnItemClickListener(listItemClicked);
        listViewPaired.setOnItemClickListener(listItemClickedonPaired);
    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if(pairedDevice.size()>0)
        {
            for(BluetoothDevice devices : pairedDevice)
            {
                if(!arrayListPairedBluetoothDevices.contains(devices)) {
                    arrayListpaired.add(devices.getName() + "\n" + devices.getAddress());
                    arrayListPairedBluetoothDevices.add(devices);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    class ListItemClicked implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bdDevice = arrayListBluetoothDevices.get(position);
            Boolean isBonded = false;
            try {
                isBonded = createBond(bdDevice);
                    if (isBonded) {
                        arrayListpaired.add(bdDevice.getName() + "\n" + bdDevice.getAddress());
                        arrayListPairedBluetoothDevices.add(bdDevice);
                        arrayListBluetoothDevices.remove(device);
                        detectedAdapter.remove(bdDevice.getName() + "\n" + bdDevice.getAddress());
                        detectedAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Complete Pairing Process and Click on Paired Device Name to Complete Registration", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry!!Not able to Pair", Toast.LENGTH_LONG).show();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ListItemClickedonPaired implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bdDevice = arrayListPairedBluetoothDevices.get(position);
            ArrayList<String> valCourses = regCourses.selectCourses(crTerm, Integer.parseInt(ccurDate.get(2)), curTime);
            if (valCourses.size() > 0) {
                popDialog(registerAttendance.this, "Confirm the CourseId", new String[]{"Confirm"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connectFaculty connectreq = new connectFaculty(registerAttendance.this, bdDevice, selectedCourse, 'R');
                                if (connectreq.connectRemote()) {
                                    Toast.makeText(getApplicationContext(), "Successfully Connected to Faculty Device", Toast.LENGTH_LONG).show();
                                    if (connectreq.writeStudentdata()) {
                                        regCourses.changeCoursestatus(crTerm,Integer.parseInt(ccurDate.get(2)),selectedCourse);
                                                //         Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Sorry!!Not able to Connect", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Sorry!!No Course Details are Available to Register", Toast.LENGTH_LONG).show();
            }
        }
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try
                {
                    device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                   // device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
                }
                catch (Exception e) {
                    Log.i("Log", "Inside the exception: ");
                    e.printStackTrace();
                }
                if(arrayListBluetoothDevices.size()<1)
                {
                    if(!arrayListPairedBluetoothDevices.contains(device)) {
                        detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                        arrayListBluetoothDevices.add(device);
                        detectedAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    boolean flag = true;
                    for(int i = 0; i<arrayListBluetoothDevices.size();i++)
                    {
                        if(device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress()))
                        {
                            flag = false;
                        }
                    }
                    if(flag == true)
                    {
                        if(!arrayListPairedBluetoothDevices.contains(device)) {
                            detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                            arrayListBluetoothDevices.add(device);
                            detectedAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    public boolean createBond(BluetoothDevice btDevice) throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    private void onBluetooth() {
        if(!bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();
        }

    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(getApplicationContext(), studentHome.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(homeIntent);
    }

    public void popDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {
        ArrayList<String> fetchedCourses = regCourses.selectCourses(crTerm, Integer.parseInt(ccurDate.get(2)),curTime);
        if (fetchedCourses.size() > 0) {
            int i = 0;
            final CharSequence[] items = new CharSequence[fetchedCourses.size()];
            for (String g : fetchedCourses) {
                items[i] = g;
                i++;
            }
            selectedCourse = (String)items[0];
            if (listener == null)
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface,
                                        int paramInt) {
                        paramDialogInterface.dismiss();
                    }
                };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setSingleChoiceItems(items, 0,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            selectedCourse = (String)items[item];
                        }
                    });
            builder.setPositiveButton(btnText[0], listener);
            if (btnText.length != 1) {
                builder.setNegativeButton(btnText[1], listener);
            }
            builder.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
 //       arrayListBluetoothDevices.clear();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerAttendance.this.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myReceiver != null) {
            try {
                unregisterReceiver(myReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
