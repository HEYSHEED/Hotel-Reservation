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

import dao.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact support at: 2269610694");
        alert.showAndWait();
    }


    @FXML
    public void initialize() {

    }
    private static final Logger logger = Logger.getLogger(AdminLoginController.class.getName());

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // VALIDATION
        if (username.isEmpty() || password.isEmpty()) {
            logger.warning("Login attempt with empty username or password");
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and Password are required.");
            return;
        }

        // AUTHENTICATE
        boolean isValid = AdminDAO.login(username, password);

        if (isValid) {
            logger.info("Admin logged in: " + username);
            try {
                AnchorPane root = FXMLLoader.load(getClass().getResource("/demo/hotel/Admin-Dashboard.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to load Admin-Dashboard.fxml", e);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard.");
            }
        } else {
            logger.warning("Invalid login attempt for username: " + username);
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("/demo/hotel/welcome.fxml"));
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to go back.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
