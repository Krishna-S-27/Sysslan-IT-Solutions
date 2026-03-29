package StudentReportGenerator;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentReportGenerator {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private String studentsFilename;
    private String coursesFilename;

    public StudentReportGenerator(String studentsFile, String coursesFile) {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.studentsFilename = studentsFile;
        this.coursesFilename = coursesFile;
        loadStudents();
        loadCourses();
    }

    public void addStudent(Student student) {
        if(student == null) {
            System.out.println("Student cannot be null!");
            return;
        }

        for(Student s : students) {
            if(s.getStudentId().equals(student.getStudentId())) {
                System.out.println("Student with this ID already exists!");
                return;
            }
        }

        students.add(student);
        saveStudents();
        System.out.println("Student added successfully!");
    }

    public void addCourse(Course course) {
        if(course == null) {
            System.out.println("Course cannot be null!");
            return;
        }

        for(Course c : courses) {
            if(c.getCourseId().equals(course.getCourseId())) {
                System.out.println("Course with this ID already exists!");
                return;
            }
        }

        courses.add(course);
        saveCourses();
        System.out.println("Course added successfully!");
    }

    public Student searchStudentById(String studentId) {
        for(Student s : students) {
            if(s.getStudentId().equals(studentId)) {
                return s;
            }
        }
        return null;
    }

    public Course searchCourseById(String courseId) {
        for(Course c : courses) {
            if(c.getCourseId().equals(courseId)) {
                return c;
            }
        }
        return null;
    }

    public void generateIndividualReport(String studentId) {
        Student student = searchStudentById(studentId);

        if(student == null) {
            System.out.println("Student not found!");
            return;
        }

        ArrayList<Course> studentCourses = new ArrayList<>();
        for(Course c : courses) {
            if(c.getCourseId().startsWith(studentId)) {
                studentCourses.add(c);
            }
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("INDIVIDUAL STUDENT REPORT");
        System.out.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(70));

        System.out.println("\nSTUDENT INFORMATION");
        System.out.println("-".repeat(70));
        System.out.println("Student ID        : " + student.getStudentId());
        System.out.println("Name              : " + student.getName());
        System.out.println("Email             : " + student.getEmail());
        System.out.println("Phone             : " + student.getPhone());
        System.out.println("Department        : " + student.getDepartment());
        System.out.println("Semester          : " + student.getSemester());
        System.out.println("Enrollment Date   : " + student.getEnrollmentDate());
        System.out.println("Current GPA       : " + String.format("%.2f", student.getGpa()));
        System.out.println("Academic Standing : " + student.getAcademicStanding());

        if(!studentCourses.isEmpty()) {
            System.out.println("\nCOURSE DETAILS");
            System.out.println("-".repeat(70));
            System.out.println(String.format("%-12s | %-25s | %-7s | %-7s | %-7s | %-10s",
                    "Course ID", "Course Name", "Credits", "Grade", "Letter", "Instructor"));
            System.out.println("-".repeat(70));

            double totalGradePoints = 0;
            int totalCredits = 0;

            for(Course c : studentCourses) {
                System.out.println(String.format("%-12s | %-25s | %-7d | %-7.2f | %-7s | %-10s",
                        c.getCourseId(),
                        c.getCourseName().length() > 25 ? c.getCourseName().substring(0, 22) + "..." : c.getCourseName(),
                        c.getCredits(),
                        c.getGrade(),
                        c.getLetterGrade(),
                        c.getInstructor()));

                totalGradePoints += c.getGradePoints() * c.getCredits();
                totalCredits += c.getCredits();
            }

            System.out.println("-".repeat(70));
            System.out.println("Total Credits     : " + totalCredits);

            if(totalCredits > 0) {
                double calculatedGpa = totalGradePoints / totalCredits;
                System.out.println("Calculated GPA    : " + String.format("%.2f", calculatedGpa));
            }
        }

        System.out.println("\n" + "=".repeat(70) + "\n");
    }

    public void generateDepartmentReport(String department) {
        ArrayList<Student> deptStudents = new ArrayList<>();

        for(Student s : students) {
            if(s.getDepartment().equalsIgnoreCase(department)) {
                deptStudents.add(s);
            }
        }

        if(deptStudents.isEmpty()) {
            System.out.println("No students found in department: " + department);
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEPARTMENT REPORT: " + department);
        System.out.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(80));

        System.out.println(String.format("%-12s | %-20s | %-15s | %-8s | %-20s",
                "Student ID", "Name", "Email", "GPA", "Academic Standing"));
        System.out.println("-".repeat(80));

        double totalGpa = 0;
        for(Student s : deptStudents) {
            System.out.println(String.format("%-12s | %-20s | %-15s | %-8.2f | %-20s",
                    s.getStudentId(),
                    s.getName(),
                    s.getEmail(),
                    s.getGpa(),
                    s.getAcademicStanding()));
            totalGpa += s.getGpa();
        }

        System.out.println("-".repeat(80));
        System.out.println("Total Students    : " + deptStudents.size());
        System.out.println("Average GPA       : " + String.format("%.2f", totalGpa / deptStudents.size()));
        System.out.println("=".repeat(80) + "\n");
    }

    public void generateClassReport(String semester) {
        ArrayList<Student> semesterStudents = new ArrayList<>();

        for(Student s : students) {
            if(s.getSemester().equals(semester)) {
                semesterStudents.add(s);
            }
        }

        if(semesterStudents.isEmpty()) {
            System.out.println("No students found in semester: " + semester);
            return;
        }

        System.out.println("\n" + "=".repeat(85));
        System.out.println("CLASS REPORT: SEMESTER " + semester);
        System.out.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(85));

        System.out.println(String.format("%-12s | %-20s | %-15s | %-12s | %-8s | %-15s",
                "Student ID", "Name", "Department", "Email", "GPA", "Standing"));
        System.out.println("-".repeat(85));

        double totalGpa = 0;
        int excellent = 0, veryGood = 0, good = 0, satisfactory = 0, passing = 0, failing = 0;

        for(Student s : semesterStudents) {
            System.out.println(String.format("%-12s | %-20s | %-15s | %-12s | %-8.2f | %-15s",
                    s.getStudentId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getEmail(),
                    s.getGpa(),
                    s.getAcademicStanding()));

            totalGpa += s.getGpa();

            switch(s.getAcademicStanding()) {
                case "Excellent": excellent++; break;
                case "Very Good": veryGood++; break;
                case "Good": good++; break;
                case "Satisfactory": satisfactory++; break;
                case "Passing": passing++; break;
                case "Failing": failing++; break;
            }
        }

        System.out.println("-".repeat(85));
        System.out.println("\nSTATISTICS:");
        System.out.println("  Total Students      : " + semesterStudents.size());
        System.out.println("  Average GPA         : " + String.format("%.2f", totalGpa / semesterStudents.size()));
        System.out.println("  Excellent (3.7+)    : " + excellent);
        System.out.println("  Very Good (3.3-3.7) : " + veryGood);
        System.out.println("  Good (3.0-3.3)      : " + good);
        System.out.println("  Satisfactory (2.5-3.0) : " + satisfactory);
        System.out.println("  Passing (2.0-2.5)   : " + passing);
        System.out.println("  Failing (< 2.0)     : " + failing);
        System.out.println("=".repeat(85) + "\n");
    }

    public void generateHonorRoll() {
        ArrayList<Student> honorRoll = new ArrayList<>();

        for(Student s : students) {
            if(s.getGpa() >= 3.5) {
                honorRoll.add(s);
            }
        }

        if(honorRoll.isEmpty()) {
            System.out.println("No students on honor roll!");
            return;
        }

        Collections.sort(honorRoll, (a, b) -> Double.compare(b.getGpa(), a.getGpa()));

        System.out.println("\n" + "=".repeat(75));
        System.out.println("HONOR ROLL REPORT (GPA >= 3.5)");
        System.out.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(75));

        System.out.println(String.format("%-12s | %-20s | %-15s | %-8s | %-15s",
                "Student ID", "Name", "Department", "GPA", "Semester"));
        System.out.println("-".repeat(75));

        for(Student s : honorRoll) {
            System.out.println(String.format("%-12s | %-20s | %-15s | %-8.2f | %-15s",
                    s.getStudentId(),
                    s.getName(),
                    s.getDepartment(),
                    s.getGpa(),
                    s.getSemester()));
        }

        System.out.println("-".repeat(75));
        System.out.println("Total on Honor Roll : " + honorRoll.size());
        System.out.println("=".repeat(75) + "\n");
    }

    public void exportReportToPDF(String studentId, String filename) {
        Student student = searchStudentById(studentId);

        if(student == null) {
            System.out.println("Student not found!");
            return;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("=".repeat(70) + "\n");
            writer.write("INDIVIDUAL STUDENT REPORT\n");
            writer.write("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("=".repeat(70) + "\n\n");

            writer.write("STUDENT INFORMATION\n");
            writer.write("-".repeat(70) + "\n");
            writer.write("Student ID        : " + student.getStudentId() + "\n");
            writer.write("Name              : " + student.getName() + "\n");
            writer.write("Email             : " + student.getEmail() + "\n");
            writer.write("Phone             : " + student.getPhone() + "\n");
            writer.write("Department        : " + student.getDepartment() + "\n");
            writer.write("Semester          : " + student.getSemester() + "\n");
            writer.write("Enrollment Date   : " + student.getEnrollmentDate() + "\n");
            writer.write("Current GPA       : " + String.format("%.2f", student.getGpa()) + "\n");
            writer.write("Academic Standing : " + student.getAcademicStanding() + "\n");

            writer.close();
            System.out.println("Report exported to " + filename);
        } catch(IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }

    private void saveStudents() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(studentsFilename));

            for(Student s : students) {
                writer.write(s.getStudentId() + "|" + s.getName() + "|" + s.getEmail() + "|" +
                        s.getPhone() + "|" + s.getDepartment() + "|" + s.getSemester() + "|" +
                        s.getGpa() + "|" + s.getEnrollmentDate());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private void saveCourses() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(coursesFilename));

            for(Course c : courses) {
                writer.write(c.getCourseId() + "|" + c.getCourseName() + "|" + c.getCredits() + "|" +
                        c.getGrade() + "|" + c.getInstructor());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            File file = new File(studentsFilename);
            if(!file.exists()) {
                System.out.println("New students file will be created: " + studentsFilename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(studentsFilename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 8) {
                    String studentId = parts[0].trim();
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    String phone = parts[3].trim();
                    String department = parts[4].trim();
                    String semester = parts[5].trim();
                    double gpa = Double.parseDouble(parts[6].trim());
                    String enrollmentDate = parts[7].trim();

                    Student student = new Student(studentId, name, email, phone,
                            department, semester, gpa, enrollmentDate);
                    students.add(student);
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " students from " + studentsFilename);
        } catch(IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    private void loadCourses() {
        try {
            File file = new File(coursesFilename);
            if(!file.exists()) {
                System.out.println("New courses file will be created: " + coursesFilename + "\n");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(coursesFilename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 5) {
                    String courseId = parts[0].trim();
                    String courseName = parts[1].trim();
                    int credits = Integer.parseInt(parts[2].trim());
                    double grade = Double.parseDouble(parts[3].trim());
                    String instructor = parts[4].trim();

                    Course course = new Course(courseId, courseName, credits, grade, instructor);
                    courses.add(course);
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " courses from " + coursesFilename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Student Report Generator ==========");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Generate Individual Report");
        System.out.println("4. Generate Department Report");
        System.out.println("5. Generate Class Report");
        System.out.println("6. Generate Honor Roll");
        System.out.println("7. Export Report");
        System.out.println("8. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 8) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 8: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Student Report Generator\n");

        StudentReportGenerator generator = new StudentReportGenerator("students_report.txt", "courses_report.txt");

        boolean running = true;
        while(running) {
            generator.displayMenu();
            int choice = generator.getValidChoice();

            Scanner sc = new Scanner(System.in);

            switch(choice) {
                case 1:
                    System.out.print("Enter student ID: ");
                    String studentId = sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter department: ");
                    String department = sc.nextLine();
                    System.out.print("Enter semester: ");
                    String semester = sc.nextLine();
                    System.out.print("Enter GPA: ");

                    try {
                        double gpa = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter enrollment date (yyyy-MM-dd): ");
                        String enrollmentDate = sc.nextLine();

                        Student student = new Student(studentId, name, email, phone,
                                department, semester, gpa, enrollmentDate);
                        generator.addStudent(student);
                    } catch(NumberFormatException e) {
                        System.out.println("Invalid GPA! Please enter a number.");
                    }
                    break;

                case 2:
                    System.out.print("Enter course ID: ");
                    String courseId = sc.nextLine();
                    System.out.print("Enter course name: ");
                    String courseName = sc.nextLine();
                    System.out.print("Enter credits: ");

                    try {
                        int credits = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter grade (0-100): ");
                        double grade = Double.parseDouble(sc.nextLine());

                        if(grade < 0 || grade > 100) {
                            System.out.println("Grade must be between 0 and 100!");
                            break;
                        }

                        System.out.print("Enter instructor name: ");
                        String instructor = sc.nextLine();

                        Course course = new Course(courseId, courseName, credits, grade, instructor);
                        generator.addCourse(course);
                    } catch(NumberFormatException e) {
                        System.out.println("Invalid credits or grade! Please enter numbers.");
                    }
                    break;

                case 3:
                    System.out.print("Enter student ID: ");
                    String indStudentId = sc.nextLine();
                    generator.generateIndividualReport(indStudentId);
                    break;

                case 4:
                    System.out.print("Enter department: ");
                    String deptName = sc.nextLine();
                    generator.generateDepartmentReport(deptName);
                    break;

                case 5:
                    System.out.print("Enter semester: ");
                    String semName = sc.nextLine();
                    generator.generateClassReport(semName);
                    break;

                case 6:
                    generator.generateHonorRoll();
                    break;

                case 7:
                    System.out.print("Enter student ID to export: ");
                    String expStudentId = sc.nextLine();
                    System.out.print("Enter export filename: ");
                    String exportFile = sc.nextLine();
                    generator.exportReportToPDF(expStudentId, exportFile);
                    break;

                case 8:
                    System.out.println("Thank you for using Student Report Generator!");
                    running = false;
                    break;
            }
        }
    }
}