package org.example;

public enum ErrorMessage {
    PAN_OUT_OF_RANGE("Pan is out of range!"),
    TRANS_AMOUNT_OUT_OF_RANGE("Transaction amount is out of range!"),
    EMPTY_ERROR_MSG("");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
