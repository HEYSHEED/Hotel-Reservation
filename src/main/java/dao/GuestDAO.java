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

import model.Guest;
import util.DBUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestDAO {

    private static final Logger logger = LoggerUtil.getLogger(GuestDAO.class);

    public static void saveGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, phoneNumber, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getPhoneNumber());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getAddress());

            stmt.executeUpdate();

            try (Statement idStmt = conn.createStatement();
                 ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    guest.setGuestId(rs.getInt(1));  // ست کردن guestId
                }
            }

            logger.info("Guest saved successfully: " + guest.getName() + " (ID: " + guest.getGuestId() + ")");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while saving guest: " + guest.getName(), e);
        }
    }

    public static List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = new Guest();
                guest.setGuestId(rs.getInt("guestId"));
                guest.setName(rs.getString("name"));
                guest.setPhoneNumber(rs.getString("phoneNumber"));
                guest.setEmail(rs.getString("email"));
                guest.setAddress(rs.getString("address"));
                guests.add(guest);
            }

            logger.info("Retrieved " + guests.size() + " guests from database.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving guests", e);
        }

        return guests;
    }

    public static Guest getGuestById(int guestId) {
        String sql = "SELECT * FROM guests WHERE guestId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Guest guest = new Guest();
                guest.setGuestId(rs.getInt("guestId"));
                guest.setName(rs.getString("name"));
                guest.setPhoneNumber(rs.getString("phoneNumber"));
                guest.setEmail(rs.getString("email"));
                guest.setAddress(rs.getString("address"));

                logger.info("Guest found with ID: " + guestId);
                return guest;
            } else {
                logger.warning("No guest found with ID: " + guestId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving guest with ID: " + guestId, e);
        }

        return null;
    }

    public static Guest findByPhone(String phone) {
        String sql = "SELECT * FROM guests WHERE phoneNumber = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Guest g = new Guest();
                g.setGuestId(rs.getInt("guestId"));
                g.setName(rs.getString("name"));
                g.setPhoneNumber(rs.getString("phoneNumber"));
                g.setEmail(rs.getString("email"));
                g.setAddress(rs.getString("address"));

                logger.info("Guest found with phone: " + phone);
                return g;
            } else {
                logger.warning("No guest found with phone: " + phone);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while searching guest by phone: " + phone, e);
        }

        return null;
    }
}
