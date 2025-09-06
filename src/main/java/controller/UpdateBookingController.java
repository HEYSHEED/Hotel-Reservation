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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Reservation;
import util.LoggerUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateBookingController {


    private static final Logger logger = LoggerUtil.getLogger(UpdateBookingController.class);

    @FXML private TextField guestNameField;

    @FXML private TableView<Reservation> bookingTable;
    @FXML private TableColumn<Reservation, LocalDate> checkInCol;
    @FXML private TableColumn<Reservation, LocalDate> checkOutCol;
    @FXML private TableColumn<Reservation, String>    roomCol;
    @FXML private TableColumn<Reservation, Integer>   peopleCol;
    @FXML private TableColumn<Reservation, String>    phoneCol;
    @FXML private TableColumn<Reservation, Void>      actionCol;

    @FXML private Button backButton;

    private final ObservableList<Reservation> reservationData = FXCollections.observableArrayList();
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        logger.info("UpdateBookingController initialized");

        checkInCol.setCellValueFactory(cd -> cd.getValue().checkInDateProperty());
        checkOutCol.setCellValueFactory(cd -> cd.getValue().checkOutDateProperty());

        roomCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue().getRoom().getRoomType().name()));

        peopleCol.setCellValueFactory(new PropertyValueFactory<>("numberOfGuests"));
        peopleCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        peopleCol.setOnEditCommit(ev -> {
            Reservation r = ev.getRowValue();
            Integer oldV = r.getNumberOfGuests();
            r.setNumberOfGuests(ev.getNewValue());
            logger.fine(() -> "Guests changed for resId=" + r.getReservationId() + " from " + oldV + " to " + ev.getNewValue());
        });

        phoneCol.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                cd.getValue().getGuest() != null ? cd.getValue().getGuest().getPhoneNumber() : ""
        ));

        bookingTable.setEditable(true);

        attachDatePickerCell(checkInCol, true);
        attachDatePickerCell(checkOutCol, false);

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button confirmBtn = new Button("Confirm");
            {
                confirmBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                confirmBtn.setOnAction(e -> {
                    Reservation r = getTableView().getItems().get(getIndex());
                    logger.info(() -> "Confirm update clicked for resId=" + r.getReservationId());
                    showConfirmationAndSave(r);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : confirmBtn);
            }
        });

        bookingTable.setItems(reservationData);
    }

    private void attachDatePickerCell(TableColumn<Reservation, LocalDate> col, boolean isCheckIn) {
        col.setCellFactory(tc -> new TableCell<>() {
            private final DatePicker dp = new DatePicker();

            {
                dp.setConverter(new StringConverter<>() {
                    @Override public String toString(LocalDate date) { return date == null ? "" : df.format(date); }
                    @Override public LocalDate fromString(String s) {
                        return (s == null || s.isBlank()) ? null : LocalDate.parse(s, df);
                    }
                });

                dp.valueProperty().addListener((obs, oldV, newV) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        Reservation r = getTableRow().getItem();
                        if (isCheckIn) {
                            r.setCheckInDate(newV);
                            logger.fine(() -> "Check-in changed for resId=" + r.getReservationId()
                                    + " from " + fmt(oldV) + " to " + fmt(newV));
                        } else {
                            r.setCheckOutDate(newV);
                            logger.fine(() -> "Check-out changed for resId=" + r.getReservationId()
                                    + " from " + fmt(oldV) + " to " + fmt(newV));
                        }
                    }
                });
            }

            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    dp.setValue(item);
                    setGraphic(dp);
                }
            }
        });
    }

    @FXML
    private void handleSearch() {
        String input = guestNameField.getText().trim();
        logger.info(() -> "Search requested. keyword='" + input + "'");

        if (input.isEmpty()) {
            showAlert("Validation Error", "Enter guest name or phone.");
            logger.warning("Search aborted: empty keyword");
            return;
        }

        var all = new java.util.ArrayList<Reservation>();
        all.addAll(ReservationDAO.searchByNameOrPhone(input));
        logger.fine(() -> "Loaded " + all.size() + " rows from DAO");

        var byId = new LinkedHashMap<Integer, Reservation>();
        for (var r : all) byId.put(r.getReservationId(), r);

        reservationData.setAll(byId.values());
        bookingTable.refresh();

        logger.info(() -> "Search results (deduped): " + reservationData.size());
        if (reservationData.isEmpty()) {
            showAlert("No Results", "No reservation matched.");
        }
    }

    private void showConfirmationAndSave(Reservation r) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Save changes to this reservation?");
        alert.setContentText("Reservation ID: " + r.getReservationId());

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    boolean ok = ReservationDAO.updateReservation(r);
                    logger.info(() -> "Update reservation resId=" + r.getReservationId() + " -> " + (ok ? "SUCCESS" : "FAIL"));
                    showAlert(ok ? "Success" : "Error",
                            ok ? "Reservation updated successfully." : "Update failed.");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error updating reservation resId=" + r.getReservationId(), ex);
                    showAlert("Error", "Unexpected error while updating.");
                }
            } else {
                logger.fine(() -> "Update canceled by user for resId=" + r.getReservationId());
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            logger.info("Back to Admin-Dashboard requested");
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Navigation to Admin-Dashboard failed", e);
            showAlert("Navigation Error", "Failed to go back.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private String fmt(LocalDate d) {
        return d == null ? "null" : df.format(d);
    }
}
