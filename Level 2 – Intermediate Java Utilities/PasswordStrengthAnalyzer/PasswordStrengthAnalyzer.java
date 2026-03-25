package PasswordStrengthAnalyzer;

import java.util.*;

public class PasswordStrengthAnalyzer {
    private String password;
    private int strengthScore;
    private List<String> issues;

    public PasswordStrengthAnalyzer() {
        this.issues = new ArrayList<>();
    }

    public void analyzePassword(String password) {
        this.password = password;
        this.strengthScore = 0;
        this.issues.clear();

        checkLength();
        checkUppercase();
        checkLowercase();
        checkNumbers();
        checkSpecialCharacters();
        checkCommonPatterns();
        checkRepeatingCharacters();
        checkSequentialCharacters();
    }

    private void checkLength() {
        if(password.length() < 8) {
            issues.add("Password must be at least 8 characters long");
        } else if(password.length() >= 8 && password.length() < 12) {
            strengthScore += 1;
        } else if(password.length() >= 12 && password.length() < 16) {
            strengthScore += 2;
        } else {
            strengthScore += 3;
        }
    }

    private void checkUppercase() {
        for(char c : password.toCharArray()) {
            if(Character.isUpperCase(c)) {
                strengthScore += 1;
                return;
            }
        }
        issues.add("Add uppercase letters (A-Z)");
    }

    private void checkLowercase() {
        for(char c : password.toCharArray()) {
            if(Character.isLowerCase(c)) {
                strengthScore += 1;
                return;
            }
        }
        issues.add("Add lowercase letters (a-z)");
    }

    private void checkNumbers() {
        for(char c : password.toCharArray()) {
            if(Character.isDigit(c)) {
                strengthScore += 1;
                return;
            }
        }
        issues.add("Add numbers (0-9)");
    }

    private void checkSpecialCharacters() {
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        for(char c : password.toCharArray()) {
            if(specialChars.indexOf(c) >= 0) {
                strengthScore += 2;
                return;
            }
        }
        issues.add("Add special characters (!@#$%^&*...)");
    }

    private void checkCommonPatterns() {
        String[] commonPatterns = {"123", "abc", "password", "qwerty", "admin", "letmein", "welcome", "monkey"};

        String lowerPassword = password.toLowerCase();
        for(String pattern : commonPatterns) {
            if(lowerPassword.contains(pattern)) {
                issues.add("Avoid common patterns like: " + pattern);
                strengthScore -= 2;
                return;
            }
        }
    }

    private void checkRepeatingCharacters() {
        for(int i = 0; i < password.length() - 2; i++) {
            if(password.charAt(i) == password.charAt(i + 1) && password.charAt(i + 1) == password.charAt(i + 2)) {
                issues.add("Avoid repeating characters (aaa, bbb, etc.)");
                strengthScore -= 1;
                return;
            }
        }
    }

    private void checkSequentialCharacters() {
        for(int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);

            if((c1 + 1 == c2 && c2 + 1 == c3) || (c1 - 1 == c2 && c2 - 1 == c3)) {
                issues.add("Avoid sequential characters (abc, 123, etc.)");
                strengthScore -= 1;
                return;
            }
        }
    }

    public String getStrengthLevel() {
        if(strengthScore <= 2) {
            return "Weak";
        } else if(strengthScore <= 4) {
            return "Fair";
        } else if(strengthScore <= 6) {
            return "Good";
        } else if(strengthScore <= 8) {
            return "Strong";
        } else {
            return "Very Strong";
        }
    }

    public void displayAnalysis() {
        System.out.println("\n========== Password Analysis ==========");
        System.out.println("Password: " + maskPassword());
        System.out.println("Length: " + password.length() + " characters");
        System.out.println("Strength Level: " + getStrengthLevel());
        System.out.println("Score: " + strengthScore);

        System.out.println("\nValidation Results:");
        System.out.println("- Uppercase letters: " + hasUppercase());
        System.out.println("- Lowercase letters: " + hasLowercase());
        System.out.println("- Numbers: " + hasNumbers());
        System.out.println("- Special characters: " + hasSpecialCharacters());

        if(issues.isEmpty()) {
            System.out.println("\nSuggestions: No issues found!");
        } else {
            System.out.println("\nSuggestions:");
            for(int i = 0; i < issues.size(); i++) {
                System.out.println((i + 1) + ". " + issues.get(i));
            }
        }
        System.out.println("========================================\n");
    }

    private String maskPassword() {
        if(password.length() <= 2) {
            return password;
        }
        String masked = password.substring(0, 1);
        for(int i = 1; i < password.length() - 1; i++) {
            masked += "*";
        }
        masked += password.substring(password.length() - 1);
        return masked;
    }

    private String hasUppercase() {
        for(char c : password.toCharArray()) {
            if(Character.isUpperCase(c)) {
                return "Yes";
            }
        }
        return "No";
    }

    private String hasLowercase() {
        for(char c : password.toCharArray()) {
            if(Character.isLowerCase(c)) {
                return "Yes";
            }
        }
        return "No";
    }

    private String hasNumbers() {
        for(char c : password.toCharArray()) {
            if(Character.isDigit(c)) {
                return "Yes";
            }
        }
        return "No";
    }

    private String hasSpecialCharacters() {
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        for(char c : password.toCharArray()) {
            if(specialChars.indexOf(c) >= 0) {
                return "Yes";
            }
        }
        return "No";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PasswordStrengthAnalyzer analyzer = new PasswordStrengthAnalyzer();

        System.out.println("Password Strength Analyzer\n");

        boolean running = true;
        while(running) {
            System.out.println("1. Analyze Password");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("Enter password to analyze: ");
                    String password = sc.nextLine();

                    if(password.isEmpty()) {
                        System.out.println("Password cannot be empty!");
                    } else {
                        analyzer.analyzePassword(password);
                        analyzer.displayAnalysis();
                    }
                    break;

                case 2:
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}
