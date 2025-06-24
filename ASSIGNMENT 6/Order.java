import java.util.*;

public class Order {
    private static final TreeMap<Integer, Order> orderList = new TreeMap<>();
    private final String customerName;
    private HashMap<Item, Integer> cart;
    private final String address;
    private final String phone;
    private final String description;
    private String status = "preparing";
    private static int count = 1;
    private final int orderID;

    public Order(String name, HashMap<Item, Integer> cart, String address, String phone, String description) {
        this.customerName = name;
        this.cart = new HashMap<>(cart);
        this.address = address;
        this.phone = phone;
        this.description = description;
        orderID = count;
        orderList.put(orderID, this);
        count++;
    }

    public static int getCount() {
        return count++;
    }

    public static TreeMap<Integer, Order> getOrderList() {
        return orderList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public HashMap<Item, Integer> getCart() {
        return cart;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void orderDetails() {
        System.out.println("OrderID: " + this.getOrderID());
        for (Map.Entry<Item, Integer> entry : this.getCart().entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }
        System.out.println("Total: " + this.getTotal());
        System.out.println("Status: " + this.getStatus());
    }

    public int getTotal() {
        int total = 0;
        for (Map.Entry<Item, Integer> entry : this.getCart().entrySet()) {
            total += entry.getValue()*entry.getKey().getPrice();
        }
        return total;
    }

    public String toFileFormat() {
        String result = "";
        result += getOrderID() +  "," + getAddress() + "," + getPhone() + "," + getDescription() + ",";
        for (Map.Entry<Item, Integer> entry : this.getCart().entrySet()) {
            result += entry.getKey().getName() + ": " + entry.getValue() + ",";
        }
        return result;
    }
}
