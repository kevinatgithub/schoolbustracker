package app.kevin.dev.schoolservicetrackerparent.models;

public class Student {
    private String name;
    private int age;
    private Vehicle schoolService;

    public Student(String name, int age, Vehicle schoolService) {
        this.name = name;
        this.age = age;
        this.schoolService = schoolService;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Vehicle getSchoolService() {
        return schoolService;
    }
}
