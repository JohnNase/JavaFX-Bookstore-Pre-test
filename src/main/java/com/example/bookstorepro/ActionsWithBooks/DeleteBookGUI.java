package com.example.bookstorepro.ActionsWithBooks;

import com.example.bookstorepro.AdministratorFiles.AdministratorGUI;
import com.example.bookstorepro.Database.DB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

public class DeleteBookGUI extends Application {
    private static TextField bookNameField;
    private static TextField ISBNField;
    static GridPane grid = new GridPane();



    @Override
    public void start(Stage primaryStage) {
        deleteBookInterface(grid);

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void deleteBookInterface(GridPane grid){
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label bookNameLabel = new Label("Book Name:");
        grid.add(bookNameLabel, 0, 0);

        bookNameField = new TextField();
        grid.add(bookNameField, 1, 0);


        Label ISBNLabel = new Label("ISBN:");
        grid.add(ISBNLabel, 0, 2);

        ISBNField = new TextField();
        grid.add(ISBNField, 1, 2);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 0,4);
        Button addButton = new Button("Delete Book");
        addButton.setOnAction(e -> {
                    if(deleteBook(bookNameField.getText(), ISBNField.getText())){
                        actionTarget.setText(":( Book deleted successfully");
                    }
                    else{
                        actionTarget.setText("Failed to delete this book.");
                    }
                }
        );
        grid.add(addButton, 1, 9);

    }

    public static boolean deleteBook(String bookName, String ISBN) {
        int status;
        try (Connection con = DB.getConnection()) {

            PreparedStatement statement = con.prepareStatement( "DELETE FROM booklist WHERE bookname = '"+bookName+"' and ISBN='"+ISBN+"';");

            status = statement.executeUpdate();

            if(status == 1) {
                System.out.println("Book deleted successfully.");
                AdministratorGUI.tableview.refresh();
                return true;
            }

        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
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