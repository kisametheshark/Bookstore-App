/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528project;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {

    // READS FROM BOOK FILE 
    public ArrayList<Book> readBookFile() throws IOException {

        ArrayList<Book> tempBookList = new ArrayList<>();

        FileReader fr = new FileReader("book.txt");
        Scanner in = new Scanner(fr);

        while (in.hasNext()) {

            // Getting tempBook Information --> Title & Price
            String[] bookInformation = in.nextLine().split(",");
            String title = bookInformation[0];
            double price = Double.parseDouble(bookInformation[1]);

            // Creating & adding tempBook to tempBookList 
            Book tempBook = new Book(title, price);
            tempBookList.add(tempBook);
        }

        // Books to be updated to existing list
        return tempBookList;
    }

    // WRITE TO BOOK FILE
    public void writeBookFile(ArrayList<Book> bookList) throws IOException {
        FileWriter fw = new FileWriter("book.txt", true);

        // Stores bookInformation, then writes to file (for all books in list)
        for (Book book : bookList) {
            String bookInformation = book.getTitle() + ", " + book.getPrice() + "\n";
            fw.write(bookInformation);
        }
        fw.close();
    }

    // READS FROM CUSTOMER FILE
    public ArrayList<Customer> readCustomerFile() throws IOException {

        ArrayList<Customer> tempCustomerList = new ArrayList<>();

        FileReader fr = new FileReader("customer.txt");
        Scanner in = new Scanner(fr);

        while (in.hasNext()) {

            // Getting tempCustomer Information --> username, password, points
            String[] customerInformation = in.nextLine().split(", ");
            String username = customerInformation[0];
            String password = customerInformation[1];
            int points = Integer.parseInt(customerInformation[2]);

            // Creating & adding tempCustomer to tempCustomerList 
            Customer tempCustomer = new Customer(username, password);
            tempCustomer.setPoints(points);
            tempCustomerList.add(tempCustomer);
        }

        // Customers to be updated to existing list
        return tempCustomerList;
    }

    // WRITE TO CUSTOMER FILE
    public void writeCustomerFile(ArrayList<Customer> customerList) throws IOException {
        FileWriter fw = new FileWriter("customer.txt", true);

        // Stores bookInformation, then writes to file (for all books in list)
        for (Customer customer : customerList) {
            String customerInformation = customer.getUsername() + ", " + customer.getPassword() + ", " + customer.getPoints() + "\n";
            fw.write(customerInformation);
        }
        fw.close();
    }

    // Stop appending to bookfile
    public void bookFileReset() throws IOException {
        FileWriter fw = new FileWriter("book.txt", false);
        fw.close();
    }

    // Stop appending to customerfile
    public void customerFileReset() throws IOException {
        FileWriter fw = new FileWriter("customer.txt", false);
        fw.close();
    }
}
