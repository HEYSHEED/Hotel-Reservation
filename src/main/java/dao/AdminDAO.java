package dao;
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

import model.Admin;
import util.DBUtil;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AdminDAO {
    private static final Logger logger = Logger.getLogger(AdminDAO.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("system_logs.%g.log", 1024 * 1024, 10, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize file handler for logger", e);
        }
    }

    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            boolean found = rs.next();
            if (found) {
                logger.info("Admin login successful: " + username);
            } else {
                logger.warning("Failed admin login attempt: " + username);
            }
            return found;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during admin login for username: " + username, e);
        }
        return false;
    }
}
