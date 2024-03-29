package com.example.omr.SETTER_G;

public class TEACHER extends USER{
    private String Name;
    private String dob;
    private String edu_department;
    private String Gender;
    private String image;

    public TEACHER() {
    }
    public TEACHER(String Name){
        this.Name=Name;
    }

    public TEACHER(String name, String dob, String edu_department, String gender, String image,String email) {
        super(email);
        Name = name;
        this.dob = dob;
        this.edu_department = edu_department;
        Gender = gender;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDOB() {
        return dob;
    }

    public void setDOB(String DOB) {
        this.dob = dob;
    }

    public String getEdu_department() {
        return edu_department;
    }

    public void setEdu_department(String edu_department) {
        this.edu_department = edu_department;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
