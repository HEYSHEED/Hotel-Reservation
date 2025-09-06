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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.*;

public class WelcomeController {

    private static final Logger logger = Logger.getLogger(WelcomeController.class.getName());

    @FXML
    private Button adminLoginBtn;

    @FXML
    private void handleAdminLogin(ActionEvent event) {
        try {
            AnchorPane adminLoginPage = FXMLLoader.load(getClass().getResource("/demo/hotel/admin_login.fxml"));
            Stage stage = (Stage) adminLoginBtn.getScene().getWindow();
            stage.setScene(new Scene(adminLoginPage));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load admin login page", e);
        }
    }
    @FXML private Button makeBookingBtn;

    @FXML
    private void handleMakeBooking() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/guests-info.fxml"));
        Stage stage = (Stage) makeBookingBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }
    @FXML private Button feedbackBtn;

    @FXML
    private void handleLeaveFeedback() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/demo/hotel/Feedback.fxml"));
        Stage stage = (Stage) feedbackBtn.getScene().getWindow();
        stage.setScene(new Scene(pane));
    }
    @FXML private Hyperlink rulesLink;
    @FXML
    private void handleShowRules() {
        String welcome = "Welcome to Hotel Tehran!\n"
                + "We’re delighted to have you. Please take a moment to read the rules below.";
        String rules =
                "• Check-in: 2:00 PM — Check-out: 12:00 PM\n" +
                        "• Quiet hours: 10:00 PM – 7:00 AM\n" +
                        "• No smoking in rooms (penalty may apply)\n" +
                        "• Pets: Only in pet-friendly rooms (fees may apply)\n" +
                        "• Visitors allowed until 10:00 PM\n" +
                        "• Please report damages immediately at the front desk\n" +
                        "• Keep valuables in the room safe; the hotel isn’t responsible for lost items\n" +
                        "• Follow fire safety instructions and emergency exits";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hotel Rules & Regulations");
        alert.setHeaderText(welcome);

        Label content = new Label(rules);
        content.setWrapText(true);
        content.setMaxWidth(520);


        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setContent(content);


        alert.getButtonTypes().setAll(new ButtonType("OK, thanks", ButtonBar.ButtonData.OK_DONE));

        alert.showAndWait();
        rulesLink.setVisited(false);
    }


}
