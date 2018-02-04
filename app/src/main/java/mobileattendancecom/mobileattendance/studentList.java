package mobileattendancecom.mobileattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 7/3/16.
 */
public class studentList extends AppCompatActivity {

    TextView vwSterm;
    TextView vwSyear;
    ListView listViewStudents;
    ArrayList<String> arylistStudents;
    ArrayAdapter<String> studAdapter;
    dataStorage listData;
    String lcourse;
    String crTerm;
    ArrayList<String> ccurDate;
    courseDetails listCurses;
    Integer uqCrsid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Intent myIntent = getIntent();
        lcourse=myIntent.getStringExtra("courseid");
        vwSterm = (TextView)findViewById(R.id.txtSterm);
        vwSyear = (TextView)findViewById(R.id.txtSyear);
        listViewStudents = (ListView) findViewById(R.id.listViewSt);
        arylistStudents = new ArrayList<String>();
       // studAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arylistStudents);
       // listViewStudents.setAdapter(studAdapter);
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
        vwSterm.setText(crTerm);
        vwSyear.setText(ccurDate.get(2));
        listData=dataStorage.getInstance(this);
        listCurses = new courseDetails();
        listCurses.setCourseid(lcourse);
        listCurses.setTerm(crTerm);
        listCurses.setYear(Integer.parseInt(ccurDate.get(2)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        uqCrsid=listData.selectCourselink(listCurses);
        arylistStudents=listData.joinedStudents(uqCrsid);
        studAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arylistStudents);
        listViewStudents.setAdapter(studAdapter);
        //studAdapter.notifyDataSetChanged();
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
