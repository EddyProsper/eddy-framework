<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${applicationName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="container">
    <header class="page-header">
        <span class="badge">Eddy Framework</span>
        <h1>${applicationName}</h1>
        <p class="lead">${message}</p>
    </header>

    <section class="grid">
        <a class="card" href="${pageContext.request.contextPath}/products">
            <h2>GET + ModelView</h2><p>Catalogue avec une liste Java transmise à une JSP.</p>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/products/featured">
            <h2>Objet dans le modèle</h2><p>Affichage d'un produit complet avec EL.</p>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/about">
            <h2>Vue HTML</h2><p>Validation de l'extension HTML autorisée.</p>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/debug">
            <h2>Retour non ModelView</h2><p>Affiche la page interne des contrôleurs et routes.</p>
        </a>
        <a class="card card--json" href="${pageContext.request.contextPath}/products/json">
            <h2>Sprint 6 — Réponse JSON</h2><p>Teste une action annotée avec @Json sans redirection vers une vue.</p>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/route-inconnue">
            <h2>Erreur 404</h2><p>Teste une URL qui n'existe pas.</p>
        </a>
    </section>

    <form method="post" action="${pageContext.request.contextPath}/products/estimate" class="post-test">
        <h2>Sprint 7 — Injection des paramètres simples</h2>
        <p>Les valeurs du formulaire sont converties vers <code>String</code>, <code>int</code> et <code>double</code>.</p>
        <div class="form-grid">
            <label>Nom du client
                <input type="text" name="customerName" value="Alice" required>
            </label>
            <label>Quantité
                <input type="number" name="quantity" value="2" min="1" required>
            </label>
            <label>Prix unitaire
                <input type="number" name="unitPrice" value="19.90" min="0" step="0.01" required>
            </label>
        </div>
        <button type="submit">Calculer le total</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/products/create" class="post-test">
        <h2>Test de la route POST</h2>
        <p>Le framework actuel ne lie pas encore les champs du formulaire, mais il distingue GET et POST.</p>
        <button type="submit">Envoyer POST /products/create</button>
    </form>
</main>
</body>
</html>
