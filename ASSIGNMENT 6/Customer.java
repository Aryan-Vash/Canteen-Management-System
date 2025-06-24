import java.util.*;
import java.io.*;

public class Customer extends User {
    private final List<Order> orderHistory;
    private final String name;
    private HashMap<Item, Integer> cart;
    private int orderID;

    public Customer(String name, String username, String password) {
        super(username, password);
        ByteMe.getCustomers().add(this);
        this.name = name;
        this.cart = new HashMap<>();
        this.orderHistory = new ArrayList<>();
    }

    public static void cleanUp() {
        for (Customer customer : ByteMe.getCustomers()) {
            customer.orderHistory.clear();
            customer.saveOrderHistory();
        }
    }

    public String getName() {
        return name;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public HashMap<Item, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<Item, Integer> cart) {
        this.cart = cart;
    }

    // BROWSE MENU

    public void viewItems() {
        for (Item item : ByteMe.getMenu()) {
            System.out.println(item);
        }
        if (ByteMe.getMenu().isEmpty()) System.out.println("No items in the Menu!");
    }

    public void searchItem(String name) {
        boolean found = false;
        for (Item item : ByteMe.getMenu()) {
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) System.out.println("Item not found!");
    }

    public void filterByCategory(String category) {
        int count = 0;
        for (Item item : ByteMe.getMenu()) {
            if (category.equals(item.getCategory())) {
                count++;
                System.out.println(item);
            }
        }
        if (count == 0) System.out.println("No items in the Menu from this Category!");
    }

    public void sortByPrice(boolean ascending) {
        List<Item> sortedItems = new ArrayList<>(ByteMe.getMenu());

        if (ascending) {
            sortedItems.sort(Comparator.comparingInt(Item::getPrice));
        } else {
            sortedItems.sort(Comparator.comparingInt(Item::getPrice).reversed());
        }

        for (Item item : sortedItems) {
            System.out.println(item);
        }

        if (ByteMe.getMenu().isEmpty()) System.out.println("No items in the Menu!");
    }

    // CART OPERATIONS

    public void viewCart() {
        for (Map.Entry<Item, Integer> entry : this.getCart().entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }
        if (this.getCart().isEmpty()) System.out.println("No items in the Cart!");
        System.out.println();
    }

    public String getItemNames() {
        if (this.getCart().isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : this.getCart().entrySet()) {
            result.append(entry.getKey().getName())
                .append(": ")
                .append(entry.getValue())
                .append("; ");
        }
        return result.substring(0, result.length() - 2);
    }

