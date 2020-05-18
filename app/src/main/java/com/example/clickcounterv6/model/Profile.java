package com.example.clickcounterv6.model;

public class Profile {
    private String strFname;
    private String strLname;

    public Profile(String Fname, String Lname) {
        strFname = Fname;
        strLname = Lname;
    }

    public String getStrFname() {
        return strFname;
    }

    public String getStrLname() {
        return strLname;
    }
}
