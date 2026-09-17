package dao;

import model.Guest;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    // Add a new guest
    public boolean addGuest(Guest guest) {

        String sql = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());
            statement.setString(3, guest.getPhone());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all guests
    public List<Guest> getAllGuests() {

        List<Guest> guests = new ArrayList<>();

        String sql = "SELECT * FROM guests";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Guest guest = new Guest();

                guest.setGuestId(resultSet.getInt("guest_id"));
                guest.setName(resultSet.getString("name"));
                guest.setEmail(resultSet.getString("email"));
                guest.setPhone(resultSet.getString("phone"));

                guests.add(guest);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guests;
    }

    // Get a guest by ID
    public Guest getGuestById(int guestId) {

        String sql = "SELECT * FROM guests WHERE guest_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, guestId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Guest guest = new Guest();

                    guest.setGuestId(resultSet.getInt("guest_id"));
                    guest.setName(resultSet.getString("name"));
                    guest.setEmail(resultSet.getString("email"));
                    guest.setPhone(resultSet.getString("phone"));

                    return guest;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update an existing guest
    public boolean updateGuest(Guest guest) {

        String sql = "UPDATE guests SET name = ?, email = ?, phone = ? " +
                "WHERE guest_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, guest.getName());
            statement.setString(2, guest.getEmail());
            statement.setString(3, guest.getPhone());
            statement.setInt(4, guest.getGuestId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a guest
    public boolean deleteGuest(int guestId) {

        String sql = "DELETE FROM guests WHERE guest_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, guestId);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}