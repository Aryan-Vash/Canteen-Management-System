import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MenuPage extends JFrame {
    private final HashMap<String, Integer> cart;
    private final MenuTableModel tableModel;
    private final JLabel totalPriceLabel;
    private int totalPrice;

    public MenuPage(HashMap<String, Integer> cart) {
        this.cart = cart;
        this.totalPrice = 0;
        setTitle("Canteen Menu");
        setSize(1500, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton exitButton = new JButton("Exit");
        JButton viewCartButton = new JButton("View Cart");
        totalPriceLabel = new JLabel("Total Price: Rs 0");
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        List<String[]> menuData = FileHandler.loadMenu();

        tableModel = new MenuTableModel(menuData);
        JTable menuTable = new JTable(tableModel);

        menuTable.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
        menuTable.getColumn("Add to Cart").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(menuTable);

        // Exit button functionality
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

        // View cart button functionality
        viewCartButton.addActionListener(e -> {
            CartPage cartPage = new CartPage(cart);
            cartPage.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(viewCartButton);

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.add(totalPriceLabel, BorderLayout.EAST);

        add(totalPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTotalPrice(int price) {
        totalPrice += price;
        totalPriceLabel.setText("Total Price: Rs " + totalPrice);
    }

    static class MenuTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Item Name", "Price", "Category", "Stock", "Add to Cart"};
        private final Object[][] data;

        public MenuTableModel(List<String[]> menuData) {
            data = new Object[menuData.size()][columnNames.length];
            for (int i = 0; i < menuData.size(); i++) {
                String[] row = menuData.get(i);
                data[i][0] = row[0]; // Item Name
                data[i][1] = Integer.parseInt(row[1]); // Price
                data[i][2] = row[2]; // Category
                data[i][3] = Integer.parseInt(row[3]); // Stock
                data[i][4] = "Add"; // Add to Cart button text
            }
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == columnNames.length - 1; // Only "Add to Cart" is editable
        }

        public void updateStock(int rowIndex, int newStock) {
            data[rowIndex][3] = newStock; // Update stock column
            fireTableCellUpdated(rowIndex, 3);
        }
    }

    // Custom renderer for the "Add to Cart" button
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Add");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String itemName;
        private int price;
        private int stock;
        private boolean clicked;
        private final MenuPage menuPage;

        public ButtonEditor(JCheckBox checkBox, MenuPage menuPage) {
            super(checkBox);
            this.menuPage = menuPage;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            itemName = (String) table.getValueAt(row, 0); // Item Name
            price = (int) table.getValueAt(row, 1);       // Price
            stock = (int) table.getValueAt(row, 3);       // Stock
            button.setText("Add");                               // Set button text as "Add"
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                if (stock > 0) {
                    cart.put(itemName, cart.getOrDefault(itemName, 0) + 1);
                    menuPage.updateTotalPrice(price);
                    stock--;
                    tableModel.updateStock(getRowIndex(itemName), stock);
                } else {
                    JOptionPane.showMessageDialog(button, "Item \"" + itemName + "\" is out of stock!");
                }
            }
            clicked = false;
            return "Add";
        }

        private int getRowIndex(String itemName) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(itemName)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
