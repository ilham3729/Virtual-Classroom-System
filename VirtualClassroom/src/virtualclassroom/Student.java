package virtualclassroom;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String name;
    private String email;
    private List<Course> enrolledCourses;

    public Student(String username, String password) {
        super(username, password);
        this.enrolledCourses = new ArrayList<>();
    }

    public void enrollInCourse(Course course) {
        enrolledCourses.add(course);
        course.enrollStudent(this);
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
