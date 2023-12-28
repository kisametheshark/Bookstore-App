/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528project;

import java.util.ArrayList;

public class Customer {

    // Instance variables
    private String membership;
    private String username;
    private String password;
    private int points;
    private double totalCosts;
    private ArrayList<Book> purchasedBooks = new ArrayList<>();

    public Customer(String username, String password) {

        this.username = username;
        this.password = password;

        this.points = 0;
        this.totalCosts = 0;

        if (this.points == 0) {
            this.membership = "Silver";
        }

    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = this.points + points;
        setMembership(this.points);
    }

    public String getMembership() {
        return this.membership;
    }

    public void setMembership(int points) {
        if (points < 1000) {
            this.membership = "Silver";
        } else {
            this.membership = "Gold";
        }
    }
}
