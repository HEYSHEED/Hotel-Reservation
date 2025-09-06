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

import dao.GuestDAO;
import dao.ReservationDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Guest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchGuestController {

    @FXML private TextField searchField;
    @FXML private TableView<Guest> guestTable;
    @FXML private TableColumn<Guest, String> nameColumn;
    @FXML private TableColumn<Guest, String> phoneColumn;
    @FXML private TableColumn<Guest, String> emailColumn;
    @FXML private TableColumn<Guest, String> addressColumn;
    @FXML private Button backBtn;
    private List<Guest> allGuests = new ArrayList<>();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        phoneColumn.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());


        addressColumn.setText("Status");
        addressColumn.setCellValueFactory(data -> {
            int guestId = data.getValue().getGuestId();
            var latestResId = ReservationDAO.findLatestReservationIdByGuestId(guestId);
            if (latestResId > 0) {
                var res = ReservationDAO.getReservationById(latestResId);
                return new SimpleStringProperty(res.getStatus());
            }
            return new SimpleStringProperty("No Reservation");
        });

        loadGuests();


        searchField.textProperty().addListener((obs, oldV, newV) -> handleSearch());
    }

    private void loadGuests() {

        allGuests = GuestDAO.getAllGuests();
        guestTable.setItems(FXCollections.observableArrayList(allGuests));
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();

        if (query.isEmpty()) {

            guestTable.setItems(FXCollections.observableArrayList(allGuests));
        } else {

            var filtered = allGuests.stream()
                    .filter(g -> (g.getName() != null && g.getName().toLowerCase().contains(query)) ||
                            (g.getPhoneNumber() != null && g.getPhoneNumber().toLowerCase().contains(query)))
                    .toList();
            guestTable.setItems(FXCollections.observableArrayList(filtered));
        }
    }



    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"))));
    }
}
