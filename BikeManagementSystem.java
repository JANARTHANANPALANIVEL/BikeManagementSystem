import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class BikeManagementSystem extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bike_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private JTextField idField, modelField, brandField, priceField, customerNameField, rentalDurationField;
    private JComboBox<String> statusField;
    private DefaultTableModel tableModel;
    private String userRole;

    // Constructor for login
    public BikeManagementSystem(String role) {
        this.userRole = role;
        initComponents();
    }

    // Login GUI
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginFrame.add(loginButton);
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    loginFrame.dispose();
                    SwingUtilities.invokeLater(() -> new BikeManagementSystem(role).setVisible(true));
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        loginFrame.setVisible(true);
    }

    private void initComponents() {
        setTitle("Bike Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for adding or editing bikes
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Bikes"));

        idField = new JTextField();
        modelField = new JTextField();
        brandField = new JTextField();
        priceField = new JTextField();
        statusField = new JComboBox<>(new String[]{"Available", "Rented"});

        panel.add(new JLabel("Bike ID:"));
        panel.add(idField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Brand:"));
        panel.add(brandField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        JButton addButton = new JButton("Add Bike");
        JButton updateButton = new JButton("Update Bike");
        JButton deleteButton = new JButton("Delete Bike");

        addButton.addActionListener(e -> addBike());
        updateButton.addActionListener(e -> updateBike());
        deleteButton.addActionListener(e -> deleteBike());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        if (!"admin".equals(userRole)) {
            deleteButton.setEnabled(false);  // Only admins can delete
        }

        add(panel, BorderLayout.NORTH);

        // Table for displaying bike list
        tableModel = new DefaultTableModel(new String[]{"ID", "Model", "Brand", "Price", "Status"}, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Rental panel
        JPanel rentalPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        rentalPanel.setBorder(BorderFactory.createTitledBorder("Rent or Return a Bike"));

        rentalPanel.add(new JLabel("Bike ID:"));
        JTextField rentalBikeIdField = new JTextField();
        rentalPanel.add(rentalBikeIdField);

        rentalPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        rentalPanel.add(customerNameField);

        rentalPanel.add(new JLabel("Rental Duration (Days):"));
        rentalDurationField = new JTextField();
        rentalPanel.add(rentalDurationField);

        JButton rentBikeButton = new JButton("Rent Bike");
        JButton returnBikeButton = new JButton("Return Bike");

        rentBikeButton.addActionListener(e -> rentBike(rentalBikeIdField));
        returnBikeButton.addActionListener(e -> returnBike(rentalBikeIdField));

        rentalPanel.add(rentBikeButton);
        rentalPanel.add(returnBikeButton);

        add(rentalPanel, BorderLayout.SOUTH);

        viewBikes();
    }

    private void addBike() {
        String model = modelField.getText();
        String brand = brandField.getText();
        double price = Double.parseDouble(priceField.getText());
        String status = (String) statusField.getSelectedItem();

        String sql = "INSERT INTO bikes (model, brand, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model);
            stmt.setString(2, brand);
            stmt.setDouble(3, price);
            stmt.setString(4, status);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bike added successfully!");
            viewBikes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewBikes() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM bikes";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                String brand = rs.getString("brand");
                double price = rs.getDouble("price");
                String status = rs.getString("status");
                tableModel.addRow(new Object[]{id, model, brand, price, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBike() {
        int id = Integer.parseInt(idField.getText());
        String model = modelField.getText();
        String brand = brandField.getText();
        double price = Double.parseDouble(priceField.getText());
        String status = (String) statusField.getSelectedItem();

        String sql = "UPDATE bikes SET model = ?, brand = ?, price = ?, status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model);
            stmt.setString(2, brand);
            stmt.setDouble(3, price);
            stmt.setString(4, status);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bike updated successfully!");
            viewBikes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBike() {
        int id = Integer.parseInt(idField.getText());
        String sql = "DELETE FROM bikes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bike deleted successfully!");
            viewBikes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rentBike(JTextField rentalBikeIdField) {
        int bikeId = Integer.parseInt(rentalBikeIdField.getText());
        String customerName = customerNameField.getText();
        int duration = Integer.parseInt(rentalDurationField.getText());

        String sql = "INSERT INTO rentals (bike_id, customer_name, rental_duration) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bikeId);
            stmt.setString(2, customerName);
            stmt.setInt(3, duration);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bike rented successfully!");
            viewBikes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBike(JTextField rentalBikeIdField) {
        int bikeId = Integer.parseInt(rentalBikeIdField.getText());

        String sql = "DELETE FROM rentals WHERE bike_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bikeId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Bike returned successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No rental record found for the provided Bike ID.");
            }
            viewBikes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
