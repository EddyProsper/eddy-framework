package mg.eddy.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;

import mg.eddy.annotation.APIAnnotation;
import mg.eddy.annotation.Json;
import mg.eddy.annotation.MyController;
import mg.eddy.annotation.URLAnnotation;
import mg.eddy.err.URLAlreadyDefinedException;
import mg.eddy.err.URLNotSupportedException;
import mg.eddy.binding.MethodParameterBinder;
import mg.eddy.util.interfaces.AnnotatedClassesProcessor;

public class URLProcessor implements AnnotatedClassesProcessor {

    private final List<Class<?>> controllerClasses = new ArrayList<>();
    private final HashMap<URLKey, URLControllerMap> urlMaps = new HashMap<>();
    private final HashMap<URLKey, URLControllerMap> apiMaps = new HashMap<>();
    private final MethodParameterBinder parameterBinder = new MethodParameterBinder();

    @Override
    public void processAnnotatedClass(Class<?> clazz) throws ReflectiveOperationException, URLAlreadyDefinedException {
        if (clazz.isAnnotationPresent(MyController.class)) {
            controllerClasses.add(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                // Process API annotations
                if (method.isAnnotationPresent(APIAnnotation.class)) {
                    APIAnnotation apiAnnotation = method.getAnnotation(APIAnnotation.class);
                    if (apiAnnotation.enabled()) {
                        URLKey key = new URLKey(apiAnnotation.value(), apiAnnotation.httpMethod());
                        if (apiMaps.containsKey(key)) {
                            throw new URLAlreadyDefinedException(key, apiMaps.get(key));
                        } else {
                            apiMaps.put(key, new URLControllerMap(method, clazz));
                        }
                    }
                }
                // Process URL annotations
                if (method.isAnnotationPresent(URLAnnotation.class)) {
                    URLAnnotation urlAnnotation = method.getAnnotation(URLAnnotation.class);
                    URLKey key = new URLKey(urlAnnotation.value(), urlAnnotation.httpMethod());
                    if (urlMaps.containsKey(key)) {
                        throw new URLAlreadyDefinedException(key, urlMaps.get(key));
                    } else {
                        urlMaps.put(key, new URLControllerMap(method, clazz));
                    }
                }
            }
        }
    }

    public Object executeRequest(URLKey url)
            throws URLNotSupportedException, ReflectiveOperationException {
        return executeRequest(url, new HashMap<String, String[]>());
    }

    public Object executeRequest(URLKey url, Map<String, String[]> requestParameters)
            throws URLNotSupportedException, ReflectiveOperationException {
        return executeRequest(url, requestParameters, java.util.Collections.<String, Part>emptyMap());
    }

    public Object executeRequest(
            URLKey url,
            Map<String, String[]> requestParameters,
            Map<String, Part> uploadedParts)
            throws URLNotSupportedException, ReflectiveOperationException {
        if (this.apiMaps.containsKey(url)) {
            URLControllerMap map = this.apiMaps.get(url);
            return invoke(map, requestParameters, uploadedParts);
        }
        if (this.urlMaps.containsKey(url)) {
            URLControllerMap map = this.urlMaps.get(url);
            return invoke(map, requestParameters, uploadedParts);
        }
        throw new URLNotSupportedException(url, urlMaps);
    }

    private Object invoke(
            URLControllerMap map,
            Map<String, String[]> requestParameters,
            Map<String, Part> uploadedParts)
            throws ReflectiveOperationException {
        Method method = map.getReflectMethod();
        Object[] arguments = parameterBinder.bind(method, requestParameters, uploadedParts);
        return method.invoke(map.getPrototypeSeed(), arguments);
    }

    public List<Class<?>> getControllerClasses() {
        return controllerClasses;
    }

    public HashMap<URLKey, URLControllerMap> getUrlMaps() {
        return urlMaps;
    }

    public HashMap<URLKey, URLControllerMap> getApiMaps() {
        return apiMaps;
    }

    public boolean isAPIRequest(URLKey key) {
        return apiMaps.containsKey(key);
    }

    public boolean isJsonRequest(URLKey key) {
        URLControllerMap mapping = apiMaps.containsKey(key)
                ? apiMaps.get(key)
                : urlMaps.get(key);

        return mapping != null
                && mapping.getReflectMethod().isAnnotationPresent(Json.class);
    }
}
