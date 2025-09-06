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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.*;

public class AdminDashboardController {

    @FXML
    private Button newReservationBtn;

    private static final Logger logger = Logger.getLogger(AdminDashboardController.class.getName());

    @FXML
    private void handleNewReservation(ActionEvent event) {
        try {
            AnchorPane guestInfoPage = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-info.fxml"));
            Stage stage = (Stage) newReservationBtn.getScene().getWindow();
            stage.setScene(new Scene(guestInfoPage));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load guests-info.fxml", e);
        }
    }
    @FXML
    private void handleCancelBooking() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Cancel-Reservation.fxml"));
        Stage stage = (Stage) newReservationBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }
    @FXML
    private void handleUpdateBooking() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Update-Booking.fxml"));
            Stage stage = (Stage) newReservationBtn.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button viewFeedbackBtn;

    @FXML
    private void handleViewFeedback() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Feedback.fxml"));
            Stage stage = (Stage) viewFeedbackBtn.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Admin-Feedback.fxml", e);
        }
    }
    @FXML
    private Button searchGuestBtn;

    @FXML
    private void handleSearchGuest() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Search-Guest.fxml"));
            Stage stage = (Stage) searchGuestBtn.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Search-Guest.fxml", e);
        }
    }
    @FXML private Button checkOutBtn;

    @FXML
    private void handleCheckOutGuest() {
        try {

            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/CheckOut-Guests.fxml"));
            Stage stage = (Stage) checkOutBtn.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Check-Out.fxml", e);
        }
    }
    @FXML private Button logoutBtn;

    @FXML
    private void handleLogout() {
        try {
            AnchorPane loginPage = FXMLLoader.load(getClass().getResource("/demo/hotel/admin_login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load admin_login.fxml", e);
        }
    }


}
