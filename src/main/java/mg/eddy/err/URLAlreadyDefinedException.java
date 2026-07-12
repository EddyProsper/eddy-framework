package mg.eddy.err;

import mg.eddy.mapping.URLControllerMap;
import mg.eddy.mapping.URLKey;

public class URLAlreadyDefinedException extends Exception {
    private URLKey url;
    private URLControllerMap value;

    public URLAlreadyDefinedException(URLKey url, URLControllerMap value) {
        super("URL " + url + " est déjà définie pour la valeur " + value);
        this.url = url;
        this.value = value;
    }

    public URLKey getUrl() {
        return url;
    }

    public URLControllerMap getValue() {
        return value;
    }
}
