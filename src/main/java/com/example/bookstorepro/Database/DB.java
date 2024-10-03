package com.example.bookstorepro.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    public static Connection getConnection(){
        Connection con = null;
        try{
            String url = "jdbc:mysql://127.0.0.1:3306/booklist";
            String username = "root";
            String password = "sarasara1";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
        return con;
    }

}