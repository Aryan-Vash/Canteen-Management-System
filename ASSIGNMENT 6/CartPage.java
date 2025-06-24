import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class CartPage extends JFrame {
    public CartPage(HashMap<String, Integer> cart) {
        setTitle("Shopping Cart");
        setSize(1500, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"Item Name", "Quantity", "Price"};

        Object[][] tableData = new Object[cart.size()][3];
        int index = 0;
        int total = 0;

        for (String itemName : cart.keySet()) {
            int quantity = cart.get(itemName);
            int price = FileHandler.getPriceForItem(itemName); // Get the price of the item
            tableData[index][0] = itemName;
            tableData[index][1] = quantity;
            tableData[index][2] = quantity * price;
            total += quantity * price;
            index++;
        }

        DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
        JTable cartTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        JLabel totalLabel = new JLabel("Total Price: Rs " + total);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton checkoutButton = new JButton("Checkout");
        int finalTotal = total;
        checkoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirm and Pay Rs " + finalTotal + "?",
                "Checkout Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                String customerName = "Customer Name"; // Replace with actual customer name
                String orderId = String.valueOf(Order.getCount()); // Generate new order ID
                StringBuilder items = new StringBuilder();
                for (String itemName : cart.keySet()) {
                    items.append(itemName)
                        .append(": ")
                        .append(cart.get(itemName))
                        .append("; "); // Format: "ItemName: Quantity; "
                }
                String status = "preparing";

                FileHandler.saveOrder(new String[]{orderId, status, items.toString().trim()});

                cart.clear();
                FileHandler.clearCartFile(); // Clear cart.txt file

                JOptionPane.showMessageDialog(this, "Order placed successfully!");

                this.dispose();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                System.out.println("Going Back");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkoutButton);
        buttonPanel.add(exitButton);

        add(scrollPane, BorderLayout.CENTER);
        add(totalLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        saveCartToFile(cart);
    }

    static void saveCartToFile(HashMap<String, Integer> cart) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
            for (String itemName : cart.keySet()) {
                int quantity = cart.get(itemName);
                writer.write(itemName + ": " + quantity);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to cart file: " + e.getMessage());
        }
    }
}
