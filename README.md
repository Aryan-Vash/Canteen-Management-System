# 🍽️ Canteen Management System (CLI + Java GUI)

This is a Java-based **graphical Canteen Management System**, built with Swing. It allows users to place orders, view menus, manage carts, and review past orders in a structured, user-friendly interface. The system distinguishes between customers and managers and uses text files for basic data persistence.

---

## Features

- **User Roles**
  - **Customer**: Browse menu, add to cart, place orders, view order history
  - **Manager**: View all orders and manage backend records

- **Cart & Menu Pages**
  - Add/remove items from cart
  - View updated total
  - Menu items are dynamically loaded from `menu.txt`

- **Order History**
  - Order records are saved in files and loaded when the app starts
  - History is user-specific (e.g., `order_history_a.txt`, `order_history_b.txt`)

- **Persistent Storage**
  - All data stored in plain `.txt` files (menu, cart, orders)
  - Simple file handling via `FileHandler.java`

- **GUI**
  - Built using Java Swing
  - Responsive interface for menu browsing and cart interaction

---

## Project Structure

```
ASSIGNMENT 4/
├── ByteMe.java             # Main application launcher
├── MenuPage.java           # GUI for menu display and item selection
├── CartPage.java           # GUI for viewing and managing cart
├── OrdersPage.java         # GUI for order history
├── MainMenuGUI.java        # Home screen GUI
├── Customer.java           # Logic for customer role
├── Manager.java            # Logic for manager role
├── Order.java              # Order structure
├── Item.java               # Menu item structure
├── FileHandler.java        # Handles file-based data persistence
├── User.java               # Base class for Customer and Manager
├── menu.txt, cart.txt, orders.txt, order_history_*.txt
├── UML_ASSIGNMENT-4.png    # UML class diagram
├── ReadME.pdf              # Additional documentation
```

---

## How to Run

### Using an IDE (IntelliJ or Eclipse recommended)

1. Open the project folder in your IDE.
2. Set the SDK to Java 8 or higher.
3. Run `ByteMe.java` as a Java Application.
4. Interact with the GUI that appears.

### Using Terminal

```bash
javac *.java
java ByteMe
```

---

## UML Diagram

The UML class structure can be viewed in:

> `UML.png`

It outlines key classes like `User`, `Customer`, `Item`, `Order`, and the GUI components.

---

## Documentation

Further documentation is provided in:

> `ReadME.pdf`

This includes design decisions, screenshots, and potential extensions.

---

## Authors

- Aryan Vashishtha: aryan23148@iiitd.ac.in

---

## License

This project is for academic and educational purposes only.
