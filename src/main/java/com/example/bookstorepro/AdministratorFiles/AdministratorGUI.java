package com.example.bookstorepro.AdministratorFiles;

import com.example.bookstorepro.ActionsWithBooks.AddBookGUI;
import com.example.bookstorepro.ActionsWithBooks.AddExistingBookGUI;
import com.example.bookstorepro.ActionsWithBooks.DeleteBookGUI;
import com.example.bookstorepro.Database.DB;
import com.example.bookstorepro.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.geometry.Pos.BOTTOM_RIGHT;
import static javafx.geometry.Pos.CENTER;

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
public class AdministratorGUI extends Application {

    //CREATE BORDERPANE FOR LIBRARIAN SCREEN UI
    BorderPane administratorsPane = new BorderPane();
    //ALERT LAUNCHING VARIABLES
    ArrayList<String> book = new ArrayList<>();
    public static int bookNo = 0;
    ArrayList<String> ISBN = new ArrayList<>();

    public static TableView tableview;
    public static ObservableList<ObservableList> data = FXCollections.observableArrayList();

    //MAIN EXECUTOR
    public static void main(String[] args) {


        launch(args);
    }

    public static TableView buildData() {
        Connection c = null;
        //TABLE VIEW AND DATA

        try {
            c = DB.getConnection();
            //SQL FOR SELECTING THE WHOLE TABLE FROM BOOKLIST
            String SQL = "SELECT * from booklist";
            //ResultSet
            ResultSet rs = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(SQL);

            //DYNAMICALLY ADDED TABLE COLUMN
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tableview.getColumns().addAll(col);
               System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch (SQLException e) {
            System.out.println("Error building data: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
   return tableview;  }

    public AdministratorGUI() {
        tableview = new TableView();
    }


    public void start(Stage stage) throws Exception {

        tableview=buildData();

        System.out.println(ReadData.users);
        //TableView
        GridPane gridForAddBook = new GridPane();
        BorderPane borderPaneForExistingBook = new BorderPane();
        GridPane gridPaneforDeleteBook = new GridPane();
        administratorsPane.setStyle("-fx-background-color: #EAE8DC; ");

        //ON THE TOP OF THE BORDERPANE WILL BE PLACED A TEXT "HELLO LIBRARIAN" AND HIS/HER PROFILE PIC.
        Label hello = new Label("Hello Administrator!");
        Label loggedInAs = new Label("Logged in as "+LogIn.getUsername());
        loggedInAs.setAlignment(BOTTOM_RIGHT);
        loggedInAs.setTextAlignment(TextAlignment.RIGHT);
        Font font1 = Font.loadFont(new FileInputStream("lib/Printer.ttf"), 13.5);
        loggedInAs.setFont(font1);
        administratorsPane.setBottom(loggedInAs);


        //IMPORT A CUSTOM FONT, TEXT COLOR, TEXT ALIGNMENT
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"), 40);
        hello.setFont(font);
        hello.setTextFill(Color.WHITE);
        hello.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        hello.setTextAlignment(TextAlignment.CENTER);

        //Profit

        //ADD AN HBOX THAT HOLDS "WELCOME" MESSAGE AND PROFILE PICTURE
        HBox HelloUser = new HBox();
        HelloUser.setPadding(new Insets(10,10,10,10));
        HelloUser.getChildren().addAll(hello);
        hello.setAlignment(CENTER);
        HelloUser.setStyle("-fx-background-color: #011936; ");



        //ADD VBOX THAT HOLDS BUTTONS
        VBox leftSide = new VBox(50);
        leftSide.setPadding(new Insets(12,12,12,12));
        leftSide.setStyle("-fx-background-color: #D5CAD6; ");

        //CREATE THE BUTTONS
        Button InventoryButton = new Button("Inventory");
        Button AddBookButton = new Button("Add Book");
        Button AddExistingBookButton = new Button("Add old Book");
        Button DeleteBookButton = new Button("Delete Book");
        Button CheckPerformanceButton = new Button("Check Performance");
        Button ManageUsersButton = new Button("Manage Users");
        Button AddUserButton = new Button("Add User");
        Button ProfitButton = new Button("Profit");

        //ADD BUTTONS TO VBOX, STYLE BUTTONS
        InventoryButton.setStyle("-fx-color: #EDEBD7; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        InventoryButton.setTranslateY(1);
        AddBookButton.setStyle("-fx-color: #EDEBD7;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        AddBookButton.setTranslateY(2);
        AddExistingBookButton.setStyle("-fx-color: #EDEBD7;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        AddExistingBookButton.setTranslateY(3);
        CheckPerformanceButton.setStyle("-fx-color: #EDEBD7; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        CheckPerformanceButton.setTranslateY(4);
        ManageUsersButton.setStyle("-fx-color: #EDEBD7;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        ManageUsersButton.setTranslateY(5);
        AddUserButton.setStyle("-fx-color: #EDEBD7;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        AddUserButton.setTranslateY(6);
        DeleteBookButton.setStyle("-fx-color: #EDEBD7;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        DeleteBookButton.setTranslateY(7);
        leftSide.getChildren().addAll(InventoryButton, AddBookButton,AddExistingBookButton, DeleteBookButton, ManageUsersButton, AddUserButton,CheckPerformanceButton);

        //ADD THE HBOX TO THE BORDERPANE
        administratorsPane.setTop(HelloUser);
        administratorsPane.setLeft(leftSide);

        //EVENT HANDLING THE BUTTONS
        AddBookButton.setOnAction(e-> {
            try {
                AddBookGUI.AddBookInterface(gridForAddBook);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            administratorsPane.setCenter(gridForAddBook);
            tableview.refresh();
        });
        DeleteBookButton.setOnAction(e->{
            DeleteBookGUI.deleteBookInterface(gridPaneforDeleteBook);
            administratorsPane.setCenter(gridPaneforDeleteBook);
            tableview.refresh();

        });
        AddExistingBookButton.setOnAction(e->{
            try {
                AddExistingBookGUI.addExistingBook(borderPaneForExistingBook);
                administratorsPane.setCenter(borderPaneForExistingBook);
                tableview.refresh();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        InventoryButton.setOnAction(e-> {
            tableview.getColumns().clear();
            data.clear();
            tableview = buildData();
            tableview.refresh();
            administratorsPane.setCenter(tableview);
        });
       CheckPerformanceButton.setOnAction(e-> administratorsPane.setCenter(PerformanceGUI.getCheckPerformanceGUI()));

        //Show Users Button Functionality
        ManageUsersButton.setOnAction(e->{
            UserController newUserController = new UserController();
            //reading the file to get the users' data
            try
            {
                File file = new File("History.dat");
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                newUserController.setUsers((ArrayList<User>)input.readObject() );
            }
            catch(IOException ee)
            {
                System.out.println("2. " + e);
            }
            catch(ClassNotFoundException e1)
            {
                System.out.println("3. " +  e1);
            }
            //creating a copy of the showUsers table
            TableView<User> userTable = new TableView<>();
            AtomicReference<ObservableList<User>> data = new AtomicReference<>(FXCollections.observableArrayList(newUserController.getUsers()));
            System.out.println("0000"+data);
            userTable.setItems(data.get());

            TableColumn firstNameCol = new TableColumn("First Name");
            TableColumn lastNameCol = new TableColumn("Last Name");
            TableColumn emailCol = new TableColumn("Email");
            TableColumn usernameCol = new TableColumn("Username");
            TableColumn roleCol = new TableColumn("Role");

            firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
            emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
            usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
            roleCol.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

            //add columns to table
            userTable.getColumns().addAll(firstNameCol, lastNameCol, emailCol, usernameCol, roleCol);
            System.out.println("data before:"+data.get());
            //Button for deleting an existing user
            Button deleteUserButton = new Button("Delete User");
            deleteUserButton.setOnAction(ee -> {
                User selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    newUserController.deleteUser(selectedUser);
                    data.get().remove(selectedUser);
                    // update the data stored in the file after deletion
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("History.dat"));
                        output.writeObject(newUserController.getUsers());
                        output.close();
                    } catch (IOException eee) {
                        System.out.println("Error writing to file: " + eee);
                    }
                    userTable.refresh();
                    newUserController.saveData();
                }
            });
            //Button for changing the role of an existing user
            Button changeRoleButton = new Button("Change Role");
            System.out.println("data before changing role:"+data.get());
            changeRoleButton.setOnAction(ee -> {
                User selectedUser = userTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    // Dialog box to select the desired role
                    ChoiceDialog<String> dialog = new ChoiceDialog<>("Librarian", "Librarian", "Manager", "Administrator");
                    dialog.setTitle("Change Role");
                    dialog.setHeaderText("Select the desired role:");
                    dialog.setContentText("Role:");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        String selectedRole = result.get();
                        selectedUser.setRole(selectedRole);
                        newUserController.updateRole(selectedUser, selectedRole);
                        try {
                            ReadData.updateUserRole(selectedUser.getUsername(), selectedRole);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println(ReadData.users);
                        // update the data stored in the file after changing the role
                        try {
                            List<User> userList = newUserController.getUsers();
                            FileOutputStream fileOutputStream = new FileOutputStream("History.dat");
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(userList);
                            objectOutputStream.close();
                            fileOutputStream.close();
                            data.set(FXCollections.observableArrayList(userList));
                            System.out.println("data while changing role:"+data.get());
                        } catch (IOException eee) {
                            System.out.println("Error writing to file: " + eee);
                        }
                        System.out.println("data after changing role:"+data.get());

                        userTable.refresh();
                        newUserController.saveData();
                    }
                }
            });
            //show the table, Delete User Button and Change Role Button
            BorderPane view = new BorderPane();

            VBox buttonBox = new VBox();
            buttonBox.getChildren().addAll(deleteUserButton, changeRoleButton);
            buttonBox.setAlignment(CENTER);
            buttonBox.setSpacing(20);
            view.setCenter(userTable);
            view.setRight(buttonBox);
            administratorsPane.setCenter(view);
        });

        //AddUserButton functionality
        AddUserButton.setOnAction(e->{
            SignUp AddUser = new SignUp();
            Stage SignUpStage = new Stage();
            AddUser.start(SignUpStage);
        });

        //CREATE THE ALERT
        lowQuantityManager();
        getContent();

        StringBuilder contentToAlert = new StringBuilder(); //This StringBuilder will hold all the data for the missing book(s).
        for(int i=0; i< book.size(); i++){
            contentToAlert.append(book.get(i)).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Manager Notification | Inventory ");
        alert.setHeaderText("Might become sold out soon!");

        alert.setResizable(true);
        alert.setContentText(String.valueOf(contentToAlert));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);


        //Main Scene
        Scene scene = new Scene(administratorsPane, 1000,650);
        tableview.setEditable(true);

        stage.setScene(scene);
        stage.show();

        //ALERT SHOULD POP UP WHEN ISBN ARRAYLIST ISN'T EMPTY, WHICH MEANS THERE ARE BOOKS THAT ARE SOLD OUT:
        if(!ISBN.isEmpty()){
            alert.showAndWait();
        }
    }

    //ALERT FUNCTIONS
    public void getContent() {
        try (Connection con = DB.getConnection()) {
            for(int i=0; i<ISBN.size();i++) {
                String queryString = "SELECT * FROM booklist WHERE ISBN='" + ISBN.get(i) + "'";
                Statement st = con.createStatement();
                ResultSet resultSet = st.executeQuery(queryString);


                while(resultSet.next()) {

                    book.add((bookNo+1)+".\t" + resultSet.getString(1) +"\t" + resultSet.getString(3) +"\t" + resultSet.getString(5)+"x " + "\t" + resultSet.getString(8));
                    bookNo++;
                }
                resultSet.close();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void lowQuantityManager(){
        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT * FROM booklist WHERE quantity<5 AND QUANTITY>0";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(queryString);
            ISBN = new ArrayList<>();
            while(resultSet.next()) {
                ISBN.add(resultSet.getString(3));
            }
            resultSet.close();

        } catch (Exception e) {
           e.printStackTrace();
        }
    }



}
