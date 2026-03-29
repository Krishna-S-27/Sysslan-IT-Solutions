package StudentRecordSystem;

public class Student {
    private String rollNumber;
    private String name;
    private String email;
    private double gpa;
    private String department;
    private String enrollmentDate;

    public Student(String rollNumber, String name, String email, double gpa, String department, String enrollmentDate) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.gpa = gpa;
        this.department = department;
        this.enrollmentDate = enrollmentDate;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String toString() {
        return "Roll Number: " + rollNumber + "\nName: " + name + "\nEmail: " + email +
                "\nGPA: " + String.format("%.2f", gpa) + "\nDepartment: " + department +
                "\nEnrollment Date: " + enrollmentDate;
    }
}
