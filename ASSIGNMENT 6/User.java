public abstract class User {
    private final String username;
    protected String password;

    public User(String _gmail, String _password) {
        this.username = _gmail;
        this.password = _password;
    }

    public User(String _gmail) {                       // For Administrator
        this.username = _gmail;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public abstract void displayDetails();
    public abstract void setPassword(String password);
}
