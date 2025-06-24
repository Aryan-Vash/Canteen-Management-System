import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name = "";
    private int price = 0;
    private String Category;
    private String Availability = "available";
    private final HashMap<String, String> reviews;
    private int stock;

    public Item (String name, int price, String Category, int stock) {
        this.name = name;
        this.price = price;
        this.Category = Category;
        this.reviews = new HashMap<>();
        this.stock = stock;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getAvailability() {
        return this.Availability;
    }

    public void setAvailability(String availability) {
        this.Availability = availability;
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toString() {
        return "Name: " + this.getName() + "\nPrice: " + this.getPrice() + "\nCategory: " +
            this.getCategory() + "\nAvailability: " + this.getAvailability() + "\n";
    }

    public boolean isAvailable() {
        return this.getAvailability().equals("available");
    }

    public void publicReviews() {
        for (Map.Entry<String, String> review : this.getReviews().entrySet()) System.out.println(review.getKey() + ": " + review.getValue());
        if (this.getReviews().isEmpty()) System.out.println("No reviews yet!");
    }
}
