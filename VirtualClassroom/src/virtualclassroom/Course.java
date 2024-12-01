package virtualclassroom;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Course {
    private String title;
    private String description;
    private Instructor instructor;
    private List<Student> enrolledStudents;
    private List<File> uploadedFiles; // List of instructor-uploaded PDFs
    private List<Assignment> assignments; // List of student-submitted assignments

    public Course(String title, String description, Instructor instructor) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.enrolledStudents = new ArrayList<>();
        this.uploadedFiles = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    // Enroll a student
    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }

    // Upload a file (Instructor)
    public void uploadFile(File file) {
        uploadedFiles.add(file);
    }

    // Submit an assignment (Student)
    public void submitAssignment(Student student, File file) {
        assignments.add(new Assignment(student, file));
    }

    // Update the course title
    public void setTitle(String title) {
        this.title = title;
    }

    // Update the course description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public List<File> getUploadedFiles() {
        return uploadedFiles;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
