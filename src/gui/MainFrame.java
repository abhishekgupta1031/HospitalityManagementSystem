package gui;

import dao.HotelDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Hotel;

public class MainFrame extends JFrame {

    // =====================================================
    // PROFESSIONAL UI THEME
    // =====================================================

    private static final Color PRIMARY_COLOR = new Color(35, 55, 75);
    private static final Color SECONDARY_COLOR = new Color(70, 130, 180);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(45, 45, 45);
    private static final Color BORDER_COLOR = new Color(210, 215, 220);

    private HotelDAO hotelDAO;

    private JTextField hotelNameField;
    private JTextField hotelLocationField;
    private JTextField hotelAmenitiesField;
    private JTable hotelTable;
    private DefaultTableModel hotelTableModel;

    public MainFrame() {

        hotelDAO = new HotelDAO();

        setTitle("Hospitality Management System");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Hotels", createHotelsPanel());
        tabs.addTab("Rooms", createRoomsPanel());
        tabs.addTab("Guests", createGuestsPanel());
        tabs.addTab("Reservations", createReservationsPanel());

        add(tabs, BorderLayout.CENTER);
        styleComponents(getContentPane());
    }

    // =====================================================
    // HOTELS
    // =====================================================

    private JPanel createHotelsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel(
                "Hotel Management",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(title, BorderLayout.NORTH);

        // -------------------------
        // Form
        // -------------------------

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createTitledBorder("Hotel Details")
        );

        formPanel.add(new JLabel("Hotel Name:"));

        hotelNameField = new JTextField();
        formPanel.add(hotelNameField);

        formPanel.add(new JLabel("Location:"));

        hotelLocationField = new JTextField();
        formPanel.add(hotelLocationField);

        formPanel.add(new JLabel("Amenities:"));

        hotelAmenitiesField = new JTextField();
        formPanel.add(hotelAmenitiesField);

        JButton addButton = new JButton("Add Hotel");
        JButton updateButton = new JButton("Update Hotel");

        formPanel.add(addButton);
        formPanel.add(updateButton);

        topPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // -------------------------
        // Table
        // -------------------------

        hotelTableModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Name",
                        "Location",
                        "Amenities"
                },
                0
        );

        hotelTable = new JTable(hotelTableModel);

        JScrollPane scrollPane = new JScrollPane(hotelTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // -------------------------
        // Buttons
        // -------------------------

        JPanel buttonPanel = new JPanel();

        JButton deleteButton = new JButton("Delete Hotel");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // -------------------------
        // Button Actions
        // -------------------------

        addButton.addActionListener(e -> addHotel());

        updateButton.addActionListener(e -> updateHotel());

        deleteButton.addActionListener(e -> deleteHotel());

        refreshButton.addActionListener(e -> loadHotels());

        // Load database data
        loadHotels();

        return mainPanel;
    }

    // =====================================================
    // ADD HOTEL
    // =====================================================

    private void addHotel() {

        String name = hotelNameField.getText().trim();
        String location = hotelLocationField.getText().trim();
        String amenities = hotelAmenitiesField.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hotel name and location are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Hotel hotel = new Hotel(
                0,
                name,
                location,
                amenities
        );

        boolean success = hotelDAO.addHotel(hotel);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hotel added successfully!"
            );

            clearHotelFields();
            loadHotels();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add hotel.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // UPDATE HOTEL
    // =====================================================

    private void updateHotel() {

        int selectedRow = hotelTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a hotel from the table."
            );

            return;
        }

        int hotelId =
                Integer.parseInt(
                        hotelTableModel
                                .getValueAt(selectedRow, 0)
                                .toString()
                );

        String name = hotelNameField.getText().trim();
        String location = hotelLocationField.getText().trim();
        String amenities = hotelAmenitiesField.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hotel name and location are required."
            );

            return;
        }

        Hotel hotel = new Hotel(
                hotelId,
                name,
                location,
                amenities
        );

        boolean success = hotelDAO.updateHotel(hotel);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hotel updated successfully!"
            );

            clearHotelFields();
            loadHotels();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update hotel.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // DELETE HOTEL
    // =====================================================

    private void deleteHotel() {

        int selectedRow = hotelTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a hotel from the table."
            );

            return;
        }

        int hotelId =
                Integer.parseInt(
                        hotelTableModel
                                .getValueAt(selectedRow, 0)
                                .toString()
                );

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this hotel?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = hotelDAO.deleteHotel(hotelId);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Hotel deleted successfully!"
            );

            clearHotelFields();
            loadHotels();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not delete hotel.\n"
                            + "Make sure no rooms are linked to this hotel.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // LOAD HOTELS
    // =====================================================

    private void loadHotels() {

        if (hotelTableModel == null) {
            return;
        }

        hotelTableModel.setRowCount(0);

        List<Hotel> hotels = hotelDAO.getAllHotels();

        for (Hotel hotel : hotels) {

            hotelTableModel.addRow(
                    new Object[]{
                            hotel.getHotelId(),
                            hotel.getName(),
                            hotel.getLocation(),
                            hotel.getAmenities()
                    }
            );
        }
    }

    // =====================================================
    // TABLE CLICK
    // =====================================================

    private void selectHotelFromTable() {

        int selectedRow = hotelTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        hotelNameField.setText(
                hotelTableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        hotelLocationField.setText(
                hotelTableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        Object amenities =
                hotelTableModel.getValueAt(selectedRow, 3);

        hotelAmenitiesField.setText(
                amenities == null
                        ? ""
                        : amenities.toString()
        );
    }

    // =====================================================
    // CLEAR FIELDS
    // =====================================================

    private void clearHotelFields() {

        hotelNameField.setText("");
        hotelLocationField.setText("");
        hotelAmenitiesField.setText("");

        hotelTable.clearSelection();
    }

    // =====================================================
    // ROOMS
    // =====================================================

    private JPanel createRoomsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel title = new JLabel(
                "Room Management",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createTitledBorder("Room Details")
        );

        formPanel.add(new JLabel("Hotel ID:"));
        JTextField hotelIdField = new JTextField();
        formPanel.add(hotelIdField);

        formPanel.add(new JLabel("Room Number:"));
        JTextField roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Room Type:"));
        JTextField roomTypeField = new JTextField();
        formPanel.add(roomTypeField);

        formPanel.add(new JLabel("Price per Night:"));
        JTextField priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Status:"));
        JComboBox<String> statusComboBox = new JComboBox<>(
                new String[]{"Available", "Occupied"}
        );
        formPanel.add(statusComboBox);

        topPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel roomTableModel = new DefaultTableModel(
                new String[]{
                        "ID", "Hotel ID", "Room Number",
                        "Room Type", "Price", "Status"
                },
                0
        );

        JTable roomTable = new JTable(roomTableModel);
        mainPanel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Room");
        JButton updateButton = new JButton("Update Room");
        JButton deleteButton = new JButton("Delete Room");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dao.RoomDAO roomDAO = new dao.RoomDAO();

        Runnable loadRooms = () -> {
            roomTableModel.setRowCount(0);

            java.util.List<model.Room> rooms =
                    roomDAO.getAllRooms();

            for (model.Room room : rooms) {
                roomTableModel.addRow(new Object[]{
                        room.getRoomId(),
                        room.getHotelId(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getPrice(),
                        room.getStatus()
                });
            }
        };

        loadRooms.run();

        addButton.addActionListener(e -> {
            try {
                String hotelIdText = hotelIdField.getText().trim();
                String roomNumber = roomNumberField.getText().trim();
                String roomType = roomTypeField.getText().trim();
                String priceText = priceField.getText().trim();
                String status = statusComboBox.getSelectedItem().toString();

                if (hotelIdText.isEmpty()
                        || roomNumber.isEmpty()
                        || roomType.isEmpty()
                        || priceText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill all room details.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int hotelId = Integer.parseInt(hotelIdText);
                double price = Double.parseDouble(priceText);

                model.Room room = new model.Room(
                        0,
                        hotelId,
                        roomNumber,
                        roomType,
                        price,
                        status
                );

                if (roomDAO.addRoom(room)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Room added successfully!"
                    );

                    hotelIdField.setText("");
                    roomNumberField.setText("");
                    roomTypeField.setText("");
                    priceField.setText("");
                    statusComboBox.setSelectedItem("Available");

                    loadRooms.run();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to add room.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Hotel ID must be a number and price must be valid.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = roomTable.getSelectedRow();

                if (row != -1) {
                    hotelIdField.setText(
                            roomTableModel.getValueAt(row, 1).toString()
                    );
                    roomNumberField.setText(
                            roomTableModel.getValueAt(row, 2).toString()
                    );
                    roomTypeField.setText(
                            roomTableModel.getValueAt(row, 3).toString()
                    );
                    priceField.setText(
                            roomTableModel.getValueAt(row, 4).toString()
                    );
                    statusComboBox.setSelectedItem(
                            roomTableModel.getValueAt(row, 5).toString()
                    );
                }
            }
        });

        updateButton.addActionListener(e -> {
            int row = roomTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a room from the table."
                );
                return;
            }

            try {
                int roomId = Integer.parseInt(
                        roomTableModel.getValueAt(row, 0).toString()
                );

                int hotelId = Integer.parseInt(
                        hotelIdField.getText().trim()
                );

                String roomNumber = roomNumberField.getText().trim();
                String roomType = roomTypeField.getText().trim();

                double price = Double.parseDouble(
                        priceField.getText().trim()
                );

                String status = statusComboBox.getSelectedItem().toString();

                model.Room room = new model.Room(
                        roomId,
                        hotelId,
                        roomNumber,
                        roomType,
                        price,
                        status
                );

                if (roomDAO.updateRoom(room)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Room updated successfully!"
                    );
                    loadRooms.run();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to update room.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        deleteButton.addActionListener(e -> {
            int row = roomTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a room from the table."
                );
                return;
            }

            int roomId = Integer.parseInt(
                    roomTableModel.getValueAt(row, 0).toString()
            );

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this room?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            if (roomDAO.deleteRoom(roomId)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Room deleted successfully!"
                );
                loadRooms.run();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not delete room.\n"
                                + "Make sure no reservation is using this room.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        refreshButton.addActionListener(e -> loadRooms.run());

        return mainPanel;
    }

    // =====================================================
    // GUESTS
    // =====================================================

    private JPanel createGuestsPanel() {

    JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
    mainPanel.setBackground(BACKGROUND_COLOR);
    mainPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );

    JLabel title = new JLabel(
            "Guest Management",
            SwingConstants.CENTER
    );

    title.setFont(new Font("Arial", Font.BOLD, 26));
    JPanel topPanel = new JPanel(new BorderLayout(10, 10));
    topPanel.setBackground(BACKGROUND_COLOR);
    topPanel.add(title, BorderLayout.NORTH);

    // =========================
    // Guest Form
    // =========================

    JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

    formPanel.setBorder(
            BorderFactory.createTitledBorder("Guest Details")
    );

    formPanel.add(new JLabel("Name:"));
    JTextField nameField = new JTextField();
    formPanel.add(nameField);

    formPanel.add(new JLabel("Email:"));
    JTextField emailField = new JTextField();
    formPanel.add(emailField);

    formPanel.add(new JLabel("Phone:"));
    JTextField phoneField = new JTextField();
    formPanel.add(phoneField);

    topPanel.add(formPanel, BorderLayout.CENTER);
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // =========================
    // Guest Table
    // =========================

    DefaultTableModel guestTableModel =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Name",
                            "Email",
                            "Phone"
                    },
                    0
            );

    JTable guestTable = new JTable(guestTableModel);

    mainPanel.add(
            new JScrollPane(guestTable),
            BorderLayout.CENTER
    );

    // =========================
    // Buttons
    // =========================

    JPanel buttonPanel = new JPanel();

    JButton addButton =
            new JButton("Add Guest");

    JButton updateButton =
            new JButton("Update Guest");

    JButton deleteButton =
            new JButton("Delete Guest");

    JButton refreshButton =
            new JButton("Refresh");

    buttonPanel.add(addButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    // =========================
    // DAO
    // =========================

    dao.GuestDAO guestDAO =
            new dao.GuestDAO();

    // =========================
    // Load Guests
    // =========================

    Runnable loadGuests = () -> {

        guestTableModel.setRowCount(0);

        java.util.List<model.Guest> guests =
                guestDAO.getAllGuests();

        for (model.Guest guest : guests) {

            guestTableModel.addRow(
                    new Object[]{
                            guest.getGuestId(),
                            guest.getName(),
                            guest.getEmail(),
                            guest.getPhone()
                    }
            );
        }
    };

    loadGuests.run();

    // =========================
    // Add Guest
    // =========================

    addButton.addActionListener(e -> {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String phone =
                phoneField.getText().trim();

        if (name.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all guest details.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        model.Guest guest =
                new model.Guest(
                        0,
                        name,
                        email,
                        phone
                );

        boolean success =
                guestDAO.addGuest(guest);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Guest added successfully!"
            );

            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");

            loadGuests.run();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add guest.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });

    // =========================
    // Select Guest
    // =========================

    guestTable.getSelectionModel()
            .addListSelectionListener(e -> {

                if (!e.getValueIsAdjusting()) {

                    int row =
                            guestTable.getSelectedRow();

                    if (row != -1) {

                        nameField.setText(
                                guestTableModel
                                        .getValueAt(row, 1)
                                        .toString()
                        );

                        emailField.setText(
                                guestTableModel
                                        .getValueAt(row, 2)
                                        .toString()
                        );

                        phoneField.setText(
                                guestTableModel
                                        .getValueAt(row, 3)
                                        .toString()
                        );
                    }
                }
            });

    // =========================
    // Update Guest
    // =========================

    updateButton.addActionListener(e -> {

        int row =
                guestTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a guest from the table."
            );

            return;
        }

        int guestId =
                Integer.parseInt(
                        guestTableModel
                                .getValueAt(row, 0)
                                .toString()
                );

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String phone =
                phoneField.getText().trim();

        if (name.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all guest details.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        model.Guest guest =
                new model.Guest(
                        guestId,
                        name,
                        email,
                        phone
                );

        boolean success =
                guestDAO.updateGuest(guest);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Guest updated successfully!"
            );

            loadGuests.run();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update guest.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });

    // =========================
    // Delete Guest
    // =========================

    deleteButton.addActionListener(e -> {

        int row =
                guestTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a guest from the table."
            );

            return;
        }

        int guestId =
                Integer.parseInt(
                        guestTableModel
                                .getValueAt(row, 0)
                                .toString()
                );

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this guest?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                guestDAO.deleteGuest(guestId);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Guest deleted successfully!"
            );

            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");

            loadGuests.run();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not delete guest.\n"
                            + "Make sure no reservation is using this guest.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });

    // =========================
    // Refresh
    // =========================

    refreshButton.addActionListener(
            e -> loadGuests.run()
    );

    return mainPanel;
}

    // =====================================================
    // RESERVATIONS
    // =====================================================

    private JPanel createReservationsPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel title = new JLabel(
                "Reservation Management",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(WHITE_COLOR);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(BORDER_COLOR),
                                "Reservation Details"
                        ),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        );

        formPanel.add(new JLabel("Guest ID:"));
        JTextField guestIdField = new JTextField();
        formPanel.add(guestIdField);

        formPanel.add(new JLabel("Room ID:"));
        JTextField roomIdField = new JTextField();
        formPanel.add(roomIdField);

        formPanel.add(new JLabel("Check-in (YYYY-MM-DD):"));
        JTextField checkInField = new JTextField();
        formPanel.add(checkInField);

        formPanel.add(new JLabel("Check-out (YYYY-MM-DD):"));
        JTextField checkOutField = new JTextField();
        formPanel.add(checkOutField);

        topPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel reservationTableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID", "Guest ID", "Room ID",
                                "Check-in", "Check-out"
                        }, 0
                );

        JTable reservationTable = new JTable(reservationTableModel);
        reservationTable.setAutoCreateRowSorter(true);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainPanel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = new JButton("Create Reservation");
        JButton updateButton = new JButton("Update Reservation");
        JButton deleteButton = new JButton("Delete Reservation");
        JButton availabilityButton = new JButton("Check Availability");
        JButton costButton = new JButton("Calculate Cost");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(availabilityButton);
        buttonPanel.add(costButton);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dao.ReservationDAO reservationDAO = new dao.ReservationDAO();

        Runnable loadReservations = () -> {
            reservationTableModel.setRowCount(0);
            java.util.List<model.Reservation> reservations =
                    reservationDAO.getAllReservations();

            for (model.Reservation reservation : reservations) {
                reservationTableModel.addRow(new Object[]{
                        reservation.getReservationId(),
                        reservation.getGuestId(),
                        reservation.getRoomId(),
                        reservation.getCheckIn(),
                        reservation.getCheckOut()
                });
            }
        };

        loadReservations.run();

        addButton.addActionListener(e -> {
            try {
                String guestIdText = guestIdField.getText().trim();
                String roomIdText = roomIdField.getText().trim();
                String checkInText = checkInField.getText().trim();
                String checkOutText = checkOutField.getText().trim();

                if (guestIdText.isEmpty() || roomIdText.isEmpty()
                        || checkInText.isEmpty() || checkOutText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill all reservation details.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int guestId = Integer.parseInt(guestIdText);
                int roomId = Integer.parseInt(roomIdText);
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInText);
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutText);

                if (!checkOut.isAfter(checkIn)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Check-out date must be after check-in date.",
                            "Invalid Dates",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (!reservationDAO.isRoomAvailable(roomId, checkIn, checkOut)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "This room is not available for the selected dates.",
                            "Room Not Available",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                model.Reservation reservation = new model.Reservation(
                        0, guestId, roomId, checkIn, checkOut
                );

                if (reservationDAO.addReservation(reservation)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Reservation created successfully!"
                    );
                    guestIdField.setText("");
                    roomIdField.setText("");
                    checkInField.setText("");
                    checkOutField.setText("");
                    loadReservations.run();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to create reservation.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Guest ID and Room ID must be numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter dates in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = reservationTable.getSelectedRow();
                if (row != -1) {
                    int modelRow = reservationTable.convertRowIndexToModel(row);
                    guestIdField.setText(reservationTableModel.getValueAt(modelRow, 1).toString());
                    roomIdField.setText(reservationTableModel.getValueAt(modelRow, 2).toString());
                    checkInField.setText(reservationTableModel.getValueAt(modelRow, 3).toString());
                    checkOutField.setText(reservationTableModel.getValueAt(modelRow, 4).toString());
                }
            }
        });

        updateButton.addActionListener(e -> {
            int row = reservationTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation from the table.");
                return;
            }

            try {
                int modelRow = reservationTable.convertRowIndexToModel(row);
                int reservationId = Integer.parseInt(
                        reservationTableModel.getValueAt(modelRow, 0).toString()
                );
                int guestId = Integer.parseInt(guestIdField.getText().trim());
                int roomId = Integer.parseInt(roomIdField.getText().trim());
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInField.getText().trim());
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutField.getText().trim());

                if (!checkOut.isAfter(checkIn)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Check-out date must be after check-in date.",
                            "Invalid Dates",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                model.Reservation reservation = new model.Reservation(
                        reservationId, guestId, roomId, checkIn, checkOut
                );

                if (reservationDAO.updateReservation(reservation)) {
                    JOptionPane.showMessageDialog(this, "Reservation updated successfully!");
                    loadReservations.run();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to update reservation.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Guest ID and Room ID must be numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter dates in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        deleteButton.addActionListener(e -> {
            int row = reservationTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation from the table.");
                return;
            }

            int modelRow = reservationTable.convertRowIndexToModel(row);
            int reservationId = Integer.parseInt(
                    reservationTableModel.getValueAt(modelRow, 0).toString()
            );

            int choice = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete this reservation?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) return;

            if (reservationDAO.deleteReservation(reservationId)) {
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully!");
                guestIdField.setText("");
                roomIdField.setText("");
                checkInField.setText("");
                checkOutField.setText("");
                loadReservations.run();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete reservation.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        availabilityButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(roomIdField.getText().trim());
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInField.getText().trim());
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutField.getText().trim());

                if (!checkOut.isAfter(checkIn)) {
                    JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.");
                    return;
                }

                boolean available = reservationDAO.isRoomAvailable(roomId, checkIn, checkOut);
                JOptionPane.showMessageDialog(
                        this,
                        available
                                ? "Room " + roomId + " is AVAILABLE."
                                : "Room " + roomId + " is NOT AVAILABLE.",
                        "Room Availability",
                        available ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
                );

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Room ID.");
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Please enter dates in YYYY-MM-DD format.");
            }
        });

        costButton.addActionListener(e -> {
            try {
                int roomId = Integer.parseInt(roomIdField.getText().trim());
                java.time.LocalDate checkIn = java.time.LocalDate.parse(checkInField.getText().trim());
                java.time.LocalDate checkOut = java.time.LocalDate.parse(checkOutField.getText().trim());

                if (!checkOut.isAfter(checkIn)) {
                    JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.");
                    return;
                }

                double cost = reservationDAO.calculateReservationCost(roomId, checkIn, checkOut);
                long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

                JOptionPane.showMessageDialog(
                        this,
                        "Number of nights: " + nights
                                + "\nTotal Reservation Cost: ₹"
                                + String.format("%.2f", cost),
                        "Reservation Cost",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Room ID.");
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Please enter dates in YYYY-MM-DD format.");
            }
        });

        refreshButton.addActionListener(e -> loadReservations.run());

        return mainPanel;
    }

    // =====================================================
    // PROFESSIONAL UI STYLING
    // =====================================================

    private void styleComponents(Container container) {

        for (Component component : container.getComponents()) {

            if (component instanceof JPanel) {
                component.setBackground(BACKGROUND_COLOR);
            }

            if (component instanceof JButton button) {
                button.setFont(new Font("Arial", Font.BOLD, 13));
                button.setFocusPainted(false);
                button.setBackground(WHITE_COLOR);
                button.setForeground(PRIMARY_COLOR);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }

            if (component instanceof JTextField field) {
                field.setFont(new Font("Arial", Font.PLAIN, 14));
                field.setForeground(TEXT_COLOR);
                field.setBackground(WHITE_COLOR);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
            }

            if (component instanceof JComboBox<?> combo) {
                combo.setFont(new Font("Arial", Font.PLAIN, 14));
                combo.setBackground(WHITE_COLOR);
                combo.setForeground(TEXT_COLOR);
            }

            if (component instanceof JTable table) {
                table.setFont(new Font("Arial", Font.PLAIN, 13));
                table.setRowHeight(30);
                table.setGridColor(BORDER_COLOR);
                table.setSelectionBackground(SECONDARY_COLOR);
                table.setSelectionForeground(Color.WHITE);
                table.setShowVerticalLines(false);
                table.setShowHorizontalLines(true);

                table.getTableHeader().setFont(
                        new Font("Arial", Font.BOLD, 13)
                );
                table.getTableHeader().setBackground(PRIMARY_COLOR);
                table.getTableHeader().setForeground(Color.WHITE);
                table.getTableHeader().setReorderingAllowed(false);
            }

            if (component instanceof JLabel label) {
                label.setForeground(TEXT_COLOR);
            }

            if (component instanceof JTabbedPane tabs) {
                tabs.setFont(new Font("Arial", Font.BOLD, 14));
                tabs.setBackground(WHITE_COLOR);
                tabs.setForeground(PRIMARY_COLOR);
            }

            if (component instanceof Container child) {
                styleComponents(child);
            }
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame frame = new MainFrame();

            frame.setVisible(true);
        });
    }
}