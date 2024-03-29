package com.example.omr.SETTER_G;

public class USER {
    private String Email;
    private String Password;
    private String Rollno;

    public USER() {
    }
    public USER(String email){
        Email=email;
    }
    public USER(String email,String password,String Rollno){
        Email=email;
        Password=password;
        this.Rollno=Rollno;
    }

    public USER(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }
}

