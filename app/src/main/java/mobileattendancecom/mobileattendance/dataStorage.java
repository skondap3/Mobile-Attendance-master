package mobileattendancecom.mobileattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by riju on 6/21/16.
 */
public class dataStorage extends SQLiteOpenHelper {

    private static dataStorage sInstance;
    public SQLiteDatabase DB;
    private static String DBName = "MobileAttendance";
    private static final int version = '1';
    private static Context currentContext;
    private static String userTable = "UserDetails";
    private static String courseTable = "CourseDetails";
    private static String attendanceTable = "AttendanceDet";
    public static boolean insertStatus = false;
    public static boolean updateStatus = false;
    public static boolean selectStatus = false;

   /* public dataStorage(Context context) {
        super(context, DBName, null, version);
        currentContext = context;
    }*/

    public static synchronized dataStorage getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new dataStorage(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private dataStorage(Context context) {
        super(context, DBName, null, version);
        currentContext = context;
    }

    public boolean insertCourse(courseDetails course) {
        insertStatus = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                courseTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,Term VARCHAR,Year INT,CourseId VARCHAR,ProfName VARCHAR,Status VARCHAR,StartTime VARCHAR, EndTime VARCHAR,Identifier VARCHAR,Duration INT,Stime INT,Etime INT, Crsflag VARCHAR);");
        DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Term", course.getTerm());
        values.put("Year", course.getYear());
        values.put("CourseId", course.getCourseid());
        values.put("ProfName", course.getProfname());
        values.put("Status",course.getStatus());
        values.put("StartTime", course.getStarttime());
        values.put("EndTime", course.getEndtime());
        values.put("Identifier", course.getId());
        values.put("Duration", course.getDuration());
        values.put("Stime", course.getStime());
        values.put("Etime", course.getEtime());
        values.put("Crsflag", "N");
        DB.insert(courseTable, null, values);
        insertStatus = true;
        return insertStatus;
    }

