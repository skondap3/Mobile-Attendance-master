package mobileattendancecom.mobileattendance;

import java.util.Date;

/**
 * Created by riju on 6/25/16.
 */
public class courseDetails {
    String term;
    int year;
    String courseid;
    String profname;
    String status;
    String starttime;
    String endtime;
    String id;
    int duration;
    String termvalue;
    int stime,etime;

    public void setTerm(String term)
    {
        this.term=term;
    }
    public String getTerm()
    {
        return term;
    }
    public void setYear(int year)
    {
        this.year=year;
    }
    public int getYear()
    {
        return year;
    }
    public void setCourseid(String courseid)
    {
        this.courseid=courseid;
    }
    public String getCourseid()
    {
        return courseid;
    }
    public void setProfname(String profname)
    {
        this.profname=profname;
    }
    public String getProfname()
    {
        return profname;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStarttime(String starttime)
    {
        this.starttime=starttime;
    }
    public String getStarttime()
    {
        return starttime;
    }
    public void setEndtime(String endtime)
    {
        this.endtime=endtime;
    }
    public String getEndtime()
    {
        return endtime;
    }
    public void setDuration(int duration)
    {
        this.duration=duration;
    }
    public int getDuration()
    {
        return duration;
    }
    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return id;
    }
    public void setStime(int stime)
    {
        this.stime=stime;
    }
    public void setEtime(int etime)
    {
        this.etime=etime;
    }
    public int getStime()
    {
        return stime;
    }
    public int getEtime()
    {
        return etime;
    }

    public String getTermvalue(int termDate)
    {
        if(termDate>=101 && termDate<=430)
            termvalue="Spring";
        if(termDate>=501 && termDate<=815)
            termvalue="Summer";
        if(termDate>=816 && termDate<=1231)
            termvalue="Fall";
     return termvalue;
    }
}
