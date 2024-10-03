package com.example.bookstorepro;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

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
public class SignUp extends Application {
    public SignUp(){
    }
    //Main pane
    VBox pane = new VBox(10);

    //Grid pane for the elements
    GridPane gridPane = new GridPane();

    //Hbox for button
    HBox hBox_for_button = new HBox(10);

    //Creating the menus
    MenuBar topMenu = new MenuBar();
    Menu optionsMenu = new Menu("Show Options");
    Menu moreMenu = new Menu("More");
    MenuItem showUsers = new MenuItem("Show Users");
    MenuItem exitProgram = new MenuItem("Exit Program");

    //Labels
    Label firstNameLabel = new Label("First Name: ");
    Label lastNameLabel = new Label("Last Name: ");
    Label emailLabel = new Label("Email: ");
    Label usernameLabel = new Label("Username: ");
    Label passwordLabel = new Label("Password: ");
    Label verifyPasswordLabel = new Label("Verify Password: ");
    Label roleLabel = new Label("Role: ");
    Label successLabel = new Label("");

    //Button
    Button signUpButton = new Button("Sign Up");

    //Text fields
    TextField firstNameTF = new TextField();
    TextField lastNameTF = new TextField();
    TextField emailTF = new TextField();
    TextField usernameTF = new TextField();
    PasswordField passwordTF = new PasswordField();
    TextField roleTF = new TextField();
    PasswordField verifyPasswordTF = new PasswordField();

    UserController newUserController = new UserController();

    public void start(Stage primaryStage) {
        read();
        addItemsToPanes();
        addStyling();

        //Creating the scene
        Scene scene = new Scene(pane);

        //Naming the stage and putting the scene into it
        primaryStage.setTitle("Sign Up Program");
        primaryStage.setScene(scene);

        //Showing the stage
        primaryStage.show();

        //Sign Up button functionality
        signUpButton.setOnAction(e -> { addUser();});

        //Exit menu element
        exitProgram.setOnAction(e -> primaryStage.close());

        //Show table menu element
        showUsers.setOnAction(e -> showTable());
    }


    public void addItemsToPanes(){
        //Adding lables to the grid pane
        gridPane.add(firstNameLabel, 0, 0);
        gridPane.add(lastNameLabel, 0, 1);
        gridPane.add(emailLabel, 0, 2);
        gridPane.add(usernameLabel, 0, 3);
        gridPane.add(roleLabel,0,4);
        gridPane.add(passwordLabel, 0, 5);
        gridPane.add(verifyPasswordLabel, 0, 6);

        //Adding button to its pane
        hBox_for_button.getChildren().add(signUpButton);

        //Adding text fields to the grid pane
        gridPane.add(firstNameTF, 1, 0);
        gridPane.add(lastNameTF, 1, 1);
        gridPane.add(emailTF, 1, 2);
        gridPane.add(usernameTF, 1, 3);
        gridPane.add(roleTF,1,4);
        gridPane.add(passwordTF, 1, 5);
        gridPane.add(verifyPasswordTF, 1, 6);

        //Adding Menu elements to MenuBar
        topMenu.getMenus().addAll(optionsMenu, moreMenu);
        optionsMenu.getItems().add(showUsers);
        moreMenu.getItems().add(exitProgram);
        pane.getChildren().addAll(topMenu, gridPane, hBox_for_button, successLabel);
        pane.setStyle("-fx-background-color: #EAE1DF");
    }

    public void addStyling(){
        gridPane.setPadding(new Insets(20, 20, 0, 20));
        hBox_for_button.setAlignment(Pos.CENTER);
        pane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        firstNameTF.setAlignment(Pos.BASELINE_RIGHT);
        lastNameTF.setAlignment(Pos.BASELINE_RIGHT);
        emailTF.setAlignment(Pos.BASELINE_RIGHT);
        usernameTF.setAlignment(Pos.BASELINE_RIGHT);
        roleTF.setAlignment(Pos.BASELINE_RIGHT);
        passwordTF.setAlignment(Pos.BASELINE_RIGHT);
        verifyPasswordTF.setAlignment(Pos.BASELINE_RIGHT);
        successLabel.setAlignment(Pos.CENTER);
    }

    public void addUser(){
        boolean isCreated = newUserController.signUp(firstNameTF.getText(), lastNameTF.getText(), emailTF.getText(),
                usernameTF.getText(), roleTF.getText(), passwordTF.getText(), verifyPasswordTF.getText());
        System.out.println("Creating user: " + isCreated);

        if(isCreated){
            firstNameTF.clear();
            lastNameTF.clear();
            emailTF.clear();
            usernameTF.clear();
            roleTF.clear();
            passwordTF.clear();
            verifyPasswordTF.clear();

            successLabel.setText("User created!");
            newUserController.printUsers();
            write();
        }
        else{
            successLabel.setText("Error! Please fill out all fields!");
        }
    }

    public void showTable(){
        TableView<User> userTable = new TableView<>();
        ObservableList<User> data = FXCollections.observableArrayList(newUserController.getUsers());

        userTable.setItems(data);

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

        userTable.getColumns().addAll(firstNameCol, lastNameCol, emailCol, usernameCol, roleCol);

        Pane pane = new Pane();
        pane.getChildren().add(userTable);
        Scene newScene = new Scene(pane);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void write(){
        try
        {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("History.dat"));
            output.writeObject(newUserController.getUsers());
            output.close();
        }
        catch(IOException e)
        {
            System.out.println("1. " + e);
        }
    }

    public void read(){
        try
        {
            File file = new File("History.dat");
            file.createNewFile();
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            newUserController.setUsers((ArrayList<User>)input.readObject() );
        }
        catch(IOException e)
        {
            System.out.println("2. " + e);
        }
        catch(ClassNotFoundException e1)
        {
            System.out.println("3. " +  e1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}