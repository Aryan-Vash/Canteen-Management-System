import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String CART_FILE = "cart.txt";

    // Read menu items from file
    public static List<String[]> loadMenu() {
        List<String[]> menu = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                menu.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }
        return menu;
    }

    // Write menu items to file
    public static void saveMenu(List<String[]> menu) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (String[] item : menu) {
                writer.write(String.join(",", item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to menu file: " + e.getMessage());
        }
    }

    // Read orders (order IDs) from file
    public static List<String[]> loadOrders() {
        List<String[]> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(line.split(",")); // Split line into orderID, items, status
            }
        } catch (IOException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
        }
        return orders;
    }

    // Save orders with their details to file
    public static void saveOrders(List<String[]> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (String[] order : orders) {
                writer.write(String.join(",", order)); // Join order details and write
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to orders file: " + e.getMessage());
        }
    }

    // Save a new order to the orders file
    public static void saveOrder(String[] orderDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.write(String.join(",", orderDetails)); // Write order details
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    // Update the status of a specific order
    public static void updateOrderStatus(String orderId, String newStatus) {
        List<String[]> orders = loadOrders();
        boolean updated = false;

        for (String[] order : orders) {
            if (order[0].equals(orderId)) {
                order[1] = newStatus;
                updated = true;
                break;
            }
        }
        if (updated) saveOrders(orders);
        else System.out.println("Order ID not found.");
    }

    // Clear the cart.txt file when cart is empty
    public static void clearCartFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))) {
            writer.write("");
        } catch (IOException e) {
            System.err.println("Error clearing cart file: " + e.getMessage());
        }
    }

    // Remove a menu item by its name
    public static void removeMenuItem(String name) {
        List<String[]> menu = loadMenu();
        for (String[] item : menu) {
            if (item[0].equals(name)) {
                menu.remove(item);
                break;
            }
        }
        saveMenu(menu);
    }

    // Update price for a menu item
    public static void updatePrice(String name, int price) {
        List<String[]> menu = loadMenu();
        for (String[] item : menu) {
            if (item[0].equals(name)) {
                item[1] = String.valueOf(price);
                break;
            }
        }
        saveMenu(menu);
    }

    // Update category for a menu item
    public static void updateCategory(String name, String category) {
        List<String[]> menu = loadMenu();
        for (String[] item : menu) {
            if (item[0].equals(name)) {
                item[2] = category;
                break;
            }
        }
        saveMenu(menu);
    }

    // Get price of an item
    public static int getPriceForItem(String itemName) {
        List<String[]> menu = loadMenu();
        for (String[] item : menu) if (item[0].equalsIgnoreCase(itemName)) return Integer.parseInt(item[1]);
        return 0;
    }
}
