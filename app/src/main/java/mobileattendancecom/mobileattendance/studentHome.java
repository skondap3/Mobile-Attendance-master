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
public class studentHome extends AppCompatActivity{

    ImageButton courseButton;
    ImageButton registerButton;
    ImageButton markButton;
    ImageButton updateButton;
    ImageButton attendlstButton;
    String lcourse="";
    String crTerm;
    ArrayList<String> ccurDate;
    dataStorage listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
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
        courseButton=(ImageButton)findViewById(R.id.imageButton);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent courseIntent = new Intent(getApplicationContext(), studentCourse.class);
                courseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                courseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                courseIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(courseIntent);
            }
        });
        registerButton=(ImageButton)findViewById(R.id.imageButton3);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), registerAttendance.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(registerIntent);
            }
        });
        markButton=(ImageButton)findViewById(R.id.imageButton4);
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent markIntent = new Intent(getApplicationContext(), markAttendance.class);
                markIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                markIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                markIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(markIntent);
            }
        });
        updateButton=(ImageButton)findViewById(R.id.imageButton2);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courDialog(studentHome.this, "Select the Course", new String[]{"Done"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(lcourse.equals("")){
                                    Toast.makeText(getApplicationContext(), "Please Select a CourseId to Continue", Toast.LENGTH_LONG).show();
                                }
                                else{
                                Intent crlistIntent = new Intent(getApplicationContext(), updateCourse.class);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                crlistIntent.putExtra("courseid", lcourse);
                                startActivity(crlistIntent);}
                            }
                        });
            }
        });
        attendlstButton=(ImageButton)findViewById(R.id.imageButton5);
        attendlstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courDialog(studentHome.this, "Select the Course", new String[]{"Done"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent crlistIntent = new Intent(getApplicationContext(), studentAttendvw.class);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                crlistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                crlistIntent.putExtra("courseid", lcourse);
                                startActivity(crlistIntent);
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
}
