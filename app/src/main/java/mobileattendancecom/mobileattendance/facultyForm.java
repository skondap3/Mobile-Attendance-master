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

/**
 * Created by riju on 6/25/16.
 */
public class facultyForm extends AppCompatActivity {

    TextView vwfacultyId;
    TextView vwffirstName;
    EditText txtfacultyId;
    EditText txtffirstName;
    EditText txtflastName;
    String textname;
    String colored="*";
    int start,end;
    SpannableStringBuilder builder;
    userDetails faculty;
    dataStorage facRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);
        vwfacultyId = (TextView)findViewById(R.id.facultyId);
        textname = "Faculty ID : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwfacultyId.setText(builder);
        vwffirstName = (TextView)findViewById(R.id.ffirstName);
        textname = "First Name : ";
        builder = new SpannableStringBuilder();
        builder.append(textname);
        start = builder.length();
        builder.append(colored);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vwffirstName.setText(builder);
        txtfacultyId = (EditText)findViewById(R.id.etfacultyId);
        txtffirstName = (EditText)findViewById(R.id.etffirstName);
        txtflastName = (EditText)findViewById(R.id.etflastName);
    }

    public void createFaculty(View view) {
        if (txtfacultyId.getText().toString().trim().equals("")) {
            txtfacultyId.setError("Faculty Id is Mandatory!");
        } else if (txtffirstName.getText().toString().trim().equals("")) {
            txtffirstName.setError("First Name is Mandatory!");
        } else {
            faculty = new userDetails();
            faculty.setutaId(txtfacultyId.getText().toString().trim());
            faculty.setfName(txtffirstName.getText().toString().trim());
            faculty.setlName(txtflastName.getText().toString().trim());
            faculty.setUserType("Faculty");
            //facRecord = new dataStorage(this);
            facRecord=dataStorage.getInstance(this);
            if (facRecord.insertUser(faculty)) {
                Toast.makeText(getApplicationContext(), "Profile Created Successfully!", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(getApplicationContext(), facultyHome.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(loginIntent);
            }
        }
    }
}
