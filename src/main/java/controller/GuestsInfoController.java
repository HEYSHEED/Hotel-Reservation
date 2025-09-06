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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ReservationSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.*;

public class GuestsInfoController {

    @FXML
    private DatePicker checkInDate;

    @FXML
    private DatePicker checkOutDate;

    @FXML
    private Spinner<Integer> guestCountSpinner;

    @FXML
    private Button viewRulesBtn;

    @FXML
    private Button continueBtn;

    private static final Logger logger = Logger.getLogger(GuestsInfoController.class.getName());

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        guestCountSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void handleViewRules(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rules and Regulations");
        alert.setHeaderText("Hotel Reservation Rules");
        alert.setContentText("""
                🛏 Single room: Max 2 guests
                🛏 Double room: Max 4 guests
                🛏 Deluxe/Pent: Max 2 guests (premium price)
                ➕ More than 2 and less than 5 guests: Double or two Single rooms
                ➕ More than 4 guests: Multiple Double or mix of Single + Double rooms
                """);
        alert.showAndWait();
    }

    @FXML
    private void handleContinue(ActionEvent event) {
        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();
        int guestCount = guestCountSpinner.getValue();


        ReservationSession session = ReservationSession.getInstance();
        session.setCheckInDate(checkIn);
        session.setCheckOutDate(checkOut);
        session.setGuestCount(guestCount);

        // ✅ Validation
        if (checkIn == null || checkOut == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select both Check-In and Check-Out dates.");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Check-Out must be after Check-In.");
            return;
        }

        if (guestCount < 1) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Guest count must be at least 1.");
            return;
        }

        try {
            AnchorPane roomPage = FXMLLoader.load(getClass().getResource("/demo/hotel/Room.fxml"));
            Stage stage = (Stage) continueBtn.getScene().getWindow();
            stage.setScene(new Scene(roomPage));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Room.fxml", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load room selection screen.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
