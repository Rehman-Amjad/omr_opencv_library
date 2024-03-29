package com.example.omr.SETTER_G;

public class OMR {
    private String Classname;
    private String Rollno;
    private String Subject;
    private String image;
    private String Total_Marks;
    private String Marks_Obtain;
    private String Date;

    public OMR(){}
    public OMR(String Subject){
        this.Subject=Subject;
    }

    public OMR(String classname, String rollno, String subject, String image, String total_Marks, String marks_Obtain, String date) {
        Classname = classname;
        Rollno = rollno;
        Subject = subject;
        this.image = image;
        Total_Marks = total_Marks;
        Marks_Obtain = marks_Obtain;
        Date = date;
    }

    public String getTotal_Marks() {
        return Total_Marks;
    }

    public void setTotal_Marks(String total_Marks) {
        Total_Marks = total_Marks;
    }

    public String getMarks_Obtain() {
        return Marks_Obtain;
    }

    public void setMarks_Obtain(String marks_Obtain) {
        Marks_Obtain = marks_Obtain;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getClassname() {
        return Classname;
    }

    public void setClassname(String classname) {
        Classname = classname;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
