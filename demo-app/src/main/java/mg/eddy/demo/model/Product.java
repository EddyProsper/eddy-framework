package mg.eddy.demo.model;

public final class Product {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private final boolean available;

    public Product(int id, String name, String category, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String category() {
        return category;
    }

    public double price() {
        return price;
    }

    public boolean available() {
        return available;
    }
}
