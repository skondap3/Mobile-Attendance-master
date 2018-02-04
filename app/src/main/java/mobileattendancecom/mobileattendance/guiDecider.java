package mobileattendancecom.mobileattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

/**
 * Created by riju on 6/25/16.
 */
public class guiDecider extends AppCompatActivity {

    userDetails users;
    courseDetails courses;
    dataStorage tuples;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "UserTypePref";
    public static final String Type = "typeKey";
    public static int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        showGUI();
    }

    public void showGUI() {
        if (sharedpreferences.contains(Type)) {
            tuples=dataStorage.getInstance(this);
           // tuples = new dataStorage(this);
            String usrType = sharedpreferences.getString(Type, null);
            if (tuples.selectAppUser(usrType)) {
                if (usrType.equals("Student")) {
                    Intent loginIntent = new Intent(getApplicationContext(), studentHome.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(loginIntent);
                } else if (usrType.equals("Faculty")) {
                    Intent loginIntent = new Intent(getApplicationContext(), facultyHome.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(loginIntent);
                }
            } else {
                if (usrType.equals("Student")) {
                    Intent loginIntent = new Intent(getApplicationContext(), studentForm.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(loginIntent);
                } else if (usrType.equals("Faculty")) {
                    Intent loginIntent = new Intent(getApplicationContext(), facultyForm.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(loginIntent);
                }
            }
        } else {
            showDialog(this, "Choose Your Role", new String[]{"Ok"},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (selectedItem == 0) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Type,"Student");
                                editor.commit();
                                Intent loginIntent = new Intent(getApplicationContext(), studentForm.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(loginIntent);
                            } else if (selectedItem == 1) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Type,"Faculty");
                                editor.commit();
                                Intent loginIntent = new Intent(getApplicationContext(), facultyForm.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(loginIntent);
                            }
                        }
                    });
        }

    }

    public void showDialog(Context context, String title, String[] btnText,
                           DialogInterface.OnClickListener listener) {
        final CharSequence[] items = {"Student", "Faculty"};
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
                        selectedItem=item;
                    }
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }
}
