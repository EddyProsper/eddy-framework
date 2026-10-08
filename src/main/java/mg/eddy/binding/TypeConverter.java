package mg.eddy.binding;

public final class TypeConverter {
    private TypeConverter() {
    }

    public static boolean supports(Class<?> type) {
        return type == String.class
                || type == int.class || type == Integer.class
                || type == long.class || type == Long.class
                || type == double.class || type == Double.class
                || type == float.class || type == Float.class
                || type == boolean.class || type == Boolean.class
                || type == short.class || type == Short.class
                || type == byte.class || type == Byte.class
                || type == char.class || type == Character.class;
    }

    public static Object convert(String value, Class<?> targetType) {
        if (!supports(targetType)) {
            throw new ParameterBindingException("Type non pris en charge : " + targetType.getName());
        }
        if (targetType == String.class) {
            return value;
        }

        try {
            if (targetType == int.class || targetType == Integer.class) return Integer.valueOf(value);
            if (targetType == long.class || targetType == Long.class) return Long.valueOf(value);
            if (targetType == double.class || targetType == Double.class) return Double.valueOf(value);
            if (targetType == float.class || targetType == Float.class) return Float.valueOf(value);
            if (targetType == short.class || targetType == Short.class) return Short.valueOf(value);
            if (targetType == byte.class || targetType == Byte.class) return Byte.valueOf(value);
            if (targetType == char.class || targetType == Character.class) {
                if (value.length() != 1) {
                    throw new IllegalArgumentException("un seul caractère est attendu");
                }
                return value.charAt(0);
            }
            if ("true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "1".equals(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value) || "off".equalsIgnoreCase(value) || "0".equals(value)) {
                return false;
            }
            throw new IllegalArgumentException("valeur booléenne invalide");
        } catch (IllegalArgumentException e) {
            throw new ParameterBindingException(
                    "Impossible de convertir '" + value + "' vers " + targetType.getSimpleName(), e);
        }
    }
}
