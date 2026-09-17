package dao;

import model.Room;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add a new room
    public boolean addRoom(Room room) {

        String sql = "INSERT INTO rooms " +
                "(hotel_id, room_number, room_type, price, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, room.getHotelId());
            statement.setString(2, room.getRoomNumber());
            statement.setString(3, room.getRoomType());
            statement.setDouble(4, room.getPrice());
            statement.setString(5, room.getStatus());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all rooms
    public List<Room> getAllRooms() {

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Room room = new Room();

                room.setRoomId(resultSet.getInt("room_id"));
                room.setHotelId(resultSet.getInt("hotel_id"));
                room.setRoomNumber(resultSet.getString("room_number"));
                room.setRoomType(resultSet.getString("room_type"));
                room.setPrice(resultSet.getDouble("price"));
                room.setStatus(resultSet.getString("status"));

                rooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // Get rooms for a specific hotel
    public List<Room> getRoomsByHotelId(int hotelId) {

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms WHERE hotel_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, hotelId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Room room = new Room();

                    room.setRoomId(resultSet.getInt("room_id"));
                    room.setHotelId(resultSet.getInt("hotel_id"));
                    room.setRoomNumber(resultSet.getString("room_number"));
                    room.setRoomType(resultSet.getString("room_type"));
                    room.setPrice(resultSet.getDouble("price"));
                    room.setStatus(resultSet.getString("status"));

                    rooms.add(room);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // Update an existing room
    public boolean updateRoom(Room room) {

        String sql = "UPDATE rooms SET " +
                "hotel_id = ?, room_number = ?, room_type = ?, " +
                "price = ?, status = ? " +
                "WHERE room_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, room.getHotelId());
            statement.setString(2, room.getRoomNumber());
            statement.setString(3, room.getRoomType());
            statement.setDouble(4, room.getPrice());
            statement.setString(5, room.getStatus());
            statement.setInt(6, room.getRoomId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a room
    public boolean deleteRoom(int roomId) {

        String sql = "DELETE FROM rooms WHERE room_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check whether a room is available
    public boolean isRoomAvailable(int roomId) {

        String sql = "SELECT status FROM rooms WHERE room_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    String status = resultSet.getString("status");

                    return "Available".equalsIgnoreCase(status);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Change room status
    public boolean updateRoomStatus(int roomId, String status) {

        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, roomId);

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}