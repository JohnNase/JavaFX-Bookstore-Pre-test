package com.example.bookstorepro;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
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
public class UserController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -155396823076863L;
    private static ArrayList<User> users;
    public UserController() {
        users = new ArrayList<>();
    }
    public boolean signUp(String firstName, String lastName, String email, String username, String role, String password, String verifyPassword) {
        if(password.equals(verifyPassword)) {
            User newUser = new User(firstName, lastName, email, username, role, password);
            users.add(newUser);
            return true;
        }
        return  false;
    }
    public void printUsers(){
        for(int i = 0; i < users.size(); i ++){
            System.out.println(users.get(i));
        }
    }
    public ArrayList<User> getUsers(){
        return users;
    }
    public void setUsers(ArrayList<User> user){
        this.users = user;
    }
    public void deleteUser(User user) {
        users.remove(user);
    }
    public void updateRole(User user, String role) {
        int index = users.indexOf(user);
        user.setRole(role);
        users.set(index, user);
    }
    public void saveData() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("History.dat"));
            output.writeObject(users);
            output.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}
