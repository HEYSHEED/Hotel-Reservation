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
import dao.GuestDAO;
import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Feedback;
import model.Guest;
import model.Reservation;

import java.io.IOException;
import java.util.regex.Pattern;

public class FeedbackController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextArea  feedbackArea;
    @FXML private ComboBox<String> ratingComboBox;
    @FXML private Button backBtn, submitBtn;

    private final Pattern phonePattern = Pattern.compile("^\\+?[0-9]{7,15}$");

    @FXML
    public void initialize() {
        if (ratingComboBox.getItems().isEmpty()) {
            ratingComboBox.getItems().addAll("1","2","3","4","5");
        }
        ratingComboBox.getSelectionModel().select("5");
    }

    @FXML
    private void handleBack() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/welcome.fxml"));
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String comments = feedbackArea.getText().trim();
        String ratingStr = ratingComboBox.getValue();

        // basic validation
        if (name.isEmpty() || phone.isEmpty() || comments.isEmpty() || ratingStr == null) {
            show(Alert.AlertType.WARNING, "Validation", "All fields are required.");
            return;
        }
        if (!phonePattern.matcher(phone).matches()) {
            show(Alert.AlertType.WARNING, "Validation", "Phone number format is not valid.");
            return;
        }


        Reservation lastRes = ReservationDAO.findLatestCheckedOutByPhone(phone);
        if (lastRes == null) {
            infoAndReturnToWelcome(
                    "Feedback is only available after check-out.\n" +
                            "If you believe this is a mistake, please contact the hotel."
            );
            return;
        }


        Guest guest = lastRes.getGuest();



        Feedback f = new Feedback();
        f.setGuest(guest);
        f.setReservation(lastRes);
        f.setComments(comments);
        f.setRating(Integer.parseInt(ratingStr));

        FeedbackDAO.insertFeedback(f);

        show(Alert.AlertType.INFORMATION, "Thanks!", "Your feedback was submitted.");
        try { handleBack(); } catch (IOException ignored) {}
    }
    private void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void infoAndReturnToWelcome(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/welcome.fxml"));
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.setScene(new Scene(pane));
        } catch (IOException ignored) {}
    }

}
