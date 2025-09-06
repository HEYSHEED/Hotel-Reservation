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

import dao.ReservationDAO;
import dao.RoomDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Reservation;
import util.LoggerUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckOutController {
    private static final Logger logger = LoggerUtil.getLogger(CheckOutController.class);

    // Search bar
    @FXML private TextField searchField;

    // Details box (hidden by default)
    @FXML private VBox detailsBox;

    // Labels inside details
    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label stayDurationLabel;
    @FXML private Label priceLabel;
    @FXML private TextField discountField;
    @FXML private Label finalPriceLabel;

    // Bottom buttons
    @FXML private Button backBtn;
    @FXML private Button confirmBtn;

    // State
    private Reservation currentReservation;
    private double baseAmount = 0.0; // price before discount

    @FXML
    public void initialize() {
        logger.info("CheckOutController initialized");
        discountField.textProperty().addListener((obs, oldVal, newVal) -> {
            logger.fine("Discount changed from " + oldVal + " to " + newVal);
            updateTotalLabel();
        });
        hideDetailsAndButtons();
    }

    @FXML
    private void handleSearch() {
        String q = searchField.getText().trim();
        logger.info("Searching reservations for: " + q);

        if (q.isEmpty()) {
            showInfo("Enter a name or phone to search.");
            hideDetailsAndButtons();
            logger.warning("Search query was empty");
            return;
        }

        List<Reservation> matches = ReservationDAO.searchByNameOrPhone(q);
        logger.fine("Found " + matches.size() + " reservations before filtering");

        matches.removeIf(r -> r.getStatus() == null || !r.getStatus().equalsIgnoreCase("Active"));
        logger.fine("Found " + matches.size() + " active reservations after filtering");

        if (matches.isEmpty()) {
            showInfo("No active reservation found for: " + q);
            hideDetailsAndButtons();
            logger.warning("No active reservation found for: " + q);
            return;
        }

        currentReservation = matches.stream()
                .max(Comparator.comparingInt(Reservation::getReservationId))
                .orElse(matches.get(0));

        logger.info("Selected reservation ID: " + currentReservation.getReservationId());
        populateDetails(currentReservation);
        showDetailsAndButtons();
    }

    @FXML
    private void handleBack() throws IOException {
        logger.info("Navigating back to Admin-Dashboard");
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"))));
    }

    @FXML
    private void handleConfirm() {
        if (currentReservation == null) {
            logger.warning("Confirm clicked but no reservation selected");
            showInfo("No reservation selected.");
            return;
        }

        try {
            currentReservation.setStatus("CheckedOut");
            boolean ok = ReservationDAO.updateReservation(currentReservation);
            if (!ok) {
                logger.severe("Failed to update reservation status for ID: " + currentReservation.getReservationId());
                showError("Failed to update reservation.");
                return;
            }

            RoomDAO.updateRoomStatus(currentReservation.getRoom().getRoomId(), "Available");
            logger.info("Reservation checked out: " + currentReservation.getReservationId()
                    + ", Room set to Available: " + currentReservation.getRoom().getRoomId());

            ButtonType doneBtn   = new ButtonType("I informed the guest", ButtonBar.ButtonData.OK_DONE);
            ButtonType laterBtn  = new ButtonType("Later", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert remind = new Alert(Alert.AlertType.CONFIRMATION,
                    """
                    Check-out completed successfully.

                    ❗ Reminder:
                    Please inform the guest that they can use the KIOSK to leave feedback about their stay.
                    """,
                    doneBtn, laterBtn);
            remind.setHeaderText("Checkout & Feedback Reminder");
            remind.showAndWait();

            goToDashboardSafe();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while confirming checkout", e);
            showError("Unexpected error occurred.");
        }
    }

    private void goToDashboardSafe() {
        try {
            logger.info("Navigating to Admin-Dashboard (safe)");
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"))
            ));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to navigate to dashboard", e);
            hideDetailsAndButtons();
        }
    }





    private void populateDetails(Reservation r) {

        logger.info("Populating details for reservation ID: " + r.getReservationId());
        String guestName = r.getGuest() != null ? r.getGuest().getName() : "-";
        String roomType = (r.getRoom() != null && r.getRoom().getRoomType() != null)
                ? r.getRoom().getRoomType().name() : "-";

        LocalDate in = r.getCheckInDate();
        LocalDate out = r.getCheckOutDate();
        long nights = (in != null && out != null) ? ChronoUnit.DAYS.between(in, out) : 0;
        if (nights <= 0) nights = 1;

        double pricePerNight = (r.getRoom() != null) ? r.getRoom().getPrice() : 0.0;
        baseAmount = pricePerNight * nights;

        guestNameLabel.setText(guestName);
        roomTypeLabel.setText(roomType);
        stayDurationLabel.setText(nights + " nights");
        priceLabel.setText(String.format("$%.2f", baseAmount));

        discountField.setText("");
        updateTotalLabel();

        logger.fine("Base amount: " + baseAmount + ", Nights: " + nights);
    }

    private void updateTotalLabel() {
        double discountPercent = parsePercent(discountField.getText());
        if (discountPercent < 0) discountPercent = 0;
        if (discountPercent > 100) discountPercent = 100;

        double finalAmount = baseAmount * (1 - discountPercent / 100.0);
        finalPriceLabel.setText(String.format("$%.2f", finalAmount));

        logger.fine("Final amount after discount " + discountPercent + "%: " + finalAmount);
    }

    private double parsePercent(String s) {
        if (s == null || s.isBlank()) return 0.0;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            logger.warning("Invalid discount format: " + s);
            return 0.0;
        }
    }

    private void showDetailsAndButtons() {
        detailsBox.setVisible(true);
        detailsBox.setManaged(true);

        backBtn.setVisible(true);
        backBtn.setManaged(true);

        confirmBtn.setDisable(false);
        confirmBtn.setVisible(true);
        confirmBtn.setManaged(true);
    }

    private void hideDetailsAndButtons() {
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);

        backBtn.setVisible(true);
        backBtn.setManaged(true);

        confirmBtn.setDisable(true);
        confirmBtn.setVisible(false);
        confirmBtn.setManaged(false);
        logger.fine("Details and buttons hidden");
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
