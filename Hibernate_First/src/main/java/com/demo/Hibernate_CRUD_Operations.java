package com.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Hibernate_CRUD_Operations {

    private static SessionFactory factory;

    // Initialize the SessionFactory
    public static void getSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            factory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) {
        getSessionFactory();
        Hibernate_CRUD_Operations operations = new Hibernate_CRUD_Operations();

        // Creating Employee objects
        Employee emp1 = new Employee(101, "Charan", "Kumar", 50000);
        Employee emp2 = new Employee(102, "Rahul", "Nivas", 50000);
        Employee emp3 = new Employee(103, "Ravi", "Prakash", 50000);

        // Insert records into the database
        operations.insertIntoDatabase(emp1);
        operations.insertIntoDatabase(emp2);
        operations.insertIntoDatabase(emp3);

        // Display the records from the database
        operations.displayRecords();

        // Display the data using Native SQL
        operations.displayRecords_NativeSQL();

        // Update the record
        operations.updateRecord(102, 55000);

        // Delete record
        operations.deleteRecord(103);

        // Clean up
        factory.close();
    }

    // Inserts a record into the database
    public void insertIntoDatabase(Employee emp) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(emp);
            tx.commit();
            System.out.println("Record inserted successfully: " + emp);
        } catch (Exception ex) {
            System.err.println("Exception during insert: " + ex);
        }
    }

    // Displays the list of records from the database
    public void displayRecords() {
        try (Session session = factory.openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            List<Employee> empList = query.list();
            System.out.println("List of records in the database:");
            for (Employee emp : empList) {
                System.out.println(emp);
            }
        } catch (Exception ex) {
            System.err.println("Exception during display: " + ex);
        }
    }

    // Display the records with Native SQL
    public void displayRecords_NativeSQL() {
        try (Session session = factory.openSession()) {
            String sql = "SELECT * FROM employee WHERE salary = 50000";
            Query<Employee> query = session.createSQLQuery(sql).addEntity(Employee.class);
            List<Employee> empList = query.list();
            System.out.println("List of records with salary 50000:");
            for (Employee emp : empList) {
                System.out.println(emp);
            }
        } catch (Exception ex) {
            System.err.println("Exception during native SQL query: " + ex);
        }
    }

    // Update the record in the database
    public void updateRecord(int id, int salary) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                emp.setSalary(salary);
                session.update(emp);
                tx.commit();
                System.out.println("Record updated successfully: " + emp);
            } else {
                System.out.println("Employee with ID " + id + " not found.");
            }
        } catch (Exception ex) {
            System.err.println("Exception during update: " + ex);
        }
    }

    // Delete a record from the database
    public void deleteRecord(int id) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                session.delete(emp);
                tx.commit();
                System.out.println("Record deleted successfully: " + emp);
            } else {
                System.out.println("Employee with ID " + id + " not found.");
            }
        } catch (Exception ex) {
            System.err.println("Exception during delete: " + ex);
        }
    }
}
