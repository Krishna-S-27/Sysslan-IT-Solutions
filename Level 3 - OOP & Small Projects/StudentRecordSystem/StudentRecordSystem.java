package StudentRecordSystem;

import java.util.*;
import java.io.*;

public class StudentRecordSystem {
    private ArrayList<Student> students;
    private String filename;

    public StudentRecordSystem(String filename) {
        this.students = new ArrayList<>();
        this.filename = filename;
        loadStudents();
    }

    public void addStudent(Student student) {
        if(student == null) {
            System.out.println("Student cannot be null!");
            return;
        }

        for(Student s : students) {
            if(s.getRollNumber().equals(student.getRollNumber())) {
                System.out.println("Student with this roll number already exists!");
                return;
            }
        }

        if(student.getGpa() < 0 || student.getGpa() > 4.0) {
            System.out.println("GPA must be between 0 and 4.0!");
            return;
        }

        students.add(student);
        saveStudents();
        System.out.println("Student added successfully!");
    }

    public void deleteStudent(String rollNumber) {
        for(int i = 0; i < students.size(); i++) {
            if(students.get(i).getRollNumber().equals(rollNumber)) {
                Student removed = students.remove(i);
                saveStudents();
                System.out.println("Student deleted: " + removed.getName());
                return;
            }
        }
        System.out.println("Student not found!");
    }

