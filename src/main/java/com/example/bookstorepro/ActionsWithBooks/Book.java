package com.example.bookstorepro.ActionsWithBooks;

import com.example.bookstorepro.Database.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

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
public class Book {
    private String bookName, author, ISBN,genre,supplier;
    private int quantity = 0;
    private double buyPrice , sellPrice ;
    private Date datePurchased;

    //constructor that inserts all the data to database and also in the variables inside the class
    public Book(String bookName, String author, String ISBN, String genre, double quantity, double buyPrice, double sellPrice, String supplier, Date datePurchased) {

        try (Connection con = DB.getConnection()) {
            Statement st = con.createStatement();
            Date dateEnter = new Date();
            ResultSet resultSet = st.executeQuery("insert into bookList(bookName,Author,ISBN,genre,quantity,buyPrice,sellPrice,supplier,datePurchases)" + " values(?,?,?,?,?,"+dateEnter+")");
            this.bookName = resultSet.getString(1);
            this.author = resultSet.getString(2);
            this.ISBN = resultSet.getString(3);
            this.genre=resultSet.getString(4);
            this.quantity = resultSet.getInt(5);
            this.buyPrice =resultSet.getInt(6);
            this.sellPrice=resultSet.getInt(7);
            this.supplier=resultSet.getString(8);
            this.datePurchased=resultSet.getDate(9);



        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Book(){

    }

    //getters
    public String getName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }


    //setters
    public void setName(String bookName) {
        this.bookName = bookName;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getGenre() {
        return genre;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }



    //searching by book name------function to check if a book is available



    //searching by ISBN------function to check if a book is available


    //by ISBN------function to delete a book

    public static void deleteBook(String ISBN) {
        int status = 0;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM booklist WHERE ISBN='" + ISBN + "';");
            preparedStatement.setInt(3, Integer.parseInt(ISBN));
            status = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showBookList() {
        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT * FROM booklist";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(queryString);
            while(resultSet.next()) {
                System.out.println("\t" + resultSet.getString(1) + "\t" + "\t" + resultSet.getString(2) + "\t" + "\t" + resultSet.getString(3));

            }
        } catch (Exception e) {
            System.out.println(e);
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


