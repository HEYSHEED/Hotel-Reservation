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
import java.time.LocalDate;

public class Reservation {

    private final IntegerProperty reservationId = new SimpleIntegerProperty();
    private final ObjectProperty<Guest> guest = new SimpleObjectProperty<>();
    private final ObjectProperty<Room> room = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> checkInDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> checkOutDate = new SimpleObjectProperty<>();
    private final IntegerProperty numberOfGuests = new SimpleIntegerProperty();
    private final StringProperty status = new SimpleStringProperty(); // e.g., "Active", "Cancelled", "CheckedOut"

    public Reservation() {
    }

    public Reservation(int reservationId, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, String status) {
        this.reservationId.set(reservationId);
        this.guest.set(guest);
        this.room.set(room);
        this.checkInDate.set(checkInDate);
        this.checkOutDate.set(checkOutDate);
        this.numberOfGuests.set(numberOfGuests);
        this.status.set(status);
    }

    // RESERVATION ID
    public int getReservationId() {
        return reservationId.get();
    }

    public void setReservationId(int value) {
        reservationId.set(value);
    }

    public IntegerProperty reservationIdProperty() {
        return reservationId;
    }

    // GUEST
    public Guest getGuest() {
        return guest.get();
    }

    public void setGuest(Guest value) {
        guest.set(value);
    }

    public ObjectProperty<Guest> guestProperty() {
        return guest;
    }

    // ROOM
    public Room getRoom() {
        return room.get();
    }

    public void setRoom(Room value) {
        room.set(value);
    }

    public ObjectProperty<Room> roomProperty() {
        return room;
    }

    // CHECK IN DATE
    public LocalDate getCheckInDate() {
        return checkInDate.get();
    }

    public void setCheckInDate(LocalDate value) {
        checkInDate.set(value);
    }

    public ObjectProperty<LocalDate> checkInDateProperty() {
        return checkInDate;
    }

    // CHECK OUT DATE
    public LocalDate getCheckOutDate() {
        return checkOutDate.get();
    }

    public void setCheckOutDate(LocalDate value) {
        checkOutDate.set(value);
    }

    public ObjectProperty<LocalDate> checkOutDateProperty() {
        return checkOutDate;
    }

    // NUMBER OF GUESTS
    public int getNumberOfGuests() {
        return numberOfGuests.get();
    }

    public void setNumberOfGuests(int value) {
        numberOfGuests.set(value);
    }

    public IntegerProperty numberOfGuestsProperty() {
        return numberOfGuests;
    }

    // STATUS
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getGuestName() {
        return guest.get() != null ? guest.get().getName() : "";
    }


    public String getRoomType() {
        return room.get() != null ? room.get().getRoomType().name() : "";
    }



}
