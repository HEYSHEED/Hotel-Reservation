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

public class Room {

    private final IntegerProperty roomId = new SimpleIntegerProperty();
    private final ObjectProperty<RoomType> roomType = new SimpleObjectProperty<>();
    private final IntegerProperty numberOfBeds = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty(); // e.g., "Available", "Booked"

    public Room() {
    }

    public Room(int roomId, RoomType roomType, int numberOfBeds, double price, String status) {
        this.roomId.set(roomId);
        this.roomType.set(roomType);
        this.numberOfBeds.set(numberOfBeds);
        this.price.set(price);
        this.status.set(status);
    }

    // ROOM ID
    public int getRoomId() {
        return roomId.get();
    }

    public void setRoomId(int value) {
        roomId.set(value);
    }

    public IntegerProperty roomIdProperty() {
        return roomId;
    }

    // ROOM TYPE
    public RoomType getRoomType() {
        return roomType.get();
    }

    public void setRoomType(RoomType value) {
        roomType.set(value);
    }

    public ObjectProperty<RoomType> roomTypeProperty() {
        return roomType;
    }

    // NUMBER OF BEDS
    public int getNumberOfBeds() {
        return numberOfBeds.get();
    }

    public void setNumberOfBeds(int value) {
        numberOfBeds.set(value);
    }

    public IntegerProperty numberOfBedsProperty() {
        return numberOfBeds;
    }

    // PRICE
    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
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
}
