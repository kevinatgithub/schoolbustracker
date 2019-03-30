package dev.kevin.app.schoolbustrackeradmin.models;

public class User {

    private String user_id,name;
    private int school_id;
    private School school;

    public User(String user_id, String name, int school_id, School school) {
        this.user_id = user_id;
        this.name = name;
        this.school_id = school_id;
        this.school = school;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public int getSchool_id() {
        return school_id;
    }

    public School getSchool() {
        return school;
    }
}
