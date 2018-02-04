package mobileattendancecom.mobileattendance;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 7/4/16.
 */
public class listenDevices extends Thread {

    BluetoothSocket bluetoothSocket;
    BluetoothDevice device;
    Context app;
    ObjectInputStream inlistenstream;
    ObjectOutputStream outlistenstream;
    ArrayList<String> recvMessage;
    dataStorage manageDatas;
    courseDetails cours;
    String curTerm;
    ArrayList<String> curDate;
    Integer crsLink=0;

    listenDevices(Context app,BluetoothSocket bluetoothSocket)
    {
        this.app=app;
        this.bluetoothSocket=bluetoothSocket;
        start();
    }

    public void run() {
        byte[] buffer = new byte[1024];
        recvMessage = new ArrayList<String>();
        String result = "";
        if (bluetoothSocket != null) {
            device = bluetoothSocket.getRemoteDevice();
            try {
                inlistenstream = new ObjectInputStream(bluetoothSocket.getInputStream());
                outlistenstream = new ObjectOutputStream(bluetoothSocket.getOutputStream());
                result=(String)inlistenstream.readObject();
                if(result.equals("")|result.isEmpty())
                {
                    outlistenstream.writeObject("N");
                }
                else{
                    for (String retval: result.split(",")) {
                        recvMessage.add(retval.trim());
                    }
                    manageDatas = dataStorage.getInstance(app);
                    if(recvMessage.get(0).equals("R")) {
                        cours = new courseDetails();
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String now = df.format(new Date());
                        curDate = new ArrayList<String>();
                        for (String retval : now.split("/")) {
                            curDate.add(retval);
                        }
                        String mdcat = curDate.get(0) + curDate.get(1);
                        int mdTerm = Integer.parseInt(mdcat);
                        curTerm = cours.getTermvalue(mdTerm);
                        cours.setTerm(curTerm);
                        cours.setYear(Integer.parseInt(curDate.get(2)));
                        cours.setCourseid(recvMessage.get(1).trim());
                        crsLink=manageDatas.selectCourselink(cours);
                        if(crsLink==-1)
                        {
                            outlistenstream.writeObject("F");
                        }
                        else{
                            outlistenstream.writeObject("Y");
                        }
                    }
                }
            //    outlistenstream = bluetoothSocket.getOutputStream();
            //    int byteread = inlistenstream.read(buffer);
            //    if (byteread != -1) {
                /*    while ((byteread == buffer.length) && (buffer[buffer.length - 1] != 0)) {
                        result = result + new String(buffer, 0, byteread);
                        byteread = inlistenstream.read(buffer);
                    }*/
              //      result = result + new String(buffer, 0, byteread);
              //      startBluetooth.devicecount=startBluetooth.devicecount-1;
             //   }
             outlistenstream.flush();
             outlistenstream.close();
             inlistenstream.close();
             bluetoothSocket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            /*for (String retval: result.split(",")) {
                recvMessage.add(retval);
            }*/
        }
        manageConnection();
    }

    public void manageConnection()
    {
        if(!recvMessage.isEmpty()) {
            if (recvMessage.get(0).equals("R") & crsLink != 0) {
                if (!manageDatas.checkStudent(crsLink, recvMessage.get(2))) {
                    userDetails stUsr = new userDetails();
                    stUsr.setutaId(recvMessage.get(2).trim());
                    stUsr.setfName(recvMessage.get(3).trim());
                    stUsr.setlName(recvMessage.get(4).trim());
                    stUsr.setMobileNumber(recvMessage.get(5).trim());
                    stUsr.setUserType("Student");
                    stUsr.setCourseLink(crsLink);
                    manageDatas.registStudent(stUsr);
                }
            } else if (recvMessage.get(0).equals("M")) {
            //    Log.d("ReceiveMessage",recvMessage.size()+"");
                try {
                    if (!manageDatas.checkAttendance(recvMessage.get(2), recvMessage.get(1))) {
                        manageDatas.insertAttendance(recvMessage.get(2), recvMessage.get(1));
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        }
    }
}
