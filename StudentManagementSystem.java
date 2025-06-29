import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    private List<Student> students;
    private final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudentsFromFile();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudentsToFile();
    }

    public boolean removeStudent(String rollNumber) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getRollNumber().equals(rollNumber)) {
                iterator.remove();
                saveStudentsToFile();
                return true;
            }
        }
        return false;
    }

    public Student searchStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equals(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void loadStudentsFromFile() {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            // File may not exist on first run; ignore
        }
    }

    private void saveStudentsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                bw.write(s.getName() + "," + s.getRollNumber() + "," + s.getGrade() + "," + s.getEmail() + "," + s.getPhoneNumber());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10,15}");
    }

    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter roll number: ");
                    String roll = scanner.nextLine().trim();
                    System.out.print("Enter grade: ");
                    String grade = scanner.nextLine().trim();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("Enter phone number: ");
                    String phone = scanner.nextLine().trim();
                    if (name.isEmpty() || roll.isEmpty() || grade.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        System.out.println("All fields are required.");
                        break;
                    }
                    if (!sms.isValidEmail(email)) {
                        System.out.println("Invalid email format.");
                        break;
                    }
                    if (!sms.isValidPhone(phone)) {
                        System.out.println("Invalid phone number format. Use 10-15 digits.");
                        break;
                    }
                    sms.addStudent(new Student(name, roll, grade, email, phone));
                    System.out.println("Student added successfully.");
                    break;
                case "2":
                    System.out.print("Enter roll number to remove: ");
                    String rollToRemove = scanner.nextLine().trim();
                    if (sms.removeStudent(rollToRemove)) {
                        System.out.println("Student removed successfully.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case "3":
                    System.out.print("Enter roll number to search: ");
                    String rollToSearch = scanner.nextLine().trim();
                    Student found = sms.searchStudent(rollToSearch);
                    if (found != null) {
                        System.out.println(found);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case "4":
                    sms.displayAllStudents();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}