package mobileattendancecom.mobileattendance;

/**
 * Created by riju on 6/25/16.
 */
public class userDetails {

    String utaId;
    String fName;
    String lName;
    String mobileNumber;
    String userType;
    Integer courseLink;

    public void setutaId(String utaId)
    {
        this.utaId=utaId;
    }
    public String getutaId()
    {
        return utaId;
    }
    public void setfName(String fName)
    {
        this.fName=fName;
    }
    public String getfName()
    {
        return fName;
    }
    public void setlName(String lName)
    {
        this.lName=lName;
    }
    public String getlName()
    {
        return lName;
    }
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber=mobileNumber;
    }
    public String getMobileNumber()
    {
        return mobileNumber;
    }
    public void setUserType(String userType)
    {
        this.userType=userType;
    }
    public String getUserType()
    {
        return userType;
    }
    public void setCourseLink(Integer courseLink)
    {
        this.courseLink=courseLink;
    }
    public Integer getcourseLink()
    {
        return courseLink;
    }
}
