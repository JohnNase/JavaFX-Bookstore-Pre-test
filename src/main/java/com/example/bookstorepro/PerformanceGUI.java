package com.example.bookstorepro;

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
import com.example.bookstorepro.Database.DB;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PerformanceGUI extends Application {

    public static TableView<ObservableList> tblTransactionData;

    public static SplitPane getCheckPerformanceGUI() {
        SplitPane mainPane = new SplitPane();
        Button btnTransactions = new Button("Transactions");
        Button btSubmit = new Button("Submit");
        Button btnOtherUser = new Button(" Check Other User");
        Button btnChart = new Button("Generate Chart");
        Label lblUsername = new Label("Username: ");
        Label lblStartDate = new Label("Start Date: ");
        Label lblEndDate = new Label("End Date: ");
        TextField txtUsername = new TextField();
        DatePicker dpStartDate = new DatePicker();
        DatePicker dpEndDate = new DatePicker();
        Label profit = new Label("Profit: "+getRevenueFromDatabase());
        profit.setStyle("-fx-font-weight: bold;");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(8,8,8,8));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(btnTransactions, 0, 0);
        gridPane.add(btnOtherUser, 1, 0);
        gridPane.add(profit,2,0);
        gridPane.add(lblUsername, 0, 1);
        gridPane.add(txtUsername, 1, 1);
        gridPane.add(lblStartDate, 0, 2);
        gridPane.add(dpStartDate, 1, 2);
        gridPane.add(lblEndDate, 0, 3);
        gridPane.add(dpEndDate, 1, 3);
        gridPane.add(btnChart,0,5);
        gridPane.add(btSubmit,0,6);


        mainPane.getItems().add(gridPane);
        mainPane.setOnSwipeLeft(gridPane.getOnSwipeLeft());


        final Text actionTarget = new Text("Fill all the fields!");
        final Text actionTarget2 = new Text("Date Mismatch");

        btnChart.setOnAction(s->{
            mainPane.getItems().removeIf(node -> node instanceof BarChart<?,?>);
            mainPane.getItems().add(BarChartExample.buildChart());
        });


        btnTransactions.setOnAction(e->{
            gridPane.getChildren().remove(actionTarget);
            tblTransactionData = new TableView<>();
            buildData();
            gridPane.add(tblTransactionData, 0, 4, 2, 1);

        });

        AtomicBoolean notFilled = new AtomicBoolean(false);
        AtomicBoolean error = new AtomicBoolean(false);

        btSubmit.setOnAction(e -> {
            tblTransactionData.getItems().clear();
            buildData();
           // gridPane.getChildren().remove(tblTransactionData);
            GridPane paneResults;
            String startDate;
            String endDate;


            //KONTROLLO USERNAMES

            //CASE 1: NESE USERNAME FIELD ESHTE EMPTY
            if(txtUsername.getText().isEmpty()) {
                txtUsername.setStyle("-fx-border-color: red;");
                txtUsername.setPromptText("Enter username");
                notFilled.set(true);

            }

            //NESE END DATE ESHTE EMPTY
            if(dpEndDate.getValue() == null) {
                dpEndDate.setStyle("-fx-border-color: red;");
                notFilled.set(true);

            }

            //NESE START DATE ESHTE EMPTY
            if(dpStartDate.getValue() == null) {
                dpStartDate.setStyle("-fx-border-color: red;");
                notFilled.set(true);

            }

            //END DATE ME E VOGEL SE START DATE
            if(dpStartDate.getValue() != null && dpEndDate.getValue() != null) {
                if(dpStartDate.getValue().compareTo(dpEndDate.getValue()) > 0) {
                    dpEndDate.setStyle("-fx-border-color: red;");
                    dpStartDate.setStyle("-fx-border-color: red;");
                    actionTarget2.setText("Date mismatch");
                    gridPane.add(actionTarget2, 0, 7);
                    error.set(true);
                }
            }
            gridPane.getChildren().remove(actionTarget2);

            if(notFilled.get()&&!error.get()){
                gridPane.add(actionTarget,0,7);
            }

            if(!notFilled.get() && !error.get()) {
                gridPane.getChildren().remove(tblTransactionData);
                dpEndDate.setStyle("-fx-border-color: green;");
                dpStartDate.setStyle("-fx-border-color: green;");
                txtUsername.setStyle("-fx-border-color: green;");
                startDate = dpStartDate.getValue().toString();
                endDate = dpEndDate.getValue().toString();
                paneResults=getQuantityAndPrice(startDate, endDate, txtUsername.getText());
                gridPane.add(paneResults, 0, 4, 2, 1);

                btnOtherUser.setOnAction(l-> {
                    dpEndDate.setStyle("-fx-border-color: grey;");
                    dpStartDate.setStyle("-fx-border-color: grey;");
                    txtUsername.setStyle("-fx-border-color: grey;");
                    gridPane.getChildren().remove(paneResults);
                    txtUsername.clear();
                    dpEndDate.setValue(null);
                    dpStartDate.setValue(null);
                });

            }




        });



        return mainPane;
    }

    public static void buildData() {
        Connection c = null;

         ObservableList<ObservableList> data = FXCollections.observableArrayList();
        //TABLE VIEW AND DATA

        try {
            c = DB.getConnection();
            //SQL FOR SELECTING THE WHOLE TABLE FROM Transactions
            String SQL = "SELECT * from Transactions";
            //ResultSet
            ResultSet rs = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(SQL);

            //DYNAMICALLY ADDED TABLE COLUMN
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tblTransactionData.getColumns().addAll(col);
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
            tblTransactionData.setItems(data);

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
            System.out.println("Data size: " + data.size());
            System.out.println(data);
        }
    }

    public static GridPane getQuantityAndPrice(String startDate, String endDate,String librarianName) {
        Connection c = null;
        String totalQuantity = "";
        String totalPrice = "";

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(8, 8, 8, 8));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        try {
            c = DB.getConnection();
            String SQL = "SELECT SUM(Quantity) as TotalQuantity, " +
                    "SUM(Price) as TotalPrice " +
                    "from Transactions" +
                    " where DateOfTransaction >= '" + startDate + "' " +
                    "AND DateOfTransaction <= '" + endDate +
                    "'AND librarianName='"+librarianName+"'";
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                totalQuantity = rs.getInt("TotalQuantity") + " books";
                totalPrice = "$" + rs.getDouble("TotalPrice");

                System.out.println(totalPrice);
                System.out.println(totalQuantity);
            }
        } catch (SQLException e) {
            System.out.println("Error building data: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Label lblQuantity = new Label("Books sold in total: ");
        Label lblPrice = new Label("Total Income:   ");
        Label lblQuantityValue = new Label(totalQuantity);
        Label lblPriceValue = new Label(totalPrice);

        gridPane.add(lblQuantity, 0, 0);
        gridPane.add(lblQuantityValue, 1, 0);
        gridPane.add(lblPrice, 0, 1);
        gridPane.add(lblPriceValue, 1, 1);
     //   gridPane.getChildren().removeAll(lblPriceValue,lblPrice,lblQuantityValue,lblQuantityValue);

        return gridPane;
    }


    @Override
    public void start(Stage stage) throws Exception {

        tblTransactionData = new TableView<>();
        SplitPane pane = new SplitPane();
        getCheckPerformanceGUI().getItems().add(tblTransactionData);
        pane.getItems().add(getCheckPerformanceGUI());

        Scene scene = new Scene(pane, 1000,600);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args){
        launch();
    }

    private static String getRevenueFromDatabase() {
        Connection c = DB.getConnection();
        try {
            String SQL = "SELECT SUM(price) FROM transactions";
            ResultSet rs = c.createStatement().executeQuery(SQL);

            while (rs.next()) {
                double totalRevenue = rs.getDouble(1);
                return "$" + totalRevenue;
            }
        } catch (SQLException e) {
            System.out.println("Error building data: " + e.getMessage());
        }
        return "$0.00";
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