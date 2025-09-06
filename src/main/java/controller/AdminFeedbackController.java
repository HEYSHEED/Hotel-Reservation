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

import dao.FeedbackDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Feedback;

import java.io.IOException;

public class AdminFeedbackController {

    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> guestNameColumn;
    @FXML private TableColumn<Feedback, Number> ratingColumn;
    @FXML private TableColumn<Feedback, String> commentColumn;

    @FXML private Button backButton;

    @FXML
    public void initialize() {
        guestNameColumn.setCellValueFactory(cd -> cd.getValue().getGuest() != null ?
                new javafx.beans.property.SimpleStringProperty(cd.getValue().getGuest().getName()) :
                new javafx.beans.property.SimpleStringProperty(""));

        ratingColumn.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getRating()));
        commentColumn.setCellValueFactory(cd -> cd.getValue().commentsProperty());

        feedbackTable.setItems(FXCollections.observableArrayList(FeedbackDAO.getAllFeedback()));
    }

    @FXML
    private void handleBack() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"));
        Stage stage = (Stage) feedbackTable.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }
}
