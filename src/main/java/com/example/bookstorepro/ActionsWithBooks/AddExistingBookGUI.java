package com.example.bookstorepro.ActionsWithBooks;

import com.example.bookstorepro.Database.DB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;

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
public class AddExistingBookGUI extends Application {

    public static String  existingBookISBN;
    public static String  existingBookAuthor;
    public static String existingBookGenre;

    public static String existingBookSupplier;

    private static TextField bookNameField;
    private static TextField authorField;
    private static TextField ISBNField;
    private static TextField genreField;
    private static TextField quantityField;
    private static TextField supplierField;
    private static DatePicker datePicker;


    public void start(Stage primaryStage) throws FileNotFoundException {
        System.out.println(getClass());
        BorderPane borderPane = new BorderPane();
        addExistingBook(borderPane);
        Scene scene2 = new Scene(borderPane, 500, 500);
        primaryStage.setScene(scene2);
        primaryStage.show();
    }

    public static void addExistingBook(BorderPane borderPane) throws FileNotFoundException {
        Label title = new Label("           Add existing book in Inventory");
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"), 25);
        borderPane.setStyle("-fx-background-color: #F2E9E4; ");
        title.setFont(font);
        title.setStyle("-fx-color:#0000FF;");
        title.setTextAlignment(TextAlignment.CENTER);
        borderPane.setTop(title);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        GridPane bookNamePane = new GridPane();
        bookNamePane.setAlignment(Pos.CENTER);
        bookNamePane.setHgap(10);
        bookNamePane.setVgap(5);
        Label bookNameLabel = new Label("Book Name: ");
        bookNameField = new TextField();
        getContent(bookNameField.getText());
        bookNamePane.add(bookNameLabel, 0, 0);
        bookNamePane.add(bookNameField, 1, 0);

        Button buttonSubmitName = new Button("Submit");
        buttonSubmitName.setStyle("-fx-color: #C9ADA7;");
        bookNamePane.add(buttonSubmitName, 1, 2, 2, 1);
        borderPane.setCenter(bookNamePane);

        Label authorLabel = new Label("Author:");
        grid.add(authorLabel, 0, 1);

        authorField = new TextField();
        grid.add(authorField, 1, 1);

        Label ISBNLabel = new Label("ISBN:");
        grid.add(ISBNLabel, 0, 2);

        ISBNField = new TextField();
        grid.add(ISBNField, 1, 2);

        Label genreLabel = new Label("Genre:");
        grid.add(genreLabel, 0, 3);

        genreField = new TextField();
        grid.add(genreField, 1, 3);

        Label quantityLabel = new Label("Quantity:");
        grid.add(quantityLabel, 0, 4);

        quantityField = new TextField();
        grid.add(quantityField, 1, 4);

        Label buyPriceLabel = new Label("Buy Price:");
        grid.add(buyPriceLabel, 0, 5);

        TextField buyPriceField = new TextField();
        grid.add(buyPriceField, 1, 5);

        Label sellPriceLabel = new Label("Sell Price:");
        grid.add(sellPriceLabel, 0, 6);

        TextField sellPriceField = new TextField();
        grid.add(sellPriceField, 1, 6);

        Label supplierLabel = new Label("Supplier:");
        grid.add(supplierLabel, 0, 7);

        supplierField = new TextField();
        grid.add(supplierField, 1, 7);

        datePicker = new DatePicker();
        grid.add(datePicker, 1, 8);

        buttonSubmitName.setOnAction(event -> {
            getContent(bookNameField.getText());
            fillTextFields();});

        Button addButton = new Button("Add Book");
        addButton.setStyle("-fx-color: #C9ADA7;");
        addButton.setOnAction(e -> UpdateBook(
                Integer.parseInt(quantityField.getText()),
                Double.parseDouble(sellPriceField.getText()),
                Double.parseDouble(buyPriceField.getText()),
                bookNameField.getText()


                )
        );
        grid.add(addButton, 1, 9);

        borderPane.setBottom(grid);

    }

    public static void fillTextFields(){
        genreField.setText(existingBookGenre);
        supplierField.setText(existingBookSupplier);
        ISBNField.setText(existingBookISBN);
        authorField.setText(existingBookAuthor);
        System.out.println("text fields filled");

    }

    public static void main(String[] args) {
        launch(args);
    }
    public static void getContent(String bookname) {

            try (Connection con = DB.getConnection()) {

                String queryString = "SELECT * FROM booklist WHERE bookname='"+bookname+"'";
                System.out.println(queryString);
                Statement st = con.createStatement();
                ResultSet resultSet = st.executeQuery(queryString);

                while(resultSet.next()) {
                    existingBookAuthor=(resultSet.getString(2));
                    existingBookISBN=(resultSet.getString(3));
                    existingBookGenre=(resultSet.getString(4));
                   existingBookSupplier=(resultSet.getString(8));
                    System.out.println("existing book author:"+existingBookAuthor);
                    System.out.println("existing book ISBN:"+existingBookISBN);
                    System.out.println("existing book Genre:"+existingBookGenre);
                    System.out.println("existing book Supplier:"+existingBookSupplier);
                }
                resultSet.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public static void UpdateBook( int quantity, double buyprice, double sellprice, String name) {
        int status;
        try (Connection con = DB.getConnection()) {
            LocalDate localDate = datePicker.getValue();
            String sql = "Update booklist  SET quantity=? , " +
                    "  buyprice=? , sellprice=? ,  datepurchases=?" +
                    "WHERE bookname='"+name+"'";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDouble(2,buyprice);
            preparedStatement.setDouble(3,sellprice);
            preparedStatement.setDate(4, Date.valueOf(localDate));

            status = preparedStatement.executeUpdate();

            if(status == 1) {
                System.out.println("Existing book added successfully.");
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
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

