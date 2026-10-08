package mg.eddy.binding;

public class ParameterBindingException extends RuntimeException {
    public ParameterBindingException(String message) {
        super(message);
    }

    public ParameterBindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
