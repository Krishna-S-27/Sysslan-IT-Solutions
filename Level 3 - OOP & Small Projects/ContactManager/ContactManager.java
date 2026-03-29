package ContactManager;

import java.util.*;
import java.io.*;

public class ContactManager {
    private ArrayList<Contact> contacts;
    private String filename;

    public ContactManager(String filename) {
        this.contacts = new ArrayList<>();
        this.filename = filename;
        loadContacts();
    }

    public void addContact(Contact contact) {
        if(contact == null) {
            System.out.println("Contact cannot be null!");
            return;
        }

        for(Contact c : contacts) {
            if(c.getPhone().equals(contact.getPhone())) {
                System.out.println("Contact with this phone number already exists!");
                return;
            }
        }

        contacts.add(contact);
        saveContacts();
        System.out.println("Contact added successfully!");
    }

    public void deleteContact(String phone) {
        for(int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getPhone().equals(phone)) {
                Contact removed = contacts.remove(i);
                saveContacts();
                System.out.println("Contact deleted: " + removed.getName());
                return;
            }
        }
        System.out.println("Contact not found!");
    }

    public Contact searchByPhone(String phone) {
        for(Contact c : contacts) {
            if(c.getPhone().equals(phone)) {
                return c;
            }
        }
        return null;
    }

    public Contact searchByName(String name) {
        for(Contact c : contacts) {
            if(c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Contact> searchByEmail(String email) {
        ArrayList<Contact> results = new ArrayList<>();
        for(Contact c : contacts) {
            if(c.getEmail().equalsIgnoreCase(email)) {
                results.add(c);
            }
        }
        return results;
    }

    public void updateContact(String phone) {
        Contact contact = searchByPhone(phone);
        if(contact == null) {
            System.out.println("Contact not found!");
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("Current contact:");
        System.out.println(contact);

        System.out.print("\nEnter new name (or press Enter to skip): ");
        String name = sc.nextLine();
        if(!name.isEmpty()) {
            contact.setName(name);
        }

        System.out.print("Enter new email (or press Enter to skip): ");
        String email = sc.nextLine();
        if(!email.isEmpty()) {
            contact.setEmail(email);
        }

        System.out.print("Enter new address (or press Enter to skip): ");
        String address = sc.nextLine();
        if(!address.isEmpty()) {
            contact.setAddress(address);
        }

        saveContacts();
        System.out.println("Contact updated successfully!");
    }

    public void displayAllContacts() {
        if(contacts.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }

        System.out.println("\n========== All Contacts ==========");
        System.out.println(String.format("%-20s | %-25s | %-15s", "Name", "Email", "Phone"));
        System.out.println("---------------------------------------------------------------");

        for(Contact c : contacts) {
            System.out.println(String.format("%-20s | %-25s | %-15s", c.getName(), c.getEmail(), c.getPhone()));
        }
        System.out.println("==================================\n");
    }

    public void displayContactDetails(Contact contact) {
        if(contact == null) {
            System.out.println("Contact not found!");
            return;
        }

        System.out.println("\n========== Contact Details ==========");
        System.out.println(contact);
        System.out.println("====================================\n");
    }

    public int getTotalContacts() {
        return contacts.size();
    }

    private void saveContacts() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(Contact c : contacts) {
                writer.write(c.getName() + "|" + c.getEmail() + "|" + c.getPhone() + "|" + c.getAddress());
                writer.newLine();
            }

            writer.close();
            System.out.println("Data saved to " + filename);
        } catch(IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private void loadContacts() {
        try {
            File file = new File(filename);
            if(!file.exists()) {
                System.out.println("New contact file will be created: " + filename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 4) {
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    String phone = parts[2].trim();
                    String address = parts[3].trim();

                    contacts.add(new Contact(name, email, phone, address));
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " contacts from " + filename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Contact Manager ==========");
        System.out.println("1. Add Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contact by Name");
        System.out.println("4. Search Contact by Phone");
        System.out.println("5. Search Contact by Email");
        System.out.println("6. Update Contact");
        System.out.println("7. Delete Contact");
        System.out.println("8. Total Contacts");
        System.out.println("9. Export Contacts");
        System.out.println("10. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 10) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 10: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public void exportContacts() {
        System.out.print("Enter export filename (without extension): ");
        Scanner sc = new Scanner(System.in);
        String exportName = sc.nextLine() + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportName));

            writer.write("========== Contact Export ==========\n");
            writer.write("Total Contacts: " + contacts.size() + "\n");
            writer.write("=====================================\n\n");

            for(Contact c : contacts) {
                writer.write("Name: " + c.getName() + "\n");
                writer.write("Email: " + c.getEmail() + "\n");
                writer.write("Phone: " + c.getPhone() + "\n");
                writer.write("Address: " + c.getAddress() + "\n");
                writer.write("-------------------------------------\n");
            }

            writer.close();
            System.out.println("Contacts exported to " + exportName);
        } catch(IOException e) {
            System.out.println("Error exporting contacts: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Contact Management System\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter filename to use (default: contacts.txt): ");
        String filename = sc.nextLine();

        if(filename.isEmpty()) {
            filename = "contacts.txt";
        }

        if(!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        ContactManager manager = new ContactManager(filename);

        boolean running = true;
        while(running) {
            manager.displayMenu();
            int choice = manager.getValidChoice();

            switch(choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter address: ");
                    String address = sc.nextLine();

                    if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        System.out.println("All fields are required!");
                    } else {
                        Contact contact = new Contact(name, email, phone, address);
                        manager.addContact(contact);
                    }
                    break;

                case 2:
                    manager.displayAllContacts();
                    break;

                case 3:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    Contact found = manager.searchByName(searchName);
                    manager.displayContactDetails(found);
                    break;

                case 4:
                    System.out.print("Enter phone to search: ");
                    String searchPhone = sc.nextLine();
                    Contact foundPhone = manager.searchByPhone(searchPhone);
                    manager.displayContactDetails(foundPhone);
                    break;

                case 5:
                    System.out.print("Enter email to search: ");
                    String searchEmail = sc.nextLine();
                    ArrayList<Contact> foundEmails = manager.searchByEmail(searchEmail);

                    if(foundEmails.isEmpty()) {
                        System.out.println("No contacts found with this email!");
                    } else {
                        System.out.println("Found " + foundEmails.size() + " contact(s):");
                        for(Contact c : foundEmails) {
                            manager.displayContactDetails(c);
                        }
                    }
                    break;

                case 6:
                    System.out.print("Enter phone number of contact to update: ");
                    String updatePhone = sc.nextLine();
                    manager.updateContact(updatePhone);
                    break;

                case 7:
                    System.out.print("Enter phone number to delete: ");
                    String deletePhone = sc.nextLine();
                    manager.deleteContact(deletePhone);
                    break;

                case 8:
                    System.out.println("Total contacts: " + manager.getTotalContacts());
                    break;

                case 9:
                    manager.exportContacts();
                    break;

                case 10:
                    System.out.println("Thank you for using Contact Manager!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}