package mobileattendancecom.mobileattendance;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by riju on 6/29/16.
 */
public class facultyCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView vwcourseYear;
    TextView vwcourseId;
    TextView vwcourseStatus;
    TextView vwstartTime;
    TextView vwendTime;
    TextView txtcourseTerm;
    TextView txtcourseYear;
    EditText txtcourseId;
    Spinner spcourseStatus;
    EditText txtDuration;
    EditText txtstartTime;
    EditText txtendTime;
    String textname;
    String colored="*";
    int start,end;
    SpannableStringBuilder builder;
    int selectedYear=0;
    String cStatus="Active";
    courseDetails courses=new courseDetails();;
    dataStorage courseRecord;
    ArrayList<String> curDate;
    String cTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course);
        vwcourseYear = (TextView)findViewById(R.id.courseYear);
        vwcourseId = (TextView)findViewById(R.id.courseId);
        textname = "Course ID : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwcourseId.setText(builder);
        vwcourseStatus = (TextView)findViewById(R.id.courseStatus);
        vwstartTime = (TextView)findViewById(R.id.startTime);
        textname = "Start Time : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwstartTime.setText(builder);
        vwendTime = (TextView)findViewById(R.id.endTime);
        textname = "End Time : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwendTime.setText(builder);
        txtcourseTerm = (TextView)findViewById(R.id.termLov);
        txtcourseYear = (TextView)findViewById(R.id.numberPicker1);
        txtcourseId = (EditText)findViewById(R.id.etcourseId);
        txtDuration = (EditText)findViewById(R.id.etduration);
        txtstartTime = (EditText)findViewById(R.id.etstartTime);
        txtendTime = (EditText)findViewById(R.id.etendTime);
        spcourseStatus = (Spinner)findViewById(R.id.statusLov);
        String[] items = new String[]{"Active", "Closed"};
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcourseStatus.setAdapter(adapter);
        spcourseStatus.setOnItemSelectedListener(this);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String now = df.format(new Date());
        curDate = new ArrayList<String>();
        for (String retval: now.split("/")) {
            curDate.add(retval);
        }
        String mdcat = curDate.get(0)+curDate.get(1);
        int mdTerm = Integer.parseInt(mdcat);
        cTerm = courses.getTermvalue(mdTerm);
        txtcourseTerm.setText(cTerm);
        txtcourseYear.setText(curDate.get(2));
        selectedYear=Integer.parseInt(curDate.get(2));
        txtstartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(facultyCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min = Integer.toString(selectedMinute), hr = Integer.toString(selectedHour);
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute;
                        if (selectedHour < 10)
                            hr = "0" + selectedHour;
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
                mTimePicker = new TimePickerDialog(facultyCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min = Integer.toString(selectedMinute), hr = Integer.toString(selectedHour);
                        if (selectedMinute < 10)
                            min = "0" + selectedMinute;
                        if (selectedHour < 10)
                            hr = "0" + selectedHour;
                        txtendTime.setText(hr + ":" + min + ":" + "00");
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        cStatus=parent.getItemAtPosition(position).toString();
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
            courses.setTerm(cTerm);
            courses.setYear(selectedYear);
            courses.setCourseid(txtcourseId.getText().toString().trim());
            if(!txtDuration.getText().toString().isEmpty()&&!txtDuration.getText().toString().equals("")){
                courses.setDuration(Integer.parseInt(txtDuration.getText().toString().trim()));}
            courses.setStatus(cStatus);
            courses.setStarttime(txtstartTime.getText().toString().trim());
            courses.setEndtime(txtendTime.getText().toString().trim());
            courseRecord = dataStorage.getInstance(this);
            if (courseRecord.insertCourse(courses)) {
                Toast.makeText(getApplicationContext(), "Course Created Successfully!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(getApplicationContext(), facultyHome.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(homeIntent);
    }
}
