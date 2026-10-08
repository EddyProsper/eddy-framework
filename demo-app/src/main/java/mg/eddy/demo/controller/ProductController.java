package mg.eddy.demo.controller;

import java.util.List;

import mg.eddy.annotation.Json;
import mg.eddy.demo.model.Product;
import mg.eddy.demo.service.ProductService;
import mg.eddy.annotation.MyController;
import mg.eddy.annotation.URLAnnotation;
import mg.eddy.mapping.HTTPMethod;
import mg.eddy.util.ModelView;

@MyController
public class ProductController {
    private final ProductService productService = new ProductService();

    @URLAnnotation(value = "/products", httpMethod = HTTPMethod.GET)
    public ModelView list() {
        return new ModelView("products/list.jsp")
                .add("title", "Catalogue de démonstration")
                .add("products", productService.findAll());
    }

    @URLAnnotation(value = "/products/featured", httpMethod = HTTPMethod.GET)
    public ModelView featured() {
        Product product = productService.findFeatured();
        return new ModelView("products/detail.jsp")
                .add("product", product)
                .add("statusLabel", product.available() ? "Disponible" : "Indisponible");
    }

    @Json
    @URLAnnotation(value = "/products/json", httpMethod = HTTPMethod.GET)
    public List<Product> json() {
        return productService.findAll();
    }

    @URLAnnotation(value = "/products/create", httpMethod = HTTPMethod.POST)
    public ModelView create() {
        return new ModelView("status/success.jsp")
                .add("title", "Route POST exécutée")
                .add("message", "Le framework a correctement distingué POST /products/create.");
    }

    @URLAnnotation(value = "/products/estimate", httpMethod = HTTPMethod.POST)
    public ModelView estimate(String customerName, int quantity, double unitPrice) {
        double total = quantity * unitPrice;
        return new ModelView("status/success.jsp")
                .add("title", "Sprint 7 — Paramètres injectés")
                .add("message", customerName + " a demandé " + quantity
                        + " article(s), pour un total de " + String.format("%.2f", total) + " €.");
    }
}
