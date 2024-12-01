package virtualclassroom;

import java.io.File;

public class Assignment {
    private Student student;
    private File file;
    private Integer grade; // Grade in percentage (null if not graded)

    public Assignment(Student student, File file) {
        this.student = student;
        this.file = file;
        this.grade = null; // Initially ungraded
    }

    public Student getStudent() {
        return student;
    }

    public File getFile() {
        return file;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
