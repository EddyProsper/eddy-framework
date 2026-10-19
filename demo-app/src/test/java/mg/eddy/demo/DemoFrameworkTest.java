package mg.eddy.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.servlet.http.Part;

import org.junit.jupiter.api.Test;
import mg.eddy.demo.controller.ProductController;
import mg.eddy.mapping.HTTPMethod;
import mg.eddy.mapping.URLKey;
import mg.eddy.mapping.URLProcessor;
import mg.eddy.util.ModelView;
import mg.eddy.annotation.MyController;
import mg.eddy.annotation.URLAnnotation;

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

    @Test
    void injectsUploadedFileIntoControllerAction() throws Exception {
        URLProcessor processor = new URLProcessor();
        processor.processAnnotatedClass(FileController.class);
        Part file = new MemoryPart("file", "test.txt", "bonjour".getBytes("UTF-8"));
        Map<String, Part> parts = new HashMap<>();
        parts.put("file", file);

        Object result = processor.executeRequest(
                new URLKey("/test-upload", HTTPMethod.POST),
                Collections.<String, String[]>emptyMap(),
                parts);

        assertEquals("test.txt:7", result);
    }

    @MyController
    public static class FileController {
        @URLAnnotation(value = "/test-upload", httpMethod = HTTPMethod.POST)
        public String upload(Part file) {
            return file.getSubmittedFileName() + ":" + file.getSize();
        }
    }

    private static class MemoryPart implements Part {
        private final String name;
        private final String fileName;
        private final byte[] content;

        MemoryPart(String name, String fileName, byte[] content) {
            this.name = name;
            this.fileName = fileName;
            this.content = content;
        }

        public InputStream getInputStream() { return new ByteArrayInputStream(content); }
        public String getContentType() { return "text/plain"; }
        public String getName() { return name; }
        public String getSubmittedFileName() { return fileName; }
        public long getSize() { return content.length; }
        public void write(String fileName) throws IOException { throw new UnsupportedOperationException(); }
        public void delete() { }
        public String getHeader(String name) { return null; }
        public Collection<String> getHeaders(String name) { return Collections.emptyList(); }
        public Collection<String> getHeaderNames() { return Collections.emptyList(); }
    }
}
