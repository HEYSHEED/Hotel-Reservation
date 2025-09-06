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
import model.Reservation;
import model.Room;
import util.DBUtil;
import util.LoggerUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDAO {

    private static final Logger logger = LoggerUtil.getLogger(ReservationDAO.class);

    public static Reservation insertReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (guestId, roomId, checkInDate, checkOutDate, numberOfGuests, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getGuest().getGuestId());
            stmt.setInt(2, reservation.getRoom().getRoomId());
            stmt.setString(3, reservation.getCheckInDate().toString());
            stmt.setString(4, reservation.getCheckOutDate().toString());
            stmt.setInt(5, reservation.getNumberOfGuests());
            stmt.setString(6, reservation.getStatus());

            stmt.executeUpdate();

            try (Statement idStmt = conn.createStatement();
                 ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    reservation.setReservationId(rs.getInt(1));
                }
            }

            reservation.setGuest(GuestDAO.getGuestById(reservation.getGuest().getGuestId()));
            reservation.setRoom(RoomDAO.getRoomById(reservation.getRoom().getRoomId()));

            logger.info("Reservation inserted successfully: " + reservation.getReservationId());
            return reservation;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting reservation for guest ID: " + reservation.getGuest().getGuestId(), e);
        }

        return null;
    }

    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = GuestDAO.getGuestById(rs.getInt("guestId"));
                Room room = RoomDAO.getRoomById(rs.getInt("roomId"));

                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservationId"));
                r.setGuest(guest);
                r.setRoom(room);
                r.setCheckInDate(LocalDate.parse(rs.getString("checkInDate")));
                r.setCheckOutDate(LocalDate.parse(rs.getString("checkOutDate")));
                r.setNumberOfGuests(rs.getInt("numberOfGuests"));
                r.setStatus(rs.getString("status"));

                reservations.add(r);
            }

            logger.info("Retrieved " + reservations.size() + " reservations from database.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving reservations list", e);
        }

        return reservations;
    }

    public static List<Reservation> searchByNameOrPhone(String keyword) {
        List<Reservation> results = new ArrayList<>();
        String sql = """
        SELECT r.*
        FROM reservations r
        JOIN guests g ON r.guestId = g.guestId
        WHERE g.name LIKE ? OR g.phoneNumber LIKE ?
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Guest guest = GuestDAO.getGuestById(rs.getInt("guestId"));
                Room room = RoomDAO.getRoomById(rs.getInt("roomId"));

                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservationId"));
                r.setGuest(guest);
                r.setRoom(room);
                r.setCheckInDate(LocalDate.parse(rs.getString("checkInDate")));
                r.setCheckOutDate(LocalDate.parse(rs.getString("checkOutDate")));
                r.setNumberOfGuests(rs.getInt("numberOfGuests"));
                r.setStatus(rs.getString("status"));

                results.add(r);
            }

            logger.info("Search completed for keyword: " + keyword + " - Results found: " + results.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching reservations for keyword: " + keyword, e);
        }

        return results;
    }

    public static void cancelReservation(int reservationId) {
        String sql = "UPDATE reservations SET status = 'Cancelled' WHERE reservationId = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Reservation cancelled: " + reservationId);
            } else {
                logger.warning("No reservation found to cancel with ID: " + reservationId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error cancelling reservation ID: " + reservationId, e);
        }
    }

    public static boolean updateReservation(Reservation r) {
        String sql = "UPDATE reservations SET checkInDate = ?, checkOutDate = ?, numberOfGuests = ?, status = ? WHERE reservationId = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, r.getCheckInDate().toString());
            stmt.setString(2, r.getCheckOutDate().toString());
            stmt.setInt(3, r.getNumberOfGuests());
            stmt.setString(4, r.getStatus());
            stmt.setInt(5, r.getReservationId());

            int rows = stmt.executeUpdate();
            logger.info("Reservation updated: " + r.getReservationId());
            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating reservation ID: " + r.getReservationId(), e);
            return false;
        }
    }

    public static int findLatestReservationIdByGuestId(int guestId) {
        String sql = "SELECT reservationId FROM reservations WHERE guestId = ? ORDER BY reservationId DESC LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("reservationId");
            } else {
                logger.warning("No reservation found for guest ID: " + guestId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding latest reservation for guest ID: " + guestId, e);
        }
        return 0;
    }

    public static Reservation getReservationById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservationId = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Guest guest = GuestDAO.getGuestById(rs.getInt("guestId"));
                Room room = RoomDAO.getRoomById(rs.getInt("roomId"));

                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservationId"));
                r.setGuest(guest);
                r.setRoom(room);
                r.setCheckInDate(LocalDate.parse(rs.getString("checkInDate")));
                r.setCheckOutDate(LocalDate.parse(rs.getString("checkOutDate")));
                r.setNumberOfGuests(rs.getInt("numberOfGuests"));
                r.setStatus(rs.getString("status"));

                logger.info("Reservation found with ID: " + reservationId);
                return r;
            } else {
                logger.warning("No reservation found with ID: " + reservationId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving reservation with ID: " + reservationId, e);
        }
        return null;
    }
    public static Reservation findLatestCheckedOutByGuestId(int guestId) {
        String sql = """
        SELECT reservationId, guestId, roomId, checkInDate, checkOutDate, status, numberOfGuests
        FROM reservations
        WHERE guestId = ?
        ORDER BY 
            CASE WHEN LOWER(TRIM(status)) = 'checkedout' THEN 0 ELSE 1 END,  -- اول CheckedOut ها
            COALESCE(checkOutDate, checkInDate) DESC,                        -- جدیدترین تاریخ
            reservationId DESC
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            try (ResultSet rs = stmt.executeQuery()) {
                Reservation picked = null;
                int row = 0;

                while (rs.next()) {
                    row++;
                    int resId = rs.getInt("reservationId");
                    String rawStatus = rs.getString("status");
                    String normStatus = rawStatus == null ? "" : rawStatus.trim().toLowerCase();

                    String inStr  = rs.getString("checkInDate");
                    String outStr = rs.getString("checkOutDate");

                    logger.info(String.format(
                            "[DEBUG] guestId=%d row#%d -> resId=%d, status='%s', checkIn=%s, checkOut=%s",
                            guestId, row, resId, rawStatus, inStr, outStr
                    ));

                    // فقط اگر CheckedOut هست به عنوان گزینه‌ی انتخابی درنظر بگیر
                    if ("checkedout".equals(normStatus)) {
                        Guest guest = GuestDAO.getGuestById(rs.getInt("guestId"));
                        Room room   = RoomDAO.getRoomById(rs.getInt("roomId"));

                        Reservation r = new Reservation();
                        r.setReservationId(resId);
                        r.setGuest(guest);
                        r.setRoom(room);
                        if (inStr != null && !inStr.isBlank())  r.setCheckInDate(LocalDate.parse(inStr));
                        if (outStr != null && !outStr.isBlank()) r.setCheckOutDate(LocalDate.parse(outStr));
                        r.setNumberOfGuests(rs.getInt("numberOfGuests"));
                        r.setStatus(rawStatus);

                        picked = r; // چون ترتیب طوریه که جدیدترین‌ها اول‌اند، اولین CheckedOut کافی‌ست
                        break;
                    }
                }

                if (picked == null) {
                    logger.warning(String.format(
                            "[DEBUG] No CheckedOut reservation found for guestId=%d. (There may be only Active/Cancelled records.)",
                            guestId
                    ));
                } else {
                    logger.info(String.format(
                            "[DEBUG] Picked CheckedOut reservation: resId=%d for guestId=%d",
                            picked.getReservationId(), guestId
                    ));
                }
                return picked;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error debugging reservations for guest ID: " + guestId, e);
        }
        return null;
    }
    public static Reservation findLatestCheckedOutByPhone(String phone) {

        String sql = """
    SELECT r.reservationId, r.guestId, r.roomId, r.checkInDate, r.checkOutDate, r.status, r.numberOfGuests
    FROM reservations r
    JOIN guests g ON g.guestId = r.guestId
    WHERE REPLACE(REPLACE(REPLACE(REPLACE(g.phoneNumber, ' ', ''), '-', ''), '(', ''), ')', '') =
          REPLACE(REPLACE(REPLACE(REPLACE(?,            ' ', ''), '-', ''), '(', ''), ')', '')
    ORDER BY 
        CASE 
            WHEN REPLACE(REPLACE(REPLACE(LOWER(r.status), ' ', ''), '_', ''), '-', '') = 'checkedout' THEN 0
            ELSE 1
        END,
        COALESCE(r.checkOutDate, r.checkInDate) DESC,
        r.reservationId DESC
    LIMIT 50
""";


        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String rawStatus = rs.getString("status");
                    // نرمال‌سازی وضعیت: حذف هر چیزی غیر از حروف/عدد و کوچیک کردن
                    String normStatus = rawStatus == null ? "" : rawStatus.toLowerCase().replaceAll("[^a-z0-9]", "");
                    if (!"checkedout".equals(normStatus)) {
                        continue; // فقط رزروهای CheckedOut
                    }

                    int resId  = rs.getInt("reservationId");
                    int guestId = rs.getInt("guestId");
                    int roomId  = rs.getInt("roomId");

                    Guest guest = GuestDAO.getGuestById(guestId);
                    Room  room  = RoomDAO.getRoomById(roomId);

                    Reservation r = new Reservation();
                    r.setReservationId(resId);
                    r.setGuest(guest);
                    r.setRoom(room);

                    String inStr  = rs.getString("checkInDate");
                    String outStr = rs.getString("checkOutDate");
                    if (inStr  != null && !inStr.isBlank())  r.setCheckInDate(LocalDate.parse(inStr));
                    if (outStr != null && !outStr.isBlank()) r.setCheckOutDate(LocalDate.parse(outStr));

                    r.setNumberOfGuests(rs.getInt("numberOfGuests"));
                    r.setStatus(rawStatus);
                    return r;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding latest CheckedOut reservation by phone: " + phone, e);
        }
        return null;
    }



}
