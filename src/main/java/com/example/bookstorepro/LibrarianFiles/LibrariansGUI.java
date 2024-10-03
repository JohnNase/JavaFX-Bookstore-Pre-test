package com.example.bookstorepro.LibrarianFiles;

import com.example.bookstorepro.Bill.BillGenerator;
import com.example.bookstorepro.Database.DB;
import com.example.bookstorepro.LogIn;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

public class LibrariansGUI extends Application{

    //ALERT LAUNCHING VARIABLES

    ArrayList<String> book = new ArrayList<>();
    public static int bookNo = 0;
    ArrayList<String> ISBN = new ArrayList<>();

    //TABLE VIEW AND DATA
    public static ObservableList<ObservableList> data = FXCollections.observableArrayList();
    private static TableView tableview;
    BorderPane librariansPane = new BorderPane();


    //MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    public LibrariansGUI(){
        tableview=new TableView<>();

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



    @Override
    public void start(Stage stage) throws Exception {
        //TableView
        tableview = buildData();

        //CREATE BORDERPANE FOR LIBRARIAN SCREEN UI

        librariansPane.setStyle("-fx-background-color: #FFFAE2; ");

        //ON THE TOP OF THE BORDERPANE WILL BE PLACED A TEXT "HELLO LIBRARIAN" AND HIS/HER PROFILE PIC.
        Label hello = new Label("Hello Librarian!");

        Label loggedInAs = new Label("Logged in as "+ LogIn.getUsername());
        loggedInAs.setAlignment(BOTTOM_RIGHT);
        loggedInAs.setTextAlignment(TextAlignment.RIGHT);
        Font font1 = Font.loadFont(new FileInputStream("lib/Printer.ttf"), 13.5);
        loggedInAs.setFont(font1);
        librariansPane.setBottom(loggedInAs);

        //IMPORT A CUSTOM FONT, TEXT COLOR, TEXT ALIGNMENT
        Font font = Font.loadFont(new FileInputStream("lib/Astrella.ttf"), 40);
        hello.setFont(font);
        hello.setTextFill(Color.WHITE);
        hello.setTextAlignment(TextAlignment.CENTER);
        hello.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0) ;");

        //ADD AN HBOX THAT HOLDS "WELCOME" MESSAGE AND PROFILE PICTURE
        HBox HelloUser = new HBox();
        HelloUser.getChildren().add(hello);
        HelloUser.setPadding(new Insets(20,20,20,20));
        hello.setAlignment(CENTER);
        HelloUser.setStyle("-fx-background-color: #92977E; ");

        //ADD VBOX THAT HOLDS TWO BUTTONS
        VBox leftSide = new VBox(50);
        leftSide.setPadding(new Insets(0,12,12,12));
        leftSide.setStyle("-fx-background-color: #EADDA6; ");

        //CREATE THE BUTTONS
        Button InventoryButton = new Button("Books Table");
        Button BillButton = new Button("Generate Bill");

        //ADD BUTTONS TO VBOX, STYLE BUTTONS
        InventoryButton.setStyle("-fx-color: #FFFAE2; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0) ;");
        InventoryButton.setTranslateY(50);
        BillButton.setStyle("-fx-color: #FFFAE2;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0) ; ");
        BillButton.setTranslateY(100);
        leftSide.getChildren().addAll(InventoryButton,BillButton);

        //CREATE THE MENUBAR

        //ADD THE HBOX TO THE BORDERPANE
        librariansPane.setTop(HelloUser);
        librariansPane.setLeft(leftSide);

        //EVENT HANDLING THE BUTTONS
        InventoryButton.setOnAction(e->{tableview.getColumns().clear();
        data.clear();
        tableview=buildData();
        tableview.refresh();
        librariansPane.setCenter(tableview);});

        BillButton.setOnAction(e-> {
            BillGenerator bill = new BillGenerator();
            Stage testStage = new Stage();
            try {
                bill.start(testStage);
            } catch (FileNotFoundException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

       //CREATE THE ALERT
        lowQuantity();
        getContent();

        StringBuilder contentToAlert = new StringBuilder(); //This StringBuilder will hold all the data for the missing book(s).
        for(int i=0; i< book.size(); i++){
            contentToAlert.append(book.get(i)).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Out Of Stock");
        if(bookNo==1){
            alert.setHeaderText(bookNo+" book Out Of Stock!");
        }
        else if(bookNo>1){
            alert.setHeaderText(bookNo+" books Out Of Stock!");
        }

        alert.setResizable(true);
        alert.setContentText(String.valueOf(contentToAlert));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);

        //Main Scene
        Scene scene = new Scene(librariansPane, 1000,600);
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

                    book.add((bookNo+1)+".\t" + resultSet.getString(1) +"\t" + resultSet.getString(3) + "\t" + resultSet.getString(8));
                    bookNo++;
                }
                resultSet.close();
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public void lowQuantity() {
        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT * FROM booklist WHERE quantity=0";
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