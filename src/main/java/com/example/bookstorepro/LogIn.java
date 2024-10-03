package com.example.bookstorepro;
import com.example.bookstorepro.AdministratorFiles.AdministratorGUI;
import com.example.bookstorepro.LibrarianFiles.LibrariansGUI;
import com.example.bookstorepro.ManagerFiles.ManagerGUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

public class LogIn extends Application {
    private static String username;
    private String password;
    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome!");
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"),23);
        sceneTitle.setId("welcome-text");
        sceneTitle.setFont(font);
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Log in");
        grid.add(btn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);
        grid.setStyle("-fx-background-color: #EAE1DF");

        pwBox.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                btn.fire();
            }
        });

        btn.setOnAction(e -> {
             username = userTextField.getText();
             password = pwBox.getText();

            try {
                ReadData.read();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            for(int i = 0; i < ReadData.users.size(); i++){
                //test if username and password inputted match those of the existing users
                if (username.equals(ReadData.usernames.get(i)) && password.equals(ReadData.passwords.get(i))) {
                    actionTarget.setText("Sign in successful!");
                    if(ReadData.roles.get(i).equals("Librarian")){ //if the role is librarian bring up the librarian GUI
                        LibrariansGUI librarian = new LibrariansGUI();
                        try {
                            librarian.start(new Stage());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    else if(ReadData.roles.get(i).equals("Manager")){ //if the role is manager bring up the manager GUI
                        ManagerGUI manager = new ManagerGUI();
                        try {
                            manager.start(new Stage());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    else{
                        AdministratorGUI admin = new AdministratorGUI(); //the role is administrator, so we bring up the administrator GUI
                        try {
                            admin.start(new Stage());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                }
                else actionTarget.setText("Sign in failed!");
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String getUsername() {
        return username;
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