    public boolean insertUser(userDetails user) {
        insertStatus = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                userTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,UTAId VARCHAR,FirstName VARCHAR,LastName VARCHAR,MobileNumber VARCHAR,UserType VARCHAR,CourseLink INTEGER);");
        DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UTAId", user.getutaId());
        values.put("FirstName", user.getfName());
        values.put("LastName", user.getlName());
        values.put("MobileNumber", user.getMobileNumber());
        values.put("UserType", user.getUserType());
        values.put("CourseLink", user.getcourseLink());
        DB.insert(userTable, null, values);
        insertStatus = true;
        return insertStatus;
    }

    public boolean selectAppUser(String userType) {
        int userCount = 0;
        selectStatus = false;
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT count(1) FROM " + userTable + " where UserType = '" + userType + "'", null);
            if (c != null) {
                userCount = c.getCount();
            }
        }
        if (userCount == 1)
            selectStatus = true;
        return selectStatus;
    }

    public ArrayList selectCourses(String termval, int yearval, int ctime) {
        ArrayList<String> courseList = new ArrayList<String>();
        try {
            DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                    courseTable +
                    " (Key INTEGER PRIMARY KEY AUTOINCREMENT,Term VARCHAR,Year INT,CourseId VARCHAR,ProfName VARCHAR,Status VARCHAR,StartTime VARCHAR, EndTime VARCHAR,Identifier VARCHAR,Duration INT,Stime INT,Etime INT);");
            File dbFile = currentContext.getDatabasePath(DBName);
            if (dbFile.exists()) {
                DB = this.getWritableDatabase();
                Cursor c = DB.rawQuery("SELECT CourseId FROM " + courseTable + " where Term = '" + termval + "' AND Year =" + yearval + " AND Stime <=" + ctime + " AND Etime >=" + ctime, null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            courseList.add(c.getString(0));
                        } while (c.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public userDetails selectStudent(String userType) {
        userDetails student = new userDetails();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT UTAId,FirstName,LastName,MobileNumber FROM " + userTable + " where UserType = '" + userType + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        student.setutaId(c.getString(c.getColumnIndex("UTAId")));
                        student.setfName(c.getString(c.getColumnIndex("FirstName")));
                        student.setlName(c.getString(c.getColumnIndex("LastName")));
                        student.setMobileNumber(c.getString(c.getColumnIndex("MobileNumber")));
                    } while (c.moveToNext());
                }
            }
        }
        return student;
    }

    public Integer selectCourselink(courseDetails crs) {
        Integer crsId = -1;
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                courseTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,Term VARCHAR,Year INT,CourseId VARCHAR,ProfName VARCHAR,Status VARCHAR,StartTime VARCHAR, EndTime VARCHAR,Identifier VARCHAR,Duration INT,Stime INT,Etime INT);");
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT Key FROM " + courseTable + " where Term = '" + crs.getTerm() + "' AND Year =" + crs.getYear() + " AND CourseId = '" + crs.getCourseid() + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    crsId = c.getInt(c.getColumnIndex("Key"));
                }
            }
        }
        return crsId;
    }

    public boolean checkStudent(Integer courseKey, String UTid) {
        boolean chkStudent = false;
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT UTAId FROM " + userTable + " where CourseLink =" + courseKey + " AND UTAId='" + UTid + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    chkStudent = true;
                }
            }
        }
        return chkStudent;
    }

    public void registStudent(userDetails studUser) {
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("UTAId", studUser.getutaId());
            values.put("FirstName", studUser.getfName());
            values.put("LastName", studUser.getlName());
            values.put("MobileNumber", studUser.getMobileNumber());
            values.put("UserType", studUser.getUserType());
            values.put("CourseLink", studUser.getcourseLink());
            DB.insert(userTable, null, values);
        }
    }

    public ArrayList<String> joinedStudents(Integer cKey) {
        ArrayList<String> jStuds = new ArrayList<String>();
        userDetails joinStudent = new userDetails();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT UTAId,FirstName,LastName,MobileNumber FROM " + userTable + " where CourseLink = " + cKey, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        joinStudent.setutaId(c.getString(c.getColumnIndex("UTAId")));
                        joinStudent.setfName(c.getString(c.getColumnIndex("FirstName")));
                        joinStudent.setlName(c.getString(c.getColumnIndex("LastName")));
                        joinStudent.setMobileNumber(c.getString(c.getColumnIndex("MobileNumber")));
                        jStuds.add(joinStudent.getutaId() + ":" + joinStudent.getfName() + ":" + joinStudent.getMobileNumber());
                    } while (c.moveToNext());
                }
            }
        }
        return jStuds;
    }

    public ArrayList selectAllcrs(String termval, int yearval) {
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                courseTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,Term VARCHAR,Year INT,CourseId VARCHAR,ProfName VARCHAR,Status VARCHAR,StartTime VARCHAR, EndTime VARCHAR,Identifier VARCHAR,Duration INT,Stime INT,Etime INT);");
        File dbFile = currentContext.getDatabasePath(DBName);
        ArrayList<String> courseList = new ArrayList<String>();
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT CourseId FROM " + courseTable + " where Term = '" + termval + "' AND Year =" + yearval, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        courseList.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
        }
        return courseList;
    }

    public boolean insertAttendance(String studentId, String courseId) {
        insertStatus = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                attendanceTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,AttendDate DATE,StudentId VARCHAR,CourseId VARCHAR);");
        DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("AttendDate", df.format(now));
        values.put("StudentId", studentId);
        values.put("CourseId", courseId);
        DB.insert(attendanceTable, null, values);
        insertStatus = true;
        return insertStatus;
    }

    public boolean checkAttendance(String studentId, String courseId) {
        boolean chkAttendance = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                attendanceTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,AttendDate DATE,StudentId VARCHAR,CourseId VARCHAR);");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT StudentId,AttendDate FROM " + attendanceTable + " where AttendDate ='" + df.format(now) + "' AND StudentId ='" + studentId + "'" + " AND CourseId ='" + courseId + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    chkAttendance = true;
                }
            }
        }
        return chkAttendance;
    }

    public courseDetails selectCorses(String crsId) {
        courseDetails crses = new courseDetails();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT Term,Year,CourseId,ProfName,Status,StartTime,EndTime,Duration,Stime,Etime FROM " + courseTable + " where CourseId = '" + crsId + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        crses.setTerm(c.getString(c.getColumnIndex("Term")));
                        crses.setYear(c.getInt(c.getColumnIndex("Year")));
                        crses.setCourseid(c.getString(c.getColumnIndex("CourseId")));
                        crses.setProfname(c.getString(c.getColumnIndex("ProfName")));
                        crses.setStatus(c.getString(c.getColumnIndex("Status")));
                        crses.setStarttime(c.getString(c.getColumnIndex("StartTime")));
                        crses.setEndtime(c.getString(c.getColumnIndex("EndTime")));
                        crses.setDuration(c.getInt(c.getColumnIndex("Duration")));
                        crses.setStime(c.getInt(c.getColumnIndex("Stime")));
                        crses.setEtime(c.getInt(c.getColumnIndex("Etime")));
                    } while (c.moveToNext());
                }
            }
        }
        return crses;
    }

    public ArrayList<String> markedAttend(String sCid, String sDt) {
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                attendanceTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,AttendDate DATE,StudentId VARCHAR,CourseId VARCHAR);");
        ArrayList<String> mStuds = new ArrayList<String>();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT StudentId FROM " + attendanceTable + " where AttendDate ='" + sDt + "' AND CourseId ='" + sCid + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Cursor c1 = DB.rawQuery("SELECT FirstName FROM " + userTable + " where UTAId ='" + c.getString(c.getColumnIndex("StudentId")) + "'", null);
                        if (c1 != null) {
                            if (c1.moveToFirst()) {
                                mStuds.add(c.getString(c.getColumnIndex("StudentId")) + ":" + c1.getString(c1.getColumnIndex("FirstName")));
                            }
                        }
                    } while (c.moveToNext());
                }
            }
        }
        return mStuds;
    }

    public ArrayList<String> markedAttendstvw(String sCid) {
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                attendanceTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,AttendDate DATE,StudentId VARCHAR,CourseId VARCHAR);");
        ArrayList<String> msvwStuds = new ArrayList<String>();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT AttendDate,StudentId FROM " + attendanceTable + " where CourseId ='" + sCid + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        msvwStuds.add(c.getString(c.getColumnIndex("AttendDate")) + ":" + c.getString(c.getColumnIndex("StudentId")));
                    } while (c.moveToNext());
                }
            }
        }
        return msvwStuds;
    }

    public boolean updateCourse(courseDetails course,String chgCourseid) {
        updateStatus = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Term", course.getTerm());
        values.put("Year", course.getYear());
        values.put("CourseId", course.getCourseid());
        values.put("ProfName", course.getProfname());
        values.put("Status", course.getStatus());
        values.put("StartTime", course.getStarttime());
        values.put("EndTime", course.getEndtime());
        values.put("Identifier", course.getId());
        values.put("Duration", course.getDuration());
        values.put("Stime", course.getStime());
        values.put("Etime", course.getEtime());
        DB.update(courseTable, values, "CourseId='" + chgCourseid + "'", null);
        updateStatus = true;
        return updateStatus;
    }

    public void changeCoursestatus(String ctterm,int ctyear,String ctcourseid) {
       // boolean changeStatus = false;
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Crsflag", "Y");
        DB.update(courseTable, values, "CourseId='" + ctcourseid + "' AND Term='" +ctterm+"'AND Year="+ctyear, null);
      //  changeStatus = true;
     //   return changeStatus;
    }

    public boolean fetchCoursestatus(String ctterm,int ctyear,String ctcourseid) {
        boolean changeStatus = false;
        String crsflg="N";
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT Crsflag FROM " + courseTable + " where Term = '" + ctterm + "' AND Year =" + ctyear + " AND CourseId = '" + ctcourseid + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    crsflg = c.getString(c.getColumnIndex("Crsflag"));
                }
            }
        }
        if(crsflg.equals("Y")){
            changeStatus=true;
        }
        return changeStatus;
    }

    public ArrayList<String> saveAttend(String selcid) {
        DB = currentContext.openOrCreateDatabase(DBName, 0, null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                attendanceTable +
                " (Key INTEGER PRIMARY KEY AUTOINCREMENT,AttendDate DATE,StudentId VARCHAR,CourseId VARCHAR);");
        ArrayList<String> saveAtts = new ArrayList<String>();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT * FROM " + attendanceTable + " where CourseId = '" + selcid +"'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        saveAtts.add(c.getString(c.getColumnIndex("AttendDate")) + ":" + c.getString(c.getColumnIndex("StudentId")) + ":" + c.getString(c.getColumnIndex("CourseId")));
                    } while (c.moveToNext());
                }
            }
        }
        return saveAtts;
    }

    public ArrayList<String> saveStud(int clin) {
        ArrayList<String> svStuds = new ArrayList<String>();
        userDetails joinStudent = new userDetails();
        File dbFile = currentContext.getDatabasePath(DBName);
        if (dbFile.exists()) {
            DB = this.getWritableDatabase();
            Cursor c = DB.rawQuery("SELECT UTAId,FirstName,LastName,MobileNumber FROM " + userTable + " where CourseLink = " + clin, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        joinStudent.setutaId(c.getString(c.getColumnIndex("UTAId")));
                        joinStudent.setfName(c.getString(c.getColumnIndex("FirstName")));
                        joinStudent.setlName(c.getString(c.getColumnIndex("LastName")));
                        joinStudent.setMobileNumber(c.getString(c.getColumnIndex("MobileNumber")));
                        svStuds.add(joinStudent.getutaId() + ":" + joinStudent.getfName() + ":" + joinStudent.getlName() + ":" + joinStudent.getMobileNumber());
                    } while (c.moveToNext());
                }
            }
        }
        return svStuds;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
