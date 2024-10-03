package com.example.bookstorepro.Bill;

import com.example.bookstorepro.ActionsWithBooks.Book;
import com.example.bookstorepro.Database.DB;
import com.example.bookstorepro.LogIn;
import com.example.bookstorepro.Transaction;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillGenerator extends Application {

    private static final List<Book> books = new ArrayList<>();
    public static final VBox vbox = new VBox();
    public static final VBox totalVbox = new VBox();
    private static final HBox hboxBook = new HBox();
    private static final HBox hboxQuantity = new HBox();
    private static final HBox hboxTitle = new HBox();
    private static final Label lblBook = new Label("Book Name");
    private static final Label lblQuantity = new Label("Quantity");
    private static final TextField tfBook = new TextField();
    private static final TextField tfQuantity = new TextField();
    private static final Button btnNewBook = new Button("New Book");
    private static final Button btnGenerateBill = new Button("Submit Bill");
    private static final Button btnSaveBill = new Button("Save & Print");
    private static final Label lblTotal = new Label("Total:");
    private static final Label lblTotalPrice = new Label();

    private static int BillNumber;

    static {
        try {
            BillNumber = readCounterValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    boolean error = false;
    boolean error2 = false;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, SQLException {


        Label title = new Label("Bill Generator");
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"), 25);
        title.setFont(font);
        title.setStyle("-fx-color:#0000FF;");
        title.setTextAlignment(TextAlignment.CENTER);


        //ADD THE TEXTFIELDS AND LABELS FOR BOOKNAME, QUANTITY, PRICE
        hboxBook.getChildren().addAll(lblBook, tfBook);
        hboxBook.setAlignment(Pos.CENTER);
        hboxQuantity.getChildren().addAll(lblQuantity, tfQuantity);
        hboxQuantity.setAlignment(Pos.CENTER);

        hboxTitle.getChildren().add(title);
        hboxBook.setSpacing(30);
        hboxQuantity.setSpacing(40);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        //ADD THEM ALL TO A VBOX
        vbox.getChildren().addAll(hboxTitle, hboxBook, hboxQuantity);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20, 20, 20, 20));

        //NEW HBOX FOR BUTTONS
        HBox hboxBtns = new HBox();
        hboxBtns.getChildren().addAll(btnNewBook, btnGenerateBill);
        hboxBtns.setSpacing(10);
        hboxBtns.setAlignment(Pos.CENTER);

        HBox hboxSave = new HBox();
        hboxSave.getChildren().addAll(lblTotal, lblTotalPrice);
        hboxSave.setSpacing(10);
        hboxSave.setAlignment(Pos.CENTER);

        btnSaveBill.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(hboxBtns, hboxSave, btnSaveBill);

        //IF 'NEW BOOK' IS CLICKED
        btnNewBook.setOnAction(event -> {
            //CASE 1: EMPTY TEXTFIELDS

            if(tfBook.getText().isEmpty()) {
                tfBook.setStyle("-fx-border-color: red;");
                tfBook.setPromptText("Enter book name");
                error = true;

                //CASE 2: BOOK IS NOT FOUND IN THE DATABASE / DOES NOT EXIST / TYPOS IN WRITING TITLE

            }
            else if(!DoesBookExist(tfBook.getText())) {
                tfBook.setStyle("-fx-border-color: red;");
                final Text actionTarget = new Text();
                actionTarget.setText("Book does not exist.");
                vbox.getChildren().add(actionTarget);
                error = true;
            }

            if(tfQuantity.getText().isEmpty()) {
                tfQuantity.setStyle("-fx-border-color: red;");
                tfQuantity.setPromptText("Enter quantity");
                error = true;
            }


            if(!error) {
                // create the book object and add it to the books list
                Book book = new Book();
                book.setName(tfBook.getText());
                book.setQuantity(Integer.parseInt(tfQuantity.getText()));
                book.setSellPrice(findSellPrice(tfBook.getText()));
                books.add(book);
                tfBook.clear();
                tfQuantity.clear();
                tfBook.setPromptText("");
                tfQuantity.setPromptText("");
            //    totalVbox.getChildren().addAll(MenuBar(), vbox);
                Scene scene = new Scene(totalVbox, 500, 500);
                primaryStage.setScene(scene);
            }
        });


        btnGenerateBill.setOnAction(event -> {

            if(tfBook.getText().isEmpty()) {
                tfBook.setStyle("-fx-border-color: red;");
                tfBook.setPromptText("Enter book name");
                error2 = true;
            }
            else if(!DoesBookExist(tfBook.getText())) {
                error2 = true;
                final Text actionTarget = new Text();
                actionTarget.setText("Book does not exist.");
                vbox.getChildren().add(actionTarget);
            }
            if(tfQuantity.getText().isEmpty()) {
                tfQuantity.setStyle("-fx-border-color: red;");
                tfQuantity.setPromptText("Enter quantity");
                error2 = true;
            }

            if(!error2) {
                Book book = new Book();
                book.setName(tfBook.getText());
                book.setQuantity(Integer.parseInt(tfQuantity.getText()));
                book.setSellPrice(findSellPrice(tfBook.getText()));
                books.add(book);
                calculateTotal();
                try {
                    lowerQuantityInDB(tfBook.getText(), Integer.parseInt(tfQuantity.getText()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnSaveBill.setOnAction(event -> {

            if(tfBook.getText().isEmpty()) {
                tfBook.setStyle("-fx-border-color: red;");
                tfBook.setPromptText("Enter book name");
                error2 = true;
            }
            if(!DoesBookExist(tfBook.getText())) {
                tfBook.setStyle("-fx-border-color: red;");
                final Text actionTarget = new Text();
                actionTarget.setText("Book not found.");
                vbox.getChildren().add(actionTarget);
                error2 = true;
            }
            if(tfQuantity.getText().isEmpty()) {
                tfQuantity.setStyle("-fx-border-color: red;");
                tfQuantity.setPromptText("Enter quantity");
                error2 = true;
            }

            else if(DoesBookExist(tfBook.getText())
                    &&!tfQuantity.getText().isEmpty()
                    &&!tfBook.getText().isEmpty()){
                int totalQuantity = 0;

                tfBook.setStyle("-fx-border-color: green;");
                tfQuantity.setStyle("-fx-border-color: green;");
                try {
                    File file = new File("Bill" + BillNumber+".txt");
                    PrintWriter writer = new PrintWriter(file);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date = new Date();
                    writer.write(formatter.format(date));
                    writer.write("\nlibrarian:"+LogIn.getUsername());
                    writer.write("\n----------------------------------------\n");
                    for(Book book1 : books) {
                        writer.println(book1.getName() + " " + book1.getQuantity() + " " + book1.getSellPrice());
                        totalQuantity=totalQuantity+book1.getQuantity();
                    }
                    writer.println("Total: $" + lblTotalPrice.getText());
                    writer.close();
                   BillNumber++;
                   writeCounterValue();
                    Transaction transaction = new Transaction(LogIn.getUsername(),LocalDate.now(),totalQuantity,Double.parseDouble(lblTotalPrice.getText()));
                    insertTransaction(transaction);
                    final Text actionTarget = new Text();
                    actionTarget.setText("Bill saved and printed successfully.");
                    vbox.getChildren().add(actionTarget);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                tfBook.clear();
                tfQuantity.clear();


            }
        });


        totalVbox.getChildren().addAll(MenuBar(), vbox);
        Scene scene = new Scene(totalVbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static MenuBar MenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemPrint = new MenuItem("Print");

        menuItemPrint.setOnAction(event -> {
            final Text actionTarget = new Text("Fill all the fields!");
            vbox.getChildren().remove(actionTarget);
            if(!(tfQuantity.getText().isEmpty()&&tfBook.getText().isEmpty())) {
                try {
                    File file = new File("Bill" + BillNumber + ".txt");
                    PrintWriter writer = new PrintWriter(file);
                    for(Book book : books) {
                        writer.println(book.getName() + " " + book.getQuantity() + "x " + "$" + book.getSellPrice());
                    }
                    writer.println("Total: " + lblTotalPrice.getText());
                    writer.close();
                    BillNumber++;
                    writeCounterValue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{

                totalVbox.getChildren().add(actionTarget);
            }
        });
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(event -> System.exit(0));
        menuFile.getItems().addAll(menuItemPrint, menuItemExit);
        menuBar.getMenus().add(menuFile);
        return menuBar;
    }

    private static void calculateTotal() {
        double total = 0;
        for(Book book : books) {
            total += book.getSellPrice() * book.getQuantity();
        }
        lblTotalPrice.setText(String.valueOf(total));
    }

    public static Double findSellPrice(String name) {
        double sellPrice = 0;
        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT sellprice FROM booklist WHERE bookname='" + name + "'";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(queryString);
            if(resultSet.next()) {
                sellPrice = resultSet.getDouble("sellprice");
            }
            else {
                System.out.println("Sell price not found because book does not exist.");
                sellPrice = 0.0;
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sellPrice;
    }

    public static void lowerQuantityInDB(String bookName, int book_q) throws SQLException {
        try (Connection connection = DB.getConnection()) {
            String query = "UPDATE booklist" + " SET quantity=quantity-" + book_q + " WHERE quantity>=0 AND  bookname='" + bookName + "'";
            Statement st = connection.createStatement();
            st.execute(query);
        }
    }

    public static boolean DoesBookExist(String bookName) {
        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT bookname FROM booklist WHERE bookname='" + bookName + "'";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(queryString);
            return resultSet.next();

        } catch (Exception e) {
           e.printStackTrace();
        }

        return false;
    }


    public void insertTransaction(Transaction transaction) {
        String sql = "INSERT INTO Transactions (librarianName,quantity,price,dateoftransaction) VALUES (?, ?, ?,?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,LogIn.getUsername());
            preparedStatement.setInt(2,transaction.getQuantity());
            preparedStatement.setDouble(3,transaction.getPrice());
            preparedStatement.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public  static int readCounterValue() throws SQLException {
        int index = 0;
        Connection con = DB.getConnection();
        String queryString = "SELECT cnt from BillCounter";
        System.out.println(queryString);
        Statement st = con.createStatement();
        ResultSet resultSet = st.executeQuery(queryString);
        while(resultSet.next()) {
            index= (resultSet.getInt(1));

        }
        resultSet.close();

        return index;  }

    public static void writeCounterValue()throws SQLException {
        Connection con = DB.getConnection();
        String queryString = "UPDATE BillCounter SET cnt='"+(BillNumber)+"'";
        Statement statement = con.createStatement();
        statement.execute(queryString);
    }


}


/* VERY IMPORTANT!!!!!!
Copyright ©[2023] [John Nase, Sara Berberi]

This program code is the intellectual property of John Nase and Sara Berberi,
and is protected by copyright law. All rights reserved.

This program code may not be reproduced, distributed, or transmitted in any form or by any means,
including photocopying, recording, or other electronic or mechanical methods, without the prior
written permission of us, except in the case of brief quotations embodied in critical reviews
and certain other noncommercial uses permitted by copyright law. By using this program code,
you agree to abide by the terms of this copyright disclaimer. For permission requests or further
inquiries, please contact us.

Github: @sara-berberi @JohnNase

ALL RIGHTS RESERVED ®
 */