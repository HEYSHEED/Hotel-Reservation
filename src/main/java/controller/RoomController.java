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
import dao.RoomDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Room;
import model.RoomType;
import util.ReservationSession;

import java.io.IOException;

public class RoomController {

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, RoomType> roomTypeCol;

    @FXML
    private TableColumn<Room, Integer> capacityCol;

    @FXML
    private TableColumn<Room, Double> priceCol;

    @FXML
    private TableColumn<Room, Void> selectCol;

    @FXML
    private Button backButton, continueButton;

    private final ToggleGroup radioGroup = new ToggleGroup();
    private Room selectedRoom;

    @FXML
    public void initialize() {

        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("numberOfBeds"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        ReservationSession session = ReservationSession.getInstance();
        int guestCount = session.getGuestCount();


        selectCol.setCellFactory(col -> new TableCell<>() {
            private final RadioButton radioButton = new RadioButton();

            {
                radioButton.setToggleGroup(radioGroup);
                radioButton.setOnAction(e -> {
                    selectedRoom = getTableView().getItems().get(getIndex());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(radioButton);
                }
            }
        });


        ObservableList<Room> filteredRooms = FXCollections.observableArrayList(
                RoomDAO.getAllRooms().stream()
                        .filter(room -> room.getStatus().equalsIgnoreCase("Available"))
                        .filter(room -> {

                            if (guestCount == 1) return true;


                            return room.getNumberOfBeds() >= guestCount;
                        })
                        .toList()
        );

        roomTable.setItems(filteredRooms);
    }

    @FXML
    private void handleBack() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-info.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load previous screen.");
        }
    }

    @FXML
    private void handleContinue() {
        if (selectedRoom == null) {
            showAlert("Please select a room to continue.");
            return;
        }


        ReservationSession.getInstance().setSelectedRoom(selectedRoom);

        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-details.fxml"));
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load guest details screen.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
