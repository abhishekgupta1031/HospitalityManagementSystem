package dao;

import model.Hotel;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // Add a new hotel
    public boolean addHotel(Hotel hotel) {

        String sql = "INSERT INTO hotels (name, location, amenities) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getLocation());
            statement.setString(3, hotel.getAmenities());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all hotels
    public List<Hotel> getAllHotels() {

        List<Hotel> hotels = new ArrayList<>();

        String sql = "SELECT * FROM hotels";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Hotel hotel = new Hotel();

                hotel.setHotelId(resultSet.getInt("hotel_id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setLocation(resultSet.getString("location"));
                hotel.setAmenities(resultSet.getString("amenities"));

                hotels.add(hotel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    // Update an existing hotel
    public boolean updateHotel(Hotel hotel) {

        String sql = "UPDATE hotels SET name = ?, location = ?, amenities = ? WHERE hotel_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getLocation());
            statement.setString(3, hotel.getAmenities());
            statement.setInt(4, hotel.getHotelId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a hotel
    public boolean deleteHotel(int hotelId) {

        String sql = "DELETE FROM hotels WHERE hotel_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, hotelId);

            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}