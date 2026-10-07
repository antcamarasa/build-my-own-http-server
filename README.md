# build-my-own-http-server

This is a living repository I use to deeply learn backend development by building as many layers as possible from scratch, to understand what frameworks usually hide.

## Build my own: HTTP Server

Chaque étape renvoie vers une page de cours dédiée.

1. [**Couche réseau : socket, flux d'octets et buffer**](docs/01-socket-stream-buffer.md)
   Ouverture du port (`ServerSocket`), acceptation des connexions (`accept()`), lecture de l'`InputStream` dans un buffer d'octets.

2. [**Parsing de la requête HTTP**](docs/02-request-parsing.md)
   Désérialisation des octets bruts en un objet `MyHttpRequest` : request line (méthode, chemin, version), headers, détection de la fin des headers (`CRLF CRLF`), lecture du body via `Content-Length`.

3. [**Routing**](docs/03-routing.md)
   Associer une requête (verbe HTTP + chemin) à la bonne route, extraire les paramètres dynamiques (`/categories/{id}`), puis déléguer au handler correspondant.

4. [**Architecture en couches : Controller, Service, Repository**](docs/04-controller-service-repository.md)
   Séparation des responsabilités : le controller reçoit la requête, le service porte la logique métier, le repository accède aux données.

5. [**Sérialisation de la réponse HTTP**](docs/05-response-serialization.md)
   Transformer un objet `MyHttpResponse` en octets conformes au protocole HTTP (status line, headers, body), puis les écrire sur la socket.
