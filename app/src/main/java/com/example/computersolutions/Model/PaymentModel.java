package com.example.computersolutions.Model;

public class PaymentModel {
    private String paymentType;
    private String cardNumber;
    private String expirationDate;
    private String billingAddress;
    private String postalCode;

    public PaymentModel(){}

    public PaymentModel(String paymentType, String cardNumber, String expirationDate, String billingAddress, String postalCode) {
        this.paymentType = paymentType;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.billingAddress = billingAddress;
        this.postalCode = postalCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}