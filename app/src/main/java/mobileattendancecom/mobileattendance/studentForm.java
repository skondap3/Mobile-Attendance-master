package mobileattendancecom.mobileattendance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class studentForm extends AppCompatActivity {

    TextView vwstudentId;
    TextView vwsfirstName;
    TextView vwmobileNumber;
    EditText txtstudentId;
    EditText txtsfirstName;
    EditText txtslastName;
    EditText txtmobileNumber;
    String textname;
    String colored="*";
    int start,end;
    SpannableStringBuilder builder;
    userDetails student;
    dataStorage studRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);
        vwstudentId = (TextView)findViewById(R.id.studentId);
        textname = "Student ID : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwstudentId.setText(builder);
        vwsfirstName = (TextView)findViewById(R.id.firstName);
        textname = "First Name : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwsfirstName.setText(builder);
        vwmobileNumber = (TextView)findViewById(R.id.mobileNum);
        textname = "Mobile Number : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwmobileNumber.setText(builder);
        txtstudentId = (EditText)findViewById(R.id.etstudentId);
        txtsfirstName = (EditText)findViewById(R.id.etfirstName);
        txtslastName = (EditText)findViewById(R.id.etlastName);
        txtmobileNumber = (EditText)findViewById(R.id.etmobileNum);
    }

    public void createStudent(View view) {
        if (txtstudentId.getText().toString().trim().equals("")) {
            txtstudentId.setError("Student Id is Mandatory!");
        } else if (txtsfirstName.getText().toString().trim().equals("")) {
            txtsfirstName.setError("First Name is Mandatory!");
        } else if (txtmobileNumber.getText().toString().trim().equals("")) {
            txtmobileNumber.setError("Mobile Number is Mandatory!");
        } else {
            student=new userDetails();
            student.setutaId(txtstudentId.getText().toString().trim());
            student.setfName(txtsfirstName.getText().toString().trim());
            student.setlName(txtslastName.getText().toString().trim());
            student.setMobileNumber(txtmobileNumber.getText().toString().trim());
            student.setUserType("Student");
            //studRecord=new dataStorage(this);
            studRecord=dataStorage.getInstance(this);
            if(studRecord.insertUser(student)){
                Toast.makeText(getApplicationContext(), "Profile Created Successfully!", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(getApplicationContext(), studentHome.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(loginIntent);
            }
        }
    }
}
