package com.example.bookstorepro.ManagerFiles;

import com.example.bookstorepro.Database.DB;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SoldOutAlertManager extends Application {
    ArrayList<String> book = new ArrayList<>();
    public static int bookNo = 0;
    ArrayList<String> ISBN = new ArrayList<>();
    @Override
    public void start(Stage stage) throws Exception {

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
        if(contentToAlert.isEmpty()){
            alert.setContentText("No Books with stock quantity less than 5.");
        }
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
        alert.showAndWait();
    }

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
            System.out.println(e);
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
            System.out.println(e);
        }
    }
    }
