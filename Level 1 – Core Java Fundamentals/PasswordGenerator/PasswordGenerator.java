package PasswordGenerator;

import java.util.*;
import java.awt.datatransfer.StringSelection;
public class PasswordGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private int passwordLength;
    private boolean includeLowercase;
    private boolean includeUppercase;
    private boolean includeDigits;
    private boolean includeSpecialChars;
    private Random random;

    public PasswordGenerator() {
        this.random = new Random();
        this.passwordLength = 8;
        this.includeLowercase = true;
        this.includeUppercase = true;
        this.includeDigits = true;
        this.includeSpecialChars = false;
    }

    public void setPreferences() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Password Generator Preferences ===");

        System.out.print("Enter desired password length (8-128): ");
        int length = sc.nextInt();
        if(length >= 8 && length <= 128) {
            this.passwordLength = length;
        } else {
            System.out.println("Invalid length! Setting to default: 8");
            this.passwordLength = 8;
        }

        System.out.print("Include lowercase letters (a-z)? (y/n): ");
        this.includeLowercase = sc.next().equalsIgnoreCase("y");

        System.out.print("Include uppercase letters (A-Z)? (y/n): ");
        this.includeUppercase = sc.next().equalsIgnoreCase("y");

        System.out.print("Include digits (0-9)? (y/n): ");
        this.includeDigits = sc.next().equalsIgnoreCase("y");

        System.out.print("Include special characters (!@#$%^&*...)? (y/n): ");
        this.includeSpecialChars = sc.next().equalsIgnoreCase("y");

        if(!includeLowercase && !includeUppercase && !includeDigits && !includeSpecialChars) {
            System.out.println("No character types selected! Enabling lowercase by default.");
            this.includeLowercase = true;
        }

        System.out.println("Preferences set successfully!\n");
    }

    private String buildCharacterPool() {
        StringBuilder pool = new StringBuilder();

        if(includeLowercase) pool.append(LOWERCASE);
        if(includeUppercase) pool.append(UPPERCASE);
        if(includeDigits) pool.append(DIGITS);
        if(includeSpecialChars) pool.append(SPECIAL_CHARS);

        return pool.toString();
    }

    public String generatePassword() {
        String characterPool = buildCharacterPool();
        StringBuilder password = new StringBuilder();

        for(int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(randomIndex));
        }

        return password.toString();
    }

    public String generateStrongPassword() {
        String password = generatePassword();
        char[] passwordArray = password.toCharArray();

        int index = 0;

        if(includeLowercase && !containsCharacterType(password, LOWERCASE)) {
            passwordArray[index] = LOWERCASE.charAt(random.nextInt(LOWERCASE.length()));
            index++;
        }

        if(includeUppercase && !containsCharacterType(password, UPPERCASE)) {
            passwordArray[index % passwordLength] = UPPERCASE.charAt(random.nextInt(UPPERCASE.length()));
            index++;
        }

        if(includeDigits && !containsCharacterType(password, DIGITS)) {
            passwordArray[index % passwordLength] = DIGITS.charAt(random.nextInt(DIGITS.length()));
            index++;
        }

        if(includeSpecialChars && !containsCharacterType(password, SPECIAL_CHARS)) {
            passwordArray[index % passwordLength] = SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
        }

        for(int i = passwordArray.length - 1; i > 0; i--) {
            int randomIdx = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIdx];
            passwordArray[randomIdx] = temp;
        }

        return new String(passwordArray);
    }

    private boolean containsCharacterType(String password, String characterSet) {
        for(char c : password.toCharArray()) {
            if(characterSet.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    public String calculatePasswordStrength(String password) {
        int strength = 0;
        if(password.length() >= 8) strength++;
        if(password.length() >= 12) strength++;
        if(password.length() >= 16) strength++;

        if(containsCharacterType(password, LOWERCASE)) strength++;
        if(containsCharacterType(password, UPPERCASE)) strength++;
        if(containsCharacterType(password, DIGITS)) strength++;
        if(containsCharacterType(password, SPECIAL_CHARS)) strength++;

        if(strength <= 2) return "Weak";
        else if(strength <= 4) return "Medium";
        else if(strength <= 6) return "Strong";
        else return "Very Strong";
    }

    public static void copyToClipboard(String text) {
        try {
            StringSelection stringSelection = new StringSelection(text);
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            System.out.println("Password copied to clipboard!");
        } catch(Exception e) {
            System.out.println("Could not copy to clipboard.");
        }
    }

    public void displayMenu() {
        System.out.println("\n=== Password Generator ===");
        System.out.println("1. Set Preferences");
        System.out.println("2. Generate Single Password");
        System.out.println("3. Generate Strong Password");
        System.out.println("4. Generate Multiple Passwords");
        System.out.println("5. Check Password Strength");
        System.out.println("6. View Current Preferences");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    public void displayPreferences() {
        System.out.println("\n=== Current Preferences ===");
        System.out.println("Length: " + passwordLength);
        System.out.println("Lowercase (a-z): " + (includeLowercase ? "Yes" : "No"));
        System.out.println("Uppercase (A-Z): " + (includeUppercase ? "Yes" : "No"));
        System.out.println("Digits (0-9): " + (includeDigits ? "Yes" : "No "));
        System.out.println("Special Characters: " + (includeSpecialChars ? "Yes" : "No"));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PasswordGenerator generator = new PasswordGenerator();
        boolean running = true;

        System.out.println("Welcome to Secure Password Generator!");

        while(running) {
            generator.displayMenu();
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    generator.setPreferences();
                    break;

                case 2:
                    String password = generator.generatePassword();
                    System.out.println("\nGenerated Password: " + password);
                    System.out.println("Strength: " + generator.calculatePasswordStrength(password));
                    System.out.print("Copy to clipboard? (y/n): ");
                    if(sc.nextLine().equalsIgnoreCase("y")) {
                        copyToClipboard(password);
                    }
                    break;

                case 3:
                    String strongPassword = generator.generateStrongPassword();
                    System.out.println("\nGenerated Strong Password: " + strongPassword);
                    System.out.println("Strength: " + generator.calculatePasswordStrength(strongPassword));
                    System.out.print("Copy to clipboard? (y/n): ");
                    if(sc.nextLine().equalsIgnoreCase("y")) {
                        copyToClipboard(strongPassword);
                    }
                    break;

                case 4:
                    System.out.print("How many passwords to generate? ");
                    int count = sc.nextInt();
                    sc.nextLine();
                    System.out.println("\nGenerated Passwords:");
                    for(int i = 1; i <= count; i++) {
                        String pwd = generator.generateStrongPassword();
                        System.out.println(i + ". " + pwd + " [" + generator.calculatePasswordStrength(pwd) + "]");
                    }
                    break;

                case 5:
                    System.out.print("Enter password to check strength: ");
                    String checkPassword = sc.nextLine();
                    System.out.println("Strength: " + generator.calculatePasswordStrength(checkPassword));
                    break;

                case 6:
                    generator.displayPreferences();
                    break;

                case 7:
                    System.out.println("\nThank you for using Password Generator! Stay secure!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1-7.");
            }
        }

        sc.close();
    }
}
