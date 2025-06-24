import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Testing {
    private Customer testCustomer;

    @Before
    public void setUp() {
        ByteMe.getMenu().clear();
        ByteMe.getCustomers().clear();
        ByteMe.getOrders().clear();

        ByteMe.addThisItem("test_item", 100, "test_category", 5);
        ByteMe.addThisItem("out_of_stock_item", 50, "test_category", 0);

        testCustomer = new Customer("Test User", "test@test.com", "password");
        ByteMe.getCustomers().add(testCustomer);
        ByteMe.currentCustomer = testCustomer;
    }

    // Test Part 1: Cart Operations

    @Test
    public void testAddItemToCart() {
        testCustomer.addToCart("test_item", 2);

        // Verify cart contains item with correct quantity
        assertEquals(1, testCustomer.getCart().size());
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals("test_item")) {
                assertEquals(2, testCustomer.getCart().get(item).intValue());
                break;
            }
        }

        // Verify total price is calculated correctly (2 * 100 = 200)
        assertEquals(200, testCustomer.viewTotal());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testModifyItemQuantity() {
        testCustomer.addToCart("test_item", 2);
        testCustomer.modifyQuantity("test_item", 3);

        // Verify new quantity
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals("test_item")) {
                assertEquals(3, testCustomer.getCart().get(item).intValue());
                break;
            }
        }

        // Verify updated total price (3 * 100 = 300)
        assertEquals(300, testCustomer.viewTotal());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testPreventNegativeQuantity() {
        testCustomer.addToCart("test_item", 2);
        testCustomer.modifyQuantity("test_item", -1);

        // Verify quantity remains unchanged
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals("test_item")) {
                assertTrue(testCustomer.getCart().get(item) > 0);
                break;
            }
        }

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testRemoveItemFromCart() {
        testCustomer.addToCart("test_item", 2);
        testCustomer.removeFromCart("test_item");

        // Verify cart is empty
        assertTrue(testCustomer.getCart().isEmpty());
        assertEquals(0, testCustomer.viewTotal());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    // Test Part 2: Out-of-Stock Items

    @Test
    public void testOrderOutOfStockItem() {
        testCustomer.addToCart("out_of_stock_item", 1);

        // Verify cart remains empty
        assertTrue(testCustomer.getCart().isEmpty());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testOrderExceedingStock() {
        testCustomer.addToCart("test_item", 6);

        // Verify cart remains empty or contains only available quantity
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals("test_item")) {
                assertTrue(testCustomer.getCart().isEmpty() ||
                    testCustomer.getCart().get(item) <= 5);
                break;
            }
        }

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testMultipleOutOfStockAttempts() {
        testCustomer.addToCart("out_of_stock_item", 1);
        testCustomer.addToCart("out_of_stock_item", 2);
        testCustomer.addToCart("out_of_stock_item", 3);

        // Verify cart remains empty
        assertTrue(testCustomer.getCart().isEmpty());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }

    @Test
    public void testMixedStockOrder() {
        testCustomer.addToCart("test_item", 2);
        testCustomer.addToCart("out_of_stock_item", 1);

        // Verify only in-stock item is in cart
        for (Item item: ByteMe.getMenu()) {
            if (item.getName().equals("test_item")) assertTrue(testCustomer.getCart().containsKey(item));
            if (item.getName().equals("out_of_stock_item")) assertFalse(testCustomer.getCart().containsKey(item));
        }
        assertEquals(1, testCustomer.getCart().size());

        ByteMe.getMenu().clear();
        MainMenuGUI.clearFiles();
        FileHandler.clearCartFile();
    }
}
