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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Guest;
import model.Reservation;
import util.ReservationSession;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SuccessController {

    @FXML private Label checkInLabel;
    @FXML private Label checkOutLabel;
    @FXML private Label guestNameLabel;
    @FXML private Label guestIdLabel;
    @FXML private Label emailLabel;

    @FXML private Button newBookingBtn;
    @FXML private Button backToHomeBtn;

    @FXML
    public void initialize() {
        ReservationSession session = ReservationSession.getInstance();
        Reservation reservation = session.getCurrentReservation();
        Guest guest = session.getGuest();
        ReservationSession.getInstance().setCurrentReservation(reservation);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        checkInLabel.setText("📅 Check-in: " + formatter.format(reservation.getCheckInDate()));
        checkOutLabel.setText("⏰ Check-out: " + formatter.format(reservation.getCheckOutDate()));
        guestNameLabel.setText("👤 Guest: " + guest.getName());
        guestIdLabel.setText("🆔 ID: #" + generateFakeReservationCode(reservation.getReservationId()));
        emailLabel.setText("Email: " + guest.getEmail());
    }


    private String generateFakeReservationCode(int id) {
        return String.format("AP-2025-%06d", id);
    }

    @FXML
    private void handleNewBooking() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-info.fxml"));
        Stage stage = (Stage) newBookingBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }

    @FXML
    private void handleBackToHome() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/welcome.fxml"));
        Stage stage = (Stage) backToHomeBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }
}
