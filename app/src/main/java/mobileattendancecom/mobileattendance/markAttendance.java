package mobileattendancecom.mobileattendance;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by riju on 7/3/16.
 */
public class markAttendance extends AppCompatActivity {

    ListView listvwPaired;
    ArrayList<String> arraylstPaired;
    ArrayAdapter<String> pairAdapter;
    ListItemMarkedonPaired listItemMarkonPaired;
    ArrayList<BluetoothDevice> arraypairedDevices;
    BluetoothDevice pdDevice;
    BluetoothAdapter bluetoothAdpter = null;
    dataStorage prCourses;
    ArrayList<String> ccurDate;
    ArrayList<String> ccurTime;
    String crTerm;
    courseDetails fcourses=new courseDetails();
    int curTime;
    String selectedCourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        prCourses=dataStorage.getInstance(this);
        listvwPaired = (ListView) findViewById(R.id.listvwPared);
        listItemMarkonPaired = new ListItemMarkedonPaired();
        arraylstPaired = new ArrayList<String>();
        arraypairedDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdpter = BluetoothAdapter.getDefaultAdapter();
        strBluetooth();
        pairAdapter= new ArrayAdapter<String>(markAttendance.this, android.R.layout.simple_list_item_1, arraylstPaired);
        listvwPaired.setAdapter(pairAdapter);
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
        arraylstPaired.clear();
        arraypairedDevices.clear();
        getMarkedDevices();
        listvwPaired.setOnItemClickListener(listItemMarkonPaired);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMarkedDevices();
        listvwPaired.setOnItemClickListener(listItemMarkonPaired);
    }

    private void getMarkedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdpter.getBondedDevices();
        if(pairedDevice.size()>0)
        {
            for(BluetoothDevice devices : pairedDevice)
            {
                if(!arraypairedDevices.contains(devices)) {
                    arraylstPaired.add(devices.getName() + "\n" + devices.getAddress());
                    arraypairedDevices.add(devices);
                }
            }
        }
        pairAdapter.notifyDataSetChanged();
    }

    private void strBluetooth() {
       if(!bluetoothAdpter.isEnabled())
       {
           bluetoothAdpter.enable();
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

    class ListItemMarkedonPaired implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            pdDevice = arraypairedDevices.get(position);
            ArrayList<String> valCourses = prCourses.selectCourses(crTerm, Integer.parseInt(ccurDate.get(2)), curTime);
            if (valCourses.size() > 0) {
                markDialog(markAttendance.this, "Confirm the CourseId", new String[]{"Confirm"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (prCourses.fetchCoursestatus(crTerm, Integer.parseInt(ccurDate.get(2)), selectedCourse)) {
                                    connectFaculty connectreq = new connectFaculty(markAttendance.this, pdDevice, selectedCourse, 'M');
                                    if (connectreq.connectRemote()) {
                                        Toast.makeText(getApplicationContext(), "Successfully Connected to Faculty Device", Toast.LENGTH_LONG).show();
                                        if (connectreq.writeAttendancedata().equals("Y")) {
                                            userDetails st = prCourses.selectStudent("Student");
                                            if (!prCourses.checkAttendance(st.getutaId(), selectedCourse)) {
                                                prCourses.insertAttendance(st.getutaId(), selectedCourse);
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sorry!!Not able to Connect", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Sorry!!You are Not Registered With Faculty Device", Toast.LENGTH_LONG).show();
                                }
                            }  });
            } else {
                Toast.makeText(getApplicationContext(), "Sorry!!No Courses are Available to Mark Attendance", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void markDialog(Context context, String title, String[] btnText,
                          DialogInterface.OnClickListener listener) {
        ArrayList<String> fetchedCourses = prCourses.selectCourses(crTerm, Integer.parseInt(ccurDate.get(2)),curTime);
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
}
