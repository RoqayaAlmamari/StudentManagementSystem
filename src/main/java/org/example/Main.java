package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Define constants for database connection
    private static final String URL = "jdbc:mysql://localhost:3306/imsdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                // Display menu options
                System.out.println("1: Insert a new student");
                System.out.println("2: Update an existing student");
                System.out.println("3: Delete a student");
                System.out.println("4: Display all students");
                System.out.println("5: Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        // Prompt user to enter new student information
                        System.out.print("Enter the student name: ");
                        String name = scanner.next();
                        System.out.print("Enter the student email: ");
                        String email = scanner.next();
                        System.out.print("Enter the student age: ");
                        int age = scanner.nextInt();
                        System.out.print("Enter the student major: ");
                        String major = scanner.next();

                        // Prepare and execute SQL statement to insert new student
                        String insertSql = "INSERT INTO students (name, email, age, major) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                        insertStatement.setString(1, name);
                        insertStatement.setString(2, email);
                        insertStatement.setInt(3, age);
                        insertStatement.setString(4, major);
                        int result = insertStatement.executeUpdate();
                        if (result > 0) {
                            System.out.println("Student added successfully!");
                        } else {
                            System.out.println("Error adding student.");
                        }
                        insertStatement.close();
                        break;

                    case 2:
                        // Prompt user to enter updated student information
                        System.out.print("Enter the student ID to update: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter the new student name: ");
                        name = scanner.next();
                        System.out.print("Enter the new student email: ");
                        email = scanner.next();
                        System.out.print("Enter the new student age: ");
                        age = scanner.nextInt();
                        System.out.print("Enter the new student major: ");
                        major = scanner.next();

                        // Prepare and execute SQL statement to update existing student
                        String updateSql = "UPDATE students SET name = ?, email = ?, age = ?, major = ? WHERE id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                        updateStatement.setString(1, name);
                        updateStatement.setString(2, email);
                        updateStatement.setInt(3, age);
                        updateStatement.setString(4, major);
                        updateStatement.setInt(5, id);
                        result = updateStatement.executeUpdate();
                        if (result > 0) {
                            System.out.println("Student updated successfully!");
                        } else {
                            System.out.println("Error updating student.");
                        }
                        updateStatement.close();
                        break;

                    case 3:
                        // Prompt user to enter ID of student to delete
                        System.out.print("Enter the student ID to delete: ");
                        id = scanner.nextInt();
                        // Prepare and execute SQL statement to delete student
                        String deleteSql = "DELETE FROM students WHERE id = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                        deleteStatement.setInt(1, id);
                        result = deleteStatement.executeUpdate();

                        if (result > 0) {
                            System.out.println("Student deleted successfully!");
                        } else {
                            System.out.println("Error deleting student.");
                        }
                        deleteStatement.close();
                        break;
                    // Display all existing student
                    case 4:
                        String selectSql = "SELECT * FROM students";
                        Statement selectStatement = connection.createStatement();
                        ResultSet resultSet = selectStatement.executeQuery(selectSql);
                        List<Student>students = new ArrayList<>();
                        while (resultSet.next()) {
                            int studentId = resultSet.getInt("id");
                            String studentName = resultSet.getString("name");
                            String studentEmail = resultSet.getString("email");
                            int studentAge = resultSet.getInt("age");
                            String studentMajor = resultSet.getString("major");
                            Student student = new Student(studentId, studentName, studentEmail, studentAge, studentMajor);
                            students.add(student);
                        }
                        resultSet.close();
                        selectStatement.close();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            for (Student student : students) {
                                System.out.println(student);
                            }
                        }
                        break;
                    //Close app
                    case 5:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            }
        } catch (SQLException e) {
            //Show error messages
            System.out.println("Database error: " + e.getMessage());
        }
    }

}
