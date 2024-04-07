package org.example;

public class Main {
    public static void main(String[] args) {
        Transaction transaction = new Transaction("1000000000000000", "01", 1000, 978);
        Transaction processedTransaction = PaymentService.PaymentServiceProcessTransaction(transaction);
        System.out.println("Transaction status: " + processedTransaction.getStatusName());
    }
}