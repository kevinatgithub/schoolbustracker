package app.kevin.dev.schoolservicetrackerparent.models;

public class School {

    private int id;
    private String Name;

    public School(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }
}
