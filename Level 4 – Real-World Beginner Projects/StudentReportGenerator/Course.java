package StudentReportGenerator;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private double grade;
    private String instructor;

    public Course(String courseId, String courseName, int credits, double grade, String instructor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.grade = grade;
        this.instructor = instructor;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLetterGrade() {
        if(grade >= 90) return "A";
        else if(grade >= 80) return "B";
        else if(grade >= 70) return "C";
        else if(grade >= 60) return "D";
        else return "F";
    }

    public double getGradePoints() {
        String letter = getLetterGrade();
        switch(letter) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    public String toString() {
        return "Course ID: " + courseId + "\nCourse Name: " + courseName + "\nCredits: " + credits +
                "\nGrade: " + String.format("%.2f", grade) + "\nLetter Grade: " + getLetterGrade() +
                "\nInstructor: " + instructor;
    }
}
