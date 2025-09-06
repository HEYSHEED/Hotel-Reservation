package controller;
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
import dao.GuestDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Guest;
import util.ReservationSession;

import java.io.IOException;
import java.util.regex.Pattern;

public class GuestsDetailsController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextArea addressField;

    @FXML
    private Button backButton, continueButton;

    private final Pattern phonePattern = Pattern.compile("^\\+?[0-9]{7,15}$");
    private final Pattern emailPattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    @FXML
    private void handleContinue() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Name, Phone Number and Email are required.");
            return;
        }

        if (!phonePattern.matcher(phone).matches()) {
            showAlert("Invalid Input", "Phone number format is not valid.");
            return;
        }

        if (!emailPattern.matcher(email).matches()) {
            showAlert("Invalid Input", "Email format is not valid.");
            return;
        }

        Guest guest = new Guest(name, phone, email, address);
        GuestDAO.saveGuest(guest); // saves to DB

        ReservationSession.getInstance().setGuest(guest); // save to singleton

        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Booking-Confirmation.fxml"));
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load confirmation page.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Room.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to go back.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
