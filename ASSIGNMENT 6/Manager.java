import java.util.*;

public class Manager extends User {
    private final String name;
    private final int managerID;
    private static int count = 1;
    private static final List<Integer> dailySales = new ArrayList<>(Collections.nCopies(1000, 0));
    private static final List<Item> mostPopular = new ArrayList<>(Collections.nCopies(1000, null));
    private static final HashMap<Item, Integer> quantities = new HashMap<>();

    private static final List<Integer> totalOrders = new ArrayList<>(Collections.nCopies(1000, 0));
    private static int arrayIndex = 0;

    public Manager(String name, String username, String password) {
        super(username, password);
        ByteMe.getManagers().add(this);
        this.name = name;
        this.managerID = count;
        count++;
    }

    public static int getArrayIndex() {
        return arrayIndex;
    }

    public static void setArrayIndex() {
        Manager.arrayIndex++;
    }

    public String getName() {
        return name;
    }

    public int getManagerID() {
        return managerID;
    }

    private static List<Integer> getDailySales() {
        return dailySales;
    }

    private static List<Item> getMostPopular() {
        return mostPopular;
    }

    private static List<Integer> getTotalOrders() {
        return totalOrders;
    }

    private static HashMap<Item, Integer> getQuantities() {
        return quantities;
    }

    // MENU MANAGEMENTS

    // 1.ADD NEW ITEMS

    public void addItem(String name, int price, String category, int stock) {
        ByteMe.addThisItem(name, price, category, stock);
        System.out.println("Item added to the Menu!\n");
        List<String[]> menu = FileHandler.loadMenu();
        FileHandler.saveMenu(menu);
    }

    // 2.UPDATE EXISTING ITEMS

    public void changePrice(String name, int price) {
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                item.setPrice(price);
                FileHandler.updatePrice(name, price);
            }
        }
        System.out.println("Price of " + name + " changed to " + price + "!\n");
    }

    public void changeAvailability(String name, String availability) {
        for (Item item: ByteMe.getMenu()) if (item.getName().equals(name)) item.setAvailability(availability);
        System.out.println("Availability of " + name + " changed to " + availability + "!");
    }

    public void changeCategory(String name, String category) {
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                item.setAvailability(category);
                FileHandler.updateCategory(name, category);
            }
        }
        System.out.println("Category of " + name + " changed to " + category + "!");
    }

    // 3.REMOVE ITEMS

    public void removeItem(String name) {
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                ByteMe.getMenu().remove(item);
                System.out.println("Item Removed!\n");
                FileHandler.removeMenuItem(name);
                for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
                    HashMap<Item, Integer> cart = entry.getValue().getCart();
                    for (Map.Entry<Item, Integer> entry2 : cart.entrySet()) {
                        if (entry2.getKey().getName().equals(name)) {
                            entry.getValue().setStatus("denied");
                        }
                    }
                }
                return;
            }
        }
        System.out.println("Item not found!\n");
    }

    // VIEW REVIEWS

    public void viewReviews(String name) {
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals(name)) {
                item.publicReviews();
                return;
            }
        }
        System.out.println("Item not found!");
    }

    // ORDER MANAGEMENT

    // 1.VIEW PENDING ORDERS

    public void viewPendingOrders() {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            entry.getValue().orderDetails();
            System.out.println();
        }
        if (Order.getOrderList().isEmpty()) System.out.println("No Pending Orders!\n");
    }

    // 2.UPDATE ORDER STATUS

    public void updateOrderStatus() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the Order ID of the order you would like to update: ");
        int orderID = sc.nextInt();
        sc.nextLine();
        System.out.print("Please enter the update for the status of the order: ");
        String status = sc.nextLine();
        System.out.println();
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            Integer id = entry.getKey();
            Order order = entry.getValue();
            if (order.getOrderID() == orderID) {
                FileHandler.updateOrderStatus(String.valueOf(orderID), status);
                order.setStatus(status);
                System.out.println("Order Status Updated!");
                if (status.equals("delivered")) {
                    // ADD TO CUSTOMER'S ORDER HISTORY
                    for (Customer customer: ByteMe.getCustomers()) {
                        if (customer.getName().equals(order.getCustomerName())) {
                            customer.addToOrderHistory();
                        }
                    }

                    // DAILY SALES ADDITION
                    int prev = Manager.getDailySales().get(Manager.getArrayIndex());
                    prev += order.getTotal();
                    Manager.getDailySales().set(Manager.getArrayIndex(), prev);

                    // NUMBER OF ORDERS INCREMENTED
                    int orders = Manager.getTotalOrders().get(Manager.getArrayIndex());
                    orders++;
                    Manager.getTotalOrders().set(Manager.getArrayIndex(), orders);

                    // QUANTITIES OF EACH ITEM ARE STORED
                    for (Map.Entry<Item, Integer> entry2 : order.getCart().entrySet()) {
                        Item item = entry2.getKey();
                        Integer quantity = entry2.getValue();
                        if (Manager.getQuantities().containsKey(item)) {
                            quantity += order.getCart().get(item);
                            Manager.getQuantities().put(item, quantity);
                        }
                        else Manager.getQuantities().put(item, quantity);
                    }
                }
                return;
            }
        }
        System.out.println("Order not found!\n");
    }

    // 3.PROCESS REFUNDS

    public void processRefunds(int orderID) {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            if (entry.getKey() == orderID) {
                if (entry.getValue().getStatus().equals("cancelled") || entry.getValue().getStatus().equals("denied")) {
                    System.out.println("Order refunded!\n");
                    Order.getOrderList().remove(entry.getKey());
                    return;
                }
            }
            System.out.println("Order didn't had any issues!");
            System.out.println();
            return;
        }
        System.out.println("Order not found!\n");
    }

    // 4.HANDLE SPECIAL REQUESTS

    public void viewSpecialRequests(int orderID) {
        for (Map.Entry<Integer, Order> entry : Order.getOrderList().entrySet()) {
            if (entry.getKey() == orderID) {
                if (entry.getValue().getDescription() != null) System.out.println(entry.getValue().getDescription());
                else System.out.println("No Special Requests for this Order");
                return;
            }
        }
        System.out.println("Order not found!");
    }

     // REPORT GENERATION

    public static void dailySalesReport() {
        int quantity = 0;
        for (HashMap.Entry<Item, Integer> entry : Manager.getQuantities().entrySet()) {
            if (entry.getValue() > quantity) {
                quantity = entry.getValue();
                Manager.getMostPopular().set(Manager.getArrayIndex(), entry.getKey());
            }
        }
        System.out.println("Daily Sales Report: ");
        int sales = Manager.getDailySales().get(Manager.getArrayIndex());
        Item popularItem =  Manager.getMostPopular().get(Manager.getArrayIndex());
        int totalOrders = Manager.getTotalOrders().get(Manager.getArrayIndex());

        if (sales != 0) System.out.println("Total Sales for today: " + sales);
        else System.out.println("Total Sales: No sales for today yet!");

        if (popularItem != null) System.out.println("Most Popular Item for today: " + popularItem.getName());
        else System.out.println("Most Popular Item: No item!");

        System.out.println("Total Orders for today: " + totalOrders);
    }

    public static void nextDay() {
        Manager.setArrayIndex();
        Manager.getQuantities().clear();
        System.out.println("Sales Stopped for Today!");
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

    public void viewMenu() {
        for (Item item: ByteMe.getMenu()) {
            System.out.println(item);
        }
        if (ByteMe.getMenu().isEmpty()) System.out.println("No items in the Menu!");
    }
}
