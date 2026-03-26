package FileEncryption;

import java.util.*;
import java.io.*;

public class FileEncryption {
    private int shiftKey;

    public FileEncryption() {
        this.shiftKey = 0;
    }

    public void setShiftKey(int key) {
        this.shiftKey = key % 26;
        if(this.shiftKey < 0) {
            this.shiftKey += 26;
        }
    }

    public String encryptCaesar(String text) {
        StringBuilder encrypted = new StringBuilder();

        for(char c : text.toCharArray()) {
            if(Character.isUpperCase(c)) {
                int pos = c - 'A';
                int newPos = (pos + shiftKey) % 26;
                encrypted.append((char)('A' + newPos));
            }
            else if(Character.isLowerCase(c)) {
                int pos = c - 'a';
                int newPos = (pos + shiftKey) % 26;
                encrypted.append((char)('a' + newPos));
            }
            else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();
    }

    public String decryptCaesar(String text) {
        StringBuilder decrypted = new StringBuilder();

        for(char c : text.toCharArray()) {
            if(Character.isUpperCase(c)) {
                int pos = c - 'A';
                int newPos = (pos - shiftKey) % 26;
                if(newPos < 0) newPos += 26;
                decrypted.append((char)('A' + newPos));
            }
            else if(Character.isLowerCase(c)) {
                int pos = c - 'a';
                int newPos = (pos - shiftKey) % 26;
                if(newPos < 0) newPos += 26;
                decrypted.append((char)('a' + newPos));
            }
            else {
                decrypted.append(c);
            }
        }

        return decrypted.toString();
    }

    public String encryptSimple(String text, String key) {
        StringBuilder encrypted = new StringBuilder();
        int keyIndex = 0;

        for(char c : text.toCharArray()) {
            if(Character.isLetter(c)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int shift = keyChar - 'a';

                if(Character.isUpperCase(c)) {
                    int pos = c - 'A';
                    int newPos = (pos + shift) % 26;
                    encrypted.append((char)('A' + newPos));
                } else {
                    int pos = c - 'a';
                    int newPos = (pos + shift) % 26;
                    encrypted.append((char)('a' + newPos));
                }

                keyIndex++;
            } else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();
    }

    public String decryptSimple(String text, String key) {
        StringBuilder decrypted = new StringBuilder();
        int keyIndex = 0;

        for(char c : text.toCharArray()) {
            if(Character.isLetter(c)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int shift = keyChar - 'a';

                if(Character.isUpperCase(c)) {
                    int pos = c - 'A';
                    int newPos = (pos - shift) % 26;
                    if(newPos < 0) newPos += 26;
                    decrypted.append((char)('A' + newPos));
                } else {
                    int pos = c - 'a';
                    int newPos = (pos - shift) % 26;
                    if(newPos < 0) newPos += 26;
                    decrypted.append((char)('a' + newPos));
                }

                keyIndex++;
            } else {
                decrypted.append(c);
            }
        }

        return decrypted.toString();
    }

    public void encryptFile(String inputFile, String outputFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            String line;
            while((line = reader.readLine()) != null) {
                String encrypted = encryptCaesar(line);
                writer.write(encrypted);
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("File encrypted successfully!");
        } catch(FileNotFoundException e) {
            System.out.println("Error: File not found - " + inputFile);
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void decryptFile(String inputFile, String outputFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            String line;
            while((line = reader.readLine()) != null) {
                String decrypted = decryptCaesar(line);
                writer.write(decrypted);
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("File decrypted successfully!");
        } catch(FileNotFoundException e) {
            System.out.println("Error: File not found - " + inputFile);
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== File Encryption ==========");
        System.out.println("1. Caesar Cipher - Encrypt File");
        System.out.println("2. Caesar Cipher - Decrypt File");
        System.out.println("3. Vigenere Cipher - Encrypt Text");
        System.out.println("4. Vigenere Cipher - Decrypt Text");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidInteger(Scanner sc, int min, int max) {
        while(true) {
            try {
                int value = sc.nextInt();
                sc.nextLine();

                if(value >= min && value <= max) {
                    return value;
                } else {
                    System.out.print("Enter a number between " + min + " and " + max + ": ");
                }
            } catch(InputMismatchException e) {
                sc.nextLine();
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FileEncryption encryption = new FileEncryption();

        System.out.println("Welcome to File Encryption Tool");

        boolean running = true;
        while(running) {
            encryption.displayMenu();
            int choice = encryption.getValidInteger(sc, 1, 5);

            switch(choice) {
                case 1:
                    System.out.print("Enter shift key (1-25): ");
                    int key = encryption.getValidInteger(sc, 1, 25);
                    encryption.setShiftKey(key);

                    System.out.print("Enter input file name: ");
                    String inputFile = sc.nextLine();
                    System.out.print("Enter output file name: ");
                    String outputFile = sc.nextLine();

                    encryption.encryptFile(inputFile, outputFile);
                    break;

                case 2:
                    System.out.print("Enter shift key (1-25): ");
                    int key2 = encryption.getValidInteger(sc, 1, 25);
                    encryption.setShiftKey(key2);

                    System.out.print("Enter input file name: ");
                    String inputFile2 = sc.nextLine();
                    System.out.print("Enter output file name: ");
                    String outputFile2 = sc.nextLine();

                    encryption.decryptFile(inputFile2, outputFile2);
                    break;

                case 3:
                    System.out.print("Enter text to encrypt: ");
                    String text = sc.nextLine();
                    System.out.print("Enter encryption key (lowercase letters): ");
                    String vigKey = sc.nextLine();

                    if(vigKey.isEmpty()) {
                        System.out.println("Key cannot be empty!");
                    } else {
                        String encrypted = encryption.encryptSimple(text, vigKey);
                        System.out.println("Encrypted text: " + encrypted);
                    }
                    break;

                case 4:
                    System.out.print("Enter text to decrypt: ");
                    String encText = sc.nextLine();
                    System.out.print("Enter decryption key (lowercase letters): ");
                    String vigKey2 = sc.nextLine();

                    if(vigKey2.isEmpty()) {
                        System.out.println("Key cannot be empty!");
                    } else {
                        String decrypted = encryption.decryptSimple(encText, vigKey2);
                        System.out.println("Decrypted text: " + decrypted);
                    }
                    break;

                case 5:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}