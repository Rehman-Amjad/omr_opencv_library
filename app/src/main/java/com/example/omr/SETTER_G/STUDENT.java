package com.example.omr.SETTER_G;

public class STUDENT extends USER{
    String name;
    String classname;
    String key;

    public STUDENT(String name, String email, String password, String key, String rollno) {
        super(email,password,rollno);
        this.name = name;
        this.key = key;
    }


    public STUDENT() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
