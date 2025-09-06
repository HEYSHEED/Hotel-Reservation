package util;

import model.Billing;
import model.Guest;
import model.Room;

import java.time.LocalDate;

public class ReservationSession {

    private static ReservationSession instance;

    private int guestCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Room selectedRoom;

    private ReservationSession() {}

    public static ReservationSession getInstance() {
        if (instance == null) {
            instance = new ReservationSession();
        }
        return instance;
    }

    public void reset() {
        guestCount = 0;
        checkInDate = null;
        checkOutDate = null;
        selectedRoom = null;
    }

    // Getters and Setters
    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }
    private Guest guest;

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Guest getGuest() {
        return guest;
    }
    private Billing currentBill;

    public Billing getCurrentBill() {
        return currentBill;
    }

    public void setCurrentBill(Billing bill) {
        this.currentBill = bill;
    }
    private model.Reservation currentReservation;
    public model.Reservation getCurrentReservation() {
        return currentReservation;
    }

    public void setCurrentReservation(model.Reservation reservation) {
        this.currentReservation = reservation;
    }



}
