# build-my-own-http-server

Un dépôt vivant que j'utilise pour apprendre le développement backend en profondeur : je reconstruis moi-même le plus de couches possible, pour comprendre ce que les frameworks cachent habituellement.

## Construire mon propre serveur HTTP

Chaque étape renvoie vers une page de cours dédiée.

| # | Étape | Contenu |
|---|---|---|
| 1 | [Couche réseau : socket, flux d'octets et buffer](docs/01-socket-stream-buffer.md) | Ouverture du port (`ServerSocket`), acceptation des connexions (`accept()`), lecture de l'`InputStream` dans un buffer d'octets. |
| 2 | [Parsing de la requête HTTP](docs/02-request-parsing.md) | Désérialisation des octets bruts en un objet `MyHttpRequest` : ligne de requête (méthode, chemin, version), en-têtes, détection de la fin des en-têtes (`CRLF CRLF`), lecture du corps via `Content-Length`. |
| 3 | [Routage](docs/03-routing.md) | Associer une requête (verbe HTTP + chemin) à la bonne route, extraire les paramètres dynamiques (`/categories/{id}`), puis déléguer au handler correspondant. |
| 4 | [Architecture en couches : Controller, Service, Repository](docs/04-controller-service-repository.md) | Séparation des responsabilités : le controller reçoit la requête, le service porte la logique métier, le repository accède aux données. |
| 5 | [Sérialisation de la réponse HTTP](docs/05-response-serialization.md) | Transformer un objet `MyHttpResponse` en octets conformes au protocole HTTP (ligne de statut, en-têtes, corps), puis les écrire sur la socket. |
