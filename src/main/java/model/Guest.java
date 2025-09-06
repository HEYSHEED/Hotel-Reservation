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
//USING SIMPLESTRINGPROPERTY
public class Guest {
    private final IntegerProperty guestId = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();

    public Guest() {
    }

    public Guest(int guestId, String name, String phoneNumber, String email, String address) {
        this.guestId.set(guestId);
        this.name.set(name);
        this.phoneNumber.set(phoneNumber);
        this.email.set(email);
        this.address.set(address);
    }

    public Guest(String name, String phone, String email, String address) {
        this.name.set(name);
        this.phoneNumber.set(phone);
        this.email.set(email);
        this.address.set(address);
    }

    // GETTERS AND SETTERS
    public int getGuestId() {
        return guestId.get();
    }

    public void setGuestId(int value) {
        guestId.set(value);
    }

    public IntegerProperty guestIdProperty() {
        return guestId;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String value) {
        phoneNumber.set(value);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public StringProperty addressProperty() {
        return address;
    }
}
