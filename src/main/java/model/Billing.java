package model;
/**********************************************
 Workshop #
 Course:<ADP> - Semester 5th
 Last Name:<Ebrahim Shirazi>
 First Name:<Mahshid>
 ID:<168024222>
 Section:<NCC>
 This assignment represents my own work in accordance with Seneca Academic Policy.
 Signature
 Date:<2025-08-08>
 **********************************************/

import javafx.beans.property.*;

public class Billing {

    private final IntegerProperty billId = new SimpleIntegerProperty();
    private final ObjectProperty<Reservation> reservation = new SimpleObjectProperty<>();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final DoubleProperty tax = new SimpleDoubleProperty();            // TAX
    private final DoubleProperty discount = new SimpleDoubleProperty();       // DISCOUNT
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();    // FINAL PRICE

    public Billing() {
    }

    public Billing(int billId, Reservation reservation, double amount, double tax, double discount) {
        this.billId.set(billId);
        this.reservation.set(reservation);
        this.amount.set(amount);
        this.tax.set(tax);
        this.discount.set(discount);
        this.totalAmount.set(calculateTotal());
    }

    // FINAL PRICE + TAX +DISCOUNT
    public double calculateTotal() {
        double subtotal = amount.get();
        double taxAmount = subtotal * (tax.get() / 100.0);
        double discountAmount = subtotal * (discount.get() / 100.0);
        return subtotal + taxAmount - discountAmount;
    }

    // BILL ID
    public int getBillId() {
        return billId.get();
    }

    public void setBillId(int value) {
        billId.set(value);
    }

    public IntegerProperty billIdProperty() {
        return billId;
    }

    // RESERVATION
    public Reservation getReservation() {
        return reservation.get();
    }

    public void setReservation(Reservation value) {
        reservation.set(value);
    }

    public ObjectProperty<Reservation> reservationProperty() {
        return reservation;
    }

    // AMOUNT
    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double value) {
        amount.set(value);
        updateTotalAmount(); // UPDATING AMOUNT
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    // TAX
    public double getTax() {
        return tax.get();
    }

    public void setTax(double value) {
        tax.set(value);
        updateTotalAmount();
    }

    public DoubleProperty taxProperty() {
        return tax;
    }

    // DISCOUNT
    public double getDiscount() {
        return discount.get();
    }

    public void setDiscount(double value) {
        discount.set(value);
        updateTotalAmount();
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    // TOTAL AMOUNT
    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double value) {
        totalAmount.set(value);
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    private void updateTotalAmount() {
        totalAmount.set(calculateTotal());
    }
}
