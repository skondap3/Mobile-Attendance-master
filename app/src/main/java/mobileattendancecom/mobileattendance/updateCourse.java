package mobileattendancecom.mobileattendance;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by riju on 7/4/16.
 */
public class updateCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtcourseId;
    Spinner spcourseStatus;
    EditText txtprofName;
    EditText txtstartTime;
    EditText txtendTime;
    EditText txtDuration;
    TextView txtcourseTerm;
    TextView txtcourseYear;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "UserTypePref";
    public static final String Type = "typeKey";
    String userType;
    String cStatus;
    courseDetails courses;
    dataStorage courseRecord;
    int stimeInt, etimeInt;
    String selCourse;
    String tempst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseRecord = dataStorage.getInstance(this);
        Intent myIntent = getIntent();
        selCourse = myIntent.getStringExtra("courseid");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userType = sharedpreferences.getString(Type, null);
        if (userType.equals("Student")) {
            setContentView(R.layout.activity_student_course);
        } else if (userType.equals("Faculty")) {
            setContentView(R.layout.activity_faculty_course);
        }
        txtcourseYear = (TextView) findViewById(R.id.numberPicker1);
        txtcourseTerm = (TextView) findViewById(R.id.termLov);
        txtcourseId = (EditText) findViewById(R.id.etcourseId);
        txtDuration = (EditText) findViewById(R.id.etduration);
        txtstartTime = (EditText) findViewById(R.id.etstartTime);
        txtendTime = (EditText) findViewById(R.id.etendTime);
        spcourseStatus = (Spinner) findViewById(R.id.statusLov);
        txtprofName = (EditText) findViewById(R.id.etprofName);
        courses = courseRecord.selectCorses(selCourse);
        if (courses.getStatus().equals("Active")) {
            tempst = "Closed";
        } else if (courses.getStatus().equals("Closed")) {
            tempst = "Active";
        }
        cStatus=courses.getStatus();
        String[] items = new String[]{courses.getStatus(), tempst};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcourseStatus.setAdapter(adapter);
        spcourseStatus.setOnItemSelectedListener(this);
        txtstartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(updateCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min = Integer.toString(selectedMinute), hr = Integer.toString(selectedHour);
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute;
                        if (selectedHour < 10)
                            hr = "0" + selectedHour;
                        String temptime = hr + min;
                        stimeInt = Integer.parseInt(temptime);
                        txtstartTime.setText(hr + ":" + min + ":" + "00");
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        txtendTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(updateCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min = Integer.toString(selectedMinute), hr = Integer.toString(selectedHour);
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute;
                        if (selectedHour < 10)
                            hr = "0" + selectedHour;
                        String temptime = hr + min;
                        etimeInt = Integer.parseInt(temptime);
                        txtendTime.setText(hr + ":" + min + ":" + "00");
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        txtcourseTerm.setText(courses.getTerm());
        txtcourseYear.setText(Integer.toString(courses.getYear()));
        txtcourseId.setText(courses.getCourseid());
        txtstartTime.setText(courses.getStarttime());
        txtendTime.setText(courses.getEndtime());
        if (userType.equals("Student")) {
            txtprofName.setText(courses.getProfname());
            stimeInt=courses.getStime();
            etimeInt=courses.getEtime();
        } else if (userType.equals("Faculty")) {
            txtDuration.setText(Integer.toString(courses.getDuration()));
        }
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        cStatus = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void createCourse(View view) {
        if (txtcourseId.getText().toString().trim().equals("")) {
            txtcourseId.setError("Course Id is Mandatory!");
        } else if (txtstartTime.getText().toString().trim().equals("")) {
            txtstartTime.setError("Start Time is Mandatory!");
        } else if (txtendTime.getText().toString().trim().equals("")) {
            txtendTime.setError("End Time is Mandatory!");
        } else {
            courses.setTerm(courses.getTerm());
            courses.setYear(courses.getYear());
            courses.setCourseid(txtcourseId.getText().toString());
            if (userType.equals("Faculty")) {
                if (!txtDuration.getText().toString().isEmpty() && !txtDuration.getText().toString().equals("")) {
                    courses.setDuration(Integer.parseInt(txtDuration.getText().toString()));
                }
            }
            if (userType.equals("Student")) {
                courses.setProfname(txtprofName.getText().toString());
                courses.setStime(stimeInt);
                courses.setEtime(etimeInt);
            }
            courses.setStatus(cStatus);
            courses.setStarttime(txtstartTime.getText().toString());
            courses.setEndtime(txtendTime.getText().toString());
            courseRecord = dataStorage.getInstance(this);
            if (courseRecord.updateCourse(courses,selCourse)) {
                Toast.makeText(getApplicationContext(), "Course Updated Successfully!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (userType.equals("Faculty")) {
            Intent homeIntent = new Intent(getApplicationContext(), facultyHome.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(homeIntent);
        } else if (userType.equals("Student")) {
            Intent homeIntent = new Intent(getApplicationContext(), studentHome.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(homeIntent);
        }
    }
}
