package util;

public class Constant {
    // Event bus address
    public static final String STUDENT_SAVE_EVENT_BUS = "student.save" ;
    public static final String STUDENT_RESULT_FETCH_EVENT_BUS = "get.student.result";
    public static final String STUDENT_RESULT_SAVE_EVENT_BUS = "result.save";

    // SQL query
    public static final String INSERT_STUDENT_DATA = "INSERT INTO Student_details (Student_ID, Student_Name, Student_Age) VALUES (?, ?, ?)";
    public static final String INSERT_RESULT_DATA = "INSERT INTO subject_results (Student_ID, Subject_ID, Marks) VALUES (?, ?, ?)";
    //Other
    public static final int SUBJECT_MARKS_THRESHOLD = 50; // Marks threshold for pass/fail

}
