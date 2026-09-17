package dao;

import model.Reservation;
import util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Add reservation
    public boolean addReservation(Reservation reservation) {

        String sql = "INSERT INTO reservations " +
                "(guest_id, room_id, check_in, check_out) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservation.getGuestId());
            statement.setInt(2, reservation.getRoomId());
            statement.setDate(3, java.sql.Date.valueOf(reservation.getCheckIn()));
            statement.setDate(4, java.sql.Date.valueOf(reservation.getCheckOut()));

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {

        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT * FROM reservations ORDER BY reservation_id";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Reservation reservation = new Reservation();

                reservation.setReservationId(
                        resultSet.getInt("reservation_id"));

                reservation.setGuestId(
                        resultSet.getInt("guest_id"));

                reservation.setRoomId(
                        resultSet.getInt("room_id"));

                reservation.setCheckIn(
                        resultSet.getDate("check_in").toLocalDate());

                reservation.setCheckOut(
                        resultSet.getDate("check_out").toLocalDate());

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // Update reservation
    public boolean updateReservation(Reservation reservation) {

        String sql = "UPDATE reservations SET " +
                "guest_id = ?, room_id = ?, check_in = ?, check_out = ? " +
                "WHERE reservation_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservation.getGuestId());
            statement.setInt(2, reservation.getRoomId());
            statement.setDate(3, java.sql.Date.valueOf(reservation.getCheckIn()));
            statement.setDate(4, java.sql.Date.valueOf(reservation.getCheckOut()));
            statement.setInt(5, reservation.getReservationId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete reservation
    public boolean deleteReservation(int reservationId) {

        String sql =
                "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reservationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check room availability
    public boolean isRoomAvailable(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut) {

        String sql =
                "SELECT COUNT(*) FROM reservations " +
                "WHERE room_id = ? " +
                "AND check_in < ? " +
                "AND check_out > ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);
            statement.setDate(2, java.sql.Date.valueOf(checkOut));
            statement.setDate(3, java.sql.Date.valueOf(checkIn));

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Calculate reservation cost
    public double calculateReservationCost(
            int roomId,
            LocalDate checkIn,
            LocalDate checkOut) {

        String sql = "SELECT price FROM rooms WHERE room_id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    double pricePerNight =
                            resultSet.getDouble("price");

                    long nights =
                            java.time.temporal.ChronoUnit.DAYS.between(
                                    checkIn,
                                    checkOut);

                    return pricePerNight * nights;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}
