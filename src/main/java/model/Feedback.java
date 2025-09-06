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

public class Feedback {

    private final IntegerProperty feedbackId = new SimpleIntegerProperty();
    private final ObjectProperty<Guest> guest = new SimpleObjectProperty<>();
    private final ObjectProperty<Reservation> reservation = new SimpleObjectProperty<>();
    private final StringProperty comments = new SimpleStringProperty();
    private final IntegerProperty rating = new SimpleIntegerProperty();

    public Feedback() {
    }

    public Feedback(int feedbackId, Guest guest, Reservation reservation, String comments, int rating) {
        this.feedbackId.set(feedbackId);
        this.guest.set(guest);
        this.reservation.set(reservation);
        this.comments.set(comments);
        this.rating.set(rating);
    }

    // FEEDBACK ID
    public int getFeedbackId() {
        return feedbackId.get();
    }

    public void setFeedbackId(int value) {
        feedbackId.set(value);
    }

    public IntegerProperty feedbackIdProperty() {
        return feedbackId;
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

    // RESERVATION
    public Reservation getReservation() {
        return reservation.get();
    }

    public void setReservation(Reservation value) {
        reservation.set(value);
    }

    public ObjectProperty<Reservation> reservationProperty() {
        return reservation;
    }

    // COMMENTS
    public String getComments() {
        return comments.get();
    }

    public void setComments(String value) {
        comments.set(value);
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    // RATING
    public int getRating() {
        return rating.get();
    }

    public void setRating(int value) {
        rating.set(value);
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }
}
