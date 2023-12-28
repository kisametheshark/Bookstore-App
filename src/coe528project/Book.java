/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528project;

import javafx.scene.control.CheckBox;

public class Book {

    private double bookPrice;
    private String bookTitle;
    public CheckBox select;

    public Book(String bookTitle, double bookPrice) {
        this.bookPrice = bookPrice;
        this.bookTitle = bookTitle;
        select = new CheckBox();
    }

    public double getPrice() {
        return this.bookPrice;
    }

    public String getTitle() {
        return this.bookTitle;
    }

    public CheckBox getSelect() {
        return this.select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
}
