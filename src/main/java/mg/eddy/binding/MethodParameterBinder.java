package mg.eddy.binding;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import javax.servlet.http.Part;

public class MethodParameterBinder {
    public Object[] bind(Method method, Map<String, String[]> requestParameters) {
        return bind(method, requestParameters, java.util.Collections.<String, Part>emptyMap());
    }

    public Object[] bind(
            Method method,
            Map<String, String[]> requestParameters,
            Map<String, Part> uploadedParts) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            if (!parameter.isNamePresent()) {
                throw new ParameterBindingException(
                        "Le nom du paramètre Java n'est pas disponible. Compilez avec l'option -parameters.");
            }
            if (parameter.getType() == Part.class) {
                Part uploadedPart = uploadedParts.get(parameter.getName());
                if (uploadedPart == null || uploadedPart.getSize() == 0) {
                    throw new ParameterBindingException(
                            "Le fichier HTTP '" + parameter.getName() + "' est requis.");
                }
                arguments[index] = uploadedPart;
                continue;
            }
            if (!TypeConverter.supports(parameter.getType())) {
                throw new ParameterBindingException(
                        "Le paramètre '" + parameter.getName() + "' n'est pas un type simple pris en charge.");
            }

            String value = firstValue(requestParameters.get(parameter.getName()));
            if (value == null) {
                throw new ParameterBindingException(
                        "Le paramètre HTTP '" + parameter.getName() + "' est requis.");
            }
            arguments[index] = TypeConverter.convert(value, parameter.getType());
        }
        return arguments;
    }

    private String firstValue(String[] values) {
        return values == null || values.length == 0 ? null : values[0];
    }
}
