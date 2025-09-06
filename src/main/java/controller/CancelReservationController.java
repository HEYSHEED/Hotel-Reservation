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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Reservation;
import util.LoggerUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancelReservationController {


    private static final Logger logger = LoggerUtil.getLogger(CancelReservationController.class);

    @FXML private TextField searchField;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, String> nameColumn;
    @FXML private TableColumn<Reservation, String> roomColumn;
    @FXML private TableColumn<Reservation, String> checkInColumn;
    @FXML private TableColumn<Reservation, String> checkOutColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private final ObservableList<Reservation> reservationData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logger.info("CancelReservationController initialized");

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGuestName()));
        roomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomType()));
        checkInColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckInDate().toString()));
        checkOutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckOutDate().toString()));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("❌ Cancel");
            {
                deleteBtn.setStyle("-fx-background-color: #e63946; -fx-text-fill: white; -fx-background-radius: 6;");
                deleteBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    logger.info(() -> "Cancel clicked for reservationId=" + res.getReservationId()
                            + ", guest=" + safeName(res));
                    showConfirmAndDelete(res);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        reservationTable.setItems(reservationData);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        logger.info(() -> "Search requested. keyword='" + keyword + "'");

        if (keyword.isEmpty()) {
            logger.warning("Search ignored: empty keyword");
            return;
        }

        var results = ReservationDAO.searchByNameOrPhone(keyword);
        logger.info(() -> "Search results: " + results.size() + " row(s)");

        reservationData.setAll(results);
        reservationTable.refresh();
    }

    private void showConfirmAndDelete(Reservation res) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("Are you sure you want to cancel this reservation?");
        alert.setContentText("Guest: " + safeName(res));

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    logger.info(() -> "Cancelling reservationId=" + res.getReservationId());
                    ReservationDAO.cancelReservation(res.getReservationId());

                    var fullRes = ReservationDAO.getReservationById(res.getReservationId());
                    if (fullRes != null && fullRes.getRoom() != null) {
                        RoomDAO.updateRoomStatus(fullRes.getRoom().getRoomId(), "Available");
                        logger.info(() -> "Room set to Available. roomId=" + fullRes.getRoom().getRoomId());
                    } else {
                        logger.warning("Room not updated (reservation/room null after reload).");
                    }

                    reservationData.remove(res);
                    logger.info("Reservation removed from table view.");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error while cancelling reservationId=" + res.getReservationId(), ex);
                    new Alert(Alert.AlertType.ERROR, "Failed to cancel reservation.", ButtonType.OK).showAndWait();
                }
            } else {
                logger.fine("Cancellation dialog dismissed by user.");
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            logger.info("Back to Admin-Dashboard requested");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Navigation to Admin-Dashboard failed", e);
            new Alert(Alert.AlertType.ERROR, "Failed to navigate to dashboard.", ButtonType.OK).showAndWait();
        }
    }

    private String safeName(Reservation r) {
        try { return r.getGuest() != null ? r.getGuest().getName() : "-"; }
        catch (Exception e) { return "-"; }
    }
}
