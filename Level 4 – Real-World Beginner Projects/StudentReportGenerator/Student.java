package StudentReportGenerator;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String semester;
    private double gpa;
    private String enrollmentDate;

    public Student(String studentId, String name, String email, String phone,
                   String department, String semester, double gpa, String enrollmentDate) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
        this.gpa = gpa;
        this.enrollmentDate = enrollmentDate;
    }

    public String getStudentId() {
        return studentId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getAcademicStanding() {
        if(gpa >= 3.7) {
            return "Excellent";
        } else if(gpa >= 3.3) {
            return "Very Good";
        } else if(gpa >= 3.0) {
            return "Good";
        } else if(gpa >= 2.5) {
            return "Satisfactory";
        } else if(gpa >= 2.0) {
            return "Passing";
        } else {
            return "Failing";
        }
    }

    public String toString() {
        return "Student ID: " + studentId + "\nName: " + name + "\nEmail: " + email +
                "\nPhone: " + phone + "\nDepartment: " + department + "\nSemester: " + semester +
                "\nGPA: " + String.format("%.2f", gpa) + "\nAcademic Standing: " + getAcademicStanding() +
                "\nEnrollment Date: " + enrollmentDate;
    }
}
