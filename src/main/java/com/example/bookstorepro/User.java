package com.example.bookstorepro;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

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
public class User implements Serializable {
    private static final long serialVersionUID = -81230957409593984L;
    private transient SimpleStringProperty firstName, lastName, email, username, password, role;
    private String firstName1, lastName1, email1, username1, password1, role1;

    public User(String firstName, String lastName, String email, String username, String role, String password){
        this.firstName1 = firstName;
        setFirstName(firstName1);
        this.lastName1 = lastName;
        setLastName(lastName);
        this.email1 = email;
        setEmail(email);
        this.username1 = username;
        setUsername(username);
        this.role1 = role;
        setRole(role);
        this.password1 = password;
    }

    public String getFirstName() {
        if(firstName == null){
            setFirstName(firstName1);
            setPassword(password1);
        }
        return firstName.get();
    }

    public String getLastName() {
        if(lastName == null) setLastName(lastName1);
        return lastName.get();
    }

    public String getEmail() {
        if(email == null) setEmail(email1);
        return email.get();
    }

    public String getUsername(){
        if(username == null) setUsername(username1);
        return username.get();
    }

    public String getRole(){
        if(role == null) setRole(role1);
        return role.get();
    }

    public String getPassword(){
        if(password == null) setPassword(password1);
        return password.get();
    }

    public void setFirstName(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.password = new SimpleStringProperty(password1);
    }
    public void setLastName(String lastName) {
        this.lastName = new SimpleStringProperty(lastName);
    }
    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }
    public void setUsername(String username){
        this.username = new SimpleStringProperty(username);
    }
    public void setRole(String role){
        this.role = new SimpleStringProperty(role);
    }
    public void setPassword(String password) {
        this.password = new SimpleStringProperty(password);
    }

    @Override
    public String toString(){
        return "Name: " + getFirstName() + " |Last Name: " + getLastName() + " | Email: " + getEmail()
                + "| Username: " + getUsername() + "| Role: "+ getRole() + "| Password: " + getPassword();
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