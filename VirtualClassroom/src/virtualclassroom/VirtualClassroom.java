package virtualclassroom;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VirtualClassroom {

    public static List<Course> courses = new ArrayList<>();

    public static void addCourse(Course course) {
        courses.add(course);
    }

    public static List<Course> getCourses() {
        return courses;
    }

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserManager userManager;
    private Student loggedInStudent;
    private Instructor loggedInInstructor;

    public VirtualClassroom() {
        // Applying Nimbus Look and Feel with Dark Theme
        setDarkNimbusLookAndFeel();

        userManager = new UserManager();

        frame = new JFrame("Virtual Classroom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add all panels to the main panel
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createStudentPanel(), "Student");
        mainPanel.add(createInstructorPanel(), "Instructor");
        mainPanel.add(createRegistrationPanel(), "Register");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void setDarkNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("control", new Color(40, 40, 40));
            UIManager.put("info", new Color(70, 70, 70));
            UIManager.put("nimbusBase", new Color(50, 50, 50));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(40, 40, 40));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(90, 90, 90));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(new Color(230, 230, 230));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(230, 230, 230));
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(70, 70, 70));
        textField.setForeground(new Color(230, 230, 230));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(70, 70, 70));
        passwordField.setForeground(new Color(230, 230, 230));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90)));
        return passwordField;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(4, 2));

        JLabel userLabel = createStyledLabel("Username:");
        JTextField userField = createStyledTextField();
        JLabel passLabel = createStyledLabel("Password:");
        JPasswordField passField = createStyledPasswordField();

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");
        JLabel statusLabel = createStyledLabel("");

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            loggedInInstructor = userManager.loginInstructor(username, password);
            loggedInStudent = userManager.loginStudent(username, password);

            if (loggedInInstructor != null) {
                statusLabel.setText("Instructor login successful!");
                refreshInstructorPanel();
                cardLayout.show(mainPanel, "Instructor");
            } else if (loggedInStudent != null) {
                statusLabel.setText("Student login successful!");
                refreshStudentPanel();
                cardLayout.show(mainPanel, "Student");
            } else {
                statusLabel.setText("Invalid credentials.");
            }

            userField.setText("");
            passField.setText("");
        });

        registerButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Register");
            userField.setText("");
            passField.setText("");
        });

        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(statusLabel);

        return loginPanel;
    }

    private JPanel createStudentPanel() {
        JPanel studentPanel = new JPanel(new BorderLayout());

        JLabel studentLabel = new JLabel("Welcome, Student!");
        DefaultListModel<String> courseListModel = new DefaultListModel<>();
        JList<String> courseList = new JList<>(courseListModel);

        JTextArea courseDescriptionArea = new JTextArea();
        courseDescriptionArea.setEditable(false);

        JButton enrollButton = new JButton("Enroll in Course");
        JButton submitAssignmentButton = new JButton("Submit Assignment");
        JButton logoutButton = new JButton("Logout");

        // Refresh course list dynamically
        Runnable refreshStudentCourses = () -> {
            courseListModel.clear();
            for (Course course : VirtualClassroom.getCourses()) {
                courseListModel.addElement(course.getTitle() + " (Instructor: " + course.getInstructor().getUsername() + ")");
            }
        };
        studentPanel.putClientProperty("refresh", refreshStudentCourses);

        // Initial course list refresh
        refreshStudentCourses.run();

        // View course description on selection
        courseList.addListSelectionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                courseDescriptionArea.setText(VirtualClassroom.getCourses().get(selectedIndex).getDescription());
            } else {
                courseDescriptionArea.setText("");
            }
        });

     // Double-click to view course contents and assignments
        courseList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = courseList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                        Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                        if (loggedInStudent.getEnrolledCourses().contains(selectedCourse)) {
                            displayCourseContentsForStudent(selectedCourse); // View course materials and assignments
                        } else {
                            JOptionPane.showMessageDialog(frame, "You must enroll in the course to view contents.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Enroll Button functionality
        enrollButton.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                if (!loggedInStudent.getEnrolledCourses().contains(selectedCourse)) {
                    loggedInStudent.enrollInCourse(selectedCourse);
                    JOptionPane.showMessageDialog(frame, "Successfully enrolled in " + selectedCourse.getTitle() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "You are already enrolled in this course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No course selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Submit Assignment button logic
        submitAssignmentButton.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                if (loggedInStudent.getEnrolledCourses().contains(selectedCourse)) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        selectedCourse.submitAssignment(loggedInStudent, selectedFile);
                        JOptionPane.showMessageDialog(frame, "Assignment submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "You must enroll in the course to submit an assignment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No course selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logout button logic
        logoutButton.addActionListener(e -> {
            loggedInStudent = null;
            cardLayout.show(mainPanel, "Login");
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(enrollButton, BorderLayout.WEST);
        bottomPanel.add(submitAssignmentButton, BorderLayout.CENTER);
        bottomPanel.add(logoutButton, BorderLayout.EAST);

        studentPanel.add(studentLabel, BorderLayout.NORTH);
        studentPanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        studentPanel.add(new JScrollPane(courseDescriptionArea), BorderLayout.EAST);
        studentPanel.add(bottomPanel, BorderLayout.SOUTH);

        return studentPanel;
    }




    private void refreshStudentPanel() {
        JPanel studentPanel = (JPanel) mainPanel.getComponent(1); // Assuming "Student" is at index 1
        Runnable refresh = (Runnable) studentPanel.getClientProperty("refresh");
        if (refresh != null) {
            refresh.run();
        }
    }

    private JPanel createInstructorPanel() {
        JPanel instructorPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, Instructor!");
        DefaultListModel<String> courseListModel = new DefaultListModel<>();
        JList<String> courseList = new JList<>(courseListModel);

        JButton createCourseButton = new JButton("Create Course");
        JButton editCourseButton = new JButton("Edit Course"); // Edit Course button
        JButton uploadFileButton = new JButton("Upload File");
        JButton viewAssignmentsButton = new JButton("View Assignments");
        JButton logoutButton = new JButton("Logout");

        // Refresh course list dynamically
        Runnable refreshInstructorCourses = () -> {
            courseListModel.clear();
            for (Course course : VirtualClassroom.getCourses()) {
                courseListModel.addElement(course.getTitle() + " (Instructor: " + course.getInstructor().getUsername() + ")");
            }
        };
        instructorPanel.putClientProperty("refresh", refreshInstructorCourses);

        // Initial course list refresh
        refreshInstructorCourses.run();
        
     // Double-click to view course contents
        courseList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = courseList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                        Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                        displayCourseContents(selectedCourse); // View uploaded files
                    }
                }
            }
        });
        
        // Create Course button logic
        createCourseButton.addActionListener(e -> {
            String courseTitle = JOptionPane.showInputDialog(frame, "Enter course title:");
            String courseDescription = JOptionPane.showInputDialog(frame, "Enter course description:");
            if (courseTitle != null && !courseTitle.trim().isEmpty()) {
                Course newCourse = new Course(courseTitle, courseDescription, loggedInInstructor);
                loggedInInstructor.createCourse(newCourse);
                refreshInstructorCourses.run();
                JOptionPane.showMessageDialog(frame, "Course created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Course title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Edit Course button logic
        editCourseButton.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                String newTitle = JOptionPane.showInputDialog(frame, "Edit course title:", selectedCourse.getTitle());
                String newDescription = JOptionPane.showInputDialog(frame, "Edit course description:", selectedCourse.getDescription());

                if (newTitle != null && !newTitle.trim().isEmpty()) {
                    selectedCourse.setTitle(newTitle); // Update title
                    selectedCourse.setDescription(newDescription); // Update description
                    refreshInstructorCourses.run(); // Refresh list
                    JOptionPane.showMessageDialog(frame, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Course title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No course selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Upload File button logic
        uploadFileButton.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().endsWith(".pdf")) {
                        selectedCourse.uploadFile(selectedFile);
                        JOptionPane.showMessageDialog(frame, "File uploaded successfully to " + selectedCourse.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Only PDF files are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No course selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // View Assignments button logic
        viewAssignmentsButton.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < VirtualClassroom.getCourses().size()) {
                Course selectedCourse = VirtualClassroom.getCourses().get(selectedIndex);
                displayAssignments(selectedCourse);
            } else {
                JOptionPane.showMessageDialog(frame, "No course selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logout button logic
        logoutButton.addActionListener(e -> {
            loggedInInstructor = null;
            cardLayout.show(mainPanel, "Login");
        });

        // Panel layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createCourseButton);
        buttonPanel.add(editCourseButton); // Add Edit button
        buttonPanel.add(uploadFileButton);
        buttonPanel.add(viewAssignmentsButton);
        buttonPanel.add(logoutButton);

        instructorPanel.add(welcomeLabel, BorderLayout.NORTH);
        instructorPanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        instructorPanel.add(buttonPanel, BorderLayout.SOUTH);

        return instructorPanel;
    }
    
    private void displayCourseContentsForStudent(Course course) {
        JFrame contentFrame = new JFrame("Course Contents: " + course.getTitle());
        contentFrame.setSize(600, 400);

        // Panel for course materials
        JPanel courseMaterialsPanel = new JPanel(new BorderLayout());
        JLabel courseMaterialsLabel = new JLabel("Uploaded Materials:");
        DefaultListModel<String> fileListModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(fileListModel);

        for (File file : course.getUploadedFiles()) {
            fileListModel.addElement(file.getName());
        }

        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = fileList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < course.getUploadedFiles().size()) {
                        File selectedFile = course.getUploadedFiles().get(selectedIndex);

                        // Show a save dialog to download the file
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setSelectedFile(new File(selectedFile.getName()));
                        int result = fileChooser.showSaveDialog(contentFrame);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File destination = fileChooser.getSelectedFile();
                            try {
                                java.nio.file.Files.copy(selectedFile.toPath(), destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                JOptionPane.showMessageDialog(contentFrame, "File downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(contentFrame, "Failed to download file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        courseMaterialsPanel.add(courseMaterialsLabel, BorderLayout.NORTH);
        courseMaterialsPanel.add(new JScrollPane(fileList), BorderLayout.CENTER);

        // Panel for submitted assignments
        JPanel assignmentsPanel = new JPanel(new BorderLayout());
        JLabel assignmentsLabel = new JLabel("Submitted Assignments:");
        DefaultListModel<String> assignmentListModel = new DefaultListModel<>();
        JList<String> assignmentList = new JList<>(assignmentListModel);

        for (Assignment assignment : course.getAssignments()) {
            if (assignment.getStudent().equals(loggedInStudent)) {
                String grade = (assignment.getGrade() != null) ? assignment.getGrade() + "%" : "Not Graded";
                assignmentListModel.addElement(assignment.getFile().getName() + " (Grade: " + grade + ")");
            }
        }

        assignmentsPanel.add(assignmentsLabel, BorderLayout.NORTH);
        assignmentsPanel.add(new JScrollPane(assignmentList), BorderLayout.CENTER);

        // Layout the content frame
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, courseMaterialsPanel, assignmentsPanel);
        splitPane.setDividerLocation(200);

        contentFrame.add(splitPane);
        contentFrame.setVisible(true);
    }


    // Display assignments in a new window
    private void displayAssignments(Course course) {
        JFrame assignmentFrame = new JFrame("Assignments for: " + course.getTitle());
        assignmentFrame.setSize(500, 400);

        DefaultListModel<String> assignmentListModel = new DefaultListModel<>();
        JList<String> assignmentList = new JList<>(assignmentListModel);

        for (Assignment assignment : course.getAssignments()) {
            String grade = assignment.getGrade() == null ? "Not Graded" : assignment.getGrade() + "%";
            assignmentListModel.addElement(assignment.getStudent().getUsername() + " - " + assignment.getFile().getName() + " (" + grade + ")");
        }

        assignmentList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = assignmentList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < course.getAssignments().size()) {
                        Assignment selectedAssignment = course.getAssignments().get(selectedIndex);
                        String gradeInput = JOptionPane.showInputDialog(assignmentFrame, "Enter grade in percentage:", "Grade Assignment", JOptionPane.PLAIN_MESSAGE);
                        try {
                            int grade = Integer.parseInt(gradeInput);
                            if (grade >= 0 && grade <= 100) {
                                selectedAssignment.setGrade(grade);
                                displayAssignments(course); // Refresh assignment list
                                JOptionPane.showMessageDialog(assignmentFrame, "Assignment graded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(assignmentFrame, "Grade must be between 0 and 100.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(assignmentFrame, "Invalid grade entered.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        assignmentFrame.add(new JScrollPane(assignmentList));
        assignmentFrame.setVisible(true);
    }


    
    private void displayCourseContents(Course course) {
        JFrame contentFrame = new JFrame("Course Contents: " + course.getTitle());
        contentFrame.setSize(400, 400);

        DefaultListModel<String> fileListModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(fileListModel);

        // Populate the file list with uploaded files
        for (File file : course.getUploadedFiles()) {
            fileListModel.addElement(file.getName());
        }

        // Double-click to download the file
        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedIndex = fileList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < course.getUploadedFiles().size()) {
                        File selectedFile = course.getUploadedFiles().get(selectedIndex);

                        // Show a save dialog to select the download location
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setSelectedFile(new File(selectedFile.getName())); // Set default file name
                        int result = fileChooser.showSaveDialog(contentFrame);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File destination = fileChooser.getSelectedFile();
                            try {
                                // Copy the file to the selected location
                                java.nio.file.Files.copy(selectedFile.toPath(), destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                JOptionPane.showMessageDialog(contentFrame, "File downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(contentFrame, "Failed to download file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        // Add the file list to the frame
        contentFrame.add(new JScrollPane(fileList));
        contentFrame.setVisible(true);
    }

    private void refreshInstructorPanel() {
        JPanel instructorPanel = (JPanel) mainPanel.getComponent(2); // Assuming "Instructor" is at index 2
        Runnable refresh = (Runnable) instructorPanel.getClientProperty("refresh");
        if (refresh != null) {
            refresh.run();
        }
    }

    private JPanel createRegistrationPanel() {
        JPanel registerPanel = new JPanel(new GridLayout(5, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Student", "Instructor"});

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        JLabel statusLabel = new JLabel("");

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (role.equals("Student")) {
                if (userManager.registerStudent(username, password)) {
                    statusLabel.setText("Student registered successfully!");
                } else {
                    statusLabel.setText("Username already exists!");
                }
            } else {
                if (userManager.registerInstructor(username, password)) {
                    statusLabel.setText("Instructor registered successfully!");
                } else {
                    statusLabel.setText("Username already exists!");
                }
            }

            userField.setText("");
            passField.setText("");
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Login");
        });

        registerPanel.add(userLabel);
        registerPanel.add(userField);
        registerPanel.add(passLabel);
        registerPanel.add(passField);
        registerPanel.add(roleLabel);
        registerPanel.add(roleComboBox);
        registerPanel.add(registerButton);
        registerPanel.add(backButton);
        registerPanel.add(statusLabel);

        return registerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VirtualClassroom::new);
    }
}
