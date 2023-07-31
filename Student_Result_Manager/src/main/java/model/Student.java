package model;

import io.vertx.core.json.JsonObject;


public class Student {

    private int studentID;
    private String name;
    private int age;

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public int getAge() { return age; }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }

    // Add constructor to create Student from JsonObject
    public Student(JsonObject jsonObject) {
        this.studentID = jsonObject.getInteger("studentID");
        this.name = jsonObject.getString("name");
        this.age = jsonObject.getInteger("age");
    }

    /* This allows other parts of the code to access and use the student data in JSON format.
    For example, if you have a Student object, and you call the toJson method on that object, it will
    convert the student's data into a JSON object, making it easy to send, store, or manipulate the
    student data in a JSON-compatible format.*/
}
