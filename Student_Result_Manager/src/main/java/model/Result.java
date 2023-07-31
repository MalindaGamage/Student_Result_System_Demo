package model;

import io.vertx.core.json.JsonObject;

public class Result {
    public Result(JsonObject jsonObject) {
        this.studentID = Integer.parseInt(jsonObject.getString("studentId"));
        this.subjectID = Integer.parseInt(jsonObject.getString("subjectId"));
        this.score = Integer.parseInt(jsonObject.getString("score"));
    }
    private int studentID;
    private int subjectID;
    private int score;
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID =studentID; }
    public int getSubjectID() { return subjectID; }
    public void setSubjectID(int subjectID) {this.subjectID =subjectID; }
    public int getScore() {return score; }
    public void setScore(int score) { this.score = score; }

}
