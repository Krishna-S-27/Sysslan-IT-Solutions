package StudentGradeCalculator;

import java.util.*;

public class StudentGradeCalculator {
    private String[] studentNames;
    private double[][] grades;
    private double[] averages;
    private int studentCount;
    private int subjectCount;

    public StudentGradeCalculator() {
        this.studentNames = new String[100];
        this.grades = new double[100][10];
        this.averages = new double[100];
        this.studentCount = 0;
        this.subjectCount = 0;
    }

    public void addStudentGrades() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        studentCount = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter number of subjects/tests: ");
        subjectCount = sc.nextInt();
        sc.nextLine();

        for(int i = 0; i < studentCount; i++) {
            System.out.print("Enter student name: ");
            studentNames[i] = sc.nextLine();

            System.out.println("Enter grades for " + studentNames[i] + ":");
            for(int j = 0; j < subjectCount; j++) {
                System.out.print("  Subject/Test " + (j + 1) + ": ");
                grades[i][j] = sc.nextDouble();
            }
            sc.nextLine();
        }
    }

    public void calculateAverages() {
        for(int i = 0; i < studentCount; i++) {
            double sum = 0;
            for(int j = 0; j < subjectCount; j++) {
                sum += grades[i][j];
            }
            averages[i] = sum / subjectCount;
        }
    }

    public void displayStudentAverages() {
        System.out.println("\n=== Student Grade Averages ===");
        System.out.printf("%-20s | %-10s | %-8s\n", "Student Name", "Average", "Grade");

        for(int i = 0; i < studentCount; i++) {
            String letterGrade = getLetterGrade(averages[i]);
            System.out.printf("%-20s | %-10.2f | %-8s\n", studentNames[i], averages[i], letterGrade);
        }
    }

    public String getLetterGrade(double average) {
        if(average >= 90) return "A";
        else if(average >= 80) return "B";
        else if(average >= 70) return "C";
        else if(average >= 60) return "D";
        else return "F";
    }

    public void findHighestAverage() {
        double maxAverage = averages[0];
        String topStudent = studentNames[0];

        for(int i = 1; i < studentCount; i++) {
            if(averages[i] > maxAverage) {
                maxAverage = averages[i];
                topStudent = studentNames[i];
            }
        }

        System.out.println("\n Highest Average: " + topStudent + " with " + String.format("%.2f", maxAverage));
    }

    public void findLowestAverage() {
        double minAverage = averages[0];
        String lowStudent = studentNames[0];

        for(int i = 1; i < studentCount; i++) {
            if(averages[i] < minAverage) {
                minAverage = averages[i];
                lowStudent = studentNames[i];
            }
        }

        System.out.println("Lowest Average: " + lowStudent + " with " + String.format("%.2f", minAverage));
    }

    public void displayClassAverage() {
        double classSum = 0;
        for(int i = 0; i < studentCount; i++) {
            classSum += averages[i];
        }
        double classAverage = classSum / studentCount;
        System.out.println("Class Average: " + String.format("%.2f", classAverage));
    }


    public void displayStudentDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter student name to view details: ");
        String name = sc.nextLine();

        int studentIndex = -1;
        for(int i = 0; i < studentCount; i++) {
            if(studentNames[i].equalsIgnoreCase(name)) {
                studentIndex = i;
                break;
            }
        }

        if(studentIndex == -1) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("\n=== Details for " + studentNames[studentIndex] + " ===");
        System.out.println("Grades:");
        for(int j = 0; j < subjectCount; j++) {
            System.out.println("  Subject/Test " + (j + 1) + ": " + String.format("%.2f", grades[studentIndex][j]));
        }
        System.out.println("Average: " + String.format("%.2f", averages[studentIndex]));
        System.out.println("Grade: " + getLetterGrade(averages[studentIndex]) + "\n");
    }

    public void displayMenu() {
        System.out.println("\n=== Student Grade Calculator ===");
        System.out.println("1. Add Student Grades");
        System.out.println("2. Display All Averages");
        System.out.println("3. Find Highest Average");
        System.out.println("4. Find Lowest Average");
        System.out.println("5. Display Class Average");
        System.out.println("6. View Student Details");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentGradeCalculator calculator = new StudentGradeCalculator();
        boolean running = true;

        while(running) {
            calculator.displayMenu();
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    calculator.addStudentGrades();
                    calculator.calculateAverages();
                    System.out.println("Grades added successfully!");
                    break;

                case 2:
                    if(calculator.studentCount == 0) {
                        System.out.println("No student data available! Please add grades first.");
                    } else {
                        calculator.displayStudentAverages();
                    }
                    break;

                case 3:
                    if(calculator.studentCount == 0) {
                        System.out.println("No student data available!");
                    } else {
                        calculator.findHighestAverage();
                    }
                    break;

                case 4:
                    if(calculator.studentCount == 0) {
                        System.out.println("No student data available!");
                    } else {
                        calculator.findLowestAverage();
                    }
                    break;

                case 5:
                    if(calculator.studentCount == 0) {
                        System.out.println("No student data available!");
                    } else {
                        calculator.displayClassAverage();
                    }
                    break;

                case 6:
                    if(calculator.studentCount == 0) {
                        System.out.println("No student data available!");
                    } else {
                        calculator.displayStudentDetails();
                    }
                    break;

                case 7:
                    System.out.println("Thank you for using Student Grade Calculator!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1-7.");
            }
        }

        sc.close();
    }
}