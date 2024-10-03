package com.example.bookstorepro.Bill;

public class BillNumber {
    private static int BillNo;

    public static int getBillNo() {
        return BillNo;
    }

    public static void setBillNo(int billNo) {
        BillNo = billNo;
    }
}