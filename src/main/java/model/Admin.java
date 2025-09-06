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

public class Admin {

    private final IntegerProperty adminId = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();

    public Admin() {
    }

    public Admin(int adminId, String username, String password) {
        this.adminId.set(adminId);
        this.username.set(username);
        this.password.set(password);
    }

    // ADMIN ID
    public int getAdminId() {
        return adminId.get();
    }

    public void setAdminId(int value) {
        adminId.set(value);
    }

    public IntegerProperty adminIdProperty() {
        return adminId;
    }

    // USERNAME
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String value) {
        username.set(value);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // PASSWORD
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String value) {
        password.set(value);
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
