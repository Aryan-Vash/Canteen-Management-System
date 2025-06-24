import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuGUI extends JFrame {
    private final HashMap<String, Integer> cart;

    public MainMenuGUI() {
        setTitle("Byte Me - Main Menu");
        setSize(1500, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        this.cart = new HashMap<>();

        // Buttons for navigation
        JButton menuButton = new JButton("View Menu");
        JButton ordersButton = new JButton("View Orders");
        JButton cartButton = new JButton("View Cart");
        JButton exitButton = new JButton("Exit");

        // Action for View Menu button
        menuButton.addActionListener(e -> {
            MenuPage menuPage = new MenuPage(cart); // Pass the cart to MenuPage
            menuPage.setVisible(true);
        });

        // Action for View Orders button
        ordersButton.addActionListener(e -> {
            OrdersPage ordersPage = new OrdersPage();
            ordersPage.setVisible(true);
        });

        // Action for View Cart button
        cartButton.addActionListener(e -> {
            CartPage cartPage = new CartPage(cart); // Pass the cart to CartPage
            cartPage.setVisible(true);
        });

        // Action for Exit button
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                clearFiles();
                dispose();
                System.out.println("Returning to CLI...");
            }
        });

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(menuButton);
        buttonPanel.add(ordersButton);
        buttonPanel.add(cartButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Add a listener to clear files on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Menu and Orders files cleared.");
            }
        });
    }

    // Method to clear the menu and orders files
    static void clearFiles() {
        // Clear menu file
        FileHandler.saveMenu(new ArrayList<>()); // Save an empty list to menu.txt
        // Clear orders file
        FileHandler.saveOrders(new ArrayList<>()); // Save an empty list to orders.txt
        // Clear Cart File
        FileHandler.clearCartFile();
        // Clear Customer Order History
        Customer.cleanUp();
    }
}
