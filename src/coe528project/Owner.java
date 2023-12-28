/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528project;

import java.util.ArrayList;
import java.io.IOException;

public class Owner {

    protected static ArrayList<Book> bookList = new ArrayList<>();
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static Files files = new Files();

    private final String username = "admin";
    private final String password = "admin";

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customerList.remove(customer);
    }

    public ArrayList<Customer> getCustomers() {
        return (ArrayList<Customer>) customerList.clone();
    }

    public ArrayList<Book> getBooks() {
        return (ArrayList<Book>) bookList.clone();
    }

    public void updateArrays() throws IOException {
        ArrayList<Book> tempBookList = files.readBookFile();
        ArrayList<Customer> tempCustomerList = files.readCustomerFile();

        // Adds tempBooks to main bookList 
        for (Book book : tempBookList) {
            bookList.add(book);
        }

        // Adds tempCustomers to main customerList 
        for (Customer customer : tempCustomerList) {
            customerList.add(customer);
        }
    }
}
