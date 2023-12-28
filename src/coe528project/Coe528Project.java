/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528project;

import java.io.IOException;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Coe528Project extends Application {

    // Defining Owner, Customer, File Instances 
    private Owner owner = new Owner();
    private Customer currentUser;
    private Files files = new Files();

    //Input TextFields 
    TextField inputUser = new TextField();
    TextField inputPass = new TextField();

    // Buttons
    Button loginButton = new Button("Login");

    Button bookButton = new Button("Books");
    Button customerButton = new Button("Customers");
    Button logoutButton = new Button("Logout");

    Button buyButton = new Button("Buy");
    Button pointBuyButton = new Button("Redeem points and Buy");

    Button backButton = new Button("Back");

    // Used in groups for layout purposes 
    HBox hLayout = new HBox();

    // ViewTables for Book and Customer Lists for javafx (visually)
    TableView<Book> bookTable = new TableView();
    TableView<Customer> customerTable = new TableView();

    // Observable Lists --> used to read from owner collected, uses addBooks to fill TableView lists
    ObservableList<Book> observBooks = FXCollections.observableArrayList();
    ObservableList<Customer> observCustomer = FXCollections.observableArrayList();

    // Add books method for ObserLists
    public ObservableList<Customer> addCustomers() {
        observCustomer.addAll(owner.getCustomers());
        return observCustomer;
    }

    public ObservableList<Book> addBooks() {
        observBooks.addAll(owner.getBooks());
        return observBooks;
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Bookstore App");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loginScreen(false), 700, 630));
        primaryStage.show();

        // Stocking arrays 
        try {
            owner.updateArrays();
        } catch (IOException e) {
            System.out.println("File reading error");
        }

        // Login button functionality 
        loginButton.setOnAction(e -> {
            boolean login = false;

            // Owner is logging in 
            if ((inputUser.getText().equals(owner.getUsername())) && (inputPass.getText().equals(owner.getPassword()))) {
                primaryStage.setScene(new Scene(ownerScreen(), 700, 630));
                login = true;
            }

            // Customer is logging in 
            for (Customer customer : owner.getCustomers()) {
                if ((inputUser.getText().equals(customer.getUsername())) && (inputPass.getText().equals(customer.getPassword()))) {
                    currentUser = customer;
                    primaryStage.setScene(new Scene(customerScreen(0), 700, 630));
                    login = true;
                }
            }

            // Invalid login
            if (!login) {
                primaryStage.setScene(new Scene(loginScreen(true), 700, 630));
            }
        });

        // Logout button functionality 
        logoutButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(loginScreen(false), 700, 630));

            for (Book book : owner.bookList) {
                book.setSelect(new CheckBox());
            }

            inputUser.clear();
            inputPass.clear();
        });

        // Book button functionality
        bookButton.setOnAction(e -> primaryStage.setScene(new Scene(bookTableScreen(), 700, 630)));

        // Customer button functionality
        customerButton.setOnAction(e -> primaryStage.setScene(new Scene(customerTableScreen(), 700, 630)));

        // Back button functionality
        backButton.setOnAction(e -> primaryStage.setScene(new Scene(ownerScreen(), 700, 630)));

        // Point-buy button functionality
        pointBuyButton.setOnAction(e -> {
            boolean bookSelected = false;

            for (Book book : Owner.bookList) {
                if (book.getSelect().isSelected()) {
                    bookSelected = true;
                }
            }

            // If no book is selected, forwarded to customerScreen w/ error 1
            if (bookSelected = false) {
                primaryStage.setScene(new Scene(customerScreen(1), 700, 630));
            } else if (currentUser.getPoints() == 0) { // Book selected, with 0 points, forwarded to customerScreen w/ error 2
                primaryStage.setScene(new Scene(customerScreen(2), 700, 630));
            } else if (currentUser.getPoints() != 0) { // Has points and able to buy, forwarded to checkoutScreen
                primaryStage.setScene(new Scene(checkoutScreen(true), 700, 630));
            }
        });

        // Buy button functionality
        buyButton.setOnAction(e -> {
            boolean bookSelected = false;
            for (Book book : Owner.bookList) {
                if (book.getSelect().isSelected()) {
                    bookSelected = true;
                }
            }

            // If a book was selected
            if (bookSelected) {
                primaryStage.setScene(new Scene(checkoutScreen(false), 700, 630));
            } else {
                // Forward to customerScreen w/ error 1
                primaryStage.setScene(new Scene(customerScreen(1), 700, 630));
            }
        });

        // When closed...
        primaryStage.setOnCloseRequest(e -> {
            try {
                files.bookFileReset();
                files.customerFileReset();
                files.writeBookFile(Owner.bookList);
                files.writeCustomerFile(owner.getCustomers());

            } catch (IOException x) {
                System.out.println("error");
            }
        });
    }

    // LOGIN SCREEN
    public Group loginScreen(boolean loginValid) {

        Group loginPanel = new Group();

        // Login Boxes
        VBox loginBox = new VBox();
        loginBox.setPadding(new Insets(65, 65, 45, 245));
        loginBox.setSpacing(10);

        Text bookstore = new Text("\nBookstore\n");
        bookstore.setFont(new Font("Impact", 50));
        Text user = new Text("Username");
        Text pass = new Text("Password");
        loginButton.setMinWidth(210);
        loginBox.getChildren().addAll(bookstore, user, inputUser, pass, inputPass, loginButton);

        // Error 
        Text error = new Text("Incorrect Username and/or Password");
        if (loginValid) { // Checks the boolean input, if login was invalid 
            loginBox.getChildren().add(error);
        }

        loginPanel.getChildren().addAll(loginBox);

        return loginPanel;
    }

    // CUSTOMER START SCREEN
    public Group customerScreen(int errorNum) {

        Group customerPanel = new Group();

        // Refresh Table
        bookTable.getItems().clear();
        bookTable.getColumns().clear();

        buyButton.setPrefSize(140, 30);
        pointBuyButton.setPrefSize(200, 30);
        logoutButton.setPrefSize(140, 30);

        // Standard Font
        Font font1 = new Font(15);

        // Intro Message
        Text introMsg = new Text("\n\nWelcome " + currentUser.getUsername());
        introMsg.setFont(font1);

        // Status Message
        Text membershipMsg = new Text("\n\nMembership: " + currentUser.getMembership() + " ");
        membershipMsg.setFont(font1);

        // Points Message
        Text pointsMsg = new Text("\n\n\tPoints: " + currentUser.getPoints());
        pointsMsg.setFont(font1);

        // Title Column
        TableColumn<Book, String> titles = new TableColumn<>("Title");
        titles.setMinWidth(200);
        titles.setCellValueFactory(new PropertyValueFactory<>("title")); // Populate Individual Cells 

        // Price Column
        TableColumn<Book, Double> prices = new TableColumn<>("Price");
        prices.setMinWidth(150);
        prices.setCellValueFactory(new PropertyValueFactory<>("price")); // Populate Individual Cells  

        // Select Column
        TableColumn<Book, String> select = new TableColumn<>("Select");
        select.setMinWidth(150);
        select.setCellValueFactory(new PropertyValueFactory<>("select")); // Populate Individual Cells 

        // Creating and Adding Book Information to Respective Columns * KEY
        bookTable.setItems(addBooks());
        bookTable.getColumns().addAll(titles, prices, select);

        // Section for Opening Msg and Status 
        HBox userInfo = new HBox();
        userInfo.getChildren().addAll(membershipMsg, pointsMsg);
        BorderPane header = new BorderPane();
        header.setLeft(introMsg);
        header.setRight(userInfo);

        // Bottom section for Buttons
        HBox bottomButtons = new HBox();
        bottomButtons.setAlignment(Pos.BOTTOM_CENTER);
        bottomButtons.setSpacing(10);
        bottomButtons.getChildren().addAll(logoutButton, buyButton, pointBuyButton);

        // Error Messages
        VBox customerScreen = new VBox();
        String errorMsg = "";

        if (errorNum == 2) { // Attempt to buy with points, with 0 points
            errorMsg = "No Points";
        } else if (errorNum == 1) { // Attempt to buy without selecting any books
            errorMsg = "No book(s) selected";
        }

        Text errorNotification = new Text(errorMsg);
        customerScreen.setSpacing(10);
        customerScreen.setAlignment(Pos.CENTER);
        customerScreen.setPadding(new Insets(40, 200, 30, 100));
        customerScreen.getChildren().addAll(header, bookTable, bottomButtons, errorNotification);

        customerPanel.getChildren().add(customerScreen);
        return customerPanel;
    }

    // CHECKOUT SCREEN
    public Group checkoutScreen(boolean usedPoints) {
        Group checkoutPanel = new Group();
        double total;
        double subtotal = 0;
        double discount;
        int pointsEarned;
        int bookCount = 0;
        int i = 0;

        String[][] booksBought = new String[30][2];

        // Searches through selected books adds them to a list (booksBought), keeping count w/ i
        for (Book book : Owner.bookList) {
            if (book.getSelect().isSelected()) {
                subtotal += book.getPrice();
                booksBought[i][0] = book.getTitle();
                booksBought[i][1] = String.valueOf(book.getPrice());
                i++;
            }
        }

        // If customer decided to play with points, boolean is TRUE, determine discount 
        if (usedPoints) {
            if ((double) currentUser.getPoints() / 100 >= subtotal) {
                discount = subtotal;
                currentUser.setPoints(-(int) subtotal * 100);
            } else {
                discount = ((double) currentUser.getPoints() / 100);
                currentUser.setPoints(-currentUser.getPoints());
            }
        } else {
            discount = 0;
        }

        // Calculating total and discount proportional to it 
        total = subtotal - discount;
        pointsEarned = (int) total * 10;
        currentUser.setPoints(pointsEarned);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);
        header.setPadding(new Insets(0, 0, 25, 0));
        Label brandName = new Label("Bookstore");
        brandName.setFont(new Font("Arial", 35));

        VBox receipt = new VBox();
        receipt.setSpacing(5);
        Text receiptTxt = new Text("Receipt");
        receiptTxt.setFont(new Font("Ariel", 15));
        receipt.getChildren().add(receiptTxt);

        // Adding books from booksBought list to receipt (its items)
        VBox receiptItems = new VBox();
        receiptItems.setSpacing(5);
        for (i = 0; i < 30; i++) {
            if (booksBought[i][0] != null) {
                Text bookTitle = new Text(booksBought[i][0]);
                Text bookPrice = new Text(booksBought[i][1]);
                BorderPane item = new BorderPane();
                item.setLeft(bookTitle);
                item.setRight(bookPrice);
                receiptItems.getChildren().addAll(item);
                bookCount++;
            }
        }

        // Receipt scroll --> if 4 or more books are purchased
        ScrollPane scrollReceipt = new ScrollPane(receiptItems);
        scrollReceipt.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollReceipt.setFitToWidth(true);
        if (bookCount <= 4) {
            scrollReceipt.setFitToHeight(true);
        } else {
            scrollReceipt.setPrefHeight(130);
        }

        // Calculating price, discount, total into individual textboxes
        Text subtotalTxt = new Text("\nSubtotal: $" + (Math.round(subtotal * 100.0)) / 100.0);
        Text pointsDisc = new Text("Points Discount: $" + (Math.round(discount * 100.0)) / 100.0);
        Text totalTxt = new Text("Total: $" + (Math.round(total * 100.0)) / 100.0);
        totalTxt.setFont(new Font("Arial", 15));
        receipt.getChildren().addAll(scrollReceipt, subtotalTxt, pointsDisc, totalTxt);

        VBox bottom = new VBox();
        bottom.setSpacing(40);
        bottom.setAlignment(Pos.CENTER);
        Text info = new Text("\nYou have earned " + pointsEarned + " points, " + "Your current status is " + currentUser.getMembership() + "!" + "\n\t\t\tThank you for your purchase!");
        bottom.getChildren().addAll(info, logoutButton);

        VBox screen = new VBox();
        screen.setPadding(new Insets(110, 105, 500, 165));
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(header, receipt, bottom);

        checkoutPanel.getChildren().addAll(screen);
        Owner.bookList.removeIf(b -> b.getSelect().isSelected()); // REMOVES BOOKS 
        return checkoutPanel;
    }

    // OWNER START SCREEN
    public VBox ownerScreen() {

        bookButton.setPrefSize(300, 75);
        customerButton.setPrefSize(300, 75);
        logoutButton.setPrefSize(300, 75);

        VBox ownerScreen = new VBox();
        ownerScreen.setAlignment(Pos.CENTER);
        ownerScreen.setSpacing(45);
        ownerScreen.setPadding(new Insets(40, 0, 30, 0));

        VBox bottomButtons = new VBox();
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setSpacing(65);
        bottomButtons.getChildren().addAll(customerButton, bookButton, logoutButton);

        ownerScreen.getChildren().addAll(bottomButtons);
        return ownerScreen;
    }

    public Group bookTableScreen() {
        Group bTable = new Group();

        hLayout.getChildren().clear();
        bookTable.getItems().clear();
        bookTable.getColumns().clear();

        Label bTitle = new Label("\n\n\nBooks In Stock");
        bTitle.setFont(new Font("Arial", 20));

        //Book title column
        TableColumn<Book, String> columnTitle = new TableColumn<>("Title");
        columnTitle.setMinWidth(200);
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        //Book price column
        TableColumn<Book, Double> columnPrice = new TableColumn<>("Price");
        columnPrice.setMinWidth(150);
        columnPrice.setStyle("-fx-alignment: CENTER;");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Adding items to columns 
        bookTable.setItems(addBooks());
        bookTable.getColumns().addAll(columnTitle, columnPrice);

        final TextField addbTitle = new TextField();
        addbTitle.setPromptText("Title");
        addbTitle.setMaxWidth(columnTitle.getPrefWidth());

        final TextField addbPrice = new TextField();
        addbPrice.setMaxWidth(columnPrice.getPrefWidth());
        addbPrice.setPromptText("Price");

        VBox structure = new VBox();
        final Button addButton = new Button("Add");

        Label addErrorMsg = new Label("Invalid Input");

        addButton.setOnAction(e -> {
            try {
                // Make and add a new book to the database
                double bPrice = Math.round((Double.parseDouble(addbPrice.getText())) * 100);
                Owner.bookList.add(new Book(addbTitle.getText(), bPrice / 100));

                // Refreshes and clears page
                bookTable.getItems().clear();
                bookTable.setItems(addBooks());

                //clears any text
                addbTitle.clear();
                addbPrice.clear();

                // Clears any invalid actions
                structure.getChildren().remove(addErrorMsg);
            } catch (Exception exception) {
                if (!structure.getChildren().contains(addErrorMsg)) {
                    structure.getChildren().add(addErrorMsg);
                }
            }
        });

        final Button dltButton = new Button("Delete");

        // Deletion process
        dltButton.setOnAction(e -> {
            Book bookSelected = bookTable.getSelectionModel().getSelectedItem();
            bookTable.getItems().remove(bookSelected); // removes visually 
            Owner.bookList.remove(bookSelected);    // removes from list
        });

        hLayout.getChildren().addAll(backButton, addbTitle, addbPrice, addButton, dltButton);
        hLayout.setSpacing(3);
        hLayout.setAlignment(Pos.CENTER);

        structure.setAlignment(Pos.CENTER);
        structure.setSpacing(5);
        structure.setPadding(new Insets(0, 0, 0, 150));
        structure.getChildren().addAll(bTitle, bookTable, hLayout);

        VBox combine = new VBox();
        combine.setPadding(new Insets(0, 200, 60, 25));
        combine.setAlignment(Pos.CENTER);
        combine.getChildren().addAll(structure);

        bTable.getChildren().addAll(combine);

        return bTable;
    }

    public Group customerTableScreen() {
        Group cTable = new Group();

        // Refresh Table
        hLayout.getChildren().clear();
        customerTable.getItems().clear();
        customerTable.getColumns().clear();

        Label customerLabel = new Label("\n\n\nCustomers");
        customerLabel.setFont(new Font("Arial", 20));

        logoutButton.setPrefSize(200, 30);

        // Customer username column
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(150);
        usernameColumn.setStyle("-fx-alignment: CENTER;");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Customer password column
        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(150);
        passwordColumn.setStyle("-fx-alignment: CENTER;");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Customer points column
        TableColumn<Customer, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setMinWidth(100);
        pointsColumn.setStyle("-fx-alignment: CENTER;");
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        customerTable.setItems(addCustomers());
        customerTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);

        // For adding new customers 
        final TextField addCustomerUsername = new TextField();
        addCustomerUsername.setPromptText("User...");
        addCustomerUsername.setMaxWidth(usernameColumn.getPrefWidth());

        final TextField addCustomerPassword = new TextField();
        addCustomerPassword.setPromptText("Pass...");
        addCustomerPassword.setMaxWidth(passwordColumn.getPrefWidth());

        VBox structure = new VBox();
        Text addCustomerError = new Text("Customer already registered");

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            
            boolean dupe = false;

            // Checks dupe customers 
            for (Customer customer : owner.getCustomers()) {
                if (((customer.getUsername().equals(addCustomerUsername.getText())) && (customer.getPassword().equals(addCustomerPassword.getText())))
                        || (addCustomerUsername.getText().equals(owner.getUsername()) && (addCustomerPassword.getText().equals(owner.getPassword())))) {
                    dupe = true;

                    if (dupe) {
                        structure.getChildren().add(addCustomerError);
                    }
                }
            }

            // If customer is not a dupe and valid, add
            if (!(addCustomerUsername.getText().equals("") || addCustomerPassword.getText().equals(""))) {

                if (dupe == false){
                // Adds to real list
                owner.addCustomer(new Customer(addCustomerUsername.getText(), addCustomerPassword.getText()));

                // Update table with real list
                customerTable.getItems().clear();
                customerTable.setItems(addCustomers());

                // Remove error 
                structure.getChildren().remove(addCustomerError);

                // Clear text fields
                addCustomerUsername.clear();
                addCustomerPassword.clear();
                }
            }
        });

        final Button dltButton = new Button("Delete");
        dltButton.setOnAction(e -> {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            // Removes from table
            customerTable.getItems().remove(selectedCustomer);

            // Removes from real list
            owner.removeCustomer(selectedCustomer);
        });

        hLayout.getChildren().addAll(backButton, addCustomerUsername, addCustomerPassword, addButton, dltButton);
        hLayout.setAlignment(Pos.CENTER);
        hLayout.setSpacing(5);

        structure.setAlignment(Pos.CENTER);
        structure.setSpacing(5);
        structure.setPadding(new Insets(0, 0, 0, 110));
        structure.getChildren().addAll(customerLabel, customerTable, hLayout);

        VBox combined = new VBox();
        combined.setPadding(new Insets(0, 150, 60, 40));
        combined.getChildren().addAll(structure);
        combined.setAlignment(Pos.CENTER);

        cTable.getChildren().add(combined);
        return cTable;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
