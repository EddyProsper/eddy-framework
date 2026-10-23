# Eddy Framework

Eddy Framework est un framework MVC Java léger basé sur un contrôleur frontal,
des annotations de routes et des vues JSP.

## Prérequis

- Java 8
- Maven
- Apache Tomcat 9

Les chemins Windows utilisés par le script de déploiement sont :

- `C:\Dev\jdk8u504-b01`
- `C:\Dev\apache-tomcat-9.0.122`

## Compilation

```powershell
mvn clean install
cd demo-app
mvn clean package
```

Le WAR généré se trouve dans `demo-app/target/eddy-demo.war`.

## Déploiement sous Windows

```powershell
.\deploy.ps1
```

Après le démarrage de Tomcat, l'application est accessible à l'adresse
`http://localhost:8080/eddy-demo/`.

## Packages Java

- Framework : `mg.eddy.*`
- Application de démonstration : `mg.eddy.demo.*`

## Réponses JSON

Ajoutez `@Json` à une méthode d'action pour sérialiser directement sa valeur de
retour dans la réponse HTTP, sans effectuer de redirection vers une vue :

```java
@Json
@URLAnnotation(value = "/products/json", httpMethod = HTTPMethod.GET)
public List<Product> productsAsJson() {
    return productService.findAll();
}
```
