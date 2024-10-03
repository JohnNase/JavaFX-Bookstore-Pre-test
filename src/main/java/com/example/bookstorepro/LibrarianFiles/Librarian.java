package com.example.bookstorepro.LibrarianFiles;
import com.example.bookstorepro.Database.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Librarian {
    private String username, password, role;
    private double performance = 0;

    //constructors
    public Librarian(){
    }
    public Librarian(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //getters
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getRole(){
        return role;
    }
    public double getPerformance(){
        return performance;
    }


    //setters
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setPerformance(double performance){
        this.performance = performance;
    }

    public static boolean isAvailableISBN(String ISBN) {

        try (Connection connection = DB.getConnection()) {
            String queryString = "SELECT bookname, quantity,ISBN FROM booklist WHERE quantity=0 AND ISBN='" + ISBN + "'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryString);

            return !resultSet.next();

        } catch (Exception e) {
          e.printStackTrace();
        }
        return false;
    }

    public static boolean isAvailableBookTitle(String bookName) {
        boolean availability = false;

        try (Connection con = DB.getConnection()) {
            String queryString = "SELECT bookname FROM booklist WHERE quantity=0 AND bookname='" + bookName + "'";
            Statement st = con.createStatement();
            ResultSet resultSet = st.executeQuery(queryString);
            if(resultSet.next()) {
                return false;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return true;
    }


    }




