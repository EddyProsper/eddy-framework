package mg.eddy.demo.service;

import java.util.Arrays;
import java.util.List;
import mg.eddy.demo.model.Product;

public class ProductService {
    public List<Product> findAll() {
        return Arrays.asList(
                new Product(1, "Clavier mécanique", "Informatique", 185_000, true),
                new Product(2, "Souris sans fil", "Informatique", 95_000, true),
                new Product(3, "Casque audio", "Audio", 140_000, false));
    }

    public Product findFeatured() {
        return findAll().get(0);
    }
}
