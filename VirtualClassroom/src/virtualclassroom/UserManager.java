package virtualclassroom;

import java.util.ArrayList;

public class UserManager {
    private ArrayList<Student> students;
    private ArrayList<Instructor> instructors;

    public UserManager() {
        students = new ArrayList<>();
        instructors = new ArrayList<>();

        // Add predefined users
        instructors.add(new Instructor("teacher1", "password123"));
        students.add(new Student("student1", "password123"));
    }

    // Register a student
    public boolean registerStudent(String username, String password) {
        if (findStudentByUsername(username) == null && findInstructorByUsername(username) == null) {
            students.add(new Student(username, password));
            return true;
        }
        return false; // Username already exists
    }

    // Register an instructor
    public boolean registerInstructor(String username, String password) {
        if (findStudentByUsername(username) == null && findInstructorByUsername(username) == null) {
            instructors.add(new Instructor(username, password));
            return true;
        }
        return false; // Username already exists
    }

    // Login a student
    public Student loginStudent(String username, String password) {
        for (Student student : students) {
            if (student.getUsername().equals(username) && student.checkPassword(password)) {
                return student;
            }
        }
        return null; // Login failed
    }

    // Login an instructor
    public Instructor loginInstructor(String username, String password) {
        for (Instructor instructor : instructors) {
            if (instructor.getUsername().equals(username) && instructor.checkPassword(password)) {
                return instructor;
            }
        }
        return null; // Login failed
    }

    // Helper method to find a student by username
    private Student findStudentByUsername(String username) {
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return student;
            }
        }
        return null;
    }

    // Helper method to find an instructor by username
    private Instructor findInstructorByUsername(String username) {
        for (Instructor instructor : instructors) {
            if (instructor.getUsername().equals(username)) {
                return instructor;
            }
        }
        return null;
    }
}
