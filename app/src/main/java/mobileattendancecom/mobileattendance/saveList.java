package mobileattendancecom.mobileattendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 7/7/16.
 */
public class saveList extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String selit;
    ArrayList<String> selwriteData;
    dataStorage svData;
    Spinner spCourseids;
    String selCourseid;
    courseDetails crs;
    ArrayAdapter<String> adapter;
    ArrayList<String> dateValue;
    String curnTerm;
    DateFormat df;
    TextView vwSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file);
        spCourseids = (Spinner)findViewById(R.id.spinner10);
        vwSave = (TextView)findViewById(R.id.textView20);
        crs = new courseDetails();
        svData=dataStorage.getInstance(this);
        df = new SimpleDateFormat("MM/dd/yyyy");
        String now = df.format(new Date());
        dateValue = new ArrayList<String>();
        for (String retval: now.split("/")) {
            dateValue.add(retval);
        }
        String mdscat = dateValue.get(0)+dateValue.get(1);
        int mdTerm = Integer.parseInt(mdscat);
        curnTerm = crs.getTermvalue(mdTerm);
        ArrayList<String> fetchCourses = svData.selectAllcrs(curnTerm, Integer.parseInt(dateValue.get(2)));
        selCourseid=fetchCourses.get(0);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fetchCourses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourseids.setAdapter(adapter);
        spCourseids.setOnItemSelectedListener(this);
        Intent myIntent = getIntent();
        selit = myIntent.getStringExtra("selecttask");
        crs.setTerm(curnTerm);
        crs.setYear(Integer.parseInt(dateValue.get(2)));
        crs.setCourseid(selCourseid);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        selCourseid=parent.getItemAtPosition(position).toString();
        crs.setCourseid(selCourseid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void saveContent(View view) {
        if (selit.equals("StudentList")) {
            int kid = svData.selectCourselink(crs);
            selwriteData = svData.saveStud(kid);
            stwritefile();
        } else if (selit.equals("AttendanceList")) {
            selwriteData = svData.saveAttend(selCourseid);
            attwritefile();
        }
    }

    public void stwritefile()
    {
        if(!selwriteData.isEmpty()) {
            try {
                Log.d("Path", Environment.getExternalStorageDirectory().getAbsolutePath());
                FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Strecord.txt");
                for (String str : selwriteData) {
                    writer.write(str+"'\n'");
                }
                writer.close();
                vwSave.setText("File Successfully Saved!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void attwritefile()
    {
        if(!selwriteData.isEmpty()) {
            try {
                Log.d("Path", Environment.getExternalStorageDirectory().getAbsolutePath());
                FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Attrecord.txt");
                for (String str : selwriteData) {
                    writer.write(str+"'\n'");
                }
                writer.close();
                vwSave.setText("File Successfully Saved!!");
            } catch (Exception e) {
                e.printStackTrace();
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
