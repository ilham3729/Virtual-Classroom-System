package virtualclassroom;

public class Instructor extends User {
    public Instructor(String username, String password) {
        super(username, password);
    }

    public void createCourse(Course course) {
        VirtualClassroom.addCourse(course);
    }
}
