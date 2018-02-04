package mobileattendancecom.mobileattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 7/4/16.
 */
public class attendanceList extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spCourseids;
    CalendarView calendarView;
    ArrayAdapter<String> adapter;
    dataStorage dataAccess;
    courseDetails crs;
    ArrayList<String> dateValue;
    String curnTerm;
    static String selCourseid,selDate;
    DateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataAccess = dataStorage.getInstance(this);
        crs = new courseDetails();
        df = new SimpleDateFormat("MM/dd/yyyy");
        String now = df.format(new Date());
        selDate=now;
        dateValue = new ArrayList<String>();
        for (String retval: now.split("/")) {
            dateValue.add(retval);
        }
        String mdscat = dateValue.get(0)+dateValue.get(1);
        int mdTerm = Integer.parseInt(mdscat);
        curnTerm = crs.getTermvalue(mdTerm);
        ArrayList<String> fetchCourses = dataAccess.selectAllcrs(curnTerm, Integer.parseInt(dateValue.get(2)));
        setContentView(R.layout.activity_attendance_list);
        spCourseids = (Spinner)findViewById(R.id.spinner1);
        calendarView = (CalendarView)findViewById(R.id.calendarView1);
        selCourseid=fetchCourses.get(0);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fetchCourses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourseids.setAdapter(adapter);
        spCourseids.setOnItemSelectedListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month=month+1;
                String d="",m="";
                if(dayOfMonth<10){
                     d = "0"+String.valueOf(dayOfMonth);}
                if(month<10){
                     m = "0"+String.valueOf(month);}
                selDate = m+"/"+d+"/"+String.valueOf(year);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        selCourseid=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void getAttendancelist(View view)
    {
        Intent registerIntent = new Intent(getApplicationContext(), attendedStudents.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(registerIntent);
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
