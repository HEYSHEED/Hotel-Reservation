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

import model.Feedback;
import model.Guest;
import model.Reservation;
import util.DBUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeedbackDAO {

    private static final Logger logger = LoggerUtil.getLogger(FeedbackDAO.class);

    public static void insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (guestId, reservationId, comments, rating) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feedback.getGuest().getGuestId());
            stmt.setInt(2, feedback.getReservation().getReservationId());
            stmt.setString(3, feedback.getComments());
            stmt.setInt(4, feedback.getRating());

            stmt.executeUpdate();
            logger.info("Inserted feedback for Guest ID: " + feedback.getGuest().getGuestId() +
                    ", Reservation ID: " + feedback.getReservation().getReservationId());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while inserting feedback for Guest ID: "
                    + feedback.getGuest().getGuestId(), e);
        }
    }

    public static List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        String sql = """
            SELECT f.feedbackId, f.comments, f.rating,
                   g.guestId, g.name, g.phoneNumber,
                   f.reservationId
            FROM feedback f
            LEFT JOIN guests g ON g.guestId = f.guestId
            ORDER BY f.feedbackId DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Guest g = new Guest();
                g.setGuestId(rs.getInt("guestId"));
                g.setName(rs.getString("name"));
                g.setPhoneNumber(rs.getString("phoneNumber"));

                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservationId"));

                Feedback f = new Feedback();
                f.setFeedbackId(rs.getInt("feedbackId"));
                f.setGuest(g);
                f.setReservation(r);
                f.setComments(rs.getString("comments"));
                f.setRating(rs.getInt("rating"));

                list.add(f);
            }

            logger.info("Retrieved " + list.size() + " feedback entries from the database.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving feedback list", e);
        }

        return list;
    }
}
