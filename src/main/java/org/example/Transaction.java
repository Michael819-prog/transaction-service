package org.example;

public class Transaction {
    private String pan;
    private String type;
    private Integer amount;
    private Integer currency;
    private String status;
    private transient String statusName; // hidden from JSON

    public Transaction(String pan, String type, Integer amount, Integer currency) {
        this.pan = pan;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.status = "00"; // by default
        this.statusName = "Active";
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getType() {
        return type;
    }

    public void setType(String tType) {
        this.type = tType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
