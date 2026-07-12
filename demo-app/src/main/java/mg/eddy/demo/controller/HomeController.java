package mg.eddy.demo.controller;

import mg.eddy.annotation.MyController;
import mg.eddy.annotation.URLAnnotation;
import mg.eddy.mapping.HTTPMethod;
import mg.eddy.util.ModelView;

@MyController
public class HomeController {
    @URLAnnotation(value = "/", httpMethod = HTTPMethod.GET)
    public ModelView home() {
        return new ModelView("home.jsp")
                .add("applicationName", "Application de test Eddy Framework")
                .add("message", "Le FrontController, le scan et le rendu JSP fonctionnent.");
    }

    @URLAnnotation(value = "/about", httpMethod = HTTPMethod.GET)
    public ModelView aboutHtml() {
        return new ModelView("about.jsp");
    }

    @URLAnnotation(value = "/debug", httpMethod = HTTPMethod.GET)
    public String debug() {
        return "Cette valeur n'est pas un ModelView";
    }
}
