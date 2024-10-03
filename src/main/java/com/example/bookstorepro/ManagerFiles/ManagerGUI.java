package com.example.bookstorepro.ManagerFiles;

import com.example.bookstorepro.ActionsWithBooks.AddBookGUI;
import com.example.bookstorepro.ActionsWithBooks.AddExistingBookGUI;

import com.example.bookstorepro.Database.DB;
import com.example.bookstorepro.LogIn;
import com.example.bookstorepro.PerformanceGUI;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static javafx.geometry.Pos.BOTTOM_RIGHT;
import static javafx.geometry.Pos.CENTER;

public class ManagerGUI extends Application {

    //ALERT LAUNCHING VARIABLES

    ArrayList<String> book = new ArrayList<>();
    public static int bookNo = 0;
    ArrayList<String> ISBN = new ArrayList<>();

    private static TableView tableview;
    public static ObservableList<ObservableList> data = FXCollections.observableArrayList();

    public ManagerGUI() {
        tableview = new TableView();
    }


    //MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    //CONNECTION DATABASE
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
                //We are using non property style for making dynamic table
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

    public void start(Stage stage) throws Exception {
        //TableView
        tableview=buildData();

        //CREATE BORDERPANE FOR LIBRARIAN SCREEN UI
        BorderPane managersPane = new BorderPane();
        managersPane.setStyle("-fx-background-color: #EAE8DC; ");

        //Panes for AddBook buttons
        GridPane gridForAddBook = new GridPane();
        BorderPane borderPaneForExistingBook = new BorderPane();

        //ON THE TOP OF THE BORDERPANE WILL BE PLACED A TEXT "HELLO LIBRARIAN" AND HIS/HER PROFILE PIC.
        Label hello = new Label("Hello Manager!");

        //IMPORT A CUSTOM FONT, TEXT COLOR, TEXT ALIGNMENT
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"), 40);
        hello.setFont(font);
        hello.setTextFill(Color.WHITE);
        hello.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        hello.setTextAlignment(TextAlignment.CENTER);

        Label loggedInAs = new Label("Logged in as "+ LogIn.getUsername());
        loggedInAs.setAlignment(BOTTOM_RIGHT);
        loggedInAs.setTextAlignment(TextAlignment.RIGHT);
        Font font1 = Font.loadFont(new FileInputStream("lib/Printer.ttf"), 13.5);
        loggedInAs.setFont(font1);
        managersPane.setBottom(loggedInAs);

        //ADD AN HBOX THAT HOLDS "WELCOME" MESSAGE AND PROFILE PICTURE
        HBox HelloUser = new HBox();
        HelloUser.setPadding(new Insets(20,20,20,20));
        HelloUser.getChildren().add(hello);
        hello.setAlignment(CENTER);
        HelloUser.setStyle("-fx-background-color: #6C534E;");

        //ADD VBOX THAT HOLDS TWO BUTTONS
        VBox leftSide = new VBox(50);
        leftSide.setPadding(new Insets(12,12,12,12));
        leftSide.setStyle("-fx-background-color: #DBB3B1; ");

        //CREATE THE BUTTONS
        Button InventoryButton = new Button("Inventory");
        Button AddBookButton = new Button("Add Book");
        Button AddExistingBookButton = new Button("Add old Book");
        Button CheckPerformanceButton = new Button("Check \nPerformance");

        //ADD BUTTONS TO VBOX, STYLE BUTTONS

        InventoryButton.setStyle("-fx-background-color: EAD7D1;\n" + "   -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        InventoryButton.setTranslateY(1);
        AddBookButton.setStyle("-fx-background-color: EAD7D1;\n" + "   -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        AddBookButton.setTranslateY(2);
        AddExistingBookButton.setStyle("-fx-background-color: EAD7D1;\n" + "   -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        AddExistingBookButton.setTranslateY(3);
        CheckPerformanceButton.setStyle("-fx-background-color: EAD7D1;\n" + "   -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        CheckPerformanceButton.setTranslateY(4);
        leftSide.getChildren().addAll(InventoryButton, AddBookButton, AddExistingBookButton, CheckPerformanceButton);


        //ADD THE HBOX TO THE BORDERPANE
        managersPane.setTop(HelloUser);
        managersPane.setLeft(leftSide);

        //EVENT HANDLING THE BUTTONS
        InventoryButton.setOnAction(e-> {
            tableview.getColumns().clear();
            data.clear();
            tableview=buildData();
            tableview.refresh();
            managersPane.setCenter(tableview);
        });


        AddBookButton.setOnAction(e-> {
            try {
                AddBookGUI.AddBookInterface(gridForAddBook);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            managersPane.setCenter(gridForAddBook);
            tableview.refresh();
        });


        AddExistingBookButton.setOnAction(e->{
            try {
                AddExistingBookGUI.addExistingBook(borderPaneForExistingBook);
                managersPane.setCenter(borderPaneForExistingBook);
                tableview.refresh();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        CheckPerformanceButton.setOnAction(e-> managersPane.setCenter(PerformanceGUI.getCheckPerformanceGUI()));

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
        Scene scene = new Scene(managersPane, 1000,600);
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
            e.printStackTrace();        }
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
