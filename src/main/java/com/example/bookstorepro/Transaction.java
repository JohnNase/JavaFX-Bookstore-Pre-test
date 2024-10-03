package com.example.bookstorepro;

import java.sql.Date;
import java.time.LocalDate;

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
public class Transaction {
    private static double price;
    private static LocalDate transactionDate;
    private static int quantity;

    private static String librarianName;

    public Transaction(String librarianName, LocalDate transactionDate, int quantity, double price) {
        Transaction.librarianName = librarianName;
        Transaction.quantity = quantity;
        Transaction.price = price;
        Transaction.transactionDate = transactionDate;
    }

    public static double getPrice() {
        return price;
    }
    public static Date getTransactionDate() {
        return Date.valueOf(transactionDate);
    }

    public static int getQuantity() {
        return quantity;
    }

    public static void setQuantity(int quantity) {
        Transaction.quantity = quantity;
    }

    public static String getLibrarianName() {
        return librarianName;
    }


    @Override
    public String toString() {
        return getLibrarianName() +" "+ getTransactionDate() +" "  + getQuantity() +" " + getPrice() ;
    }
}