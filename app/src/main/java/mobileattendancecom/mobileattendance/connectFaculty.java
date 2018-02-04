package mobileattendancecom.mobileattendance;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by riju on 7/2/16.
 */
public class connectFaculty {

    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    ObjectInputStream instream;
    ObjectOutputStream outstream;
    private final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    String courseIdentifier;
    char callFlag;
    String Regmessage;
    userDetails stud;
    dataStorage data;
    Context appcontext;

    connectFaculty(Context appcontext,BluetoothDevice device,String courseIdentifier, char callFlag)
    {
        this.courseIdentifier=courseIdentifier;
        this.callFlag=callFlag;
        this.appcontext=appcontext;
        bluetoothDevice = device;
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connectRemote()
    {
        try {
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            connectException.printStackTrace();
        }
        if (bluetoothSocket.isConnected()) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean writeStudentdata(){
        try {
            outstream = new ObjectOutputStream(bluetoothSocket.getOutputStream());
            instream = new ObjectInputStream(bluetoothSocket.getInputStream());
            //data = new dataStorage(appcontext);
            data=dataStorage.getInstance(appcontext);
            stud = data.selectStudent("Student");
            Regmessage = callFlag+","+courseIdentifier+","+stud.getutaId()+","+stud.getfName()+","+stud.getlName()+","+stud.getMobileNumber();
            outstream.writeObject(Regmessage);
      //      instream.close();
            outstream.flush();
            String response=(String)instream.readObject();
            if(response.equals("N")|response.equals("")|response.isEmpty())
            {
                Toast.makeText(appcontext, "Registration Unsuccessful!!Please Retry", Toast.LENGTH_LONG).show();
            }
            else if(response.equals("Y")){
                Toast.makeText(appcontext, "Registration Successful!!!", Toast.LENGTH_LONG).show();
            }
            else if(response.equals("F")){
                Toast.makeText(appcontext, "Sorry,This Course is Not Available in Faculty Mobile!!!", Toast.LENGTH_LONG).show();
            }
            /*instream.close();
            outstream.close();
            bluetoothSocket.close();*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String writeAttendancedata() {
        String response="Y";
        try {
            outstream = new ObjectOutputStream(bluetoothSocket.getOutputStream());
            instream = new ObjectInputStream(bluetoothSocket.getInputStream());
            data=dataStorage.getInstance(appcontext);
            stud = data.selectStudent("Student");
            Regmessage = callFlag+","+courseIdentifier+","+stud.getutaId();
            outstream.writeObject(Regmessage);
            outstream.flush();
           // response=(String)instream.readObject();
            /*if(response.equals("N")|response.equals("")|response.isEmpty())
            {
                Toast.makeText(appcontext, "Marking Unsuccessful!!Please Retry", Toast.LENGTH_LONG).show();
            }*/
            if(response.equals("Y")){
                Toast.makeText(appcontext, "Marking Successful!!!", Toast.LENGTH_LONG).show();
            }
           /* instream.close();
            outstream.close();
            bluetoothSocket.close();*/
            if(response.equals("")|response.isEmpty()){response="N";}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
