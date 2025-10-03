import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// --------------------- Student Class ---------------------
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    String studentID;
    String name;
    String grade;

    public Student(String studentID, String name, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    public void display() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Grade: " + grade);
    }
}

// --------------------- Employee Class ---------------------
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    String id, name, designation;
    double salary;

    public Employee(String id, String name, String designation, double salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.salary = salary;
    }

    public void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Designation: " + designation + ", Salary: " + salary);
    }
}

// --------------------- Helper for Appending Objects ---------------------
class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();
    }
}

// --------------------- Main Program ---------------------
public class practice1 {
    private static final String STUDENT_FILE = "student.dat";
    private static final String EMPLOYEE_FILE = "employees.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Java Tasks Menu ===");
            System.out.println("1. Sum of Integers (Autoboxing/Unboxing)");
            System.out.println("2. Student Serialization/Deserialization");
            System.out.println("3. Employee Management System");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    sumOfIntegers(sc);
                    break;
                case 2:
                    studentSerialization(sc);
                    break;
                case 3:
                    employeeManagement(sc);
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);

        sc.close();
    }

    // --------------------- Part A: Sum of Integers ---------------------
    public static void sumOfIntegers(Scanner sc) {
        ArrayList<Integer> numbers = new ArrayList<>();
        System.out.println("Enter integers separated by spaces:");
        String input = sc.nextLine();
        String[] inputs = input.split("\\s+");

        for (String str : inputs) {
            try {
                Integer num = Integer.parseInt(str); // Autoboxing
                numbers.add(num);
            } catch (NumberFormatException e) {
                System.out.println(str + " is not a valid integer. Skipping.");
            }
        }

        int sum = 0;
        for (Integer num : numbers) { // Unboxing
            sum += num;
        }

        System.out.println("Total sum: " + sum);
    }

    // --------------------- Part B: Student Serialization ---------------------
    public static void studentSerialization(Scanner sc) {
        System.out.print("Enter Student ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Grade: ");
        String grade = sc.nextLine();

        Student student = new Student(id, name, grade);

        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENT_FILE))) {
            oos.writeObject(student);
            System.out.println("Student object serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENT_FILE))) {
            Student s = (Student) ois.readObject();
            System.out.println("Deserialized Student Data:");
            s.display();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // --------------------- Part C: Employee Management ---------------------
    public static void employeeManagement(Scanner sc) {
        int choice;
        do {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addEmployee(sc);
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 3);
    }

    public static void addEmployee(Scanner sc) {
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Designation: ");
        String designation = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();
        sc.nextLine();

        Employee emp = new Employee(id, name, designation, salary);

        try {
            ObjectOutputStream oos;
            File file = new File(EMPLOYEE_FILE);
            if (file.exists() && file.length() > 0) {
                oos = new AppendableObjectOutputStream(new FileOutputStream(file, true));
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(file));
            }
            oos.writeObject(emp);
            oos.close();
            System.out.println("Employee added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMPLOYEE_FILE))) {
            System.out.println("\n--- Employee Records ---");
            while (true) {
                Employee emp = (Employee) ois.readObject();
                emp.display();
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No employee records found or file is empty.");
        }
    }
}
