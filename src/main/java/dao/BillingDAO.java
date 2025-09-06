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

import model.Billing;
import model.Reservation;
import util.DBUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingDAO {

    private static final Logger logger = LoggerUtil.getLogger(BillingDAO.class);

    public static void insertBill(Billing bill) {
        String sql = "INSERT INTO billing (reservationId, amount, tax, discount, totalAmount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getReservation().getReservationId());
            stmt.setDouble(2, bill.getAmount());
            stmt.setDouble(3, bill.getTax());
            stmt.setDouble(4, bill.getDiscount());
            stmt.setDouble(5, bill.getTotalAmount());

            stmt.executeUpdate();
            logger.info("Inserted bill for reservation ID: " + bill.getReservation().getReservationId());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while inserting bill for reservation ID: "
                    + bill.getReservation().getReservationId(), e);
        }
    }

    public static Billing getBillByReservationId(int reservationId) {
        String sql = "SELECT * FROM billing WHERE reservationId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Billing b = new Billing();
                Reservation r = new Reservation();
                r.setReservationId(reservationId);
                b.setReservation(r);
                b.setBillId(rs.getInt("billId"));
                b.setAmount(rs.getDouble("amount"));
                b.setTax(rs.getDouble("tax"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTotalAmount(rs.getDouble("totalAmount"));

                logger.info("Retrieved bill for reservation ID: " + reservationId);
                return b;
            } else {
                logger.warning("No bill found for reservation ID: " + reservationId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving bill for reservation ID: " + reservationId, e);
        }

        return null;
    }
}
