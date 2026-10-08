package mg.eddy.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import mg.eddy.demo.controller.ProductController;
import mg.eddy.mapping.HTTPMethod;
import mg.eddy.mapping.URLKey;
import mg.eddy.mapping.URLProcessor;
import mg.eddy.util.ModelView;

class DemoFrameworkTest {
    @Test
    void modelViewStoresAndRemovesAttributes() {
        ModelView mv = new ModelView("test.jsp").add("name", "Eddy").add("count", 3);
        assertEquals("Eddy", mv.getValue("name"));
        assertEquals(2, mv.getAttributes().size());
        assertEquals(3, mv.remove("count"));
        assertFalse(mv.getAttributes().containsKey("count"));
    }

    @Test
    void urlProcessorExecutesGetAndPostRoutes() throws Exception {
        URLProcessor processor = new URLProcessor();
        processor.processAnnotatedClass(ProductController.class);

        Object getResult = processor.executeRequest(new URLKey("/products", HTTPMethod.GET));
        Object postResult = processor.executeRequest(new URLKey("/products/create", HTTPMethod.POST));

        assertInstanceOf(ModelView.class, getResult);
        assertEquals("products/list.jsp", ((ModelView) getResult).getDestination());
        assertEquals("status/success.jsp", ((ModelView) postResult).getDestination());
    }

    @Test
    void jsonAnnotationMarksActionForJsonResponse() throws Exception {
        URLProcessor processor = new URLProcessor();
        processor.processAnnotatedClass(ProductController.class);

        URLKey jsonKey = new URLKey("/products/json", HTTPMethod.GET);
        Object jsonResult = processor.executeRequest(jsonKey);

        assertInstanceOf(List.class, jsonResult);
        assertTrue(processor.isJsonRequest(jsonKey));
        assertFalse(processor.isJsonRequest(new URLKey("/products", HTTPMethod.GET)));
    }

    @Test
    void urlKeySeparatesHttpMethods() {
        assertNotEquals(new URLKey("/products", HTTPMethod.GET), new URLKey("/products", HTTPMethod.POST));
    }

    @Test
    void injectsAndConvertsSimpleRequestParameters() throws Exception {
        URLProcessor processor = new URLProcessor();
        processor.processAnnotatedClass(ProductController.class);
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("customerName", new String[] {"Alice"});
        parameters.put("quantity", new String[] {"3"});
        parameters.put("unitPrice", new String[] {"12.50"});

        Object result = processor.executeRequest(
                new URLKey("/products/estimate", HTTPMethod.POST), parameters);

        assertInstanceOf(ModelView.class, result);
        ModelView modelView = (ModelView) result;
        assertEquals("status/success.jsp", modelView.getDestination());
        assertTrue(modelView.getValue("message").toString().contains("37,50")
                || modelView.getValue("message").toString().contains("37.50"));
    }
}
