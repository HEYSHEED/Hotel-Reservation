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

import dao.BillingDAO;
import dao.GuestDAO;
import dao.ReservationDAO;
import dao.RoomDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Billing;
import model.BillingItem;
import model.Reservation;
import model.Room;
import util.ReservationSession;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class BookingConfirmationController {

    @FXML private Label checkInLabel, checkOutLabel, guestCountLabel, roomTypeLabel, roomCountLabel;
    @FXML private TableView<BillingItem> billingTable;
    @FXML private TableColumn<BillingItem, Integer> quantityCol;
    @FXML private TableColumn<BillingItem, String> descCol;
    @FXML private TableColumn<BillingItem, Double> unitPriceCol, amountCol;
    @FXML private Label subtotalLabel, taxLabel, totalLabel;

    private static final double TAX_RATE = 0.09;  // ۹٪

    @FXML
    public void initialize() {
        ReservationSession session = ReservationSession.getInstance();
        Room selectedRoom = session.getSelectedRoom();


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        checkInLabel.setText("Check-in Date: " + fmt.format(session.getCheckInDate()));
        checkOutLabel.setText("Check-out Date: " + fmt.format(session.getCheckOutDate()));


        guestCountLabel.setText("Number of Guests: " + session.getGuestCount());
        roomTypeLabel.setText("Room Type: " + selectedRoom.getRoomType().name());
        roomCountLabel.setText("Rooms Booked: 1");


        long nights = ChronoUnit.DAYS.between(session.getCheckInDate(), session.getCheckOutDate());
        if (nights <= 0) nights = 1;


        double unitPrice = selectedRoom.getPrice() * nights;
        BillingItem item = new BillingItem(1, selectedRoom.getRoomType().name() + " - " + nights + " nights", unitPrice);
        billingTable.setItems(FXCollections.observableArrayList(item));


        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty().asObject());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());


        double subtotal = item.getAmount();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        taxLabel.setText(String.format("Tax (9%%): $%.2f", tax));
        totalLabel.setText(String.format("Total: $%.2f", total));


        Billing bill = new Billing();
        bill.setAmount(subtotal);
        bill.setTax(TAX_RATE * 100);
        bill.setDiscount(0);
        bill.setTotalAmount(total);
        session.setCurrentBill(bill);
    }

    @FXML
    private void handleBack() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-details.fxml"));
        Stage stage = (Stage) billingTable.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }

    @FXML
    private void handleConfirm() throws IOException {
        ReservationSession session = ReservationSession.getInstance();


        Reservation reservation = new Reservation();
        reservation.setGuest(session.getGuest());
        reservation.setRoom(session.getSelectedRoom());
        reservation.setCheckInDate(session.getCheckInDate());
        reservation.setCheckOutDate(session.getCheckOutDate());
        reservation.setNumberOfGuests(session.getGuestCount());
        reservation.setStatus("Active");


        reservation = ReservationDAO.insertReservation(reservation);
        reservation.setGuest(GuestDAO.getGuestById(reservation.getGuest().getGuestId()));
        reservation.setRoom(RoomDAO.getRoomById(reservation.getRoom().getRoomId()));


        session.setCurrentReservation(reservation);
        RoomDAO.updateRoomStatus(reservation.getRoom().getRoomId(), "Booked");

        Billing bill = session.getCurrentBill();
        bill.setReservation(reservation);
        BillingDAO.insertBill(bill);


        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Success.fxml"));
        Stage stage = (Stage) billingTable.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }

}
