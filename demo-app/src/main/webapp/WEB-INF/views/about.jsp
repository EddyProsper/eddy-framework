<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Vue HTML</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head>
    <body>
        <main class="container">
        <div class="content-box">
            <h1>La vue HTML fonctionne</h1>
            <p>Cette page valide la configuration <code>VIEW_EXTENSIONS=jsp,html</code>.</p>
            <p>Comme il s'agit d'un fichier HTML statique, elle n'utilise pas les attributs du modèle.</p>
            <a class="back-link" href="${pageContext.request.contextPath}/">Retour à l'accueil</a>
        </div>
        </main>
    </body>
</html>