    public Student searchByRollNumber(String rollNumber) {
        for(Student s : students) {
            if(s.getRollNumber().equals(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    public Student searchByName(String name) {
        for(Student s : students) {
            if(s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<Student> searchByDepartment(String department) {
        ArrayList<Student> results = new ArrayList<>();
        for(Student s : students) {
            if(s.getDepartment().equalsIgnoreCase(department)) {
                results.add(s);
            }
        }
        return results;
    }

    public ArrayList<Student> searchByGpaRange(double minGpa, double maxGpa) {
        ArrayList<Student> results = new ArrayList<>();
        for(Student s : students) {
            if(s.getGpa() >= minGpa && s.getGpa() <= maxGpa) {
                results.add(s);
            }
        }
        return results;
    }

    public void updateStudent(String rollNumber) {
        Student student = searchByRollNumber(rollNumber);
        if(student == null) {
            System.out.println("Student not found!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Current student record:");
        System.out.println(student);

        System.out.print("\nEnter new name (or press Enter to skip): ");
        String name = sc.nextLine();
        if(!name.isEmpty()) {
            student.setName(name);
        }

        System.out.print("Enter new email (or press Enter to skip): ");
        String email = sc.nextLine();
        if(!email.isEmpty()) {
            student.setEmail(email);
        }

        System.out.print("Enter new GPA (or press Enter to skip): ");
        String gpaStr = sc.nextLine();
        if(!gpaStr.isEmpty()) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                if(gpa >= 0 && gpa <= 4.0) {
                    student.setGpa(gpa);
                } else {
                    System.out.println("Invalid GPA! Must be between 0 and 4.0");
                }
            } catch(NumberFormatException e) {
                System.out.println("Invalid GPA format!");
            }
        }

        System.out.print("Enter new department (or press Enter to skip): ");
        String department = sc.nextLine();
        if(!department.isEmpty()) {
            student.setDepartment(department);
        }

        saveStudents();
        System.out.println("Student record updated successfully!");
    }

    public void displayAllStudents() {
        if(students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        System.out.println("\n========== All Students ==========");
        System.out.println(String.format("%-12s | %-20s | %-25s | %-6s | %-15s", "Roll No", "Name", "Email", "GPA", "Department"));
        System.out.println("-------------------------------------------------------------------------------------------------");

        for(Student s : students) {
            System.out.println(String.format("%-12s | %-20s | %-25s | %-6.2f | %-15s",
                    s.getRollNumber(), s.getName(), s.getEmail(), s.getGpa(), s.getDepartment()));
        }
        System.out.println("==================================\n");
    }

    public void displayStudentDetails(Student student) {
        if(student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("\n========== Student Details ==========");
        System.out.println(student);
        System.out.println("====================================\n");
    }

    public int getTotalStudents() {
        return students.size();
    }

    public double getAverageGpa() {
        if(students.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for(Student s : students) {
            sum += s.getGpa();
        }
        return sum / students.size();
    }

    public Student getTopStudent() {
        if(students.isEmpty()) {
            return null;
        }

        Student topStudent = students.get(0);
        for(Student s : students) {
            if(s.getGpa() > topStudent.getGpa()) {
                topStudent = s;
            }
        }
        return topStudent;
    }

    public void getDepartmentStatistics() {
        if(students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        Map<String, Integer> deptCount = new HashMap<>();
        Map<String, Double> deptAvgGpa = new HashMap<>();

        for(Student s : students) {
            String dept = s.getDepartment();
            deptCount.put(dept, deptCount.getOrDefault(dept, 0) + 1);
            deptAvgGpa.put(dept, deptAvgGpa.getOrDefault(dept, 0.0) + s.getGpa());
        }

        System.out.println("\n========== Department Statistics ==========");
        System.out.println(String.format("%-20s | %-10s | %-10s", "Department", "Count", "Avg GPA"));
        System.out.println("-------------------------------------------");

        for(String dept : deptCount.keySet()) {
            int count = deptCount.get(dept);
            double avgGpa = deptAvgGpa.get(dept) / count;
            System.out.println(String.format("%-20s | %-10d | %-10.2f", dept, count, avgGpa));
        }
        System.out.println("==========================================\n");
    }

    private void saveStudents() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(Student s : students) {
                writer.write(s.getRollNumber() + "|" + s.getName() + "|" + s.getEmail() + "|" +
                        s.getGpa() + "|" + s.getDepartment() + "|" + s.getEnrollmentDate());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            File file = new File(filename);
            if(!file.exists()) {
                System.out.println("New student record file will be created: " + filename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 6) {
                    String rollNumber = parts[0].trim();
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    double gpa = Double.parseDouble(parts[3].trim());
                    String department = parts[4].trim();
                    String enrollmentDate = parts[5].trim();

                    students.add(new Student(rollNumber, name, email, gpa, department, enrollmentDate));
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " students from " + filename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public void exportStudents() {
        System.out.print("Enter export filename (without extension): ");
        Scanner sc = new Scanner(System.in);
        String exportName = sc.nextLine() + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportName));

            writer.write("========== Student Records Export ==========\n");
            writer.write("Total Students: " + students.size() + "\n");
            writer.write("Average GPA: " + String.format("%.2f", getAverageGpa()) + "\n");
            writer.write("===========================================\n\n");

            for(Student s : students) {
                writer.write("Roll Number: " + s.getRollNumber() + "\n");
                writer.write("Name: " + s.getName() + "\n");
                writer.write("Email: " + s.getEmail() + "\n");
                writer.write("GPA: " + String.format("%.2f", s.getGpa()) + "\n");
                writer.write("Department: " + s.getDepartment() + "\n");
                writer.write("Enrollment Date: " + s.getEnrollmentDate() + "\n");
                writer.write("---------------------------------------------\n");
            }

            writer.close();
            System.out.println("Students exported to " + exportName);
        } catch(IOException e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Student Record System ==========");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search by Roll Number");
        System.out.println("4. Search by Name");
        System.out.println("5. Search by Department");
        System.out.println("6. Search by GPA Range");
        System.out.println("7. Update Student");
        System.out.println("8. Delete Student");
        System.out.println("9. View Statistics");
        System.out.println("10. Top Student");
        System.out.println("11. Department Statistics");
        System.out.println("12. Export Students");
        System.out.println("13. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 13) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 13: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Student Record System\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter filename to use (default: students.txt): ");
        String filename = sc.nextLine();

        if(filename.isEmpty()) {
            filename = "students.txt";
        }

        if(!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        StudentRecordSystem system = new StudentRecordSystem(filename);

        boolean running = true;
        while(running) {
            system.displayMenu();
            int choice = system.getValidChoice();

            switch(choice) {
                case 1:
                    System.out.print("Enter roll number: ");
                    String rollNumber = sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter GPA (0-4.0): ");
                    double gpa = 0;
                    try {
                        gpa = Double.parseDouble(sc.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("Invalid GPA!");
                        break;
                    }
                    System.out.print("Enter department: ");
                    String department = sc.nextLine();
                    System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                    String enrollmentDate = sc.nextLine();

                    if(rollNumber.isEmpty() || name.isEmpty() || email.isEmpty() || department.isEmpty() || enrollmentDate.isEmpty()) {
                        System.out.println("All fields are required!");
                    } else {
                        Student student = new Student(rollNumber, name, email, gpa, department, enrollmentDate);
                        system.addStudent(student);
                    }
                    break;

                case 2:
                    system.displayAllStudents();
                    break;

                case 3:
                    System.out.print("Enter roll number to search: ");
                    String searchRoll = sc.nextLine();
                    Student found = system.searchByRollNumber(searchRoll);
                    system.displayStudentDetails(found);
                    break;

                case 4:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    Student foundName = system.searchByName(searchName);
                    system.displayStudentDetails(foundName);
                    break;

                case 5:
                    System.out.print("Enter department to search: ");
                    String searchDept = sc.nextLine();
                    ArrayList<Student> deptStudents = system.searchByDepartment(searchDept);

                    if(deptStudents.isEmpty()) {
                        System.out.println("No students found in this department!");
                    } else {
                        System.out.println("Found " + deptStudents.size() + " student(s):");
                        System.out.println(String.format("%-12s | %-20s | %-25s | %-6s", "Roll No", "Name", "Email", "GPA"));
                        System.out.println("-------------------------------------------------------------------");
                        for(Student s : deptStudents) {
                            System.out.println(String.format("%-12s | %-20s | %-25s | %-6.2f",
                                    s.getRollNumber(), s.getName(), s.getEmail(), s.getGpa()));
                        }
                    }
                    break;

                case 6:
                    System.out.print("Enter minimum GPA: ");
                    double minGpa = Double.parseDouble(sc.nextLine());
                    System.out.print("Enter maximum GPA: ");
                    double maxGpa = Double.parseDouble(sc.nextLine());

                    ArrayList<Student> gpaStudents = system.searchByGpaRange(minGpa, maxGpa);
                    if(gpaStudents.isEmpty()) {
                        System.out.println("No students found in this GPA range!");
                    } else {
                        System.out.println("Found " + gpaStudents.size() + " student(s):");
                        System.out.println(String.format("%-12s | %-20s | %-6s", "Roll No", "Name", "GPA"));
                        System.out.println("-------------------------------------------");
                        for(Student s : gpaStudents) {
                            System.out.println(String.format("%-12s | %-20s | %-6.2f",
                                    s.getRollNumber(), s.getName(), s.getGpa()));
                        }
                    }
                    break;

                case 7:
                    System.out.print("Enter roll number to update: ");
                    String updateRoll = sc.nextLine();
                    system.updateStudent(updateRoll);
                    break;

                case 8:
                    System.out.print("Enter roll number to delete: ");
                    String deleteRoll = sc.nextLine();
                    system.deleteStudent(deleteRoll);
                    break;

                case 9:
                    System.out.println("\n========== Statistics ==========");
                    System.out.println("Total Students: " + system.getTotalStudents());
                    System.out.println("Average GPA: " + String.format("%.2f", system.getAverageGpa()));
                    System.out.println("================================\n");
                    break;

                case 10:
                    Student topStudent = system.getTopStudent();
                    if(topStudent != null) {
                        System.out.println("\n========== Top Student ==========");
                        System.out.println(topStudent);
                        System.out.println("================================\n");
                    } else {
                        System.out.println("No students found!");
                    }
                    break;

                case 11:
                    system.getDepartmentStatistics();
                    break;

                case 12:
                    system.exportStudents();
                    break;

                case 13:
                    System.out.println("Thank you for using Student Record System!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}