    public void addToCart(String name, int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!\n");
            return;
        }
        for (Item item : ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                if (item.isAvailable()) {
                    if (item.getStock() < quantity) {
                        System.out.println("Item out of Stock!\n");
                        return;
                    }
                    this.cart.merge(item, quantity, Integer::sum);
                    item.setStock(item.getStock() - quantity);
                    HashMap<String, Integer> tempCart = new HashMap<>();
                    for (Item itemName: this.getCart().keySet()) {
                        tempCart.merge(itemName.getName(), quantity, Integer::sum);
                    }
                    CartPage.saveCartToFile(tempCart);
                    System.out.println("Item added to Cart!");
                } else {
                    System.out.println("Item Unavailable!");
                }
                System.out.println();
                return;
            }
        }
        System.out.println("Item not found!");
        System.out.println();
    }

    public void modifyQuantity(String name, int quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!\n");
            return;
        }
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                int currentQuantity = entry.getValue();
                int stockChange = quantity - currentQuantity;
                Item item = entry.getKey();

                if (item.getStock() < stockChange) {
                    System.out.println("Item out of Stock!\n");
                    System.out.println();
                    return;
                }
                this.cart.put(item, quantity);
                item.setStock(item.getStock() - stockChange);

                HashMap<String, Integer> tempCart = new HashMap<>();
                for (Item cartItem : this.getCart().keySet()) {
                    tempCart.merge(cartItem.getName(), this.getCart().get(cartItem), Integer::sum);
                }
                CartPage.saveCartToFile(tempCart);

                System.out.println("Quantity updated!");
                System.out.println();
                return;
            }
        }
        System.out.println("Item not found in the Cart!");
        System.out.println();
    }


    public void removeFromCart(String name) {
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                this.cart.remove(entry.getKey());
                HashMap<String, Integer> tempCart = new HashMap<>();
                for (Item itemName: this.getCart().keySet()) {
                    tempCart.put(itemName.getName(), this.getCart().get(itemName));
                }
                CartPage.saveCartToFile(tempCart);
                System.out.println("Item Removed!");
                System.out.println();
                return;
            }
        }
        System.out.println("Item not found in the Cart!\n");
    }

    public int viewTotal() {
        int total = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            total += entry.getValue() * entry.getKey().getPrice();
        }
        if (this.getCart().isEmpty()) System.out.println("No items in the Cart!\n");
        return total;
    }

    public void checkout(String pay) {
        if (pay.equals("pay")) {
            viewCart();
            if (cart.isEmpty()) return;
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Address Details: ");
            String address = sc.nextLine();
            System.out.print("Enter Phone Number: ");
            String phone = sc.nextLine();
            System.out.print("Enter Description (if any): ");
            String description = sc.nextLine();
            System.out.println();

            Order order = new Order(this.getName(), this.getCart(), address, phone, description);
            order.orderDetails();

            List<String[]> orders = FileHandler.loadOrders();
            String[] newOrder = new String[]{
                String.valueOf(order.getOrderID()), // Order ID
                order.getStatus(),                  // Status
                this.getItemNames()                 // Items (comma-separated)
            };
            orders.add(newOrder);
            FileHandler.saveOrders(orders);

            this.setOrderID(order.getOrderID());
            System.out.println();
            System.out.println("Order Accepted!");
            System.out.println();
            cart.clear();
            return;
        }
        if (pay.equals("not pay")) System.out.println("Pay first for Order to be Accepted!");
        else System.out.println("Wrong Input, Try Again Next Time!");
        System.out.println();
    }

    public void addToOrderHistory() {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            Order order = entry.getValue();
            if (order.getOrderID() == this.getOrderID()) {
                if ("delivered".equals(order.getStatus())) {
                    this.getOrderHistory().add(order);
                    this.saveOrderHistory();
                    Order.getOrderList().remove(entry.getKey());
                    System.out.println("Order Delivered & Added to Customer History!\n");
                    return;
                }
                System.out.println("Order not Delivered yet!\n");
            }
        }
    }

    // ORDER TRACKING

    public String viewOrderStatus() {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            Order order = entry.getValue();
            if (order.getOrderID() == this.getOrderID()) {
                return order.getStatus();
            }
        }
        return "Order Not Found!";
    }

    public void cancelOrder() {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            Order order = entry.getValue();
            if (order.getOrderID() == this.getOrderID()) {
                if ("preparing".equals(order.getStatus())) {
                    System.out.println("Order Cancelled!");
                    order.setStatus("cancelled");
                    return;
                }
                System.out.println("Can't Cancel the Order Now!");
                return;
            }
        }
        System.out.println("Order Not Found!");
    }

    public void viewOrderHistory() {
        loadAndDisplayOrderHistory();
    }

    public void orderAgain() {
        if (!this.getCart().isEmpty()) {
            System.out.println("Cart Currently has Some Items, Remove Them to Add These Items!\n");
            return;
        }
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the Order Number that You Wish to Order Again: ");
        int orderID = sc.nextInt();

        for (Order order : this.getOrderHistory()) {
            if (order.getOrderID() == orderID) {
                HashMap<Item, Integer> newCart = new HashMap<>(order.getCart());
                this.setCart(newCart);
                System.out.println("Items of this Order Added to the Cart!\n");
                return;
            }
        }
        System.out.println("Order Not Found in Customer History!\n");
    }

    // FILE HANDLING METHODS

    public void saveOrderHistory() {
        String fileName = "order_history_" + this.getUsername() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Order order: this.getOrderHistory()) {
                writer.write(order.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving order history: " + e.getMessage());
        }
    }

    private void loadAndDisplayOrderHistory() {
        String fileName = "order_history_" + this.getUsername() + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("Order History for Customer: " + this.getUsername());
            System.out.println("------------------------------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                // Print the raw details of the order
                String[] items = line.split(",");
                System.out.println("Order ID: " + items[0]);
                System.out.println("Address: " + items[1]);
                System.out.println("Phone: " + items[2]);
                System.out.println("Description: " + items[3]);
                System.out.println("Items: ");
                for (int i = 4; i < items.length; i++ ) {
                    System.out.println(items[i]);
                }
                System.out.println("------------------------------------------------------------------------------------" +
                    "---------------------------------------------------------------------------------------");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No order history found for customer: " + this.getUsername());
        } catch (IOException e) {
            System.err.println("Error loading order history: " + e.getMessage());
        }
    }


    // ITEM REVIEWS

    public void viewReviews(String name) {
        for (Item item : ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                item.publicReviews();
                return;
            }
        }
        System.out.println("Item not found!");
    }

    public void provideReview(String name) {
        for (Item item : ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter your Review: ");
                String review = sc.nextLine();
                item.getReviews().put(this.name, review);
                System.out.println("Review Added!");
                return;
            }
        }
        System.out.println("Item not found!");
    }

    @Override
    public void displayDetails() {
        System.out.println("Name: " + this.getName());
        System.out.println("Username: " + this.getUsername());
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
