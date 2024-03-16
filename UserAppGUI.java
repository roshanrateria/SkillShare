import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class UserAppGUI {
    private static final String USERS_FILE = "users.txt";
    private static Map<String, String[]> users = new HashMap<>();

    private JFrame frame;
    private JPanel mainPanel;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JPanel homePanel;

    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JTextField regLocationField;
    private JTextField regSkillsField;
    private JTextField regContactNumberField;
    private JTextField regEmailField;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JButton registerSubmitButton;
    private JButton loginSubmitButton;

    public UserAppGUI() {
        loadUsers();

        frame = new JFrame("User Registration & Login App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createRegisterPanel();
        createLoginPanel();
        createHomePanel();
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(registerPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.add(loginPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridLayout(8, 1));

        JLabel regUsernameLabel = new JLabel("Username:");
        regUsernameField = new JTextField();
        JLabel regPasswordLabel = new JLabel("Password:");
        regPasswordField = new JPasswordField();
        JLabel regLocationLabel = new JLabel("Location:");
        regLocationField = new JTextField();
        JLabel regSkillsLabel = new JLabel("Skills:");
        regSkillsField = new JTextField();
        JLabel regContactNumberLabel = new JLabel("Contact Number:");
        regContactNumberField = new JTextField();
        JLabel regEmailLabel = new JLabel("Email:");
        regEmailField = new JTextField();

        registerSubmitButton = new JButton("Register");

        registerPanel.add(regUsernameLabel);
        registerPanel.add(regUsernameField);
        registerPanel.add(regPasswordLabel);
        registerPanel.add(regPasswordField);
        registerPanel.add(regLocationLabel);
        registerPanel.add(regLocationField);
        registerPanel.add(regSkillsLabel);
        registerPanel.add(regSkillsField);
        registerPanel.add(regContactNumberLabel);
        registerPanel.add(regContactNumberField);
        registerPanel.add(regEmailLabel);
        registerPanel.add(regEmailField);
        registerPanel.add(new JLabel());
        registerPanel.add(registerSubmitButton);

        registerSubmitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = regUsernameField.getText();
                String password = new String(regPasswordField.getPassword());
                String location = regLocationField.getText();
                String skills = regSkillsField.getText();String contactNumber = regContactNumberField.getText();
                String email = regEmailField.getText();

                if (username.isEmpty() || password.isEmpty() || location.isEmpty() || skills.isEmpty()|| contactNumber.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                users.put(username, new String[]{password, location, skills,contactNumber, email});
                saveUsers();

                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                regUsernameField.setText("");
                regPasswordField.setText("");
                regLocationField.setText("");
                regSkillsField.setText("");
                regContactNumberField.setText("");
                regEmailField.setText("");
                showHomePanel();
            }
        });
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel loginUsernameLabel = new JLabel("Username:");
        loginUsernameField = new JTextField();
        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordField = new JPasswordField();

        loginSubmitButton = new JButton("Login");

        loginPanel.add(loginUsernameLabel);
        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPasswordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginSubmitButton);

        loginSubmitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = loginUsernameField.getText();
                String password = new String(loginPasswordField.getPassword());

                if (users.containsKey(username) && Arrays.equals(users.get(username)[0].toCharArray(), password.toCharArray())) {
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    showHomePanel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                loginUsernameField.setText("");
                loginPasswordField.setText("");
            }
        });
    }

    private void createHomePanel() {
        homePanel = new JPanel(new BorderLayout());
    
        JPanel filterPanel = new JPanel();
        JLabel locationLabel = new JLabel("Filter by Location:");
        JComboBox<String> locationFilterComboBox = new JComboBox<>(getLocations());
        JLabel skillsLabel = new JLabel("Filter by Skills:");
        JComboBox<String> skillsFilterComboBox = new JComboBox<>(getSkills());
    
        locationFilterComboBox.insertItemAt("All", 0);
        skillsFilterComboBox.insertItemAt("All", 0);
    
        // Set default selection to "All"
        locationFilterComboBox.setSelectedIndex(0);
        skillsFilterComboBox.setSelectedIndex(0);
    
        locationFilterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilters(locationFilterComboBox.getSelectedItem().toString(), skillsFilterComboBox.getSelectedItem().toString());
            }
        });
    
        skillsFilterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilters(locationFilterComboBox.getSelectedItem().toString(), skillsFilterComboBox.getSelectedItem().toString());
            }
        });
    
        filterPanel.add(locationLabel);
        filterPanel.add(locationFilterComboBox);
        filterPanel.add(skillsLabel);
        filterPanel.add(skillsFilterComboBox);
    
        homePanel.add(filterPanel, BorderLayout.NORTH);
    
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
    
        for (Map.Entry<String, String[]> entry : users.entrySet()) {
            String username = entry.getKey();
    
            JPanel userPanel = new JPanel();
            userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
            JLabel usernameLabel = new JLabel("Username: " + username);
            JButton detailsButton = new JButton("Details");
    
            userPanel.add(usernameLabel);
            userPanel.add(detailsButton);
    
            detailsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Display details of the user
                    String[] userDetails = users.get(username);
                    String password = userDetails[0];
                    String location = userDetails[1];
                    String skills = userDetails[2];
                    String contactNumber = userDetails[3];
                    String email = userDetails[4];
                    JOptionPane.showMessageDialog(frame, 
                            "Username: " + username + "\n" +
                            "Password: " + password + "\n" +
                            "Location: " + location + "\n" +
                            "Skills: " + skills+"\n" +  "Contact Number: " + contactNumber + "\n" +
                            "Email: " + email,
                            "User Details", JOptionPane.INFORMATION_MESSAGE);
                }
            });
    
            userListPanel.add(userPanel);
        }
    
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        homePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    
    private String[] getLocations() {
        Set<String> locations = new HashSet<>();
        for (String[] userData : users.values()) {
            locations.add(userData[1]);
        }
        return locations.toArray(new String[0]);
    }
    
    private String[] getSkills() {
        Set<String> skills = new HashSet<>();
        for (String[] userData : users.values()) {
            skills.add(userData[2]);
        }
        return skills.toArray(new String[0]);
    }
    
    private void applyFilters(String location, String skills) {
        homePanel.remove(1);
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
    
        for (Map.Entry<String, String[]> entry : users.entrySet()) {
            String username = entry.getKey();
            String userLocation = entry.getValue()[1];
            String userSkills = entry.getValue()[2];
    
            if ((location.equals("All") || userLocation.equals(location)) &&
                (skills.equals("All") || userSkills.equals(skills))) {
                JPanel userPanel = new JPanel();
                userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                userPanel.setLayout(new GridLayout(3, 1));
    
                JLabel usernameLabel = new JLabel("Username: " + username);
                JLabel locationLabel = new JLabel("Location: " + userLocation);
                JLabel skillsLabel = new JLabel("Skills: " + userSkills);
    
                userPanel.add(usernameLabel);
                userPanel.add(locationLabel);
                userPanel.add(skillsLabel);
    
                userListPanel.add(userPanel);
            }
        }
    
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        homePanel.add(scrollPane, BorderLayout.CENTER);
        homePanel.revalidate();
        homePanel.repaint();
    }
    
    private void showHomePanel() {
        mainPanel.removeAll();
        mainPanel.add(homePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.put(parts[0], new String[]{parts[1], parts[2], parts[3]});
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String[]> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue()[0] + "," + entry.getValue()[1] + "," + entry.getValue()[2] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new UserAppGUI();
    }
}
