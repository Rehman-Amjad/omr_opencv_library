package com.example.omr.SETTER_G;

public class STUDENT_RECORD {
    private String Name;
    private String Rollno;
    private String Classname;
    private String Dategenerated;
    private String qrref;
    private String qrgenerated_user;

    public STUDENT_RECORD() {
    }

//    public STUDENT_RECORD(String name, String rollno, String classname, String dategenerated, String qrref) {
//        Name = name;
//        Rollno = rollno;
//        Classname = classname;
//        Dategenerated = dategenerated;
//        this.qrref=qrref;
//    }

    public String getQrref() {
        return qrref;
    }

    public void setQrref(String qrref) {
        this.qrref = qrref;
    }

    public STUDENT_RECORD(String rollno, String classname, String dategenerated, String qrref,String qrgenerated_user) {
        Rollno = rollno;
        Classname = classname;
        Dategenerated = dategenerated;
        this.qrref=qrref;
        this.qrgenerated_user=qrgenerated_user;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public String getClassname() {
        return Classname;
    }

    public void setClassname(String classname) {
        Classname = classname;
    }

    public String getDategenerated() {
        return Dategenerated;
    }

    public void setDategenerated(String dategenerated) {
        Dategenerated = dategenerated;
    }

    public String getQrgenerated_user() {
        return qrgenerated_user;
    }

    public void setQrgenerated_user(String qrgenerated_user) {
        this.qrgenerated_user = qrgenerated_user;
    }
}
