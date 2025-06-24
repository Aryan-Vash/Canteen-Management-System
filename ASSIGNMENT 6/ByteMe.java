import java.util.*;
import javax.swing.*;
import java.util.List;

public class ByteMe {

    public static Customer currentCustomer = null;
    public static Manager currentManager = null;

    private static final List<Item> menu = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Manager> managers = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    public static List<Order> getOrders() {
        return orders;
    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static List<Manager> getManagers() {
        return managers;
    }

    public static List<Item> getMenu() {
        return menu;
    }

    public static boolean checkCustomer(String gmail, String password) {
        if (getCustomers().isEmpty()) return false;
        for (Customer customer : getCustomers()) {
            if (customer.getUsername().equals(gmail) && customer.getPassword().equals(password)) {
                currentCustomer = customer; return true;
            }
        }
        return false;
    }

    public static boolean checkManager(String gmail, String password) {
        if (getManagers().isEmpty()) return false;
        for (Manager manager : getManagers()) {
            if (manager.getUsername().equals(gmail) && manager.getPassword().equals(password)) {
                currentManager = manager;
                return true;
            }
        }
        return false;
    }

    public static void addThisItem(String name, int price, String category, int stock) {
        Item item = new Item(name, price, category, stock);
        menu.add(item);
        List<String[]> menu = FileHandler.loadMenu();
        menu.add(new String[] { name, String.valueOf(price), category, String.valueOf(stock)});
        FileHandler.saveMenu(menu);
    }

    public static void writeInitializedData() {
        addThisItem("paratha", 30, "meals", 20);
        addThisItem("coke", 20, "beverage", 10);
        addThisItem("slice", 20, "beverage", 10);
        addThisItem("kurkure", 20, "snacks", 25);
        addThisItem("doritos", 50, "snacks", 5);
        addThisItem("pao bhaji", 40, "meals", 10);
        addThisItem("chole bhature", 40, "meals", 10);
        addThisItem("lays", 30, "snacks", 20);
        Customer customer1 = new Customer("Aryan", "a", "a");
        customers.add(customer1);
        Manager manager1 = new Manager("Sumit", "s", "s");
        managers.add(manager1);
    }

    public static void main(String[] args) {

        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();

        Scanner sc = new Scanner(System.in);
        writeInitializedData();
        List<String[]> menu = FileHandler.loadMenu();
        List<String[]> orders = FileHandler.loadOrders();
        boolean iterator = true;

        while (iterator) {
            System.out.println();
            System.out.println("-------WELCOME TO BYTE ME-------");
            System.out.println();
            System.out.println("1.Enter the Application");
            System.out.println("2.Exit the Application");
            System.out.println("3.Enter the ByteMe GUI");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("Welcome to the Application\n");
                    enterApp();
                    break;
                case 2:
                    System.out.println("Thank you for using the Application!\nMenu and Orders files cleared!");
                    MainMenuGUI.clearFiles();
                    iterator = false;
                    break;
                case 3:
                    SwingUtilities.invokeLater(() -> {
                        MainMenuGUI gui = new MainMenuGUI();
                        gui.setVisible(true);
                        gui.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                                System.out.println("Returning to CLI...");
                            }
                        });
                    });
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void enterApp() {

        boolean iterator = true;
        Scanner sc = new Scanner(System.in);

        while (iterator) {
            System.out.println("-------LOGIN PAGE-------");
            System.out.println("1.Login as Customer");
            System.out.println("2.Login as Manager");
            System.out.println("3.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    customerMode();
                    break;
                case 2:
                    managerMode();
                    break;
                case 3:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void managerMode() {

        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("-------Welcome to the Manager Mode-------");
            System.out.println("1.Sign in");
            System.out.println("2.Sign up & Login");
            System.out.println("3.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Username: ");
                    String gmail = sc.nextLine();
                    System.out.print("Enter the Password: ");
                    String password = sc.nextLine();
                    if(!checkManager(gmail, password)) {
                        System.out.println();
                        System.out.println("---Record Not Found, Sign Up First!---\n");
                        break;
                    }
                    System.out.println();
                    System.out.println("---Logged In Successfully---");
                    System.out.println();
                    managerTask();
                    break;
                case 2:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter the Username: ");
                    gmail = sc.nextLine();
                    System.out.print("Enter the Password: ");
                    password = sc.nextLine();
                    Manager manager = new Manager(name, gmail, password);
                    System.out.println();
                    System.out.println("---Sign Up Successful---");
                    checkManager(gmail, password);
                    System.out.println();
                    managerTask();
                    break;
                case 3:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void managerTask() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Menu Management");
            System.out.println("2.Order Management");
            System.out.println("3.Report Generation");
            System.out.println("4.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    menuManagement();
                    break;
                case 2:
                    orderManagement();
                    break;
                case 3:
                    reportGeneration();
                    break;
                case 4:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void menuManagement() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Add New Items");
            System.out.println("2.Update Existing Items");
            System.out.println("3.Remove Items");
            System.out.println("4.View Menu");
            System.out.println("5.View Reviews");
            System.out.println("6.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter the Price: ");
                    int price = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter the Category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter the Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();

                    currentManager.addItem(name, price, category, stock);
                    break;
                case 2:
                    updateItems();
                    break;
                case 3:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();

                    currentManager.removeItem(name);
                    break;
                case 4:
                    currentManager.viewMenu();
                    break;
                case 5:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();

                    currentManager.viewReviews(name);
                    System.out.println();
                    break;
                case 6:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void updateItems() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Update Price");
            System.out.println("2.Update Availability");
            System.out.println("3.Update Category");
            System.out.println("4.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter the Price: ");
                    int price = sc.nextInt();
                    sc.nextLine();

                    currentManager.changePrice(name, price);
                    break;
                case 2:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();
                    System.out.print("Enter the Availability: ");
                    String availability = sc.nextLine();

                    currentManager.changeAvailability(name, availability);
                    break;
                case 3:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();
                    System.out.print("Enter the Category: ");
                    String category = sc.nextLine();

                    currentManager.changeCategory(name, category);
                    break;
                case 4:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void orderManagement() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.View Pending Orders");
            System.out.println("2.Update Order Status");
            System.out.println("3.Process Refunds");
            System.out.println("4.Handle Special Requests");
            System.out.println("5.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    currentManager.viewPendingOrders();
                    break;
                case 2:
                    currentManager.updateOrderStatus();
                    break;
                case 3:
                    System.out.print("Enter the Order ID: ");
                    int orderID = sc.nextInt();
                    sc.nextLine();

                    currentManager.processRefunds(orderID);
                    break;
                case 4:
                    System.out.print("Enter the Order ID: ");
                    orderID = sc.nextInt();
                    sc.nextLine();

                    currentManager.viewSpecialRequests(orderID);
                    System.out.println();
                    break;
                case 5:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void reportGeneration() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.View Daily Sales Report");
            System.out.println("2.Stop Sales for Today");
            System.out.println("3.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    Manager.dailySalesReport();
                    System.out.println();
                    break;
                case 2:
                    Manager.nextDay();
                    System.out.println();
                    break;
                case 3:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void customerMode() {

        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("-------Welcome to the Customer Mode-------");
            System.out.println("1.Sign in");
            System.out.println("2.Sign up & Login");
            System.out.println("3.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Username: ");
                    String gmail = sc.nextLine();
                    System.out.print("Enter the Password: ");
                    String password = sc.nextLine();
                    if(!checkCustomer(gmail, password)) {
                        System.out.println();
                        System.out.println("---Record Not Found, Sign Up First!---");
                        System.out.println();
                        break;
                    }
                    System.out.println();
                    System.out.println("---Logged In Successfully---");
                    System.out.println();
                    customerTask();
                    break;
                case 2:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter the Username: ");
                    gmail = sc.nextLine();
                    System.out.print("Enter the Password: ");
                    password = sc.nextLine();
                    Customer customer = new Customer(name, gmail, password);
                    checkCustomer(gmail, password);
                    System.out.println("---Sign Up Successful---");
                    System.out.println();
                    customerTask();
                    break;
                case 3:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void customerTask() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Browse Menu");
            System.out.println("2.Cart Operations");
            System.out.println("3.Order Tracking");
            System.out.println("4.Item Reviews");
            System.out.println("5.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    browseMenu();
                    break;
                case 2:
                    cartOperations();
                    break;
                case 3:
                    orderTracking();
                    break;
                case 4:
                    itemReviews();
                    break;
                case 5:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void browseMenu() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.View All Items");
            System.out.println("2.Search");
            System.out.println("3.Filter By Category");
            System.out.println("4.Sort By Price");
            System.out.println("5.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    currentCustomer.viewItems();
                    break;
                case 2:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.println();

                    currentCustomer.searchItem(name);
                    break;
                case 3:
                    System.out.print("Enter the Category: ");
                    String category = sc.nextLine();
                    System.out.println();

                    currentCustomer.filterByCategory(category);
                    break;
                case 4:
                    System.out.print("Enter the Order: ");
                    String order = sc.nextLine();
                    System.out.println();

                    currentCustomer.sortByPrice(order.equals("ascending"));
                    break;
                case 5:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void cartOperations() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Add Items");
            System.out.println("2.Modify Quantities");
            System.out.println("3.Remove Items");
            System.out.println("4.View Total");
            System.out.println("5.Checkout Process");
            System.out.println("6.View Cart");
            System.out.println("7.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter the Quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();

                    currentCustomer.addToCart(name, quantity);
                    break;
                case 2:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();
                    System.out.print("Enter the Quantity: ");
                    quantity = sc.nextInt();
                    sc.nextLine();

                    currentCustomer.modifyQuantity(name, quantity);
                    break;
                case 3:
                    System.out.print("Enter the Name: ");
                    name = sc.nextLine();

                    currentCustomer.removeFromCart(name);
                    break;
                case 4:
                    System.out.println("Total Bill: " + currentCustomer.viewTotal());
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Pay/Not Pay for the Order: ");
                    String pay = sc.nextLine();

                    currentCustomer.checkout(pay);
                    break;
                case 6:
                    currentCustomer.viewCart();
                    break;
                case 7:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void orderTracking() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.View Order Status");
            System.out.println("2.Cancel Order");
            System.out.println("3.Order History");
            System.out.println("4.Order Again");
            System.out.println("5.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("Current Order Status: " + currentCustomer.viewOrderStatus());
                    System.out.println();
                    break;
                case 2:
                    currentCustomer.cancelOrder();
                    System.out.println();
                    break;
                case 3:
                    currentCustomer.viewOrderHistory();
                    break;
                case 4:
                    currentCustomer.orderAgain();
                    break;
                case 5:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }

    private static void itemReviews() {
        Scanner sc = new Scanner(System.in);
        boolean iterator = true;

        while (iterator) {
            System.out.println("1.Provide Reviews");
            System.out.println("2.View Reviews");
            System.out.println("3.Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.print("Enter the Item Name: ");
                    String name = sc.nextLine();

                    currentCustomer.provideReview(name);
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Enter the Item Name: ");
                    name = sc.nextLine();

                    currentCustomer.viewReviews(name);
                    System.out.println();
                    break;
                case 3:
                    iterator = false;
                    break;
                default:
                    System.out.println("Wrong Choice, Choose Again!\n");
            }
        }
    }
}
