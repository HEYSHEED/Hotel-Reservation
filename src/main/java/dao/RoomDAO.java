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

import model.Room;
import model.RoomType;
import util.DBUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAO {

    private static final Logger logger = LoggerUtil.getLogger(RoomDAO.class);

    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("roomId"));
                room.setRoomType(RoomType.valueOf(rs.getString("roomType")));
                room.setNumberOfBeds(rs.getInt("numberOfBeds"));
                room.setPrice(rs.getDouble("price"));
                room.setStatus(rs.getString("status"));
                rooms.add(room);
            }

            logger.info("Loaded " + rooms.size() + " rooms from database.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading all rooms", e);
        }

        return rooms;
    }

    public static List<Room> getAvailableRoomsByType(RoomType type) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE roomType = ? AND status = 'Available'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("roomId"));
                    room.setRoomType(RoomType.valueOf(rs.getString("roomType")));
                    room.setNumberOfBeds(rs.getInt("numberOfBeds"));
                    room.setPrice(rs.getDouble("price"));
                    room.setStatus(rs.getString("status"));
                    rooms.add(room);
                }
            }

            logger.info("Available rooms for type " + type + ": " + rooms.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading available rooms for type: " + type, e);
        }

        return rooms;
    }

    public static void updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE roomId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Room " + roomId + " status updated to " + status);
            } else {
                logger.warning("No room found to update. roomId=" + roomId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating room status. roomId=" + roomId + ", status=" + status, e);
        }
    }

    public static Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE roomId = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("roomId"));
                    room.setRoomType(RoomType.valueOf(rs.getString("roomType")));
                    room.setNumberOfBeds(rs.getInt("numberOfBeds"));
                    room.setPrice(rs.getDouble("price"));
                    room.setStatus(rs.getString("status"));
                    logger.info("Room loaded. roomId=" + roomId);
                    return room;
                } else {
                    logger.warning("Room not found. roomId=" + roomId);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting room by id: " + roomId, e);
        }
        return null;
    }
}
