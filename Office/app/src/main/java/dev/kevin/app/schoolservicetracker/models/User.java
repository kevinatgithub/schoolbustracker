package dev.kevin.app.schoolservicetracker.models;

public class User {

    private String user_id;
    private String name;
    private int school_id;

    public User(String user_id, String name, int school_id) {
        this.user_id = user_id;
        this.name = name;
        this.school_id = school_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }
}
