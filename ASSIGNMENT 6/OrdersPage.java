import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdersPage extends JFrame {
    public OrdersPage() {
        setTitle("Orders");
        setSize(1500, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton exitButton = new JButton("Exit");

        List<String[]> orderDetails = FileHandler.loadOrders();

        String[] columnNames = {"Order ID", "Status", "Items"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columnNames);

        for (String[] order : orderDetails) {
            String formattedItems = order[2].replace("; ", "\n");
            model.addRow(new Object[]{order[0], order[1], formattedItems});
        }

        JTable ordersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        // Action for Exit button
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
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
    }
}
