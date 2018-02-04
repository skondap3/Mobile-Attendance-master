package mobileattendancecom.mobileattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 6/25/16.
 */
public class facultyHome extends AppCompatActivity {

    ImageButton bluetoothButton;
    ImageButton studentlistButton;
    ImageButton crcourseButton;
    ImageButton upcourseButton;
    ImageButton attendlistButton;
    ImageButton savefileButton;
    startBluetooth lDevices;
    String lcourse="";
    String seltask="";
    String crTerm;
    ArrayList<String> ccurDate;
    dataStorage listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        bluetoothButton=(ImageButton)findViewById(R.id.imageButton4);
        crcourseButton=(ImageButton)findViewById(R.id.imageButton);
        upcourseButton=(ImageButton)findViewById(R.id.upimageButton);
        attendlistButton=(ImageButton)findViewById(R.id.imageButton2);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String now = df.format(new Date());
        ccurDate = new ArrayList<String>();
        for (String retval: now.split("/")) {
            ccurDate.add(retval);
        }
        String mdscat = ccurDate.get(0)+ccurDate.get(1);
        int mdTerm = Integer.parseInt(mdscat);
        courseDetails fcrses = new courseDetails();
        crTerm = fcrses.getTermvalue(mdTerm);
        listData=dataStorage.getInstance(this);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Listening for Registration/Attendance!Please Click Student/Attendance List to View Details", Toast.LENGTH_LONG).show();
                lDevices = new startBluetooth(getApplicationContext());
            }
        });
        crcourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), facultyCourse.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(registerIntent);
            }
        });
        attendlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> fetchCourses = listData.selectAllcrs(crTerm, Integer.parseInt(ccurDate.get(2)));
                if(fetchCourses.size() > 0) {
                    Intent attendIntent = new Intent(getApplicationContext(), attendanceList.class);
                    attendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    attendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    attendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(attendIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry! No Courses are Created", Toast.LENGTH_LONG).show();
                }
            }
        });
        studentlistButton=(ImageButton)findViewById(R.id.imageButton3);
        studentlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courDialog(facultyHome.this, "Select the Course", new String[]{"Done"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent stlistIntent = new Intent(getApplicationContext(), studentList.class);
                                stlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                stlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                stlistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                stlistIntent.putExtra("courseid", lcourse);
                                startActivity(stlistIntent);
                            }
                        });
            }
        });

        upcourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courDialog(facultyHome.this, "Select the Course", new String[]{"Done"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(lcourse.equals("")){
                                    Toast.makeText(getApplicationContext(), "Please Select a CourseId to Continue", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Intent crlistIntent = new Intent(getApplicationContext(), updateCourse.class);
                                    crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    crlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    crlistIntent.putExtra("courseid", lcourse);
                                    startActivity(crlistIntent);
                                }
                            }
                        });
            }
        });

        savefileButton=(ImageButton)findViewById(R.id.imageButton5);
        savefileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savefDialog(facultyHome.this, "Select the List", new String[]{"Done"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent sellistIntent = new Intent(getApplicationContext(), saveList.class);
                                sellistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                sellistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                sellistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                sellistIntent.putExtra("selecttask", seltask);
                                startActivity(sellistIntent);
                            }
                        });
            }
        });
    }

    public void courDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {
        ArrayList<String> fetchedCourses = listData.selectAllcrs(crTerm, Integer.parseInt(ccurDate.get(2)));
        if (fetchedCourses.size() > 0) {
            int i = 0;
            final CharSequence[] items = new CharSequence[fetchedCourses.size()];
            for (String g : fetchedCourses) {
                items[i] = g;
                i++;
            }
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
            builder.setSingleChoiceItems(items, -1,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            lcourse = (String) items[item];
                        }
                    });
            builder.setPositiveButton(btnText[0], listener);
            if (btnText.length != 1) {
                builder.setNegativeButton(btnText[1], listener);
            }
            builder.show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Sorry! No Courses are Created", Toast.LENGTH_LONG).show();
        }
    }

    public void savefDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {
        final CharSequence[] items = {"StudentList", "AttendanceList"};
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
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        seltask=(String) items[item];
                    }
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }
}
