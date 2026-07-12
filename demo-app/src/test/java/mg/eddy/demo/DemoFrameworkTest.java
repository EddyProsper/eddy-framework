package mg.eddy.demo;

import static org.junit.jupiter.api.Assertions.*;
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
    void urlKeySeparatesHttpMethods() {
        assertNotEquals(new URLKey("/products", HTTPMethod.GET), new URLKey("/products", HTTPMethod.POST));
    }
}
